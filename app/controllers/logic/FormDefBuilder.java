/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.logic;

import controllers.exceptions.SurveyXmlCreatorException;
import models.QuestionOption;
import models.Question;
import models.Survey;
import controllers.util.XFormsTypeMappings;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.IDataReference;
import org.javarosa.core.model.QuestionDef;
import org.javarosa.core.model.SelectChoice;
import org.javarosa.core.model.condition.Constraint;
import org.javarosa.core.model.condition.IConditionExpr;
import org.javarosa.core.model.instance.FormInstance;
import org.javarosa.core.model.instance.TreeElement;
import org.javarosa.core.services.locale.Localizer;
import org.javarosa.core.services.locale.TableLocaleSource;
import org.javarosa.model.xform.XPathReference;
import org.javarosa.xpath.XPathConditional;
import org.javarosa.xpath.parser.XPathSyntaxException;

/**
 *
 * @author wojciech.luczkow
 */
public class FormDefBuilder {

    public FormDefBuilder() {
    }

  
    public FormDef readFormDefinitionFromDb(String surveyId) throws SurveyXmlCreatorException {
        Survey survey = getSurveyFromDb(surveyId);
      
        return readFormDefinition(survey);
    }
    
      
    public FormDef readFormDefinition(Survey survey) throws SurveyXmlCreatorException {
        FormDef formDefinition = new FormDef();

        List<Question> questions = survey.questionCollection;

        formDefinition.setName(survey.title);

        setupLocalizer(survey.lang, formDefinition);

        formDefinition.setInstance(setupInstance(survey, questions));

        addQuestions(formDefinition, survey);
        
        return formDefinition;
    }

    private Survey getSurveyFromDb(String surveyId) throws SurveyXmlCreatorException {
        Survey retval = null;
        retval = (Survey) Survey.find("bySurveyId", surveyId).first();
        return retval;
    }

    private void addQuestions(FormDef formDefinition, Survey survey) {
        Collection<Question> questions = survey.questionCollection;
        TableLocaleSource localization = new TableLocaleSource();
        for (Question question : questions) {
            QuestionDef newQuestion = new QuestionDef();
            newQuestion.setTextID(getRefString(question, "data", "label"));
            newQuestion.setHelpTextID(getRefString(question, "data", "hint"));
            newQuestion.setControlType(findControlType(question));
            localization.setLocaleMapping(newQuestion.getTextID(), question.label);
            if (question.hint != null) {
                localization.setLocaleMapping(newQuestion.getHelpTextID(), question.hint);
            }
            if (newQuestion.getControlType() == org.javarosa.core.model.Constants.CONTROL_SELECT_ONE
                    || newQuestion.getControlType() == org.javarosa.core.model.Constants.CONTROL_SELECT_MULTI) {
                addOptions(newQuestion, question, localization);
            }
            IDataReference dataRef = null;
            dataRef = new XPathReference(getRefString(question, "data", null));
            newQuestion.setBind(dataRef);
            
            formDefinition.addChild(newQuestion);
        }
        if (survey.lang != null) {
            formDefinition.getLocalizer().registerLocaleResource(survey.lang, localization);
        }
    }
    
    
    private void addOptions(QuestionDef newQuestion, Question dbQuestion, TableLocaleSource localization) {
        Collection<QuestionOption> questionOptions = dbQuestion.questionOptionCollection;
        for (QuestionOption questionOption : questionOptions) {
            String reference = getRefString(dbQuestion, "data", "option" + questionOption.optionIndex);
            SelectChoice choice = new SelectChoice(reference, questionOption.optionValue);
            localization.setLocaleMapping(reference, questionOption.label);
            newQuestion.addSelectChoice(choice);
        }
    }

    private FormInstance setupInstance(Survey survey, Collection<Question> questions) {
        FormInstance retval = null;
        TreeElement dataElement = new TreeElement("data");
        dataElement.setAttribute(null, "id", survey.surveyId);
        //todo add orx meta
        for (Question question : questions) {
            TreeElement questionMeta = new TreeElement(question.objectName);
            dataElement.addChild(questionMeta);
            questionMeta.setRequired(question.required == 0 ? false : true);
            questionMeta.setEnabled(question.readonly != null && question.readonly == 1 ? false : true);
            questionMeta.dataType = XFormsTypeMappings.getTypeToInteger(question.questionType.typeName);
            
            if(question.constraintText != null && !question.constraintText.isEmpty())
            {
                try {
                    IConditionExpr constraint = new XPathConditional(question.constraintText);
                     questionMeta.setConstraint(new Constraint(constraint, null));
                } catch (XPathSyntaxException ex) {
                    Logger.getLogger(FormDefBuilder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        retval = new FormInstance(dataElement);
        retval.addNamespace("orx", "http://openrosa.org/xforms/metadata");
        retval.setName(survey.title);
        return retval;
    }

    private int findControlType(Question question) {
        int retval = org.javarosa.core.model.Constants.DATATYPE_UNSUPPORTED;
        String typeName = question.questionType.typeName;
        int dataTypeMapping = XFormsTypeMappings.getTypeToInteger(typeName);
        switch (dataTypeMapping) {
            case org.javarosa.core.model.Constants.DATATYPE_CHOICE:
                retval = org.javarosa.core.model.Constants.CONTROL_SELECT_ONE;
                break;
            case org.javarosa.core.model.Constants.DATATYPE_CHOICE_LIST:
                retval = org.javarosa.core.model.Constants.CONTROL_SELECT_MULTI;
                break;
            case org.javarosa.core.model.Constants.DATATYPE_BINARY:
                if (typeName.equals("binary#image")) {
                    retval = org.javarosa.core.model.Constants.CONTROL_IMAGE_CHOOSE;
                } else if (typeName.equals("binary#audio")) {
                    retval = org.javarosa.core.model.Constants.CONTROL_AUDIO_CAPTURE;
                } else if (typeName.equals("binary#video")); {
                retval = org.javarosa.core.model.Constants.CONTROL_VIDEO_CAPTURE;
            }
            break;
            default:
                retval = org.javarosa.core.model.Constants.CONTROL_INPUT;
        }
        return retval;
    }

    private void setupLocalizer(String lang, FormDef formDefinition) {
        if (lang != null) {
            Localizer localizer = new Localizer(true, true);
            formDefinition.setLocalizer(localizer);
            localizer.addAvailableLocale(lang);
            localizer.setDefaultLocale(lang);
        }
    }

    private String getRefString(Question question, String instanceName, String type) {
        StringBuilder builder = new StringBuilder();
        builder.append("/").append(instanceName).append("/");
        builder.append(question.objectName);
        if(type != null)
        {
            builder.append(":").append(type);
        }
        return builder.toString();
    }
}
