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
import java.util.Set;

import com.opencsv.CSVWriter;

import it.cnr.iasi.leks.bedspread.AbstractSemanticSpread;
import it.cnr.iasi.leks.bedspread.ComputationStatus;
import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.ExecutionPolicies;
import it.cnr.iasi.leks.bedspread.WeightingFunction;
import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.exceptions.impl.InteractionProtocolViolationException;
import it.cnr.iasi.leks.bedspread.impl.weights.WeightingFunctionFactory;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;

/**
 * 
 * @author gulyx
 *
 */
public class HT13ConfSemanticSpread extends AbstractSemanticSpread {

	private WeightingFunction weightingModule; 
	
// Note that these two attributes (and related operations) are only conceived to better
// structuring the implementation of this class and its potential sub-classes. Actually
// they do not bring any contribution to the concept. So it is recommended to limit
// the dependencies on them.
	private Node weightNodeFirstArg;
	private Node weightNodeSecongArg;
	
	public HT13ConfSemanticSpread(Node origin, KnowledgeBase kb) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		super(origin, kb);

		this.weightingModule = WeightingFunctionFactory.getInstance().getWeightingFunction(this.kb);
	}

	public HT13ConfSemanticSpread(Node origin, KnowledgeBase kb, ExecutionPolicies term) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		super(origin, kb, term);

		this.weightingModule = WeightingFunctionFactory.getInstance().getWeightingFunction(this.kb);
	}

	@Override
	protected double computeScore(Node spreadingNode, Node targetNode) {
//		Note that in this implementation the parameter {@code Node spreadingNode} is not used!!!

		this.configureWeightNodes(targetNode, this.getOrigin());
		double score = this.calculateScore(spreadingNode, targetNode);
		
		return score;
	}

	protected void configureWeightNodes(Node firstArg, Node secondArg){
		this.weightNodeFirstArg = firstArg;
		this.weightNodeSecongArg = secondArg;
	}
	
	protected double calculateScore(Node spreadingNode, Node targetNode) {
		
//	Note that in this implementation the parameter {@code Node spreadingNode} is not used!!!
		
		Set<AnyResource> neighborhood = this.kb.getNeighborhood(targetNode.getResource());
		double neighborhoodScore = 0;
		for (AnyResource neighborResource : neighborhood) {
			int degree = this.kb.degree(neighborResource);
			Node neighborNode = this.backtrackToNode(neighborResource, targetNode.getResource());
						
			neighborhoodScore += (neighborNode.getScore()/degree);
		}
		
		double weight = this.weightingModule.weight(this.weightNodeFirstArg, this.weightNodeSecongArg);
		double score = this.stimulus(targetNode) + (weight * neighborhoodScore);
		
		return score;
	}

	@Override
	protected void filterCurrenltyActiveNode() {
		// TODO  IT DOES NOTHING FOR THE MOMENT!!
	}

	@Override
	public void flushData(Writer out) throws IOException, InteractionProtocolViolationException {
		if (this.getStatus() != ComputationStatus.Completed){
			InteractionProtocolViolationException ex = new InteractionProtocolViolationException(PropertyUtil.INTERACTION_PROTOCOL_ERROR_MESSAGE);
			throw ex;
		}
		
		CSVWriter writer = new CSVWriter(out);
	    String[] csvEntry = new String[2];
	     
	    for (Node n : this.getActiveNodes()) {
	    	 csvEntry[0] = n.getResource().getResourceID();
	    	 csvEntry[1] = String.valueOf(n.getScore());
		     writer.writeNext(csvEntry);
	    }
	     
	    writer.close();
	}

	private Node backtrackToNode(AnyResource neighborResource, AnyResource targetResource) {		
		for (Node node : this.getAllActiveNodes()) {
			if (node.getResource().getResourceID().equalsIgnoreCase(neighborResource.getResourceID())){
				return node;
			}	
		}

		Node node = new Node(neighborResource);
		return node;
	}
	
	private double stimulus(Node node) {
		if (node.equals(this.getOrigin())){
			return 1;
		}	
		return 0;
	}

}
