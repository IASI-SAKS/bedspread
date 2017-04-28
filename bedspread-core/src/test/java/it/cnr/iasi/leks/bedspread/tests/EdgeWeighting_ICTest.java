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
package it.cnr.iasi.leks.bedspread.tests;


import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import it.cnr.iasi.leks.bedspread.exceptions.impl.UnexpectedValueException;
import it.cnr.iasi.leks.bedspread.impl.weights.ic.EdgeWeightingFactory;
import it.cnr.iasi.leks.bedspread.impl.weights.ic.EdgeWeighting_CombIC;
import it.cnr.iasi.leks.bedspread.impl.weights.ic.EdgeWeighting_IC;
import it.cnr.iasi.leks.bedspread.impl.weights.ic.EdgeWeighting_IC_PMI;
import it.cnr.iasi.leks.bedspread.impl.weights.ic.EdgeWeighting_JointIC;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFFactory;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFTriple;
import it.cnr.iasi.leks.bedspread.rdf.sparqlImpl.DBpediaKB;
import it.cnr.iasi.leks.bedspread.tests.util.weightsIC.EdgeWeighting_CombIC_Test;
import it.cnr.iasi.leks.bedspread.tests.util.weightsIC.EdgeWeighting_IC_PMI_Test;
import it.cnr.iasi.leks.bedspread.tests.util.weightsIC.EdgeWeighting_IC_Test;
import it.cnr.iasi.leks.bedspread.tests.util.weightsIC.EdgeWeighting_JointIC_Test;

public class EdgeWeighting_ICTest {

	private static DBpediaKB kb = DBpediaKB.getInstance();
	
	private static RDFTriple edge_noLiterals = 
			new RDFTriple(
					RDFFactory.getInstance().createURI("http://dbpedia.org/resource/Barack_Obama"), 
					RDFFactory.getInstance().createURI("http://dbpedia.org/ontology/residence"), 
					RDFFactory.getInstance().createURI("http://dbpedia.org/resource/Washington,_D.C.")
					);

	@Ignore
	@Test
	public void edgeWeight_IC() throws UnexpectedValueException {		
		EdgeWeighting_IC ew = EdgeWeightingFactory.getInstance().getEdgeWeighting_IC(kb);
		double result = ew.computeNormalizedEdgeWeight(edge_noLiterals); 
		System.out.println("EdgeWeighting_IC.computeNormalizedEdgeWeight="+result);
		Assert.assertTrue(result>0);
	}

	@Ignore
	@Test
	public void edgeWeight_jointIC() throws UnexpectedValueException{
		EdgeWeighting_JointIC ew = EdgeWeightingFactory.getInstance().getEdgeWeighting_JointIC(kb);
		double result = ew.computeNormalizedEdgeWeight(edge_noLiterals); 
		System.out.println("EdgeWeighting_JointIC.computeNormalizedEdgeWeight="+result);
		Assert.assertTrue(result>0);
	}

	@Ignore
	@Test
	public void edgeWeight_CombIC() throws UnexpectedValueException {
		EdgeWeighting_CombIC ew = EdgeWeightingFactory.getInstance().getEdgeWeighting_CombIC(kb);
		double result = ew.computeNormalizedEdgeWeight(edge_noLiterals); 
		System.out.println("EdgeWeighting_CombIC.computeNormalizedEdgeWeight="+result);
		Assert.assertTrue(result>0);
	}
	
	@Ignore
	@Test
	public void edgeWeight_ICplusPMI() throws UnexpectedValueException{
		EdgeWeighting_IC_PMI ew = EdgeWeightingFactory.getInstance().getEdgeWeighting_IC_PMI(kb);
		double result = ew.computeNormalizedEdgeWeight(edge_noLiterals); 
		System.out.println("EdgeWeighting_IC_PMI.computeNormalizedEdgeWeight="+result);
		Assert.assertTrue(result>0);
	}
	
	@Ignore
	@Test
	public void predicateProbability() {
		EdgeWeighting_IC_Test ew = new EdgeWeighting_IC_Test(kb);
		double result = ew.predicateProbability(edge_noLiterals.getTriplePredicate());
		System.out.println("EdgeWeighting_IC_Test.predicateProbability="+result);
		Assert.assertTrue(result>0);
	}
	
	@Ignore
	@Test
	public void nodeProbability() {
		EdgeWeighting_CombIC_Test ew = new EdgeWeighting_CombIC_Test(kb);
		double result = ew.nodeProbability(edge_noLiterals.getTriplePredicate());
		System.out.println("EdgeWeighting_CombIC_Test.nodeProbability="+result);
		Assert.assertTrue(result>0);
	}

	@Ignore
	@Test
	public void nodeProbabilityConditionalToPredicate() {
		EdgeWeighting_JointIC_Test ew = new EdgeWeighting_JointIC_Test(kb);
		double result = ew.nodeProbabilityConditionalToPredicate(edge_noLiterals.getTriplePredicate(), edge_noLiterals.getTripleObject());
		System.out.println("EdgeWeighting_JointIC_Test.nodeProbabilityConditionalToPredicate="+result);
		Assert.assertTrue(result>0);
	}

	@Ignore
	@Test
	public void nodeAndPredicateProbability() {
		EdgeWeighting_IC_PMI_Test ew = new EdgeWeighting_IC_PMI_Test(kb);
		double result = ew.nodeAndPredicateProbability(edge_noLiterals.getTriplePredicate(), edge_noLiterals.getTripleObject());
		System.out.println("EdgeWeighting_IC_PMI_Test.nodeAndPredicateProbability="+result);
		Assert.assertTrue(result>0);
	}
	
	@Ignore
	@Test
	public void node_IC() {
		EdgeWeighting_CombIC_Test ew = new EdgeWeighting_CombIC_Test(kb);
		double result = ew.node_IC(edge_noLiterals.getTripleObject());
		System.out.println("EdgeWeighting_CombIC_Test.node_IC="+result);
		Assert.assertTrue(result>0);
	}
	
	@Ignore
	@Test
	public void nodeConditionalToPredicate_IC() {
		EdgeWeighting_JointIC_Test ew = new EdgeWeighting_JointIC_Test(kb);
		double result = ew.nodeConditionalToPredicate_IC(edge_noLiterals.getTriplePredicate(), edge_noLiterals.getTripleObject());
		System.out.println("EdgeWeighting_JointIC_Test.nodeConditionalToPredicate_IC="+result);
		Assert.assertTrue(result>0);
	}

	@Ignore
	@Test
	public void pmi() {
		EdgeWeighting_IC_PMI_Test ew = new EdgeWeighting_IC_PMI_Test(kb);
		double result = ew.pmi(edge_noLiterals.getTriplePredicate(), edge_noLiterals.getTripleObject());
		System.out.println("EdgeWeighting_IC_PMI_Test.pmi="+result);
		Assert.assertTrue(result>0);
	}
	
	@Ignore
	@Test
	public void predicate_IC() {
		EdgeWeighting_IC_Test ew = new EdgeWeighting_IC_Test(kb);
		double result = ew.predicate_IC(edge_noLiterals.getTriplePredicate());
		System.out.println("EdgeWeighting_IC_Test.predicate_IC="+result);
		Assert.assertTrue(result>0);
	}
	
}
