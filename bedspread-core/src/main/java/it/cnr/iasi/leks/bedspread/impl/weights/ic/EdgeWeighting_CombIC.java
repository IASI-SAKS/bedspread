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
public class EdgeWeighting_CombIC extends EdgeWeighting_UnconditionedJointIC{

	private static EdgeWeighting_CombIC instance = null;
	
	public EdgeWeighting_CombIC(KnowledgeBase kb) {
		super(kb);
		computeMaxWeight();
	}
	
	public synchronized static EdgeWeighting_CombIC getInstance(KnowledgeBase kb){
    	if (instance == null){
    		instance = new EdgeWeighting_CombIC(kb);
    	}
    	return instance;
    }		
	
	/**
	 * Compute the Information Content of a node 
	 *
	 * @param resource
	 * @return
	 */
	protected double node_IC(AnyResource resource) {
		double result = 0.0;
		result = - Math.log(nodeProbability(resource));
		return result;
	}
		
	/**
	 * Compute the Combined Information Content (combIC), which is identified by a triple
	 * 
	 * edgeWeight_CombIC
	 * 
	 * @param edge
	 * @return
	 */
	@Override
	public double computeEdgeWeight(RDFTriple edge) {
		double result = 0.0;
		result = predicate_IC(edge.getTriplePredicate()) + node_IC(edge.getTripleObject());
		return result;
	}

	// TO BE IMPLEMENTED
	@Override
	protected void computeMaxWeight() {
		
	}
}
