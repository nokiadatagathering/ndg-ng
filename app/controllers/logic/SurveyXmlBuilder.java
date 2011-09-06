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
import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.QuestionDef;
import org.javarosa.core.model.instance.TreeElement;
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
        addBindings(model, formDef);
        head.addChild(Node.ELEMENT, model);
    }

    private void addInstanceToModel(Element model, FormDef formDef) {
        Element instance = model.createElement(null, "instance");
        Element data = instance.createElement(null, "data");
        data.setAttribute(null, "id", formDef.getInstance().getRoot().getAttributeValue("", "id"));

        int childCount = formDef.getInstance().getRoot().getNumChildren();
        for (int i = 0; i < childCount; i++) {
            TreeElement element = formDef.getInstance().getRoot().getChildAt(i);
            Element child = data.createElement(null, element.getName());
            data.addChild(Node.ELEMENT, child);
        }

        instance.addChild(Node.ELEMENT, data);
        model.addChild(Node.ELEMENT, instance);
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

    private void addBindings(Element model, FormDef formDef) {
        for (Object formChild : formDef.getChildren()) {
            if (formChild instanceof QuestionDef) {
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
                model.addChild(Node.ELEMENT, binding);
            }
        }
    }

    private String getNodesetFromTextId(QuestionDef question) {
        String textId = question.getTextID();
        return textId.substring(0, textId.indexOf(":"));
    }

    private void setupBody(Element html, FormDef formDef) {
        Element body = html.createElement(null, "h:body");

        for (Object formChild : formDef.getChildren()) {
            if (formChild instanceof QuestionDef) {
                QuestionDef question = (QuestionDef) formChild;
                addQuestionElement(question, body, formDef);
            }
        }

        html.addChild(Node.ELEMENT, body);
    }

    private void addQuestionElement(QuestionDef question, Element body, FormDef formDef) {
        String type = XFormsTypeMappings.getIntegerToControlType(question.getControlType());
        Element questionNode = body.createElement(null, type);
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

        Element hint = questionNode.createElement(null, "hint");
        hint.setAttribute(null, "ref", buildItextRef(question.getHelpTextID()));
        questionNode.addChild(Node.ELEMENT, hint);

        if (type.equals(SELECT) || type.equals(SELECTONE)) {
            addChoiceItems(questionNode, question, formDef);
        }

        body.addChild(Node.ELEMENT, questionNode);
    }

    private void addChoiceItems(Element questionNode, QuestionDef question, FormDef formDef) {
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
