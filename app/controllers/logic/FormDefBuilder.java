/*
*  Nokia Data Gathering
*
*  Copyright (C) 2011 Nokia Corporation
*
*  This program is free software; you can redistribute it and/or
*  modify it under the terms of the GNU Lesser General Public
*  License as published by the Free Software Foundation; either
*  version 2.1 of the License, or (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*  Lesser General Public License for more details.
*
*  You should have received a copy of the GNU Lesser General Public License
*  along with this program.  If not, see <http://www.gnu.org/licenses/
*/

package controllers.logic;

import controllers.exceptions.SurveyXmlCreatorException;
import models.Category;
import models.QuestionOption;
import models.Question;
import models.Survey;
import controllers.util.XFormsTypeMappings;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.GroupDef;
import org.javarosa.core.model.IDataReference;
import org.javarosa.core.model.IFormElement;
import org.javarosa.core.model.QuestionDef;
import org.javarosa.core.model.SelectChoice;
import org.javarosa.core.model.condition.Condition;
import org.javarosa.core.model.condition.Constraint;
import org.javarosa.core.model.condition.IConditionExpr;
import org.javarosa.core.model.condition.Triggerable;
import org.javarosa.core.model.data.StringData;
import org.javarosa.core.model.instance.FormInstance;
import org.javarosa.core.model.instance.TreeElement;
import org.javarosa.core.model.instance.TreeReference;
import org.javarosa.core.services.locale.Localizer;
import org.javarosa.core.services.locale.TableLocaleSource;
import org.javarosa.model.xform.XPathReference;
import org.javarosa.xpath.XPathConditional;
import org.javarosa.xpath.parser.XPathSyntaxException;

public class FormDefBuilder {

    private FormDef formDefinition = null;

    public FormDefBuilder() {
    }

    public FormDef readFormDefinitionFromDb(String surveyId) throws SurveyXmlCreatorException {
        Survey survey = getSurveyFromDb(surveyId);

        return readFormDefinition(survey);
    }

    public FormDef readFormDefinition(Survey survey) throws SurveyXmlCreatorException {
        formDefinition = new FormDef();

        List<Question> questions = survey.getQuestions();
        Collections.sort(questions, new QuestionComparator());


        formDefinition.setName(survey.title);

        setupLocalizer(survey.lang, formDefinition);

        formDefinition.setInstance(setupInstance(survey, questions));

        addChilds(formDefinition, survey, questions);

        return formDefinition;
    }

    private Survey getSurveyFromDb(String surveyId) throws SurveyXmlCreatorException {
        Survey retval = null;
        retval = (Survey) Survey.find("bySurveyId", surveyId).first();
        return retval;
    }

    private void addChilds(FormDef formDefinition, Survey survey, List<Question> questions) {
        TableLocaleSource localization = new TableLocaleSource();
        IFormElement currentElement = formDefinition;
        ListIterator<Question> questionIterator = questions.listIterator();

        while (questionIterator.hasNext()) {
            Question question = questionIterator.next();
            if(!question.category.categoryIndex.equals(new Integer(0)))
            {
                addGroupDef(question, localization, questionIterator, currentElement);
            } else
            {
                addQuestionDef(question, localization, currentElement, null);
            }
        }
        if (survey.lang != null) {
            formDefinition.getLocalizer().registerLocaleResource(survey.lang, localization);
        }
    }

     private void addQuestionDef(Question question, TableLocaleSource localization, IFormElement currentElement, String parentXPath) {
        QuestionDef newQuestion = new QuestionDef();

        String xPath = parentXPath == null ?
                getRefString(question.objectName, "data", null) :
                getRefString(question.objectName, "data/" + parentXPath, null);
        newQuestion.setTextID(xPath +  ":label");
        localization.setLocaleMapping(newQuestion.getTextID(), question.label);

        if (question.hint != null && !question.hint.isEmpty()) {
            newQuestion.setHelpTextID(xPath + ":hint");
            localization.setLocaleMapping(newQuestion.getHelpTextID(), question.hint);
        }

        newQuestion.setControlType(findControlType(question));
        if (newQuestion.getControlType() == org.javarosa.core.model.Constants.CONTROL_SELECT_ONE
                || newQuestion.getControlType() == org.javarosa.core.model.Constants.CONTROL_SELECT_MULTI) {
            addOptions(newQuestion, question, localization, xPath);
        }

        XPathReference dataRef = null;
        dataRef = new XPathReference(xPath);
        newQuestion.setBind(dataRef);

        currentElement.addChild(newQuestion);

        if( question.relevant != null && !question.relevant.isEmpty() ){
            try {
                XPathConditional xCondition = new XPathConditional( question.relevant );
                Triggerable c = new Condition( xCondition, 1,2, (TreeReference) dataRef.getReference() );
                c = formDefinition.addTriggerable( c );
                c.addTarget( (TreeReference) dataRef.getReference() );

            } catch (XPathSyntaxException ex) {
                Logger.getLogger( FormDefBuilder.class.getName() ).log( Level.SEVERE, null, ex );
            }
        }
    }

    private void addGroupDef(Question question, TableLocaleSource localization, ListIterator<Question> questionIterator, IFormElement currentElement) {
        Category currentCategory = question.category;
        GroupDef currentGroup = new GroupDef();
        currentGroup.setTextID(getRefString(currentCategory.objectName, "data", "label"));
        localization.setLocaleMapping(currentGroup.getTextID(), currentCategory.label);
        addQuestionDef(question, localization, currentGroup, currentCategory.objectName);
        for(int i = 1 ; i < currentCategory.questionCollection.size() && questionIterator.hasNext(); i++)
        {
            addQuestionDef(questionIterator.next(), localization, currentGroup, currentCategory.objectName);
        }
        currentElement.addChild(currentGroup);
    }

    private void addOptions(QuestionDef newQuestion, Question dbQuestion, TableLocaleSource localization, String xPath) {
        Collection<QuestionOption> questionOptions = dbQuestion.questionOptionCollection;
        for (QuestionOption questionOption : questionOptions) {
            String reference = new StringBuilder(xPath).append(":option").append(questionOption.optionValue).toString();
            SelectChoice choice = new SelectChoice(reference, questionOption.optionValue );
            localization.setLocaleMapping(reference, questionOption.label);
            newQuestion.addSelectChoice(choice);
        }
    }

    private FormInstance setupInstance(Survey survey, List<Question> questions) {
        FormInstance retval = null;
        TreeElement dataElement = new TreeElement("data");
        dataElement.setAttribute(null, "id", survey.surveyId);
        //todo add orx meta
        ListIterator<Question> iterator = questions.listIterator();
        TreeElement currentCategory = null;
        while (iterator.hasNext()) {
            Question currentQuestion = iterator.next();
            if (currentQuestion.category.categoryIndex.equals(new Integer(0))) {
                currentCategory = null;
                addQuestionToModel(dataElement, currentQuestion);
            } else {
                if (currentCategory == null || !currentQuestion.category.objectName.equals(currentCategory.getName())) {
                    currentCategory = addCategoryToModel(dataElement, currentQuestion.category, iterator);
                }
                addQuestionToModel(currentCategory, currentQuestion);
            }
        }
        retval = new FormInstance(dataElement);
        retval.addNamespace("orx", "http://openrosa.org/xforms/metadata");
        retval.setName(survey.title);
        return retval;
    }

    private void addQuestionToModel(TreeElement parent, Question question) {
        TreeElement questionMeta = new TreeElement(question.objectName);
        if( question.defaultAnswer != null ){
            questionMeta.setValue( new StringData( question.defaultAnswer.textData ) );
        }

        parent.addChild(questionMeta);
        questionMeta.setRequired(question.required == 0 ? false : true);
        questionMeta.setEnabled(question.readonly != null && question.readonly == 1 ? false : true);
        questionMeta.dataType = XFormsTypeMappings.getTypeToInteger(question.questionType.typeName);

        if (question.constraintText != null && !question.constraintText.isEmpty()) {
            try {
                IConditionExpr constraint = new XPathConditional(question.constraintText);
                questionMeta.setConstraint(new Constraint(constraint, null));
            } catch (XPathSyntaxException ex) {
                Logger.getLogger(FormDefBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private TreeElement addCategoryToModel(TreeElement parent, Category category, ListIterator<Question> iterator) {
        TreeElement categoryElement = new TreeElement(category.objectName);
        parent.addChild(categoryElement);
        return categoryElement;
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
                } else if (typeName.equals("binary#video")) {
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

    private String getRefString(String objectName, String instanceName, String type) {
        StringBuilder builder = new StringBuilder();
        builder.append("/").append(instanceName).append("/");
        builder.append(objectName);
        if (type != null) {
            builder.append(":").append(type);
        }
        return builder.toString();
    }

    private static class QuestionComparator implements Comparator<Question> {

        public QuestionComparator() {
        }

        public int compare( Question q, Question q1 ) {
            if( q1.questionIndex == null || q.questionIndex == null ){
                return 0;
            }else{
                return q.questionIndex - q1.questionIndex;
            }
        }
    }
}
