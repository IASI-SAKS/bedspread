/*
 * 	 This file is part of Bedspread, originally promoted and
 *	 developed at CNR-IASI. For more information visit:
 *	 https://github.com/IASI-LEKS/bedspread
 *	     
 *	 This is free software: you can redistribute it and/or modify
 *	 it under the terms of the GNU General Public License as 
 *	 published by the Free Software Foundation, either version 3 of the 
 *	 License, or (at your option) any later version.
 *	 
 *	 This software is distributed in the hope that it will be useful,
 *	 but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	 GNU General Public License for more details.
 * 
 *	 You should have received a copy of the GNU General Public License
 *	 along with this source.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.cnr.iasi.leks.bedspread.tests;

import org.junit.Assert;
import org.junit.Test;

import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.debspread.tests.util.PropertyUtilNoSingleton;

/**
 * 
 * @author gulyx
 *
 */
public class PropertyUtilTest {

	@Test
	public void loadPropertyFromDefaultLocation(){
		String systemPropertyFile = System.getProperties().getProperty(PropertyUtil.CONFIG_FILE_LOCATION_LABEL);
		if (systemPropertyFile != null){
			Assert.assertTrue("Remove '"+PropertyUtil.CONFIG_FILE_LOCATION_LABEL+"' among the configured System.properties", false);
		}
		PropertyUtil prop = PropertyUtilNoSingleton.getInstance();
		Assert.assertTrue(true);
	}
	
	@Test
	public void loadPropertyFromSystemProperties(){
		String testLabel = "this-is-a-test";
		String testExpectedValue = "this-is-a-test";
		
		String testPropertyFile = "configTest.properties";
		System.getProperties().put(PropertyUtil.CONFIG_FILE_LOCATION_LABEL, testPropertyFile);
		
		PropertyUtil prop = PropertyUtilNoSingleton.getInstance();
		String testValue = prop.getProperty(testLabel);
		
		System.getProperties().remove(PropertyUtil.CONFIG_FILE_LOCATION_LABEL);

		Assert.assertTrue(testValue.equalsIgnoreCase(testExpectedValue));
	}
}
