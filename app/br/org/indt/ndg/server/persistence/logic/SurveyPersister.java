package br.org.indt.ndg.server.persistence.logic;

import br.org.indt.ndg.server.exceptions.SurveySavingException;
import br.org.indt.ndg.server.persistence.structure.QuestionOption;
import br.org.indt.ndg.server.persistence.structure.QuestionType;
import br.org.indt.ndg.server.persistence.structure.Question;
import br.org.indt.ndg.server.persistence.structure.Survey;
import br.org.indt.ndg.server.persistence.structure.NdgUser;
import br.org.indt.ndg.server.util.XFormsTypeMappings;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.QuestionDef;
import org.javarosa.core.model.SelectChoice;
import org.javarosa.core.model.instance.TreeElement;
import org.javarosa.core.services.locale.Localizer;
import org.javarosa.xform.parse.XFormParser;
import org.javarosa.xpath.XPathConditional;

import play.db.jpa.JPA;

/**
 * 
 * @author wojciech.luczkow
 */
public class SurveyPersister {

    private InputStreamReader is = null;
    private Survey survey = null;

    public SurveyPersister(InputStreamReader is) {
        this.is = is;
    }

    public void saveSurvey() throws SurveySavingException {
        try {
            logInfo("saving survey");
            XFormParser parser = new XFormParser(is);
            logInfo("parsing");
            FormDef formDefinition = parser.parse();
            logInfo("parsing finished");
            persistSurvey(formDefinition);
            persistQuestionSet(formDefinition);
        } finally {
            logInfo("finished saving");
        }

    }

    private void logInfo(String info) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, info);
    }

    private void persistSurvey(FormDef formDefinition) throws SurveySavingException {

        Survey newSurvey = new Survey(formDefinition.getInstance().getRoot().getAttributeValue("", "id"),
                formDefinition.getName());
        NdgUser owner = getOwner(1);
        newSurvey.setUseridUser(owner);
        newSurvey.setUploadDate(new Date());
        newSurvey.setAvailable(0);

        String locale = formDefinition.getLocalizer().getDefaultLocale();
        if (locale == null) {
            String[] locales = formDefinition.getLocalizer().getAvailableLocales();
            if (locales != null && locales.length > 0) {
                locale = locales[0];
            }
        }

        newSurvey.setLang(locale);

        JPA.em().persist(newSurvey);
        survey = newSurvey;
    }

    private void persistQuestionSet(FormDef formDefinition) throws SurveySavingException {
        Vector /* <IFormElement> */formElements = formDefinition.getChildren();
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
        if (questionHint == null) {
            questionHint = localizer.getLocalizedText(questionDef.getHelpTextID());
        }

        QuestionType type = findQuestionType(questionModel.dataType, questionDef.getControlType());

        Question newQuestion = new Question();
        newQuestion.setSurveysSurveyId(survey);
        newQuestion.setQuestionTypesQuestionTypeId(type);
        newQuestion.setObjectName(questionModel.getName());
        newQuestion.setLabel(questionText);
        newQuestion.setHint(questionHint);
        if (questionModel.getConstraint() != null) {
            XPathConditional expression = (XPathConditional) questionModel.getConstraint().constraint;
            newQuestion.setConstraintText(expression.xpath);
        }
        newQuestion.setRequired(questionModel.required ? new Integer(1) : new Integer(0));

        newQuestion.setReadonly(questionModel.isEnabled() ? new Integer(0) : new Integer(1));

        JPA.em().persist(newQuestion);

        if (type.getTypeName().equals(XFormsTypeMappings.SELECT)
                || type.getTypeName().equals(XFormsTypeMappings.SELECTONE)) {
            persistQuestionOptions(questionDef, newQuestion, localizer);
        }
    }

    private QuestionType findQuestionType(int dataType, int controlType) {
        EntityManager em = null;
        QuestionType retval = null;
        {
            em = JPA.em();
            Query query = em.createNamedQuery("QuestionType.findByTypeName");
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
            query.setParameter("typeName", controlTypeDecoded.toString());
            retval = (QuestionType) query.getSingleResult();
        }
        return retval;
    }

    private NdgUser getOwner(int userId) {
        NdgUser retval = null;
        Query getUserQuery = JPA.em().createNamedQuery("NdgUser.findByNdgUserId");
        getUserQuery.setParameter("ndgUserId", userId);
        retval = (NdgUser) getUserQuery.getSingleResult();
        return retval;
    }

    private void persistQuestionOptions(QuestionDef questionDef, Question newQuestion, Localizer localizer)
            throws SurveySavingException {
        Vector<SelectChoice> choices = questionDef.getChoices();
        Logger.getAnonymousLogger().log(Level.INFO, "ChoiceCount: " + choices.size());
        for (SelectChoice selectChoice : choices) {

            String label = selectChoice.getLabelInnerText();
            if (label == null) {
                label = localizer.getLocalizedText(selectChoice.getTextID());
            }

            QuestionOption option = new QuestionOption();
            option.setOptionIndex(selectChoice.getIndex());
            option.setLabel(label);
            option.setOptionValue(selectChoice.getValue());
            option.setQuestionsQuestionId(newQuestion);

            JPA.em().persist(option);
        }
    }
}
