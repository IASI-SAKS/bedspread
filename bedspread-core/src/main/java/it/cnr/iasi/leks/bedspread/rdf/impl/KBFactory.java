/*
 * 	 This file is part of Bedspread, originally promoted and
 *	 developed at CNR-IASI. For more information visit:
 *	 http://leks.iasi.cnr.it/tools/bedspread
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
package it.cnr.iasi.leks.bedspread.rdf.impl;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.rdf.sparqlImpl.DBpediaKB;

/**
 * 
 * @author gulyx
 *
 */
public class KBFactory {

	protected static KBFactory FACTORY = null;
	
	protected KBFactory(){		
	}
	
	public static synchronized KBFactory getInstance(){
		if (FACTORY == null){
			FACTORY = new KBFactory();
		}
		return FACTORY;
	}

	public KnowledgeBase getKnowledgeBase() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException{		
		KnowledgeBase localKB = null; 

		PropertyUtil prop = PropertyUtil.getInstance();
		localKB = this.getKnowledgeBase(prop);
		
		return localKB;
	}

	
	public KnowledgeBase getKnowledgeBase(PropertyUtil prop) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		
		KnowledgeBase localKB = null; 
		
		String kbFileName = prop.getProperty(PropertyUtil.KB_FILE_LABEL);
		String kbClassName = prop.getProperty(PropertyUtil.KB_LABEL);
		if (kbClassName != null){
			if (kbClassName.equals(DBpediaKB.class.getCanonicalName())){
				localKB = DBpediaKB.getInstance();
			}else{
				ClassLoader loader = ClassLoader.getSystemClassLoader();
				localKB = (KnowledgeBase) loader.loadClass(kbClassName).newInstance();
			}
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
