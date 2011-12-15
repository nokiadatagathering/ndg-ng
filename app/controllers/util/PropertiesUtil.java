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

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesUtil {

    public static String PROPERTIES_CORE_FILE = "msm-core.properties";
    public static String SETTINGS_FILE = "msm-settings.properties";
    private static Properties core_properties = null;
    private static Properties settings_properties = null;

    private static Properties loadFileProperty(String propName) {
        Properties result = null;

        try {
            if (propName.equals(PROPERTIES_CORE_FILE)) {
                core_properties = new Configuration(PROPERTIES_CORE_FILE);
                result = core_properties;
            } else if (propName.equals(SETTINGS_FILE)) {
                settings_properties = new Configuration(SETTINGS_FILE);
                result = settings_properties;
            }
        } catch (InstantiationException ex) {
            Logger.getLogger(PropertiesUtil.class.getName()).log(Level.SEVERE, "Error reading configuration file", ex);
        }

        return result;
    }

    public static Properties getSettingsProperties() {
        return settings_properties != null
                ? settings_properties
                : loadFileProperty(SETTINGS_FILE);

    }

    public static Properties getCoreProperties() {
        return core_properties != null
                ? core_properties
                : loadFileProperty(PROPERTIES_CORE_FILE);
    }
}
