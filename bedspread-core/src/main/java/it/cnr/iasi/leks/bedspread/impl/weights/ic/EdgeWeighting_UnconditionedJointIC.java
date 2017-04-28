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
package it.cnr.iasi.leks.bedspread.impl.weights.ic;

import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;

/**
 * The Implementation of this class refers to the following publication
 * Schuhumacher M., Ponzetto S.P. 
 * Knowledge-based Graph Document Modeling
 * WSDM'14 February 24-28, 2014, New York, New York, USA.
 *  
 * @author ftaglino
 *
 */
public abstract class EdgeWeighting_UnconditionedJointIC extends Abstract_EdgeWeighting_IC{

	public EdgeWeighting_UnconditionedJointIC(KnowledgeBase kb) {
		super(kb);
	}

	/**
	 * Compute the frequency of the triples having node as the subject or as the object, with respect to all the triples in the kb 	
	 *
	 * @param resource
	 * @return
	 */
	protected double nodeProbability(AnyResource resource) {
		double result = 0.0;
		double total_triple = this.kb.countAllTriples();
		double total_triple_by_node = this.kb.countTriplesBySubjectOrObject(resource);
		result = total_triple_by_node/total_triple;
		return result;
	}
		
}
