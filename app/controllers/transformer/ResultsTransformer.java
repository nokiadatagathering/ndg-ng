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

package controllers.transformer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collection;

import models.NdgResult;
import models.Survey;
import play.db.jpa.Blob;
//import org.jboss.util.Base64;

public abstract class ResultsTransformer extends Transformer {

    public static final String PHOTOS_DIR = "photos";
    public static final String UNDERLINE_SEPARATOR = "_";
    public static final String JPG_EXTENSION = ".jpg";
    protected Boolean exportWithImages;

    public ResultsTransformer( Survey survey, Boolean exportWithImages ) {
        super( survey );
        this.exportWithImages = exportWithImages;
    }

    public ResultsTransformer( Survey survey, Collection<NdgResult> results, Boolean exportWithImages ) {
        super( survey, results );
        this.exportWithImages = exportWithImages;
    }

    public abstract byte[] getBytes();

    public String storeImagesAndGetValueToExport( String surveyId, String resultId, Long answerId, Blob data ) {
        String value = "";
        if ( exportWithImages ) {
            String imageDirPath =
                   File.separator + PHOTOS_DIR
                   + File.separator + resultId
                   + File.separator;
            new File( surveyId + imageDirPath ).mkdirs();

            if ( data != null ) {
                try {
                    String imageFileName = answerId + JPG_EXTENSION;
                    // Store image
                    FileOutputStream outImg = new FileOutputStream( surveyId + imageDirPath + imageFileName );//stream is closed after coping
                    copyFile( new FileInputStream( data.getFile() ) , outImg );

                    value += (imageDirPath + imageFileName);
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
                value = value.trim();
            } else {
                value = "no images";
            }
        } else {
            value = "<img>";
        }
        return value;
    }

    private static void copyFile( FileInputStream fIn, FileOutputStream fOut) {
        FileChannel fIChan = null, fOChan = null;
        long fSize;
        MappedByteBuffer mBuf;
        try {
            fIChan = fIn.getChannel();
            fOChan = fOut.getChannel();

            fSize = fIChan.size();
            mBuf = fIChan.map( FileChannel.MapMode.READ_ONLY, 0, fSize );

            fOChan.write( mBuf ); // this copies the file

        } catch ( IOException exc ) {
        } catch ( ArrayIndexOutOfBoundsException exc ) {
            System.out.println( "Usage: Copy from to" );
        } finally {
            try {
                fIChan.close();
            } catch ( Exception ex ) {
            }
            try {
                fIn.close();
            } catch ( Exception ex ) {
            }
            try {
                fOChan.close();
            } catch ( Exception ex ) {
            }
            try {
                fOut.close();
            } catch ( Exception ex ) {
            }
        }
    }
}
