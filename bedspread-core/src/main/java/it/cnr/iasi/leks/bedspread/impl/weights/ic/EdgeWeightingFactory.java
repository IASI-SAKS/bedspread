package it.cnr.iasi.leks.bedspread.impl.weights.ic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.exceptions.impl.UndefinedPropertyException;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;

/**
 * 
 * @author gulyx
 *
 */
public class EdgeWeightingFactory {
	
	protected static EdgeWeightingFactory FACTORY = null;

	protected static final String ERROR_MSG = "Undefined property:" + PropertyUtil.EDGE_WEIGHTING_IC_LABEL; 
	
	protected EdgeWeightingFactory(){}

	public static synchronized EdgeWeightingFactory getInstance(){
		if (FACTORY == null){
			FACTORY = new EdgeWeightingFactory();
		}
		return FACTORY;
	}

	public EdgeWeighting_IC getEdgeWeighting_IC(KnowledgeBase kb){
		EdgeWeighting_IC ew = new EdgeWeighting_IC(kb);
		return ew;
	}
	
	public EdgeWeighting_JointIC getEdgeWeighting_JointIC(KnowledgeBase kb){
		EdgeWeighting_JointIC ew = new EdgeWeighting_JointIC(kb);
		return ew;
	}

	public EdgeWeighting_CombIC getEdgeWeighting_CombIC(KnowledgeBase kb){
		EdgeWeighting_CombIC ew = new EdgeWeighting_CombIC(kb);
		return ew;
	}

	public EdgeWeighting_IC_PMI getEdgeWeighting_IC_PMI(KnowledgeBase kb){
		EdgeWeighting_IC_PMI ew = new EdgeWeighting_IC_PMI(kb);
		return ew;
	}
	
	public Abstract_EdgeWeighting_IC getAbstract_EdgeWeighting_IC(KnowledgeBase kb) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, UndefinedPropertyException{
		
		Abstract_EdgeWeighting_IC ew = null;
		
		PropertyUtil prop = PropertyUtil.getInstance();
		String ewClassName = prop.getProperty(PropertyUtil.EDGE_WEIGHTING_IC_LABEL);
		
		if ( ewClassName != null ){
			ClassLoader loader = ClassLoader.getSystemClassLoader();
			Class<?> ewClass = loader.loadClass(ewClassName);
			Constructor<?> constructor = ewClass.getConstructor(new Class[]{KnowledgeBase.class});
			ew = (Abstract_EdgeWeighting_IC) constructor.newInstance(kb);			
		}else{
			UndefinedPropertyException e = new UndefinedPropertyException(EdgeWeightingFactory.ERROR_MSG);
			throw e;
		}
		
		return ew;
	}

}
