package it.cnr.iasi.leks.bedspread.rdf.impl;

import it.cnr.iasi.leks.bedspread.rdf.AnyResource;

public class EdgeWeighting_IC {

	public static double predicateFrequence(DBpediaKB kb, AnyResource resource) {
		double result = 0.0;
		int total_triple = kb.countAllTriples();
		int total_triple_by_predicate = kb.countTriplesByPredicate(resource);
		result = total_triple_by_predicate/total_triple;
		return result;
	}  
	
	public static double nodeFrequence(DBpediaKB kb, AnyResource resource) {
		double result = 0.0;
		int total_triple = kb.countAllTriples();
		int total_triple_by_node = kb.countTriplesByNode(resource);
		result = total_triple_by_node/total_triple;
		return result;
	}
	
	// TO CHECK
	public static double nodeFrequenceConditional(DBpediaKB kb, AnyResource pred, AnyResource node) {
		double result = 0.0;
		int total_triple_by_node = kb.countTriplesByNode(node);
		int total_triple_by_predicate_and_node = kb.countTriplesByPredicateAndNode(kb, pred, node);
		result = total_triple_by_predicate_and_node/total_triple_by_node;
		return result;
	}
	
	public static double predicateInformationContent(DBpediaKB kb, AnyResource resource) {
		double result = 0.0;
		result = - Math.log(predicateFrequence(kb, resource));
		return result;
	}
	
	public static double nodeInformationContent(DBpediaKB kb, AnyResource resource) {
		double result = 0.0;
		result = - Math.log(nodeFrequence(kb, resource));
		return result;
	}
	
	public static double nodeConditionalInformationContent(DBpediaKB kb, AnyResource pred, AnyResource node) {
		double result = 0.0;
		result = - Math.log(nodeFrequenceConditional(kb, pred, node));
		return result;
	}
	
	public static double edgeWeight_IC(DBpediaKB kb, AnyResource resource) {
		double result = 0.0;
		result = predicateInformationContent(kb, resource);
		return result;
	}
	
	public static double edgeWeight_CombIC(DBpediaKB kb, AnyResource pred, AnyResource node) {
		double result = 0.0;
		result = predicateInformationContent(kb, pred) + nodeInformationContent(kb, node);
		return result;
	}
	
	public static double edgeWeight_jointIC(DBpediaKB kb, AnyResource pred, AnyResource node) {
		double result = 0.0;
		result = predicateInformationContent(kb, pred) +  nodeConditionalInformationContent(kb, pred, node);
		return result;
	}
}
