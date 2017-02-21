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

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.opencsv.CSVWriter;

import it.cnr.iasi.leks.bedspread.AbstractSemanticSpread;
import it.cnr.iasi.leks.bedspread.ComputationStatus;
import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.PolicentricSemanticSpread;
import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.exceptions.impl.InteractionProtocolViolationException;
import it.cnr.iasi.leks.bedspread.impl.SematicSpreadFactory;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.util.SetOfNodesFactory;

/**
 * 
 * @author gulyx
 *
 */
public class HT13PolicentricSemanticSpread extends PolicentricSemanticSpread{

	private Set<Node> setOfNodes;
	
	
	public HT13PolicentricSemanticSpread (Set<Node> originSet, KnowledgeBase kb) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		super(originSet, kb);
		this.setOfNodes = SetOfNodesFactory.getInstance().getSetOfNodesInstance();
	}
	
	public Set<Node> mergeProcessingResults() throws InteractionProtocolViolationException{
		HashMap<String, Node> nodeMap = new HashMap<String, Node>();
		
		for (AbstractSemanticSpread semSpread : this.getCompletedSemanticSpreadList()) {
			for (Node node : semSpread.getSemanticSpreadForNode()) {
				String key = node.getResource().getResourceID();
				Node n = nodeMap.get(key);
				if ( n != null){
					double newScore = n.getScore() * node.getScore();
					n.updateScore(newScore);
				}else{
					n = new Node(node.getResource());
					n.updateScore(node.getScore());
					nodeMap.put(key, n);
				}				
			}
		}
		
		for (Node node : nodeMap.values()) {
			int degree = this.kb.degree(node.getResource());
// Note that the original implementation for this computation foresees "Math.log(degree)"
// Here we added "+1" because we would like to avoid division by 0 ... even
// if in real data-sets there degrees of nodes will always be > 2
// however this cannot be excluded for testing data-sets
			double newScore = node.getScore() / Math.log(degree+2);
			node.updateScore(newScore);
			this.setOfNodes.add(node);
		}
		
		return this.setOfNodes;
	}

	protected void flushData(Writer out) throws InteractionProtocolViolationException, IOException{
		if (! this.isOver()){
			InteractionProtocolViolationException ex = new InteractionProtocolViolationException(PropertyUtil.INTERACTION_PROTOCOL_ERROR_MESSAGE);
			throw ex;
		}
		CSVWriter writer = new CSVWriter(out);
	    String[] csvEntry = new String[2];
	     
	    for (Node n : this.setOfNodes) {
	    	 csvEntry[0] = n.getResource().getResourceID();
	    	 csvEntry[1] = String.valueOf(n.getScore());
		     writer.writeNext(csvEntry);
	    }
	     
	    writer.close();		
	}
	
}
