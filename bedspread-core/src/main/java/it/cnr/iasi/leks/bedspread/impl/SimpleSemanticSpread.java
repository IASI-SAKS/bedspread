package it.cnr.iasi.leks.bedspread.impl;

import it.cnr.iasi.leks.bedspread.AbstractSemanticSpread;
import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.policies.TerminationPolicy;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;

public class SimpleSemanticSpread extends AbstractSemanticSpread {

	public SimpleSemanticSpread(Node origin, KnowledgeBase kb, TerminationPolicy term) {
		super(origin, kb, term);
	}

	@Override
	protected double computeScore(Node node) {
		AnyResource resource = node.getResource();
		int n = this.kb.getNeighborhood(resource).size();
		double score = node.getScore();
		if (n != 0){
			score = score/n;
		}
		return score;
	}

}
