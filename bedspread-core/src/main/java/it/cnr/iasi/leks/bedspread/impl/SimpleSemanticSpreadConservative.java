package it.cnr.iasi.leks.bedspread.impl;

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
		AnyResource resource = spreadingNode.getResource();
		int neighborhoodSize = this.kb.getNeighborhood(resource).size();
		
		if (!this.getOrigin().equals(spreadingNode)){
			neighborhoodSize --;
		}
		
		double score = spreadingNode.getScore();
		if (neighborhoodSize > 0){
			score = score/neighborhoodSize;
		}
		return score;
	}


}
