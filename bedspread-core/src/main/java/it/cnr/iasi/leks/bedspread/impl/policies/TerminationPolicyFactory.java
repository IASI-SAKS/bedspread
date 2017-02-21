package it.cnr.iasi.leks.bedspread.impl.policies;

import it.cnr.iasi.leks.bedspread.TerminationPolicy;
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

	public TerminationPolicy getTerminationPolicy() throws InstantiationException, IllegalAccessException, ClassNotFoundException{		
		TerminationPolicy tp = null;
		
		PropertyUtil prop = PropertyUtil.getInstance();
		String tpClassName = prop.getProperty(PropertyUtil.TERMINATION_POLICY_LABEL);
		
		if ( tpClassName != null ){
			ClassLoader loader = ClassLoader.getSystemClassLoader();
			tp = (TerminationPolicy) loader.loadClass(tpClassName).newInstance();			
		}else{
			tp = new SimpleTerminationPolicy();
		}
		
		return tp;
	}

	public TerminationPolicy getTerminationPolicy(int maxNumberOfIterations){		
		TerminationPolicy tp = new SimpleTerminationPolicy(maxNumberOfIterations);
		return tp;
	}

}
