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

package controllers.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtilities {

    public static final String CSV = ".csv";
    public static final String XLS = ".xls";
    public static final String ZIP = ".zip";
    public static final String SURVEY = "survey";

    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead = 0;

        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();

        return bytes;
    }

    public static void zipSurvey(String surveyId, byte[] fileContent, String fileType, String outputPath) {
        FileOutputStream arqExport;
        try {
            if (fileType.equals(XLS)) {
                arqExport = new FileOutputStream(surveyId + File.separator + SURVEY + surveyId + XLS);
                arqExport.write(fileContent);
                arqExport.close();
            } else if (fileType.equals(CSV)) {
                arqExport = new FileOutputStream(surveyId + File.separator + SURVEY + surveyId + CSV);
                arqExport.write(fileContent);
                arqExport.close();
            }

            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outputPath));

            zipDir(surveyId, zos);

            zos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void zipDir(String dir2zip, ZipOutputStream zos) {
        try {
            File zipDir = new File(dir2zip);

            String[] dirList = zipDir.list();
            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;

            for (int i = 0; i < dirList.length; i++) {
                File f = new File( zipDir, dirList[i] );
                
                if (f.isDirectory()) {
                    String filePath = f.getPath();
                    zipDir(filePath, zos);
                    continue;
                }

                FileInputStream fis = new FileInputStream(f);

                ZipEntry anEntry = new ZipEntry(f.getPath());
                zos.putNextEntry(anEntry);

                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }

                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));

                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }
}
