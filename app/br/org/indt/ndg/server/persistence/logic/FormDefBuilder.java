/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.indt.ndg.server.persistence.logic;

import br.org.indt.ndg.server.exceptions.SurveyXmlCreatorException;
import br.org.indt.ndg.server.persistence.structure.QuestionOption;
import br.org.indt.ndg.server.persistence.structure.Question;
import br.org.indt.ndg.server.persistence.structure.Survey;
import java.util.Collection;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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

    private static Hashtable<String, Integer> typeMappings;
    private static final String SELECTONE = "select1";
    private static final String SELECT = "select";

    public FormDefBuilder() {
        initTypeMappings();
    }

    private static void initTypeMappings() {
        typeMappings = new Hashtable<String, Integer>();
        typeMappings.put("string", new Integer(org.javarosa.core.model.Constants.DATATYPE_TEXT));               //xsd:
        typeMappings.put("integer", new Integer(org.javarosa.core.model.Constants.DATATYPE_INTEGER));           //xsd:
        typeMappings.put("long", new Integer(org.javarosa.core.model.Constants.DATATYPE_LONG));                 //xsd:
        typeMappings.put("int", new Integer(org.javarosa.core.model.Constants.DATATYPE_INTEGER));               //xsd:
        typeMappings.put("decimal", new Integer(org.javarosa.core.model.Constants.DATATYPE_DECIMAL));           //xsd:
        typeMappings.put("double", new Integer(org.javarosa.core.model.Constants.DATATYPE_DECIMAL));            //xsd:
        typeMappings.put("float", new Integer(org.javarosa.core.model.Constants.DATATYPE_DECIMAL));             //xsd:
        typeMappings.put("dateTime", new Integer(org.javarosa.core.model.Constants.DATATYPE_DATE_TIME));        //xsd:
        typeMappings.put("date", new Integer(org.javarosa.core.model.Constants.DATATYPE_DATE));                 //xsd:
        typeMappings.put("time", new Integer(org.javarosa.core.model.Constants.DATATYPE_TIME));                 //xsd:
        typeMappings.put("gYear", new Integer(org.javarosa.core.model.Constants.DATATYPE_UNSUPPORTED));         //xsd:
        typeMappings.put("gMonth", new Integer(org.javarosa.core.model.Constants.DATATYPE_UNSUPPORTED));        //xsd:
        typeMappings.put("gDay", new Integer(org.javarosa.core.model.Constants.DATATYPE_UNSUPPORTED));          //xsd:
        typeMappings.put("gYearMonth", new Integer(org.javarosa.core.model.Constants.DATATYPE_UNSUPPORTED));    //xsd:
        typeMappings.put("gMonthDay", new Integer(org.javarosa.core.model.Constants.DATATYPE_UNSUPPORTED));     //xsd:
        typeMappings.put("boolean", new Integer(org.javarosa.core.model.Constants.DATATYPE_BOOLEAN));           //xsd:
        typeMappings.put("base64Binary", new Integer(org.javarosa.core.model.Constants.DATATYPE_UNSUPPORTED));  //xsd:
        typeMappings.put("hexBinary", new Integer(org.javarosa.core.model.Constants.DATATYPE_UNSUPPORTED));     //xsd:
        typeMappings.put("anyURI", new Integer(org.javarosa.core.model.Constants.DATATYPE_UNSUPPORTED));        //xsd:
        typeMappings.put("listItem", new Integer(org.javarosa.core.model.Constants.DATATYPE_CHOICE));           //xforms:
        typeMappings.put("listItems", new Integer(org.javarosa.core.model.Constants.DATATYPE_CHOICE_LIST));        //xforms:    
        typeMappings.put(SELECTONE, new Integer(org.javarosa.core.model.Constants.DATATYPE_CHOICE));            //non-standard    
        typeMappings.put(SELECT, new Integer(org.javarosa.core.model.Constants.DATATYPE_CHOICE_LIST));        //non-standard
        typeMappings.put("geopoint", new Integer(org.javarosa.core.model.Constants.DATATYPE_GEOPOINT));         //non-standard
        typeMappings.put("barcode", new Integer(org.javarosa.core.model.Constants.DATATYPE_BARCODE));           //non-standard
        typeMappings.put("binary#image", new Integer(org.javarosa.core.model.Constants.DATATYPE_BINARY));             //non-standard
        typeMappings.put("binary#audio", new Integer(org.javarosa.core.model.Constants.DATATYPE_BINARY));
        typeMappings.put("binary#video", new Integer(org.javarosa.core.model.Constants.DATATYPE_BINARY));
    }

    public FormDef readFormDefinitionFromDb(String surveyId) throws SurveyXmlCreatorException {
        FormDef formDefinition = new FormDef();

        EntityManager em = JPA.em();
        Survey survey = getSurveyFromDb(surveyId, em);
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
            questionMeta.dataType = typeMappings.get(question.getQuestionTypesQuestionTypeId().getTypeName());
            
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
        int dataTypeMapping = typeMappings.get(typeName);

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
