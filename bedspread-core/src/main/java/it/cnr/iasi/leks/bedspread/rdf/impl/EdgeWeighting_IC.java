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
package it.cnr.iasi.leks.bedspread.rdf.impl;

import it.cnr.iasi.leks.bedspread.rdf.AnyResource;

/**
 * The Implementation of this class refers to the following publication
 * Schuhumacher M., Ponzetto S.P. 
 * Knowledge-based Graph Document Modeling
 * WSDM'14 February 24-28, 2014, New York, New York, USA.
 *  
 * @author ftaglino
 *
 */
public class EdgeWeighting_IC {

	/**
	 * Compute the frequence of the triples having pred as the predicate with respect to all the triples in the kb
	 * @param kb
	 * @param resource
	 * @return
	 */
	public static double predicateProbability(DBpediaKB kb, AnyResource resource) {
		double result = 0.0;
		int total_triple = kb.countAllTriples();
		int total_triple_by_predicate = kb.countTriplesByPredicate(resource);
		result = total_triple_by_predicate/total_triple;
		return result;
	}  
	
	/**
	 * Compute the frequence of the triples having node as the subject or as the object, with respect to all the triples in the kb 	
	 * @param kb
	 * @param resource
	 * @return
	 */
	public static double nodeProbability(DBpediaKB kb, AnyResource resource) {
		double result = 0.0;
		int total_triple = kb.countAllTriples();
		int total_triple_by_node = kb.countTriplesByNode(resource);
		result = total_triple_by_node/total_triple;
		return result;
	}
	
	/**
	 * Compute the frequence of the triples having node as the subject or as the object with respect to those triples having pred as the predicate 
	 * @param kb
	 * @param pred
	 * @param node
	 * @return
	 */
	public static double nodeProbabilityConditionalToPredicate(DBpediaKB kb, AnyResource pred, AnyResource node) {
		double result = 0.0;
		int total_triple_by_predicate = kb.countTriplesByPredicate(pred);
		int total_triple_by_predicate_and_node = kb.countTriplesByPredicateAndNode(kb, pred, node);
		result = total_triple_by_predicate_and_node/total_triple_by_predicate;
		return result;
	}

	/**
	 * Compute the frequence of the triples having node as the object or as the predicate and pred as the predicate with respect to all the triples in the kb 
	 * @param kb
	 * @param pred
	 * @param node
	 * @return
	 */
	public static double nodeAndPredicateProbability(DBpediaKB kb, AnyResource pred, AnyResource node) {
		double result = 0.0;
		int total_triple_by_predicate_and_node = kb.countTriplesByPredicateAndNode(kb, pred, node);
		int total_triple = kb.countAllTriples();
		result = total_triple_by_predicate_and_node/total_triple;
		return result;
	}

	/**
	 * Compute the Information Content of a predicate 
	 * @param kb
	 * @param resource
	 * @return
	 */
	public static double predicate_IC(DBpediaKB kb, AnyResource resource) {
		double result = 0.0;
		result = - Math.log(predicateProbability(kb, resource));
		return result;
	}
	
	/**
	 * Compute the Information Content of a node 
	 * @param kb
	 * @param resource
	 * @return
	 */
	public static double node_IC(DBpediaKB kb, AnyResource resource) {
		double result = 0.0;
		result = - Math.log(nodeProbability(kb, resource));
		return result;
	}
	
	/**
	 * Compute the Information Content of a node knowing the predicate 
	 * @param kb
	 * @param pred
	 * @param node
	 * @return
	 */
	public static double nodeConditionalToPredicate_IC(DBpediaKB kb, AnyResource pred, AnyResource node) {
		double result = 0.0;
		result = - Math.log(nodeProbabilityConditionalToPredicate(kb, pred, node));
		return result;
	}
	
	/**
	 * Compute the pointwise mutual information
	 * @param kb
	 * @param pred
	 * @param node
	 * @return
	 */
	public static double pmi(DBpediaKB kb, AnyResource pred, AnyResource node) {
		double result = 0.0;
		double nodeAndPredicateProbability = nodeAndPredicateProbability(kb, pred, node);
		double predicateProbability = predicateProbability(kb, pred);
		double nodeProbability = nodeProbability(kb, node);
		result = Math.log(nodeAndPredicateProbability/(predicateProbability*nodeProbability));
		return result;
	}
	
	/**
	 * Compute the information content of an edge, which is identified by a triple 
	 * @param kb
	 * @param edge
	 * @return
	 */
	public static double edgeWeight_IC(DBpediaKB kb, RDFTriple edge) {
		double result = 0.0;
		result = predicate_IC(kb, edge.getTriplePredicate());
		return result;
	}
	
	/**
	 * Compute the Joint Information Content (jointIC) of an edge, which is identified by a triple 
	 * @param kb
	 * @param edge
	 * @return
	 */
	public static double edgeWeight_jointIC(DBpediaKB kb, RDFTriple edge) {
		double result = 0.0;
		result = predicate_IC(kb, edge.getTriplePredicate()) +  nodeConditionalToPredicate_IC(kb, edge.getTriplePredicate(), edge.getTripleObject());
		return result;
	}
	
	/**
	 * Compute the Combined Information Content (combIC), which is identified by a triple
	 * @param kb
	 * @param edge
	 * @return
	 */
	public static double edgeWeight_CombIC(DBpediaKB kb, RDFTriple edge) {
		double result = 0.0;
		result = predicate_IC(kb, edge.getTriplePredicate()) + node_IC(kb, edge.getTripleObject());
		return result;
	}
	
	/**
	 * Compute the Information Content and Pointwise Mutual Information (IC+PMI), which is identified by a triple 
	 * @param kb
	 * @param edge
	 * @return
	 */
	public static double edgeWeight_ICplusPMI(DBpediaKB kb, RDFTriple edge) {
		double result = 0.0;
		result = predicate_IC(kb, edge.getTriplePredicate()) + pmi(kb, edge.getTriplePredicate(), edge.getTripleObject());
		return result;
	}
	
}
