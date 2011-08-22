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
 */package controllers.logic;

import models.Answer;
import models.NdgResult;
import java.io.Reader;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.javarosa.xform.parse.XFormParser;
import org.kxml2.kdom.Document;

class ResultParser {

    private static final Logger log = Logger.getLogger( ResultPersister.class.getName() );
    private Collection<Answer> answerSet;
    private NdgResult result;
    private final Reader reader;

    ResultParser(Reader reader) {
        this.reader = reader;
    }

    public void parse()
    {
        Document xmlDoc = XFormParser.getXMLDocument(reader);
        
    }
    
    public NdgResult parseResult( ) {
        
        log.log( Level.INFO, "[ResultParser.parseResult]: parsing START" );


        //TODO parse result XML2 DB structure
        log.log( Level.INFO, "[ResultParser.parseResult]: parsing END" );
        return new NdgResult();
    }

    NdgResult getResult() {
        return result;
    }
}
