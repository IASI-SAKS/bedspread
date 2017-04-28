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
package it.cnr.iasi.leks.bedspread.impl.policies;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import it.cnr.iasi.leks.bedspread.ExecutionPolicy;
import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.rdf.impl.KBFactory;

/**
 * 
 * @author gulyx
 *
 */
public class ExecutionPolicyFactory {
	
	protected static ExecutionPolicyFactory FACTORY = null;
			
	protected ExecutionPolicyFactory(){		
	}
	
	public static synchronized ExecutionPolicyFactory getInstance(){
		if (FACTORY == null){
			FACTORY = new ExecutionPolicyFactory();
		}
		return FACTORY;
	}			
	
	public ExecutionPolicy getExecutionPolicy() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		KnowledgeBase kb = null;
		
		PropertyUtil prop = PropertyUtil.getInstance();
		kb = KBFactory.getInstance().getKnowledgeBase(prop);
		
		ExecutionPolicy ep = this.getExecutionPolicy(kb);
		
		return ep;
	}

	public ExecutionPolicy getExecutionPolicy(KnowledgeBase kb) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		
		ExecutionPolicy ep = null;
		
		PropertyUtil prop = PropertyUtil.getInstance();
		String epClassName = prop.getProperty(PropertyUtil.EXECUTION_POLICY_LABEL);
		
		if ( epClassName != null ){
			ClassLoader loader = ClassLoader.getSystemClassLoader();
			Class<?> epClass = loader.loadClass(epClassName);
			Constructor<?> constructor;
			try {
				constructor = epClass.getConstructor(new Class[]{KnowledgeBase.class});
				ep = (ExecutionPolicy) constructor.newInstance(kb);			
			} catch (NoSuchMethodException e) {
				constructor = epClass.getConstructor();
				ep = (ExecutionPolicy) constructor.newInstance();			
			}
		}else{
			ep = new DefaultExecutionPolicy();
		}
		
		return ep;
	}


	public ExecutionPolicy getExecutionPolicy(int maxNumberOfIterations){		
		ExecutionPolicy policy = new SimpleExecutionPolicy(maxNumberOfIterations);
		return policy;
	}

}
