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

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.iasi.leks.bedspread.AbstractSemanticSpread;
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
public class EdgeWeighting_IC extends Abstract_EdgeWeighting_IC{

	private static EdgeWeighting_IC instance = null;
	
	public EdgeWeighting_IC(KnowledgeBase kb) {
		super(kb);
		computeMaxWeight();
		//max_weight = 19.801538998683533d;
	}
	
	public synchronized static EdgeWeighting_IC getInstance(KnowledgeBase kb){
    	if (instance == null){
    		instance = new EdgeWeighting_IC(kb);
    	}
    	return instance;
    }

	/**
	 * Compute the information content of an edge, which is identified by a triple
	 * 
	 * edgeWeight_IC
	 *  
	 * @param edge
	 * @return
	 */
	@Override
	public double computeEdgeWeight(RDFTriple edge) {
		double result = 0.0;
		result = this.predicate_IC(edge.getTriplePredicate());
		return result/max_weight;
	}
	
	@Override
	protected void computeMaxWeight() {
		double result = 0.0d;
		Set<AnyResource> allPredicates = this.kb.getAllPredicates();
		for(AnyResource p:allPredicates) {
			double w = this.predicate_IC(p);
			if(w>result)
				result = w;
		}
		this.max_weight = result;
	}
	
}
