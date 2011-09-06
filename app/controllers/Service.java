/*
*  Copyright (C) 2010  INdT - Instituto Nokia de Tecnologia
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

import flexjson.JSONSerializer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.apache.commons.logging.LogFactory;



import org.apache.commons.logging.Log;
import play.mvc.Controller;


public class Service extends Controller {
	
	
	//private MSMBusinessDelegate msmBD = new MSMBusinessDelegate();
	public static final String UNEXPECTED_SERVER_EXCEPTION = "UNEXPECTED_SERVER_EXCEPTION";
	public static final String CSV = ".csv";
	public static final String XLS = ".xls";
	public static final String ZIP = ".zip";
	
	private static Log log = LogFactory.getLog(Service.class);

	/**
	 * 
	 * @param format
	 * @param surveyId
	 * @param exportWithImages
	 * @return
	 * @throws IOException 
	 */
	public static void get( String ids/*String username, String format, String surveyId,
                            Boolean exportWithImages*/) {
        try {
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);

            result.write( "sfjahfhaskjfhaskfhaskh" );
            InputStream inputStream = new ByteArrayInputStream(result.toString().getBytes("UTF-8"));
            renderBinary(inputStream, "survey.xml");
        } catch (Exception ex) {
            Logger.getLogger(Management.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public static void preparezip( String ids, String fileFormat/*String username, String format, String surveyId,
                            Boolean exportWithImages*/) {
        String[] resultsIds = ids.split( ",");

        for( int i = 0; i< resultsIds.length; i++ ) {
//            CSVTransformer transformer = new CSVTransformer(survey, exportWithImages);
//			fileContent = transformer.getBytes();
//    		fileType = CSV; 
        }

        if( CSV.equals( fileFormat ) ) {
            
        } else if ( XLS.equals( fileFormat ) ) {

        }
        
        
        JSONSerializer surveyListSerializer = new JSONSerializer();
        surveyListSerializer.include( "*" ).rootName( "surveysCount" );
        renderJSON( surveyListSerializer.serialize( "Random ID" ) );
    }
//		final String SURVEY = "survey";
//		String fileType = "";
//		String strFileContent = null;
//		byte[] fileContent = null;
//		
//		if (exportWithImages == true) {
//			new File(surveyId).mkdir();
//			new File(surveyId + File.separator + "photos").mkdir();
//		}
//		
//		try{
//			SurveyXML survey = msmBD.loadSurveyAndResultsDB(username, surveyId);
//			
//			if (CSV.equalsIgnoreCase(format)){
//				CSVTransformer transformer = new CSVTransformer(survey, exportWithImages);
//				fileContent = transformer.getBytes();
//				fileType = CSV; 
//			} else if (XLS.equalsIgnoreCase(format)){
//				ExcelTransformer transformer = new ExcelTransformer(survey, exportWithImages);
//				fileContent = transformer.getBytes();
//				fileType = XLS;
//			}
//			strFileContent = Base64Encode.base64Encode(fileContent);
//		} catch (MSMApplicationException e){
//			e.printStackTrace();
//			throw new NDGServerException(e.getErrorCode());
//		} catch (MSMSystemException e){
//			e.printStackTrace();
//			throw new NDGServerException(e.getErrorCode());
//		} catch (Exception e){
//			e.printStackTrace();
//			throw new NDGServerException(UNEXPECTED_SERVER_EXCEPTION);
//		}
//		
//		if (exportWithImages == true)
//		{
//			zipSurvey(surveyId, fileContent, fileType);
//			File zipFile = new File(SURVEY + surveyId + ZIP);
//			File zipDir =  new File(surveyId);
//			try {
//				fileContent = getBytesFromFile(zipFile);
//				strFileContent = Base64Encode.base64Encode(fileContent);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			
//			zipFile.delete();
//			deleteDir(zipDir);
//		}
//		
//		return  strFileContent;
//	}
//
//	private static boolean deleteDir(File dir) {
//		if (dir.isDirectory()) { 
//			String[] children = dir.list();
//			for (int i=0; i<children.length; i++) {
//				boolean success = deleteDir(new File(dir, children[i]));
//				
//				if (!success) {
//					return false;
//				}
//			}
//		} 
//		return dir.delete();
//	} 	
//	
//	
//	private static byte[] getBytesFromFile(File file) throws IOException {
//		InputStream is = new FileInputStream(file);
//		
//		long length = file.length(); 
//		
//		byte[] bytes = new byte[(int)length]; 
//		
//		int offset = 0;
//		int numRead = 0;
//		
//		while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
//			offset += numRead;
//		} 
//		
//		if (offset < bytes.length) {
//			throw new IOException("Could not completely read file "+file.getName());
//		} 
//		
//		is.close();
//		
//		return bytes; 	
//	}
//	
//	private void zipSurvey(String surveyId, byte[] fileContent, String fileType)
//	{
//		final String SURVEY = "survey";
//		FileOutputStream arqExport;
//		try
//		{
//			if (fileType.equals(XLS)) {
//				arqExport = new FileOutputStream(surveyId + File.separator + SURVEY + surveyId + XLS);
//				arqExport.write(fileContent);
//				arqExport.close();
//			}
//			else if (fileType.equals(CSV)) {
//				arqExport = new FileOutputStream(surveyId + File.separator + SURVEY + surveyId + CSV);
//				arqExport.write(fileContent);
//				arqExport.close();
//			}
//			
//			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(SURVEY + surveyId + ZIP));
//			
//			zipDir(surveyId, zos);
//			
//			zos.close();
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}		
//	}
//	
//	private void zipDir(String dir2zip, ZipOutputStream zos) 
//	{ 
//		try 
//		{ 
//	     
//           File zipDir = new File(dir2zip); 
//	     
//	        String[] dirList = zipDir.list(); 
//	        byte[] readBuffer = new byte[2156]; 
//	        int bytesIn = 0; 
//	     
//	        for(int i=0; i<dirList.length; i++) 
//	        { 
//	            File f = new File(zipDir, dirList[i]); 
//		        if(f.isDirectory()) 
//		        { 
//		            String filePath = f.getPath(); 
//		            zipDir(filePath, zos); 
//		            continue; 
//		        } 
//
//	            FileInputStream fis = new FileInputStream(f); 
// 
//	            ZipEntry anEntry = new ZipEntry(f.getPath()); 
//	             
//	            zos.putNextEntry(anEntry); 
//	             
//	            while((bytesIn = fis.read(readBuffer)) != -1) 
//	            { 
//	                zos.write(readBuffer, 0, bytesIn); 
//	            } 
//
//	           fis.close(); 
//	        } 
//		} 
//		catch(Exception e) 
//		{
//			e.printStackTrace();
//		}
//	} 



}
