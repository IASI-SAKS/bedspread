/*
 * 	 This file is part of Bedspread, originally promoted and
 *	 developed at CNR-IASI. For more information visit:
 *	 https://github.com/IASI-LEKS/bedspread
 *	     
 *	 This is free software: you can redistribute it and/or modify
 *	 it under the terms of the GNU Lesser General Public License as 
 *	 published by the Free Software Foundation, either version 3 of the 
 *	 License, or (at your option) any later version.
 *	 
 *	 This software is distributed in the hope that it will be useful,
 *	 but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	 GNU Lesser General Public License for more details.
 * 
 *	 You should have received a copy of the GNU Lesser General Public License
 *	 along with this source.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.cnr.iasi.leks.bedspread.impl;

import it.cnr.iasi.leks.bedspread.AbstractSemanticSpread;
import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.policies.TerminationPolicy;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;

public class SimpleSemanticSpread extends AbstractSemanticSpread {

	public SimpleSemanticSpread(Node origin, KnowledgeBase kb, TerminationPolicy term) {
		super(origin, kb, term);
	}

	@Override
	protected double computeScore(Node node) {
		AnyResource resource = node.getResource();
		int n = this.kb.getNeighborhood(resource).size();
		double score = node.getScore();
		if (n != 0){
			score = score/n;
		}
		return score;
	}

}
