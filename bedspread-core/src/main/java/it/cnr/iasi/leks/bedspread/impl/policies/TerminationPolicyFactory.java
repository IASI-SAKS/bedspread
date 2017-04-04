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

public class TerminationPolicyFactory {
	
	protected static TerminationPolicyFactory FACTORY = null;
			
	protected TerminationPolicyFactory(){		
	}
	
	public static synchronized TerminationPolicyFactory getInstance(){
		if (FACTORY == null){
			FACTORY = new TerminationPolicyFactory();
		}
		return FACTORY;
	}			

	public ExecutionPolicy getTerminationPolicy() throws InstantiationException, IllegalAccessException, ClassNotFoundException{		
		ExecutionPolicy tp = null;
		
		PropertyUtil prop = PropertyUtil.getInstance();
		String tpClassName = prop.getProperty(PropertyUtil.TERMINATION_POLICY_LABEL);
		
		if ( tpClassName != null ){
			ClassLoader loader = ClassLoader.getSystemClassLoader();
			tp = (ExecutionPolicy) loader.loadClass(tpClassName).newInstance();			
		}else{
			tp = new SimpleTerminationPolicy();
		}
		
		return tp;
	}

	public ExecutionPolicy getTerminationPolicy(int maxNumberOfIterations){		
		ExecutionPolicy tp = new SimpleTerminationPolicy(maxNumberOfIterations);
		return tp;
	}

}
