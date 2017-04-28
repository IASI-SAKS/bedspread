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

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.WeightingFunction;
import it.cnr.iasi.leks.bedspread.exceptions.impl.UndefinedPropertyException;
import it.cnr.iasi.leks.bedspread.exceptions.impl.UnexpectedValueException;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.AnyURI;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.rdf.URI;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFTriple;

/**
 * 
 * @author ftaglino
 *
 */
public class SemanticWeighting_IC implements WeightingFunction {
	
	protected KnowledgeBase kb; 
	protected Abstract_EdgeWeighting_IC edgeWeightingIC;
	
	public SemanticWeighting_IC(KnowledgeBase kb) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, UndefinedPropertyException {
		this.kb = kb;
		this.edgeWeightingIC = EdgeWeightingFactory.getInstance().getAbstract_EdgeWeighting_IC(kb);
	}

	public SemanticWeighting_IC(KnowledgeBase kb, Abstract_EdgeWeighting_IC ew) {
		this.kb = kb;
		this.edgeWeightingIC = ew;
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
	 * @throws UnexpectedValueException 
	 */
	@Override
	public double weight(Node n1, Node n2) throws UnexpectedValueException {
		double result = 0.0; 
		
		// Find predicates linking n1 and n2 
		// 	firstly predicates from n1 to n2
		if(n1.getResource() instanceof AnyURI) {
			Set<AnyResource> predicates =  this.kb.getPredicatesBySubjectAndObject(n1.getResource(), n2.getResource());
	
			// For each predicate compute the weighting function
			for(AnyResource pred:predicates) {
				AnyURI s =  (AnyURI)n1.getResource();
				URI p = (URI)pred;
				AnyResource o = n2.getResource();
				// create the edge
				RDFTriple edge = new RDFTriple(s, p, o);
//				double w = EdgeWeighting_IC.edgeWeight_IC(kb, edge);
//				double w = EdgeWeighting_IC.edgeWeight_CombIC(kb, edge);
//				double w = this.edgeWeightingIC.computeEdgeWeight(edge);
				double w = this.edgeWeightingIC.computeNormalizedEdgeWeight(edge);
				if(w>result)
					result = w;
			}
		}
		// 	secondly predicates from n2 to n1
		if(n2.getResource() instanceof AnyURI) {
			Set<AnyResource> predicates =  kb.getPredicatesBySubjectAndObject(n2.getResource(), n1.getResource());
			// For each predicate compute the weighting function
			for(AnyResource pred:predicates) {
				AnyURI s =  (AnyURI)n2.getResource();
				URI p = (URI)pred;
				AnyResource o = n1.getResource();
				// create the edge
				RDFTriple edge = new RDFTriple(s, p, o);
//				double w = EdgeWeighting_IC.edgeWeight_IC(kb, edge);
//				double w = EdgeWeighting_IC.edgeWeight_CombIC(kb, edge);
				double w = this.edgeWeightingIC.computeNormalizedEdgeWeight(edge);
				if(w>result)
					result = w;
			}
		}

		return result;
	}

	public KnowledgeBase getKb() {
		return kb;
	}

	public void setKb(KnowledgeBase kb) {
		this.kb = kb;
	}
	
}