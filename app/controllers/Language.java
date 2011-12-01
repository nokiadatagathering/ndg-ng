package controllers;

import java.io.File;
import java.util.Collection;
import models.NdgLanguage;
import play.mvc.Controller;
import play.mvc.Http;


public class Language extends Controller {

    public static void list() {
        StringBuilder buffer = new StringBuilder();

        Collection<NdgLanguage> languages = NdgLanguage.findAll();

        for (NdgLanguage language : languages) {
            buffer.append( language.name ).append( " " ).append( language.localeString ).append( '\n');
        }

        renderText( buffer.toString() );
    }

    public static void languageText( String locale) {
        if ( locale == null) {
            error(Http.StatusCode.BAD_REQUEST, "Missing 'locale' parameter");
        }
        NdgLanguage language = NdgLanguage.find( "byLocaleString", locale ).first();

        File translations = language.translationFile.getFile();
        renderBinary( translations );
    }

    public static void languageFont( String locale ) {
        if ( locale == null) {
            error(Http.StatusCode.BAD_REQUEST, "Missing 'locale' parameter");
        }
        NdgLanguage language = NdgLanguage.find( "byLocaleString", locale ).first();

        try {
            if( language!= null && language.fontFile != null && language.fontFile.getFile() != null) {
                renderBinary( language.fontFile.getFile() );
            } else {
                error( Http.StatusCode.NOT_FOUND, "Font file not found" );
            }
        } catch ( Exception ex ) {
            error( Http.StatusCode.NOT_FOUND, "Font file not found" );
        }
    }
}
