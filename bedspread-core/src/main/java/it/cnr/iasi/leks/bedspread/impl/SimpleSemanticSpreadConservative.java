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

import java.util.Set;

import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.ExecutionPolicies;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;

public class SimpleSemanticSpreadConservative extends SimpleSemanticSpread {

	public SimpleSemanticSpreadConservative(Node origin, KnowledgeBase kb) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		super(origin, kb);
	}

	public SimpleSemanticSpreadConservative(Node origin, KnowledgeBase kb, ExecutionPolicies term){
		super(origin, kb, term);
	}

	@Override
	protected double computeScore(Node spreadingNode, Node targetNode) {		
		double score = spreadingNode.getScore();

		AnyResource resource = spreadingNode.getResource();
		Set<AnyResource> neighborhood = this.kb.getNeighborhood(resource);
		if (neighborhood == null){
			return score;
		}
		
		int neighborhoodSize = neighborhood.size();
		
		for (AnyResource neighbor : neighborhood) {
			Node n = new Node(neighbor);
			if (this.getActiveNodes().contains(n)){
				neighborhoodSize --;
			}
		}
				
		if (neighborhoodSize > 0){
			score = score/neighborhoodSize;
		}
		return score;
	}


}
