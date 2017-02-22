/*
 * 	 This file is part of Bedspread, originally promoted and
 *	 developed at CNR-IASI. For more information visit:
 *	 https://github.com/IASI-LEKS/bedspread
 *	     
 *	 This is free software: you can redistribute it and/or modify
 *	 it under the terms of the GNU Lesser General Public License as 
 *	 published by the Free Software Foundation, either version 3 of the 
 *	 License, or (at your option) any later version.
 *	 
 *	 This software is distributed in the hope that it will be useful,
 *	 but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	 GNU Lesser General Public License for more details.
 * 
 *	 You should have received a copy of the GNU Lesser General Public License
 *	 along with this source.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.cnr.iasi.leks.bedspread.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @author gulyx
 *
 */
public class PropertyUtil {

	protected static final Object MUTEX = new Object();
	
	protected static PropertyUtil INSTANCE = null;
	
	private static final String DEFAULT_CONFIG_FILE = "config.properties";

	public static final String INTERACTION_PROTOCOL_ERROR_MESSAGE = "Error : Processing was not completed yet!!";
	
	public static final String CONFIG_FILE_LOCATION_LABEL = "bedspread.config.file.location";
	public static final String KB_LABEL =  "bedspread.kb.class";
	public static final String KB_FILE_LABEL =  "bedspread.kb.file";
	public static final String SEMANTIC_SPREAD_LABEL =  "bedspread.semanticspread.class";
	public static final String TERMINATION_POLICY_LABEL =  "bedspread.terminationpolicy.class";
	// TODO THE FOLLOWING FEATURE IS NOT SUPPORTED YET!!!
	public static final String WEIGHTING_FUNCTION_LABEL =  "bedspread.weightingfunction.class";
	
	private Properties properties;
	
	protected PropertyUtil(){
		this.properties = new Properties();
		try {
			String configFileLocation = System.getProperty(CONFIG_FILE_LOCATION_LABEL, DEFAULT_CONFIG_FILE);
			
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			InputStream s = classloader.getResourceAsStream(configFileLocation);			
			
			this.properties.load(s);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static PropertyUtil getInstance(){
		synchronized (MUTEX) {
			if ( INSTANCE == null ){
				INSTANCE = new PropertyUtil();
			}			
		}
		return INSTANCE;
	}
	
	public String getProperty(String key){
		  return this.properties.getProperty(key);
	  }

	  public String getProperty(String key, String defalutValue){
		  return this.properties.getProperty(key, defalutValue);
	  }
 
}
