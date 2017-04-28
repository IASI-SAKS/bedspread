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

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.opencsv.CSVWriter;

import it.cnr.iasi.leks.bedspread.AbstractSemanticSpread;
import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.PolicentricSemanticSpread;
import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.exceptions.AbstractBedspreadException;
import it.cnr.iasi.leks.bedspread.exceptions.impl.InteractionProtocolViolationException;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.util.SetOfNodesFactory;

/**
 * 
 * @author gulyx
 *
 */
public class HT13PolicentricSemanticSpread extends PolicentricSemanticSpread{

	private Set<Node> setOfNodes;
	private PropertyUtil prop;
	
	private Set<Node> smallestSet;
	private List<Set<Node>> otherSets;
	
	public HT13PolicentricSemanticSpread (Set<Node> originSet, KnowledgeBase kb) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException{
		super(originSet, kb);
		this.setOfNodes = SetOfNodesFactory.getInstance().getSetOfNodesInstance();
		this.prop = PropertyUtil.getInstance();
	}
	

	private synchronized void configureSmallestSetAndOthers(List<AbstractSemanticSpread> completedList) throws InteractionProtocolViolationException{
		this.otherSets = new ArrayList<Set<Node>>();
		this.smallestSet = completedList.get(0).getSemanticSpreadForNode();
		List<AbstractSemanticSpread> tmpCompletedList = completedList.subList(1, completedList.size());
		Set<Node> current = null;
		for (AbstractSemanticSpread semSpread : tmpCompletedList) {
			current = semSpread.getSemanticSpreadForNode();
			if (current.size() < this.smallestSet.size()){
				this.otherSets.add(this.smallestSet);
				this.smallestSet = current;
			}else{
				this.otherSets.add(current);
			}			
		}
	}
	
	private synchronized Set<Node> computeValuesForTheIntersection() throws InteractionProtocolViolationException{
		this.setOfNodes.clear();

		this.logger.info("XMerge-1X Configuring Local Sets ... ");
		List<AbstractSemanticSpread> completedList = this.getCompletedSemanticSpreadList();
		this.configureSmallestSetAndOthers(completedList);
		this.logger.info("XMerge-1X ... done");
		
		int size = this.smallestSet.size();
		for (Node nodeFromPivot : this.smallestSet) {
			this.logger.info("XMerge-2X Missing Nodes to be processed for Intersection: {}", size);
			boolean elementIsPresent = true;
			for (Iterator<Set<Node>> iterator = this.otherSets.iterator(); (iterator.hasNext()) && (elementIsPresent);) {
				Set<Node> s = (Set<Node>) iterator.next();
				elementIsPresent = s.contains(nodeFromPivot);
			}
			if (!elementIsPresent){
				// The element is not common to all the sets (i.e. is not part of the intersection)				
			}else{
				// The element is common to all the sets (i.e. is part of the intersection)
				double score = nodeFromPivot.getScore();
				
				for (Set<Node> s : this.otherSets) {
					boolean skipOthers = false;
					for (Iterator<Node> nodeIterator = s.iterator(); (nodeIterator.hasNext()) && (!skipOthers);) {
						Node node = (Node) nodeIterator.next();
						if (node.equals(nodeFromPivot)){
							score = score * node.getScore();
							skipOthers = true;
						}						
					}
				}

				Node commonNode = new Node(nodeFromPivot.getResource());
				int degree = this.kb.degree(commonNode.getResource());
				// Note that the original implementation for this computation foresees "Math.log(degree)"
				// Here we added "+1" because we would like to avoid division by 0 ... even
				// if in real data-sets there degrees of nodes will always be > 2
				// however this cannot be excluded for testing data-sets
				score = score / Math.log(degree+2);
				commonNode.updateScore(score);

				this.setOfNodes.add(commonNode);
			}
			size --;
		}
		
		return this.setOfNodes;		
	} 
	
	@Override
	public Set<Node> mergeProcessingResults() throws InteractionProtocolViolationException{
		return this.computeValuesForTheIntersection();
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

	@Override
	protected void doSomethingWhileProcessing() throws AbstractBedspreadException {
		try {
			long sleeptime = Long.parseLong(this.prop.getProperty(PropertyUtil.POLICENTRIC_SEMANTIC_SPREAD_SLEEP_LABEL, "5000"));
			Thread.sleep(sleeptime);
		} catch (InterruptedException e) {
			AbstractBedspreadException  ex = new AbstractBedspreadException(e.getMessage(),e.getCause()) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 7917488952633335500L;
			};
			throw ex;			
		}
	}
	
}
