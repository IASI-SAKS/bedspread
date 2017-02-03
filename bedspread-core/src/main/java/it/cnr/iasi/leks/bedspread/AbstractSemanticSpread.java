package it.cnr.iasi.leks.bedspread;

import java.lang.Runnable;
import java.util.Set;

import it.cnr.iasi.leks.bedspread.policies.TerminationPolicy;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.util.RevisedHashSet;
import it.cnr.iasi.leks.bedspread.util.SetOfNodesFactory;

public abstract class AbstractSemanticSpread implements Runnable{
	private Node origin;
	private KnowledgeBase kb;
	private TerminationPolicy term;
	private ComputationStatus status;
	
	private Set<Node> currentlyActiveNodes;
	private Set<Node> forthcomingActiveNodes;
	private Set<Node> tempActiveNodes;
	
	private SetOfNodesFactory setOfNodesFactory;
	
	public AbstractSemanticSpread(Node origin, KnowledgeBase kb, TerminationPolicy term){
		this.origin = origin;
		this.origin.updateScore(1);
		
		this.kb = kb;
		this.term = term;
		this.status = ComputationStatus.NotStarted;
		
		this.setOfNodesFactory = SetOfNodesFactory.getInstance();
		
		this.currentlyActiveNodes = this.setOfNodesFactory.getSetOfNodesInstance();
		this.currentlyActiveNodes.add(this.origin);
		
		this.forthcomingActiveNodes = this.setOfNodesFactory.getSetOfNodesInstance();
		this.tempActiveNodes = this.setOfNodesFactory.getSetOfNodesInstance();
	}
		
	public Node getOrigin(){
		return this.origin;
	}
	
	public ComputationStatus getComputationStatus() {
		return this.status;
	}
	
	public void run (){
		this.status = ComputationStatus.Running;
		while (!term.wasMet()){
			this.tempActiveNodes.clear();
			for (Node node : currentlyActiveNodes) {
				this.extractForthcomingActiveNodes(node);
				
				for (Node newNode : this.forthcomingActiveNodes) {
					Double newScore = this.computeScore(newNode);
					newNode.updateScore(newScore);
// Note that elements already present are not doubled in "tempActiveNodes" according to : java.util.Set				
					this.tempActiveNodes.add(newNode);
				}
			}
			
			for (Node tmpNode : this.tempActiveNodes) {
				this.currentlyActiveNodes.add(tmpNode);
			}						
		}
		this.status = ComputationStatus.Completed;
	}
	
	private void extractForthcomingActiveNodes(Node node) {
		this.forthcomingActiveNodes.clear();
		for (AnyResource neighbor : this.kb.getNeighborhood(node.getResource())) {
			Node neighborNode = new Node(neighbor);
// Note that elements already present are not doubled in "forthcomingActiveNodes" according to : java.util.Set				
			this.forthcomingActiveNodes.add(neighborNode);
		}
		
	}

	protected abstract double computeScore(Node n); 
}
