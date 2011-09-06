/*
*  Copyright (C) 2010-2011 INdT - Instituto Nokia de Tecnologia
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
package controllers.transformer;

import java.util.Collection;

import models.NdgResult;
import models.Survey;
//import org.jboss.util.Base64;


public abstract class ResultsTransformer extends Transformer {

	public static final String PHOTOS_DIR = "photos";
	public static final String UNDERLINE_SEPARATOR = "_";
	public static final String JPG_EXTENSION = ".jpg";

	protected Boolean exportWithImages;

	public ResultsTransformer(Survey survey, Boolean exportWithImages) {
		super(survey);
		this.exportWithImages = exportWithImages;
	}

	public void write(String path) {
		Collection<NdgResult> results = survey.resultCollection;
		processResults(path, results);
	}

	public void write(String path, Collection<NdgResult> results) {
		processResults(path, results);
	}

	public abstract byte[] getBytes();

	protected abstract void processResults(String path, Collection<NdgResult> results);

	/**
	 * This method stores image files from structures.
	 * NOTE: Root of the storage path is hardcoded, outside code depend on it.
	 * @return	string in format: "image_path" or "image_path[latitude,longitude]"
	 */
//	public String storeImagesAndGetValueToExport( String surveyId, int categoryId, String resultId, int fieldId,
//			ArrayList<TaggedImage> imageList )
//	{
//		String value = "";
//		if ( exportWithImages ) {
//			String imageDirPath =
//				File.separator + PHOTOS_DIR +
//				File.separator + resultId +
//				File.separator;
//			new File(surveyId + imageDirPath).mkdirs();
//
//			String imageFileNameBase = categoryId + UNDERLINE_SEPARATOR + fieldId;
//
//			if (!imageList.isEmpty()) {
//				for ( int imgIndex = 0; imgIndex < imageList.size(); imgIndex++ ) {
//					try {
//						TaggedImage image = imageList.get(imgIndex);
//						String imageFileName = imageFileNameBase + UNDERLINE_SEPARATOR + (imgIndex+1) + JPG_EXTENSION;
//						// Store image
//						FileOutputStream arqImg = new FileOutputStream(surveyId + imageDirPath + imageFileName);
//						arqImg.write(Base64.decode(image.getImageData()));
//						arqImg.close();
//						// add file path and GeoTag to returned value
//						String formattedGeoTag = "";
//						if ( image.hasGeoTag() ) {
//							formattedGeoTag = "[" + image.getLatitude() + "," + image.getLongitude() +"]";
//						}
//						value += (imageDirPath + imageFileName + formattedGeoTag + " ");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//				value = value.trim();
//			} else {
//				value = "no images";
//			}
//		} else {
//			value = "<img>";
//		}
//		return value;
//	}
}
