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
package it.cnr.iasi.leks.bedspread.impl;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import com.opencsv.CSVWriter;

import it.cnr.iasi.leks.bedspread.AbstractSemanticSpread;
import it.cnr.iasi.leks.bedspread.AbstractSemanticSpreadOrchestrator;
import it.cnr.iasi.leks.bedspread.ComputationStatus;
import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.ExecutionPolicy;
import it.cnr.iasi.leks.bedspread.WeightingFunction;
import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.exceptions.AbstractBedspreadException;
import it.cnr.iasi.leks.bedspread.exceptions.impl.InteractionProtocolViolationException;
import it.cnr.iasi.leks.bedspread.exceptions.impl.UnexpectedValueException;
import it.cnr.iasi.leks.bedspread.impl.weights.WeightingFunctionFactory;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;

/**
 * 
 * @author gulyx
 *
 */
//public class HT13ConfSemanticSpread extends AbstractSemanticSpread {
public class HT13ConfSemanticSpread extends AbstractSemanticSpreadOrchestrator {

	private WeightingFunction weightingModule; 
	
// Note that these two attributes (and related operations) are only conceived to better
// structuring the implementation of this class and its potential sub-classes. Actually
// they do not bring any contribution to the concept. So it is recommended to limit
// the dependencies on them.
	private Node weightNodeFirstArg;
	private Node weightNodeSecongArg;
	
	public HT13ConfSemanticSpread(Node origin, KnowledgeBase kb) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, IOException {
		super(origin, kb);

		this.weightingModule = WeightingFunctionFactory.getInstance().getWeightingFunction(this.kb);
	}

	public HT13ConfSemanticSpread(Node origin, KnowledgeBase kb, ExecutionPolicy policy) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		super(origin, kb, policy);

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
			Node neighborNode = this.backtrackToNode(neighborResource, targetNode.getResource());
			if (neighborNode.getScore() != 0){
				int degree = this.kb.degree(neighborResource);
				if (degree == 0){
					this.logger.warn("DEGREE FOUND AS 0 FOR A NODE ({}) IN THE NEIGHBORHOOD OF TARGET NODE ({})", neighborResource.getResourceID(), targetNode.getResource().getResourceID());						
				}else{		
					neighborhoodScore += (neighborNode.getScore()/degree);
					if (Double.isNaN(neighborhoodScore) || Double.isInfinite(neighborhoodScore))
						logger.warn("args");
				}	
			}	
		}
		
		if (! neighborhood.contains(spreadingNode.getResource())){
			this.logger.warn("SPREADING NODE ({}) NOT FOUND IN THE NEIGHBORHOOD OF TARGET NODE ({}), ", spreadingNode.getResource().getResourceID(), targetNode.getResource().getResourceID());						
			if (neighborhoodScore == 0){
				this.logger.warn("neighborhoodScore FOUND 0 FOR: {}", targetNode.getResource().getResourceID());			
			}
		}

		
		double weight;
		double score; 
		try {
			weight = this.weightingModule.weight(this.weightNodeFirstArg, this.weightNodeSecongArg);
			score = this.stimulus(targetNode) + (weight * neighborhoodScore);
		} catch (UnexpectedValueException e) {
			this.logger.warn("SCORE FORCED TO 0 : {}", e.getMessage());
			score = 0;
		}catch (AbstractBedspreadException e) {
			this.logger.warn("SCORE FORCED TO 0 : {}", e.getMessage());
			score = 0;
		}

		if (Double.isNaN(score) || Double.isInfinite(score)){
			this.logger.warn("SCORE FOUND AS \"{}\", THUS FORCED TO 0", score);
			score = 0;
		}
		
		return score;
	}

	@Override
	public void flushData(Writer out) throws IOException, InteractionProtocolViolationException {
		if (this.getComputationStatus() != ComputationStatus.Completed){
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
/*
 * new instantiation of node has the score set to 0
 */
		Node node = new Node(neighborResource);
		Set<Node> allActiveNodes = this.getAllActiveNodes();
		
		
		if (allActiveNodes.contains(node)){
			/*
			 * if an instantiation the node already exists we must return the value previously computed
			 */
			for (Node n : this.getAllActiveNodes()) {
				if (n.equals(node)){
					return n;
				}	
			}			
		}
		return node;
	}
	
	private double stimulus(Node node) {
		if (node.equals(this.getOrigin())){
			return 1;
		}	
		return 0;
	}

}
