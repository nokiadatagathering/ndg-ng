package controllers.logic;

import controllers.exceptions.SurveySavingException;
import models.QuestionOption;
import models.QuestionType;
import models.Question;
import models.Survey;
import models.NdgUser;
import controllers.util.XFormsTypeMappings;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.persistence.EntityManager;
import models.Category;
import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.QuestionDef;
import org.javarosa.core.model.SelectChoice;
import org.javarosa.core.model.instance.TreeElement;
import org.javarosa.core.services.locale.Localizer;
import org.javarosa.xform.parse.XFormParser;
import org.javarosa.xpath.XPathConditional;

public class SurveyPersister {

    private InputStreamReader is = null;
    private Survey survey = null;
    private Category defCategory = null;

    public SurveyPersister(InputStreamReader is) {
        this.is = is;
    }

    public void saveSurvey() throws SurveySavingException
    {
        saveSurvey(null);
    }

    public void saveSurvey(String surveyId) throws SurveySavingException {
        XFormParser parser = new XFormParser(is);
        FormDef formDefinition = parser.parse();
        Survey newSurvey = findOrCreateSurvey(surveyId, formDefinition);
        persistSurvey(formDefinition, newSurvey);
        persistQuestionSet(formDefinition);
    }

    private void persistSurvey(FormDef formDefinition, Survey newSurvey) throws SurveySavingException {
        NdgUser owner = getOwner(new Long(1));//todo other owners than admin
        newSurvey.ndgUser = owner;
        newSurvey.uploadDate = new Date();
        newSurvey.available = 0;

        String locale = formDefinition.getLocalizer().getDefaultLocale();
        if (locale == null) {
            String[] locales = formDefinition.getLocalizer().getAvailableLocales();
            if (locales != null && locales.length > 0) {
                locale = locales[0];
            }
        }

        newSurvey.lang = locale;

        defCategory = new Category();
        defCategory.survey = newSurvey;
        defCategory.label = "Default";
        newSurvey.categoryCollection = new ArrayList<Category>();
        newSurvey.categoryCollection.add(defCategory);

        newSurvey.save();
        survey = newSurvey;
    }

    private void persistQuestionSet(FormDef formDefinition) throws SurveySavingException {
        Vector /* <IFormElement> */ formElements = formDefinition.getChildren();
        for (int i = 0; i < formElements.size(); i++) {
            if (formElements.get(i) instanceof QuestionDef) {
                QuestionDef question = (QuestionDef) formElements.get(i);
                TreeElement questionModelElement = formDefinition.getInstance().resolveReference(question.getBind());
                persistQuestion(question, questionModelElement, formDefinition.getLocalizer());
            }
        }
    }

    private void persistQuestion(QuestionDef questionDef, TreeElement questionModel, Localizer localizer)
            throws SurveySavingException {

        String questionText = questionDef.getLabelInnerText();
        if (questionText == null) {
            localizer.setLocale("eng");// TODO
            questionText = localizer.getLocalizedText(questionDef.getTextID());
        }

        String questionHint = questionDef.getHelpText();
        if (questionHint == null && questionDef.getHelpTextID() != null) {
            questionHint = localizer.getLocalizedText(questionDef.getHelpTextID());
        }

        QuestionType type = findQuestionType(questionModel.dataType, questionDef.getControlType());

        Question newQuestion = new Question();
        newQuestion.category = defCategory;
        newQuestion.questionType = type;
        newQuestion.objectName = questionModel.getName();
        newQuestion.label = questionText;
        newQuestion.hint = questionHint;
        if (questionModel.getConstraint() != null) {
            XPathConditional expression = (XPathConditional) questionModel.getConstraint().constraint;
            newQuestion.constraintText = expression.xpath;
        }
        newQuestion.required = questionModel.required ? new Integer(1) : new Integer(0);

        newQuestion.readonly = questionModel.isEnabled() ? new Integer(0) : new Integer(1);

        newQuestion.save();

        if (type.typeName.equals(XFormsTypeMappings.SELECT)
                || type.typeName.equals(XFormsTypeMappings.SELECTONE)) {
            persistQuestionOptions(questionDef, newQuestion, localizer);
        }
    }

    private QuestionType findQuestionType(int dataType, int controlType) {
        EntityManager em = null;
        QuestionType retval = null;
        {
            StringBuilder controlTypeDecoded = new StringBuilder(XFormsTypeMappings.getIntegerToType(dataType));
            if (controlTypeDecoded.toString().equals("binary")) {
                switch (controlType) {
                    case org.javarosa.core.model.Constants.CONTROL_AUDIO_CAPTURE:
                        controlTypeDecoded.append("#audio");
                        break;
                    case org.javarosa.core.model.Constants.CONTROL_VIDEO_CAPTURE:
                        controlTypeDecoded.append("#video");
                        break;
                    case org.javarosa.core.model.Constants.CONTROL_IMAGE_CHOOSE:
                        controlTypeDecoded.append("#image");
                        break;
                    default:
                        break;
                }
            }
            retval = QuestionType.find("byTypeName", controlTypeDecoded.toString()).first();
        }
        return retval;
    }

    private NdgUser getOwner(Long userId) {
        NdgUser retval = null;
        retval = NdgUser.find("byId", userId).first();
        return retval;
    }

    private void persistQuestionOptions(QuestionDef questionDef, Question newQuestion, Localizer localizer)
            throws SurveySavingException {
        Vector<SelectChoice> choices = questionDef.getChoices();
        for (SelectChoice selectChoice : choices) {

            String label = selectChoice.getLabelInnerText();
            if (label == null) {
                label = localizer.getLocalizedText(selectChoice.getTextID());
            }

            QuestionOption option = new QuestionOption();
            option.optionIndex = selectChoice.getIndex();
            option.label = label;
            option.optionValue = selectChoice.getValue();
            option.question = newQuestion;

            option.save();
        }
    }

    private Survey findOrCreateSurvey(String surveyId, FormDef formDefinition) {
        Survey retval = null;
        if(surveyId == null)
        {
            retval = new Survey(formDefinition.getInstance().getRoot().getAttributeValue("", "id"),
                 formDefinition.getName());
        }
        else
        {
            retval = Survey.find("bySurveyId", surveyId).first();
            retval.delete();
            retval = new Survey(surveyId, formDefinition.getName());
        }
        return retval;
    }
}
