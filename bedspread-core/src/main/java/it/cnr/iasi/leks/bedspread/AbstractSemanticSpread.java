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
package it.cnr.iasi.leks.bedspread;

import java.io.IOException;
import java.io.Writer;
import java.lang.Runnable;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.exceptions.impl.InteractionProtocolViolationException;
import it.cnr.iasi.leks.bedspread.impl.policies.ExecutionPolicyFactory;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.util.SetOfNodesFactory;

/**
 * 
 * @author gulyx
 *
 */
public abstract class AbstractSemanticSpread implements Runnable{
	private Node origin;
	private ComputationStatus status;
	
	private Set<Node> activatedNodes;
	private Set<Node> currentlyActiveNodes;
	private Set<Node> forthcomingActiveNodes;	
	private Set<Node> justProcessedForthcomingActiveNodes;
	
	private Set<Node> explorationLeaves; 
	
	protected SetOfNodesFactory setOfNodesFactory;
	
	private final Object callbackMutex = new Object();
	private ComputationStatusCallback callback;
	private String optionalID;
	
	protected KnowledgeBase kb;
	protected ExecutionPolicy policy;

	protected final Logger logger = LoggerFactory.getLogger(AbstractSemanticSpread.class);
	
	private final double INITIAL_STIMULUS = 1;
	
	public AbstractSemanticSpread(Node origin, KnowledgeBase kb) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException{
		this(origin, kb, ExecutionPolicyFactory.getInstance().getExecutionPolicy(kb));
	}
		
	public AbstractSemanticSpread(Node origin, KnowledgeBase kb, ExecutionPolicy policy){
		this.origin = origin;
		this.origin.updateScore(INITIAL_STIMULUS);
		
		this.kb = kb;
		this.policy = policy;
		this.status = ComputationStatus.NotStarted;
		
		this.setOfNodesFactory = SetOfNodesFactory.getInstance();		

		this.currentlyActiveNodes = this.setOfNodesFactory.getSetOfNodesInstance();		
		this.activatedNodes = this.setOfNodesFactory.getNaviteSetOfNodesInstance();
		this.forthcomingActiveNodes = this.setOfNodesFactory.getSetOfNodesInstance();
		this.justProcessedForthcomingActiveNodes = this.setOfNodesFactory.getSetOfNodesInstance();		

		this.explorationLeaves = this.setOfNodesFactory.getSetOfNodesInstance();		
	}

	public Node getOrigin(){
		return this.origin;
	}
	
	public ComputationStatus getComputationStatus() {
		ComputationStatus s;
		synchronized (this.status) {
			s = this.status;
		}
		return s;
	}
	
	private void refreshInternalState(){
		this.currentlyActiveNodes.clear();		
		this.activatedNodes.clear();
		this.forthcomingActiveNodes.clear();
		this.justProcessedForthcomingActiveNodes.clear();
		this.explorationLeaves.clear();		

		this.currentlyActiveNodes.add(this.origin);
	}
	
	public void run (){
		synchronized (this.status) {
			this.status = ComputationStatus.Running;
			this.logger.info("--- NEW EXECUTION ---");
		}
		this.refreshInternalState();
		while (!this.policy.terminationPolicyMet()){
			this.justProcessedForthcomingActiveNodes.clear();
			
			this.filterCurrentlyActiveNodes();
			
			int i = 0;
			for (Node node : this.currentlyActiveNodes) {
				i++;
				this.logger.info("{}", "+++ "+i+" "+node.getResource().getResourceID()+": "+kb.getNeighborhood(node.getResource()).size());
				
				this.extractForthcomingActiveNodes(node);
				
				if ((this.forthcomingActiveNodes == null) || this.forthcomingActiveNodes.isEmpty()){
					this.explorationLeaves.add(node);
				}
				int j = 0;
				for (Node newNode : this.forthcomingActiveNodes) {
					j++;
					Double newScore = this.computeScore(node, newNode);
					newNode.updateScore(newScore);
// Note that elements already present are not doubled in "tempActiveNodes" according to : java.util.Set				
					this.justProcessedForthcomingActiveNodes.add(newNode);

					this.logger.info("{} --> {}, {}", "*** "+j+" "+node.getResource().getResourceID(),newNode.getResource().getResourceID(),newNode.getScore());
				}
			}
			
			for (Node node : this.currentlyActiveNodes) {
				this.activatedNodes.add(node);
			}						
			this.currentlyActiveNodes.clear();			
			for (Node tmpNode : this.justProcessedForthcomingActiveNodes) {
				this.currentlyActiveNodes.add(tmpNode);
			}
		}
		synchronized (this.status) {
			this.status = ComputationStatus.Completed;
			this.logger.info("--- EXECUTION COMPLETED ---");
		}
		this.notifyCallback();
	}
	
	public Set<Node> getSemanticSpreadForNode() throws InteractionProtocolViolationException{
		if (this.getStatus() != ComputationStatus.Completed){
			InteractionProtocolViolationException ex = new InteractionProtocolViolationException(PropertyUtil.INTERACTION_PROTOCOL_ERROR_MESSAGE);
			throw ex;
		}
		Set<Node> s = this.getAllActiveNodes();
		return s;
	}	
	
	private void extractForthcomingActiveNodes(Node node) {
		this.forthcomingActiveNodes.clear();
		for (AnyResource neighbor : this.kb.getNeighborhood(node.getResource())) {
			Node neighborNode = new Node(neighbor);
			if ((! this.activatedNodes.contains(neighborNode)) && (! this.currentlyActiveNodes.contains(neighborNode)) && (! this.justProcessedForthcomingActiveNodes.contains(neighborNode))){
// Note that elements already present are not doubled in "forthcomingActiveNodes" according to : java.util.Set				
				this.forthcomingActiveNodes.add(neighborNode);
			}	
		}
		
	}

	protected Set<Node> getActiveNodes(){
		Set<Node> n = this.setOfNodesFactory.getSetOfNodesInstance();
		n.addAll(this.activatedNodes);
		n.addAll(this.currentlyActiveNodes);
		
		return n;
	}

	protected Set<Node> getAllActiveNodes(){
		Set<Node> n = this.getActiveNodes();
		
		n.addAll(this.justProcessedForthcomingActiveNodes);
		
		return n;
	}
	
	private void filterCurrentlyActiveNodes() {
		Set<Node> filteredSetOfNodes = this.setOfNodesFactory.getSetOfNodesInstance();		
		for (Node tmpNode : this.currentlyActiveNodes) {
			if (this.policy.isSpreadingEnabled(tmpNode)){
				filteredSetOfNodes.add(tmpNode);
			} else {
				this.activatedNodes.add(tmpNode);
				this.explorationLeaves.add(tmpNode);
			}	
		}
		this.currentlyActiveNodes = filteredSetOfNodes;
	}

	public Set<Node> getExplorationLeaves(){
		Set<Node> n = this.setOfNodesFactory.getSetOfNodesInstance();
		n.addAll(this.explorationLeaves);
		
		return n;
	}

	public synchronized ComputationStatus getStatus() {
		return this.status;
	}
	
	public void setCallback(String notifierID, ComputationStatusCallback callback){
		synchronized (this.callbackMutex) {
			this.optionalID = notifierID;
			this.callback = callback;			
		}
	}
	
	private void notifyCallback(){
		synchronized (this.callbackMutex) {
			if ((this.optionalID != null) && (this.callback != null)){
					this.callback.notifyStatus(this.optionalID, this.getStatus());
			}
		}	
	}
	
	protected abstract double computeScore(Node spreadingNode, Node targetNode); 
	
	public abstract void flushData (Writer out) throws IOException, InteractionProtocolViolationException; 
}
