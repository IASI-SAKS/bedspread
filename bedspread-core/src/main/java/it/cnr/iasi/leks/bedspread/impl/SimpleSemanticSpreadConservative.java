package it.cnr.iasi.leks.bedspread.impl;

import java.util.Set;

import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.TerminationPolicy;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;

public class SimpleSemanticSpreadConservative extends SimpleSemanticSpread {

	public SimpleSemanticSpreadConservative(Node origin, KnowledgeBase kb) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		super(origin, kb);
	}

	public SimpleSemanticSpreadConservative(Node origin, KnowledgeBase kb, TerminationPolicy term){
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
