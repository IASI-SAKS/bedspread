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
package it.cnr.iasi.leks.bedspread.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import it.cnr.iasi.leks.bedspread.AbstractSemanticSpread;
import it.cnr.iasi.leks.bedspread.ComputationStatusCallback;
import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.ExecutionPolicy;
import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;

/**
 * 
 * @author gulyx
 *
 */
public class SemanticSpreadFactory {

	protected static SemanticSpreadFactory FACTORY = null;
	
	protected SemanticSpreadFactory(){		
	}
	
	public static synchronized SemanticSpreadFactory getInstance(){
		if (FACTORY == null){
			FACTORY = new SemanticSpreadFactory();
		}
		return FACTORY;
	}

	public AbstractSemanticSpread getSemanticSpread(Node origin, KnowledgeBase kb, ExecutionPolicy term, String id, ComputationStatusCallback callback) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		AbstractSemanticSpread semSpread = this.configureSemanticSpread(origin, kb, term);
		semSpread.setCallback(id, callback);
		return semSpread;
	}

	public AbstractSemanticSpread getSemanticSpread(Node origin, KnowledgeBase kb) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		AbstractSemanticSpread semSpread = this.configureSemanticSpread(origin, kb, null);
		return semSpread;
	}

	public AbstractSemanticSpread getSemanticSpread(Node origin, KnowledgeBase kb, String id, ComputationStatusCallback callback) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		AbstractSemanticSpread semSpread = this.configureSemanticSpread(origin, kb, null);
		semSpread.setCallback(id, callback);
		return semSpread;
	}

	public AbstractSemanticSpread getSemanticSpread(Node origin, KnowledgeBase kb, ExecutionPolicy term) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		AbstractSemanticSpread semSpread = this.configureSemanticSpread(origin, kb, term);
		return semSpread;
	}

	private AbstractSemanticSpread configureSemanticSpread(Node origin, KnowledgeBase kb, ExecutionPolicy term) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		AbstractSemanticSpread semSpread = null; 
		
		String semSpreadClassName = PropertyUtil.getInstance().getProperty(PropertyUtil.SEMANTIC_SPREAD_LABEL);
		if (semSpreadClassName != null){
			Class<?> semSpreadClass = Class.forName(semSpreadClassName);
			Constructor<?> constructor;
			if (term != null){
				constructor = semSpreadClass.getConstructor(new Class[]{Node.class, KnowledgeBase.class, ExecutionPolicy.class});
				semSpread = (AbstractSemanticSpread) constructor.newInstance(origin, kb, term);
			}else{
				constructor = semSpreadClass.getConstructor(new Class[]{Node.class, KnowledgeBase.class});				
				semSpread = (AbstractSemanticSpread) constructor.newInstance(origin, kb);
			}	
		}else{
			if (term != null){
				semSpread = new SimpleSemanticSpread(origin, kb, term);
			}else{
				semSpread = new SimpleSemanticSpread(origin, kb);				
			}	
		}
		return semSpread;
	}
	
	
}
