/*
 *  Copyright (C) 2011  INdT - Instituto Nokia de Tecnologia
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
