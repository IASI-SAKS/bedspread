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
package it.cnr.iasi.leks.bedspread.impl.policies;

import it.cnr.iasi.leks.bedspread.ExecutionPolicy;
import it.cnr.iasi.leks.bedspread.config.PropertyUtil;

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

	public ExecutionPolicy getExecutionPolicy() throws InstantiationException, IllegalAccessException, ClassNotFoundException{		
		ExecutionPolicy policy = null;
		
		PropertyUtil prop = PropertyUtil.getInstance();
		String policyClassName = prop.getProperty(PropertyUtil.TERMINATION_POLICY_LABEL);
		
		if ( policyClassName != null ){
			ClassLoader loader = ClassLoader.getSystemClassLoader();
			policy = (ExecutionPolicy) loader.loadClass(policyClassName).newInstance();			
		}else{
			policy = new SimpleExecutionPolicy();
		}
		
		return policy;
	}

	public ExecutionPolicy getExecutionPolicy(int maxNumberOfIterations){		
		ExecutionPolicy policy = new SimpleExecutionPolicy(maxNumberOfIterations);
		return policy;
	}

}
