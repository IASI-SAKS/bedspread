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
package it.cnr.iasi.leks.bedspread.impl.policies;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.iasi.leks.bedspread.ExecutionPolicy;
import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.rdf.impl.KBFactory;

/**
 * 
 * @author ftaglino
 *
 */
public class SpreadingBound implements ExecutionPolicy {

	private final int DEFAULT_MAX_QUERIES_BEFORE_TRUE = 10;
	private final int DEFAULT_NODE_DEGREE_BOUND = 3000;
	private final double DEFAULT_SCORE_THRESHOLD = 0.6d;
//	private final double DEFAULT_SCORE_THRESHOLD = 0.0001d;

	private int nQueries;
	private int nodeDegreeBound;
	private double scoreThreshold;
	
	private KnowledgeBase kb;

	protected final Logger logger = LoggerFactory.getLogger(SpreadingBound.class);
		
	public SpreadingBound() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException{
		this(null);
	}
	
	public SpreadingBound(KnowledgeBase kb) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException{
		PropertyUtil prop = PropertyUtil.getInstance();
		
		if (kb!=null){
			this.kb = kb;
		}else{
			this.kb = KBFactory.getInstance().getKnowledgeBase(prop);
		}
		
		this.nQueries = prop.getProperty(PropertyUtil.EXECUTION_POLICY_NQUERIES_LABEL, DEFAULT_MAX_QUERIES_BEFORE_TRUE);
		this.nodeDegreeBound = prop.getProperty(PropertyUtil.EXECUTION_POLICY_NODE_DEGREE_BOUND_LABEL, DEFAULT_NODE_DEGREE_BOUND);
		this.scoreThreshold = prop.getProperty(PropertyUtil.EXECUTION_POLICY_SCORE_THRESHOLD_LABEL, DEFAULT_SCORE_THRESHOLD);
	}

	public SpreadingBound(KnowledgeBase kb, int i) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException{
		this(kb);		
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
		if(degree > this.nodeDegreeBound) {
			logger.info("{}", "FILTERED NODE(degree="+degree+"): "+ n.getResource().getResourceID());
			return false;
		}
		else if(n.getScore() < this.scoreThreshold) {
			logger.info("{}", "FILTERED NODE(score="+n.getScore()+"): "+ n.getResource().getResourceID());
			return false;
		}
		return true;
	}

}
