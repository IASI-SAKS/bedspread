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
package it.cnr.iasi.leks.bedspread;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.impl.weights.DefaultWeightingFunction;
import it.cnr.iasi.leks.bedspread.impl.weights.SemanticWeightingFunction;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFGraph;

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

	public WeightingFunction getWeightingFunction() throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException{
		
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

	public WeightingFunction getWeightingFunction(KnowledgeBase kb){
		WeightingFunction wf = new SemanticWeightingFunction(kb);		
		
		return wf;
	}
	
	private KnowledgeBase configureKB(PropertyUtil prop) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		KnowledgeBase localKB = null; 
		
		String kbFileName = prop.getProperty(PropertyUtil.KB_FILE_LABEL);
		String kbClassName = prop.getProperty(PropertyUtil.KB_LABEL);
		if (kbClassName != null){
			ClassLoader loader = ClassLoader.getSystemClassLoader();
			localKB = (KnowledgeBase) loader.loadClass(kbClassName).newInstance();
			
		}else{
			if (kbFileName != null){
				Reader kbReader;
					kbReader = new FileReader(kbFileName);
					localKB = new RDFGraph(kbReader);
			}
		}
		return localKB;
	}

}
