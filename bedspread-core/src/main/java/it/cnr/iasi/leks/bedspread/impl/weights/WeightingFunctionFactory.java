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
package it.cnr.iasi.leks.bedspread.impl.weights;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import it.cnr.iasi.leks.bedspread.WeightingFunction;
import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.rdf.impl.KBFactory;

/**
 * 
 * @author gulyx
 *
 */
public class WeightingFunctionFactory {

	protected static WeightingFunctionFactory FACTORY = null;
	
	protected WeightingFunctionFactory(){		
	}
	
	public static synchronized WeightingFunctionFactory getInstance(){
		if (FACTORY == null){
			FACTORY = new WeightingFunctionFactory();
		}
		return FACTORY;
	}

	public WeightingFunction getWeightingFunction() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, SecurityException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException{
		
		WeightingFunction wf = null;
		KnowledgeBase kb = null;
		
		PropertyUtil prop = PropertyUtil.getInstance();
		kb = this.configureKB(prop);
		
		if ( kb != null ){
			wf = this.getWeightingFunction(kb);
		}else{
			wf = new DefaultWeightingFunction();
		}
		
		return wf;
	}

	public WeightingFunction getWeightingFunction(KnowledgeBase kb) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		
		WeightingFunction wf = null;
		
		PropertyUtil prop = PropertyUtil.getInstance();
		String wfClassName = prop.getProperty(PropertyUtil.WEIGHTING_FUNCTION_LABEL);
		
		if ( wfClassName != null ){
			ClassLoader loader = ClassLoader.getSystemClassLoader();
			Class<?> wfClass = loader.loadClass(wfClassName);
			Constructor<?> constructor;
			try {
				constructor = wfClass.getConstructor(new Class[]{KnowledgeBase.class});
				wf = (WeightingFunction) constructor.newInstance(kb);			
			} catch (NoSuchMethodException e) {
				constructor = wfClass.getConstructor();
				wf = (WeightingFunction) constructor.newInstance();			
			}
		}else{
			wf = new DefaultWeightingFunction();
		}
		
		return wf;
	}
	
	private KnowledgeBase configureKB(PropertyUtil prop) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		KnowledgeBase localKB = KBFactory.getInstance().getKnowledgeBase(prop); 
	
		return localKB;
	}

}
