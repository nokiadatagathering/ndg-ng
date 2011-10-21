package controllers;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import models.NdgLanguage;
import play.db.jpa.Blob;
import play.mvc.Controller;
import play.mvc.Http;


public class LanguageUploader extends Controller {

    private static final long serialVersionUID = 1L;
    private static final String LOCALE_FILE_PATTERN= "messages_%s.properties";

    public static void upload( String locale_name, String locale_str, File locale_file, File font_file ){

        LocalizationStructure structure = new LocalizationStructure();

        structure.setName( locale_name );
        structure.setLocale( locale_str );
        structure.setLocaleFile( locale_file );
        structure.setFontFile( font_file );

        if( structureCorrect(structure) ) {
            try {
                NdgLanguage language = NdgLanguage.find( "byLocaleString", locale_str ).first();
                if ( language == null ) {
                    language = new NdgLanguage();
                    language.localeString = locale_str;
                }

                language.name= locale_name;
                language.translationFile = new Blob();
                language.translationFile.set( new FileInputStream( locale_file ) , "text/plain" );
                if ( font_file != null ) {
                    language.fontFile = new Blob();
                    language.fontFile.set( new FileInputStream( font_file ), "application/binary" );
                }

                language.save();
            } catch ( FileNotFoundException ex ) {
                error( Http.StatusCode.INTERNAL_ERROR, "Missing file!" );
            }
            renderHtml( "<tr><td>Localization file successfully uploaded</td></tr>" );
        }
        renderHtml( "<tr><td><p style='color:red;' >Upload failed</p></td></tr>" );
    }

    static private boolean structureCorrect(LocalizationStructure structure){
        if(structure == null){
            return false;
        }

        if(structure.getLocale() == null || structure.getLocale().isEmpty() ||
            structure.getName() == null || structure.getName().isEmpty()){
            return false;
        }

        String shortLocale = structure.getLocale().substring(0, 2);
        if(!String.format(LOCALE_FILE_PATTERN, shortLocale).equals(structure.getLocaleFile().getName())){
            return false;
        }
        return true;
    }


    static class LocalizationStructure {
        private String name = "";
        private String locale = "";
        private File localeFile = null;
        private File fontFile = null;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLocale() {
        return locale;
    }

        public void setLocale(String locale) {
            this.locale = locale;
        }

        public File getLocaleFile() {
            return localeFile;
        }

        public void setLocaleFile(File localeFile) {
            this.localeFile = localeFile;
        }

        public File getFontFile() {
            return fontFile;
        }

        public void setFontFile(File fontFile) {
            this.fontFile = fontFile;
        }
    }
}
