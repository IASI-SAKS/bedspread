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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.iasi.leks.bedspread.AbstractSemanticSpread;
import it.cnr.iasi.leks.bedspread.ExecutionPolicy;
import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;

/**
 * 
 * @author ftaglino
 *
 */
public class SpreadingBound extends ExecutionPolicy {

	private final int MAX_QUERIES_BEFORE_TRUE = 10;
	private int nQueries;
	private KnowledgeBase kb;
	private final int NODE_DEGREE_BOUND = 2600;
	private final double SCORE_THRESHOLD = 0.6d;

	protected final Logger logger = LoggerFactory.getLogger(SpreadingBound.class);
	
	public SpreadingBound(KnowledgeBase kb){
		this.kb = kb;
		this.nQueries = MAX_QUERIES_BEFORE_TRUE;
	}
	
	public SpreadingBound(KnowledgeBase kb, int i){
		this.kb = kb;
		this.nQueries = i;
	}

	@Override
	public boolean terminationPolicyMet() {
		if (this.nQueries > 0){
			this.nQueries --;
			return false;
		}	
		return true;
	}

    /*
     * 	In this simplified implementation, any Node can spread the signal.
     * @see it.cnr.iasi.leks.bedspread.ExecutionPolicy#isActivable(it.cnr.iasi.leks.bedspread.Node)
     */
	@Override
	public boolean isSpreadingEnabled(Node n) {
		int degree = kb.degree(n.getResource()); 
		if(degree > NODE_DEGREE_BOUND) {
			logger.info("{}", "FILTERED NODE(degree="+degree+"): "+ n.getResource().getResourceID());
			return false;
		}
		else if(n.getScore()<SCORE_THRESHOLD) {
			logger.info("{}", "FILTERED NODE(score="+n.getScore()+"): "+ n.getResource().getResourceID());
			return false;
		}
		return true;
	}

}
