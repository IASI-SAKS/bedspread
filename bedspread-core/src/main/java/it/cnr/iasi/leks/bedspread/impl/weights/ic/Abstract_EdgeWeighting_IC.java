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
package it.cnr.iasi.leks.bedspread.impl.weights.ic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.iasi.leks.bedspread.AbstractSemanticSpread;
import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.exceptions.impl.UnexpectedValueException;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFTriple;

/**
 * The Implementation of this class refers to the following publication
 * Schuhumacher M., Ponzetto S.P. 
 * Knowledge-based Graph Document Modeling
 * WSDM'14 February 24-28, 2014, New York, New York, USA.
 *  
 * @author ftaglino
 *
 */
public abstract class Abstract_EdgeWeighting_IC{

	protected KnowledgeBase kb;
	
	protected final Logger logger = LoggerFactory.getLogger(Abstract_EdgeWeighting_IC.class);
	
	public Abstract_EdgeWeighting_IC(KnowledgeBase kb) {
		this.kb = kb;
	}

	/**
	 * Compute the frequence of the triples having pred as the predicate with respect to all the triples in the kb
	 * @param resource
	 * @return
	 */
	protected double predicateProbability(AnyResource resource) {
		double result = 0.0;
		double total_triple = this.kb.countAllTriples();
		double total_triple_by_predicate = this.kb.countTriplesByPredicate(resource);
		result = total_triple_by_predicate/total_triple;
		return result;
	}  
		
	/**
	 * Compute the Information Content of a predicate 
	 * @param resource
	 * @return
	 */
	protected double predicate_IC(AnyResource resource) {
		double result = 0.0;
		result = - Math.log(predicateProbability(resource));
		return result;
	}	
	
	public double computeNormalizedEdgeWeight(RDFTriple edge) throws UnexpectedValueException{
		double max = this.getMaxWeight();
		if (max <= 0){
			throw new UnexpectedValueException("Unexpected Value from Abstract_EdgeWeighting_IC.getMax_weight(): " + max);
		}
		double edgeWeight = this.computeEdgeWeight(edge);
		double norm = edgeWeight / max;
		return norm;
	}
	
	public abstract double computeEdgeWeight(RDFTriple edge);	
	protected abstract double computeMaxWeight();
	
	private double getMaxWeight(){
		double max;
		String notDefined = "Undefined";
		String value = PropertyUtil.getInstance().getProperty(PropertyUtil.EDGE_WEIGHTING_IC_MAX_WEIGHT_LABEL,notDefined);
		if (value.equals(notDefined)){
			max = this.computeMaxWeight();			
		}else{
			max = Double.parseDouble(value);
		}
		return max;
	}
}
