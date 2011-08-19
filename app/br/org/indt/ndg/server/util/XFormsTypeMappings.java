/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.indt.ndg.server.util;

import java.util.Hashtable;

/**
 *
 * @author wojciech.luczkow
 */
public class XFormsTypeMappings {

    public static final String SELECTONE = "select1";
    public static final String SELECT = "select";
    private static Hashtable<String, Integer> typeToInteger;
    private static Hashtable<Integer, String> integerToType;
    private static Hashtable<Integer, String> integerToControlType;

    public static Hashtable<String, Integer> getTypeToIntegerMapping() {
        return typeToInteger != null  ?
                  typeToInteger
                : initTypeToIntegerMappings();
    }
    
    
    public static Hashtable<Integer, String> getIntegerToTypeMapping() {
        return integerToType != null  ?
                  integerToType
                : initIntegerToTypeMappings();
    }
    
    public static Hashtable<Integer, String> getIntegerToControlTypeMapping() {
        return integerToControlType != null  ?
                  integerToControlType
                : initIntegerToControlTypeMappings();
    }

    private static Hashtable<String, Integer> initTypeToIntegerMappings() {
        typeToInteger = new Hashtable<String, Integer>();
        typeToInteger.put("string", new Integer(org.javarosa.core.model.Constants.DATATYPE_TEXT));               //xsd:
        typeToInteger.put("integer", new Integer(org.javarosa.core.model.Constants.DATATYPE_INTEGER));           //xsd:
        typeToInteger.put("long", new Integer(org.javarosa.core.model.Constants.DATATYPE_LONG));                 //xsd:
        typeToInteger.put("int", new Integer(org.javarosa.core.model.Constants.DATATYPE_INTEGER));               //xsd:
        typeToInteger.put("decimal", new Integer(org.javarosa.core.model.Constants.DATATYPE_DECIMAL));           //xsd:
        typeToInteger.put("double", new Integer(org.javarosa.core.model.Constants.DATATYPE_DECIMAL));            //xsd:
        typeToInteger.put("float", new Integer(org.javarosa.core.model.Constants.DATATYPE_DECIMAL));             //xsd:
        typeToInteger.put("dateTime", new Integer(org.javarosa.core.model.Constants.DATATYPE_DATE_TIME));        //xsd:
        typeToInteger.put("date", new Integer(org.javarosa.core.model.Constants.DATATYPE_DATE));                 //xsd:
        typeToInteger.put("time", new Integer(org.javarosa.core.model.Constants.DATATYPE_TIME));                 //xsd:
        typeToInteger.put("gYear", new Integer(org.javarosa.core.model.Constants.DATATYPE_UNSUPPORTED));         //xsd:
        typeToInteger.put("gMonth", new Integer(org.javarosa.core.model.Constants.DATATYPE_UNSUPPORTED));        //xsd:
        typeToInteger.put("gDay", new Integer(org.javarosa.core.model.Constants.DATATYPE_UNSUPPORTED));          //xsd:
        typeToInteger.put("gYearMonth", new Integer(org.javarosa.core.model.Constants.DATATYPE_UNSUPPORTED));    //xsd:
        typeToInteger.put("gMonthDay", new Integer(org.javarosa.core.model.Constants.DATATYPE_UNSUPPORTED));     //xsd:
        typeToInteger.put("boolean", new Integer(org.javarosa.core.model.Constants.DATATYPE_BOOLEAN));           //xsd:
        typeToInteger.put("base64Binary", new Integer(org.javarosa.core.model.Constants.DATATYPE_UNSUPPORTED));  //xsd:
        typeToInteger.put("hexBinary", new Integer(org.javarosa.core.model.Constants.DATATYPE_UNSUPPORTED));     //xsd:
        typeToInteger.put("anyURI", new Integer(org.javarosa.core.model.Constants.DATATYPE_UNSUPPORTED));        //xsd:
        typeToInteger.put("listItem", new Integer(org.javarosa.core.model.Constants.DATATYPE_CHOICE));           //xforms:
        typeToInteger.put("listItems", new Integer(org.javarosa.core.model.Constants.DATATYPE_CHOICE_LIST));	    //xforms:	
        typeToInteger.put(SELECTONE, new Integer(org.javarosa.core.model.Constants.DATATYPE_CHOICE));	        //non-standard	
        typeToInteger.put(SELECT, new Integer(org.javarosa.core.model.Constants.DATATYPE_CHOICE_LIST));        //non-standard
        typeToInteger.put("geopoint", new Integer(org.javarosa.core.model.Constants.DATATYPE_GEOPOINT));         //non-standard
        typeToInteger.put("barcode", new Integer(org.javarosa.core.model.Constants.DATATYPE_BARCODE));           //non-standard
        typeToInteger.put("binary#image", new Integer(org.javarosa.core.model.Constants.DATATYPE_BINARY));             //non-standard
        typeToInteger.put("binary#audio", new Integer(org.javarosa.core.model.Constants.DATATYPE_BINARY));
        typeToInteger.put("binary#video", new Integer(org.javarosa.core.model.Constants.DATATYPE_BINARY));
        return typeToInteger;
    }
    
      private static Hashtable<Integer, String> initIntegerToTypeMappings() {
        integerToType = new Hashtable<Integer, String>();
        integerToType.put(new Integer(org.javarosa.core.model.Constants.DATATYPE_TEXT), "string");
        integerToType.put(new Integer(org.javarosa.core.model.Constants.DATATYPE_INTEGER), "integer");
        integerToType.put(new Integer(org.javarosa.core.model.Constants.DATATYPE_LONG), "long");
        integerToType.put(new Integer(org.javarosa.core.model.Constants.DATATYPE_INTEGER), "int");
        integerToType.put(new Integer(org.javarosa.core.model.Constants.DATATYPE_DECIMAL), "decimal");
        integerToType.put(new Integer(org.javarosa.core.model.Constants.DATATYPE_DATE_TIME), "dateTime");
        integerToType.put(new Integer(org.javarosa.core.model.Constants.DATATYPE_DATE), "date");
        integerToType.put(new Integer(org.javarosa.core.model.Constants.DATATYPE_TIME), "time");
        integerToType.put(new Integer(org.javarosa.core.model.Constants.DATATYPE_BOOLEAN), "boolean");
        integerToType.put(new Integer(org.javarosa.core.model.Constants.DATATYPE_CHOICE), SELECTONE);
        integerToType.put(new Integer(org.javarosa.core.model.Constants.DATATYPE_CHOICE_LIST), SELECT);
        integerToType.put(new Integer(org.javarosa.core.model.Constants.DATATYPE_GEOPOINT), "geopoint");
        integerToType.put(new Integer(org.javarosa.core.model.Constants.DATATYPE_BARCODE), "barcode");
        integerToType.put(new Integer(org.javarosa.core.model.Constants.DATATYPE_BINARY), "binary");
        return integerToType;
    }

    private static Hashtable<Integer, String> initIntegerToControlTypeMappings() {
        integerToControlType = new Hashtable<Integer, String>();
        integerToControlType.put(new Integer(org.javarosa.core.model.Constants.CONTROL_INPUT), "input");
        integerToControlType.put(new Integer(org.javarosa.core.model.Constants.CONTROL_SELECT_ONE), "select1");
        integerToControlType.put(new Integer(org.javarosa.core.model.Constants.CONTROL_SELECT_MULTI), "select");
        integerToControlType.put(new Integer(org.javarosa.core.model.Constants.CONTROL_TEXTAREA), "textarea");
        integerToControlType.put(new Integer(org.javarosa.core.model.Constants.CONTROL_SECRET), "secret");
        integerToControlType.put(new Integer(org.javarosa.core.model.Constants.CONTROL_RANGE), "range");
        integerToControlType.put(new Integer(org.javarosa.core.model.Constants.CONTROL_UPLOAD), "upload");
        integerToControlType.put(new Integer(org.javarosa.core.model.Constants.CONTROL_SUBMIT), "submit");
        integerToControlType.put(new Integer(org.javarosa.core.model.Constants.CONTROL_TRIGGER), "trigger");
        integerToControlType.put(new Integer(org.javarosa.core.model.Constants.CONTROL_IMAGE_CHOOSE), "binary");
        integerToControlType.put(new Integer(org.javarosa.core.model.Constants.CONTROL_LABEL), "label");
        integerToControlType.put(new Integer(org.javarosa.core.model.Constants.CONTROL_AUDIO_CAPTURE), "binary");
        integerToControlType.put(new Integer(org.javarosa.core.model.Constants.CONTROL_VIDEO_CAPTURE), "binary");
        return integerToControlType;
    }
}
