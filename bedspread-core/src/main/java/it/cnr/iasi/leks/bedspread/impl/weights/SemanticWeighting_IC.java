package it.cnr.iasi.leks.bedspread.impl.weights;

import java.util.Set;

import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.WeightingFunction;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.AnyURI;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.rdf.URI;
import it.cnr.iasi.leks.bedspread.rdf.impl.DBpediaKB;
import it.cnr.iasi.leks.bedspread.rdf.impl.EdgeWeighting_IC;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFTriple;
import it.cnr.iasi.leks.bedspread.rdf.impl.URIImpl;

public class SemanticWeighting_IC implements WeightingFunction {

	DBpediaKB kb;
	
	public SemanticWeighting_IC(DBpediaKB kb) {
		this.kb = kb;
	}
	
	/**
	 * Compute the weight between adjacent nodes, as the maximum information content (IC) of the predicates linking n1 and n2. 
	 * The method first finds the predicates from n1 to n2. For each predicate p of such predicates, builds the triple (or edge) <n1, p, n2> and computes the weight of the edge as its IC.
	 * Then the same is performed exchanging n1 and n2.
	 * The result is the maximum computed weight over all the built edges.
	 * The values range is [0, 1] 
	 * param n1
	 * param n2
	 * return
	 */
	@Override
	public double weight(Node n1, Node n2) {
		double result = 0.0; 
		// Find predicates linking n1 and n2 
		// 	firstly predicates from n1 to n2 
		Set<AnyResource> predicates =  this.kb.getPredicatesBySubjectAndObject(n1.getResource(), n2.getResource());
		// For each predicate compute the weighting function
		for(AnyResource pred:predicates) {
			URIImpl s = new URIImpl(n1.getResource().getResourceID());
			URIImpl p = new URIImpl(pred.getResourceID());
			URIImpl o = new URIImpl(n2.getResource().getResourceID());
			// create the edge
			RDFTriple edge = new RDFTriple(s, p, o);
			double w = EdgeWeighting_IC.edgeWeight_IC(kb, edge);
			if(w>result)
				result = w;
		}
		
		// 	secondly predicates from n2 to n1
		predicates =  kb.getPredicatesBySubjectAndObject(n2.getResource(), n1.getResource());
		// For each predicate compute the weighting function
		for(AnyResource pred:predicates) {
			URIImpl s = new URIImpl(n2.getResource().getResourceID());
			URIImpl p = new URIImpl(pred.getResourceID());
			URIImpl o = new URIImpl(n1.getResource().getResourceID());
			// create the edge
			RDFTriple edge = new RDFTriple(s, p, o);
			double w = EdgeWeighting_IC.edgeWeight_IC(kb, edge);
			if(w>result)
				result = w;
		}

		// Return the maximum weight 
		return result;
	}

}
