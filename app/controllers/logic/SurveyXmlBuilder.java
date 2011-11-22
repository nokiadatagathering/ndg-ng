/*
 *  Copyright (C) 2010-2011  INdT - Instituto Nokia de Tecnologia
 *
 *  NDG is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  NDG is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with NDG.  If not, see <http://www.gnu.org/licenses/
 */
package controllers.logic;

import controllers.exceptions.SurveyXmlCreatorException;
import models.Survey;
import controllers.util.XFormsTypeMappings;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.GroupDef;
import org.javarosa.core.model.IFormElement;
import org.javarosa.core.model.QuestionDef;
import org.javarosa.core.model.condition.Condition;
import org.javarosa.core.model.condition.Triggerable;
import org.javarosa.core.model.instance.TreeElement;
import org.javarosa.core.model.instance.TreeReference;
import org.javarosa.core.services.locale.Localizer;
import org.javarosa.core.util.OrderedHashtable;
import org.javarosa.xpath.XPathConditional;
import org.kxml2.io.KXmlSerializer;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

public class SurveyXmlBuilder {

    private static final String SELECTONE = "select1";
    private static final String SELECT = "select";
    private static final String ITEXT_CLOSE = "')";
    private static final String ITEXT_OPEN = "jr:itext('";

    public SurveyXmlBuilder() {
    }

    public void printSurveyXml(String surveyId, PrintWriter writer) throws SurveyXmlCreatorException, IOException {
            FormDefBuilder definitionBuilder = new FormDefBuilder();
        FormDef formDef = definitionBuilder.readFormDefinitionFromDb(surveyId);

        printFormDef(formDef, writer);
    }

    public void printSurveyXml(Survey survey, PrintWriter writer) throws SurveyXmlCreatorException, IOException {
        FormDefBuilder definitionBuilder = new FormDefBuilder();
        FormDef formDef = definitionBuilder.readFormDefinition(survey);

        printFormDef(formDef, writer);

    }


    private void printFormDef(FormDef formDef, PrintWriter writer) throws SurveyXmlCreatorException, IOException {
        Document xmlDocument = new Document();

        Element html = createHtmlElement(xmlDocument);
        xmlDocument.addChild(Node.ELEMENT, html);

        setupHead(html, formDef);

        setupBody(html, formDef);

        KXmlSerializer serializer = new KXmlSerializer();
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        serializer.setOutput(writer);
        xmlDocument.write(serializer);
    }

    private Element createHtmlElement(Document xmlDocument) {
        Element retval = xmlDocument.createElement(null, "h:html");
        retval.setAttribute(null, "xmlns", "http://www.w3.org/2002/xforms");
        retval.setAttribute(null, "xmlns:h", "http://www.w3.org/1999/xhtml");
        retval.setAttribute(null, "xmlns:ev", "http://www.w3.org/2001/xml-events");
        retval.setAttribute(null, "xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
        retval.setAttribute(null, "xmlns:jr", "http://openrosa.org/javarosa");
        return retval;
    }

    private void setupHead(Element html, FormDef formDef) {
        Element head = html.createElement(null, "h:head");
        html.addChild(Node.ELEMENT, head);

        Element title = head.createElement(null, "h:title");
        title.addChild(Node.TEXT, formDef.getName());
        head.addChild(Node.ELEMENT, title);

        addModelToHead(head, formDef);
    }

    private void addModelToHead(Element head, FormDef formDef) {
        Element model = head.createElement(null, "model");
        addInstanceToModel(model, formDef);
        addItextToModel(model, formDef);
        addBindings(model, formDef, formDef);
        head.addChild(Node.ELEMENT, model);
    }

    private void addInstanceToModel(Element model, FormDef formDef) {
        Element instance = model.createElement(null, "instance");
        Element data = instance.createElement(null, "data");
        data.setAttribute(null, "id", formDef.getInstance().getRoot().getAttributeValue("", "id"));

        int childCount = formDef.getInstance().getRoot().getNumChildren();
        fillModelTree(formDef.getInstance().getRoot(), data);

        instance.addChild(Node.ELEMENT, data);
        model.addChild(Node.ELEMENT, instance);
    }

    private void fillModelTree(TreeElement parent, Element data) {
        for (int i = 0; i < parent.getNumChildren(); i++) {
            TreeElement element = parent.getChildAt(i);
            Element child = data.createElement(null, element.getName());
            data.addChild(Node.ELEMENT, child);
            if(element.getNumChildren() > 0)
            {
                fillModelTree(element, child);
            }
        }
    }

    private void addItextToModel(Element model, FormDef formDef) {
        Element iText = model.createElement(null, "itext");


        Element translation = iText.createElement(null, "translation");

        Localizer localizer = formDef.getLocalizer();
        translation.setAttribute(null, "lang", localizer.getDefaultLocale());
        OrderedHashtable localeData = localizer.getLocaleData(localizer.getDefaultLocale());
        for (int i = 0; i < localeData.size(); i++) {
            String key = (String) localeData.keyAt(i);
            if (key != null) {
                Element text = translation.createElement(null, "text");
                String value = (String) localeData.get(key);
                if (value != null) {
                    Element valueElement = text.createElement(null, "value");
                    text.setAttribute(null, "id", key);
                    valueElement.addChild(Node.TEXT, value);
                    text.addChild(Node.ELEMENT, valueElement);
                    translation.addChild(Node.ELEMENT, text);
                }
            }
        }

        iText.addChild(Node.ELEMENT, translation);
        model.addChild(Node.ELEMENT, iText);
    }

    private void addBindings(Element model, FormDef formDef, IFormElement currentNode) {
        for (Object formChild : currentNode.getChildren()) {
            if (formChild instanceof QuestionDef) {
                addQuestionBinding( formChild, model, formDef);
            } else if(formChild instanceof GroupDef) {
                addBindings(model, formDef, (GroupDef) formChild);
            }
        }
    }

    private void addQuestionBinding(Object formChild, Element model, FormDef formDef) {
        QuestionDef question = (QuestionDef) formChild;
        Element binding = model.createElement(null, "bind");
        String nodeset = getNodesetFromTextId(question); //hack to reach object name
        binding.setAttribute(null, "nodeset", nodeset);
        TreeElement questionModelElement = formDef.getInstance().resolveReference(question.getBind());
        binding.setAttribute(null, "type", XFormsTypeMappings.getIntegerToType(questionModelElement.dataType));
        if (questionModelElement.required) {
            binding.setAttribute(null, "required", "true()");
        }
        if (!questionModelElement.isEnabled()) {
            binding.setAttribute(null, "readonly", "true()");
        }

        if (questionModelElement.getConstraint() != null) {
            XPathConditional expression = (XPathConditional) questionModelElement.getConstraint().constraint;
            binding.setAttribute(null, "constraint", expression.xpath);
        }

        if ( questionModelElement.isRelevant() ){
            Condition condition = findConditionByRef( questionModelElement.getRef(), formDef );
            if(condition != null ){
                String expr = ( ( XPathConditional )condition.expr ).xpath;
                binding.setAttribute(null, "relevant", expr );
            }
        }

        model.addChild(Node.ELEMENT, binding);
    }

    private Condition findConditionByRef(TreeReference reference, FormDef formDef ){
        for( Object obj : formDef.triggerables ){
            Condition cond = (Condition)obj;

            for( Object target : cond.getTargets() ){
                TreeReference targetRef = (TreeReference) target;
                if( compareReference( targetRef, reference ) ){
                    return cond;
                }
            }
        }
        return null;
    }

    private static boolean compareReference( TreeReference ref1, TreeReference ref2 ){

        if(ref1.size() != ref2.size()){
            return false;
        }

        for(int i = 0; i < ref1.size(); i++ ){
            if( !ref1.getName( i ).equals( ref2.getName( i ) ) ){
                return false;
            }
        }

        return true;
    }


    private String getNodesetFromTextId(QuestionDef question) {
        String textId = question.getTextID();
        return textId.substring(0, textId.indexOf(":"));
    }

    private void setupBody(Element parent, IFormElement formDef) {
        Element body = parent.createElement(null, "h:body");
        fillBodyTree(formDef, body);

        parent.addChild(Node.ELEMENT, body);
    }

    private void fillBodyTree(IFormElement formDef, Element body) {
        for (Object formChild : formDef.getChildren()) {
            if (formChild instanceof QuestionDef) {
                QuestionDef question = (QuestionDef) formChild;
                addQuestionElement(question, body);
            } else if(formChild instanceof GroupDef) {
                GroupDef group = (GroupDef) formChild;
                addGroupElement(group, body);
            }
        }
    }

    private void addQuestionElement(QuestionDef question, Element parent) {
        String type = XFormsTypeMappings.getIntegerToControlType(question.getControlType());
        Element questionNode = parent.createElement(null, type);
        questionNode.setAttribute(null, "ref", getNodesetFromTextId(question));
        if(type.equals("upload"))
        {
            String mimeType = null;
            switch(question.getControlType())
            {
                case org.javarosa.core.model.Constants.CONTROL_IMAGE_CHOOSE:
                    mimeType = "image/*";
                    break;
                case org.javarosa.core.model.Constants.CONTROL_AUDIO_CAPTURE:
                    mimeType = "audio/*";
                    break;
                case org.javarosa.core.model.Constants.CONTROL_VIDEO_CAPTURE:
                    mimeType = "video/*";
                    break;
            }
            if(mimeType != null)
            {
                questionNode.setAttribute(null, "mediatype", mimeType);
            }
        }

        Element label = questionNode.createElement(null, "label");
        label.setAttribute(null, "ref", buildItextRef(question.getTextID()));
        questionNode.addChild(Node.ELEMENT, label);

        if(question.getHelpTextID() != null)
        {
        Element hint = questionNode.createElement(null, "hint");
        hint.setAttribute(null, "ref", buildItextRef(question.getHelpTextID()));
        questionNode.addChild(Node.ELEMENT, hint);
        }

        if (type.equals(SELECT) || type.equals(SELECTONE)) {
            addChoiceItems(questionNode, question);
        }

        parent.addChild(Node.ELEMENT, questionNode);
    }

    private void addGroupElement(GroupDef groupDef, Element parent) {
        Element groupNode = parent.createElement(null, "group");

        Element label = groupNode.createElement(null, "label");
        label.setAttribute(null, "ref", buildItextRef(groupDef.getTextID()));
        groupNode.addChild(Node.ELEMENT, label);

        fillBodyTree(groupDef, groupNode);

        parent.addChild(Node.ELEMENT, groupNode);
    }

    private void addChoiceItems(Element questionNode, QuestionDef question) {
        for (int i = 0; i < question.getNumChoices(); i++) {
            Element item = questionNode.createElement(null, "item");

            Element label = item.createElement(null, "label");
            label.setAttribute(null, "ref", buildItextRef(question.getChoice(i).getTextID()));
            item.addChild(Node.ELEMENT, label);

            Element value = item.createElement(null, "value");
            value.addChild(Node.TEXT, question.getChoice(i).getValue());
            item.addChild(Node.ELEMENT, value);

            questionNode.addChild(Node.ELEMENT, item);
        }
    }

    private String buildItextRef(String textID) {
        StringBuilder builder = new StringBuilder();
        builder.append(ITEXT_OPEN);
        builder.append(textID);
        builder.append(ITEXT_CLOSE);
        return builder.toString();
    }

}
