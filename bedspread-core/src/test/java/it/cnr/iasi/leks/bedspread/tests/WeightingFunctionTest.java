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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import it.cnr.iasi.leks.bedspread.WeightingFunction;
import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.impl.weights.DefaultWeightingFunction;
import it.cnr.iasi.leks.bedspread.impl.weights.SemanticWeightingFunction;
import it.cnr.iasi.leks.bedspread.impl.weights.WeightingFunctionFactory;
import it.cnr.iasi.leks.bedspread.tests.util.PropertyUtilNoSingleton;
import junit.framework.Assert;

/**
 * 
 * @author gulyx
 *
 */
public class WeightingFunctionTest {

	@Test
	public void testWeightingFunctionFactoryDefault() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{		
		String testPropertyFile = "configTest.properties";
		System.getProperties().put(PropertyUtil.CONFIG_FILE_LOCATION_LABEL, testPropertyFile);
		this.configurePropertyUtilInstance();
		
		WeightingFunctionFactory wf = WeightingFunctionFactory.getInstance();		
		WeightingFunction w = wf.getWeightingFunction();

		System.getProperties().remove(PropertyUtil.CONFIG_FILE_LOCATION_LABEL);
		
		Assert.assertTrue( w instanceof DefaultWeightingFunction );		
	}

	@Test
	public void testWeightingFunctionFactoryConf() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		String testPropertyFile = "config.properties";
		System.getProperties().put(PropertyUtil.CONFIG_FILE_LOCATION_LABEL, testPropertyFile);
		this.configurePropertyUtilInstance();
		
		WeightingFunctionFactory wf = WeightingFunctionFactory.getInstance();
		WeightingFunction w = wf.getWeightingFunction();

		System.getProperties().remove(PropertyUtil.CONFIG_FILE_LOCATION_LABEL);
		
		Assert.assertTrue( w instanceof SemanticWeightingFunction );
	}

	
	private void configurePropertyUtilInstance (){
		// It simply ensures that a ProperyUtil instance always exists
		PropertyUtilNoSingleton.getInstance();
	}
}
