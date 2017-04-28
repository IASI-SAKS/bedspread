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
package it.cnr.iasi.leks.bedspread.impl.weights.ic;

import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFTriple;

/**
 * The Implementation of this class refers to the following publication
 * Schuhumacher M., Ponzetto S.P. 
 * Knowledge-based Graph Document Modeling
 * WSDM'14 February 24-28, 2014, New York, New York, USA.
 *  
 * @author ftaglino
 *
 */
public class EdgeWeighting_IC_PMI extends EdgeWeighting_UnconditionedJointIC{

	private static int KB_HASHCODE=0;
	private static double CASHED_MAX_WEIGHT = 0.0d;

	private static final Object MUTEX = new Object();

	public EdgeWeighting_IC_PMI(KnowledgeBase kb) {
		super(kb);
	}
	
	/**
	 * Compute the frequence of the triples having node as the object or as the predicate and pred as the predicate with respect to all the triples in the kb 
	 * 
	 * @param pred
	 * @param node
	 * @return
	 */
	protected double nodeAndPredicateProbability(AnyResource pred, AnyResource node) {
		double result = 0.0;
		double total_triple_by_predicate_and_node = this.kb.countTriplesByPredicateAndSubjectOrObject(pred, node);
		double total_triple = this.kb.countAllTriples();
		result = total_triple_by_predicate_and_node/total_triple;
		return result;
	}

	/**
	 * Compute the pointwise mutual information
	 * 
	 * @param pred
	 * @param node
	 * @return
	 */
	protected double pmi(AnyResource pred, AnyResource node) {
		double result = 0.0;
		double nodeAndPredicateProbability = nodeAndPredicateProbability(pred, node);
		double predicateProbability = predicateProbability(pred);
		double nodeProbability = nodeProbability(node);
		result = Math.log(nodeAndPredicateProbability/(predicateProbability*nodeProbability));
		return result;
	}
	
	/**
	 * Compute the Information Content and Pointwise Mutual Information (IC+PMI), which is identified by a triple 
	 *
	 * @param edge
	 * @return
	 */
	@Override
	public double computeEdgeWeight(RDFTriple edge) {
		double result = 0.0;
		result = predicate_IC(edge.getTriplePredicate()) + pmi(edge.getTriplePredicate(), edge.getTripleObject());
		return result;
	}

	private double doTheComputation() {
// TODO		
		return 0.0d;	
	}

	@Override
	protected synchronized double computeMaxWeight() {
		double result;
		int hashCurrentKB = this.kb.hashCode();
		/*
		 * This synchronized block is needed because : 
		 * 
		 * 1. the "synchronized" keyword on the method is only active at instance level, since the
		 * method is not static;
		 * 
		 * 2. the method cannot be static because it uses data that depend
		 * on the instance ad that may be (or may be not) shared among all the instances
		 */
		synchronized (MUTEX) {			
			if (hashCurrentKB != KB_HASHCODE) {
				this.logger.info("MaxWeight Must be Computed Again (this activity may cost time ... )");
				KB_HASHCODE = hashCurrentKB;
				result = this.doTheComputation();
				CASHED_MAX_WEIGHT = result;
				this.logger.info("MaxWeight Computed");
			} else {
				result = CASHED_MAX_WEIGHT;
			}
		}
		return result;
	}
	
}
