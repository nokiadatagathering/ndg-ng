/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.indt.ndg.server.persistence.logic;

import br.org.indt.ndg.server.exceptions.SurveyXmlCreatorException;
import br.org.indt.ndg.server.persistence.structure.QuestionOption;
import br.org.indt.ndg.server.persistence.structure.Question;
import br.org.indt.ndg.server.persistence.structure.Survey;
import br.org.indt.ndg.server.util.XFormsTypeMappings;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
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

import play.db.jpa.JPA;

/**
 *
 * @author wojciech.luczkow
 */
public class FormDefBuilder {

    public FormDefBuilder() {
    }

  
    public FormDef readFormDefinitionFromDb(String surveyId) throws SurveyXmlCreatorException {
        EntityManager em = JPA.em();
        Survey survey = getSurveyFromDb(surveyId, em);
      
        return readFormDefinition(survey);
    }
    
      
    public FormDef readFormDefinition(Survey survey) throws SurveyXmlCreatorException {
        FormDef formDefinition = new FormDef();

        Collection<Question> questions = survey.getQuestionsCollection();

        formDefinition.setName(survey.getTitle());

        setupLocalizer(survey.getLang(), formDefinition);

        formDefinition.setInstance(setupInstance(survey, questions));

        addQuestions(formDefinition, survey);
        
        return formDefinition;
    }

    private Survey getSurveyFromDb(String surveyId, EntityManager em) throws SurveyXmlCreatorException {
        Survey retval = null;
        Query getSurvey = em.createNamedQuery("Survey.findBySurveyId");
        getSurvey.setParameter("surveyId", surveyId);
        retval = (Survey) getSurvey.getSingleResult();
        if (retval == null) {
            throw new SurveyXmlCreatorException(SurveyXmlCreatorException.SURVEY_NO_FOUND);
        }
        return retval;
    }

    private void addQuestions(FormDef formDefinition, Survey survey) {
        Collection<Question> questions = survey.getQuestionsCollection();
        TableLocaleSource localization = new TableLocaleSource();
        for (Question question : questions) {
            QuestionDef newQuestion = new QuestionDef();
            newQuestion.setTextID(getRefString(question, "data", "label"));
            newQuestion.setHelpTextID(getRefString(question, "data", "hint"));
            newQuestion.setControlType(findControlType(question));
            localization.setLocaleMapping(newQuestion.getTextID(), question.getLabel());
            if (question.getHint() != null) {
                localization.setLocaleMapping(newQuestion.getHelpTextID(), question.getHint());
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
        if (survey.getLang() != null) {
            formDefinition.getLocalizer().registerLocaleResource(survey.getLang(), localization);
        }
    }
    
    
    private void addOptions(QuestionDef newQuestion, Question dbQuestion, TableLocaleSource localization) {
        Collection<QuestionOption> questionOptions = dbQuestion.getQuestionOptionsCollection();
        for (QuestionOption questionOption : questionOptions) {
            String reference = getRefString(dbQuestion, "data", "option" + questionOption.getOptionIndex());
            SelectChoice choice = new SelectChoice(reference, questionOption.getOptionValue());
            localization.setLocaleMapping(reference, questionOption.getLabel());
            newQuestion.addSelectChoice(choice);
        }
    }

    private FormInstance setupInstance(Survey survey, Collection<Question> questions) {
        FormInstance retval = null;
        TreeElement dataElement = new TreeElement("data");
        dataElement.setAttribute(null, "id", survey.getSurveyId());
        //todo add orx meta
        for (Question question : questions) {
            TreeElement questionMeta = new TreeElement(question.getObjectName());
            dataElement.addChild(questionMeta);
            questionMeta.setRequired(question.getRequired() == 0 ? false : true);
            questionMeta.setEnabled(question.getReadonly() != null && question.getReadonly() == 1 ? false : true);
            questionMeta.dataType = XFormsTypeMappings.getTypeToIntegerMapping().get(question.getQuestionTypesQuestionTypeId().getTypeName());
            
            if(question.getConstraintText() != null && !question.getConstraintText().isEmpty())
            {
                try {
                    IConditionExpr constraint = new XPathConditional(question.getConstraintText());
                     questionMeta.setConstraint(new Constraint(constraint, null));
                } catch (XPathSyntaxException ex) {
                    Logger.getLogger(FormDefBuilder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        retval = new FormInstance(dataElement);
        retval.addNamespace("orx", "http://openrosa.org/xforms/metadata");
        retval.setName(survey.getTitle());
        return retval;
    }

    private int findControlType(Question question) {
        int retval = org.javarosa.core.model.Constants.DATATYPE_UNSUPPORTED;
        String typeName = question.getQuestionTypesQuestionTypeId().getTypeName();
        int dataTypeMapping = XFormsTypeMappings.getTypeToIntegerMapping().get(typeName);

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
        builder.append(question.getObjectName());
        if(type != null)
        {
            builder.append(":").append(type);
        }
        return builder.toString();
    }
}
