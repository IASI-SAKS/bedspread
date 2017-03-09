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
package it.cnr.iasi.leks.bedspread.tests;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import it.cnr.iasi.leks.bedspread.impl.weights.EdgeWeighting_IC;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.rdf.impl.DBpediaKB;
import it.cnr.iasi.leks.bedspread.rdf.impl.LiteralImpl;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFTriple;
import it.cnr.iasi.leks.bedspread.rdf.impl.URIImpl;

public class EdgeWeighting_ICTest {

	private static DBpediaKB kb = DBpediaKB.getInstance();
	
	private static RDFTriple edge_noLiterals = 
			new RDFTriple(
					new URIImpl("http://dbpedia.org/resource/Barack_Obama"), 
					new URIImpl("http://dbpedia.org/ontology/residence"), 
					new URIImpl("http://dbpedia.org/resource/Washington,_D.C.")
					);

	@Test
	public void edgeWeight_IC() {
		double result = EdgeWeighting_IC.edgeWeight_IC(kb, edge_noLiterals);
		System.out.println("EdgeWeighting_IC.edgeWeight_IC="+result);
		Assert.assertTrue(result>0);
	}

	@Test
	public void edgeWeight_jointIC(){
		double result = EdgeWeighting_IC.edgeWeight_jointIC(kb, edge_noLiterals);
		System.out.println("EdgeWeighting_IC.edgeWeight_jointIC="+result);
		Assert.assertTrue(result>0);
	}

	@Test
	public void edgeWeight_CombIC() {
		double result = EdgeWeighting_IC.edgeWeight_CombIC(kb, edge_noLiterals);
		System.out.println("EdgeWeighting_IC.edgeWeight_CombIC="+result);
		Assert.assertTrue(result>0);
	}
	
	@Test
	public void edgeWeight_ICplusPMI(){
		double result = EdgeWeighting_IC.edgeWeight_ICplusPMI(kb, edge_noLiterals);
		System.out.println("EdgeWeighting_IC.edgeWeight_ICplusPMI="+result);
		Assert.assertTrue(result>0);
	}
	
	@Test
	public void predicateProbability() {
		double result = EdgeWeighting_IC.predicateProbability(kb, edge_noLiterals.getTriplePredicate());
		System.out.println("EdgeWeighting_IC.predicateProbability="+result);
		Assert.assertTrue(result>0);
	}
	
	@Test
	public void nodeProbability() {
		double result = EdgeWeighting_IC.nodeProbability(kb, edge_noLiterals.getTripleObject());
		System.out.println("EdgeWeighting_IC.nodeProbability="+result);
		Assert.assertTrue(result>0);
	}

	@Test
	public void nodeProbabilityConditionalToPredicate() {
		double result = EdgeWeighting_IC.nodeProbabilityConditionalToPredicate(kb, edge_noLiterals.getTriplePredicate(), edge_noLiterals.getTripleObject());
		System.out.println("EdgeWeighting_IC.nodeProbabilityConditionalToPredicate="+result);
		Assert.assertTrue(result>0);
	}

	@Test
	public void nodeAndPredicateProbability() {
		double result = EdgeWeighting_IC.nodeAndPredicateProbability(kb, edge_noLiterals.getTriplePredicate(), edge_noLiterals.getTripleObject());
		System.out.println("EdgeWeighting_IC.nodeAndPredicateProbability="+result);
		Assert.assertTrue(result>0);
	}
	
	@Test
	public void node_IC() {
		double result = EdgeWeighting_IC.node_IC(kb, edge_noLiterals.getTripleObject());
		System.out.println("EdgeWeighting_IC.node_IC="+result);
		Assert.assertTrue(result>0);
	}
	
	@Test
	public void nodeConditionalToPredicate_IC() {
		double result = EdgeWeighting_IC.nodeConditionalToPredicate_IC(kb, edge_noLiterals.getTriplePredicate(), edge_noLiterals.getTripleObject());
		System.out.println("EdgeWeighting_IC.nodeConditionalToPredicate_IC="+result);
		Assert.assertTrue(result>0);
	}

	@Test
	public void pmi() {
		double result = EdgeWeighting_IC.pmi(kb, edge_noLiterals.getTriplePredicate(), edge_noLiterals.getTripleObject());
		System.out.println("EdgeWeighting_IC.pmi="+result);
		Assert.assertTrue(result>0);
	}
	
	@Test
	public void predicate_IC() {
		double result = EdgeWeighting_IC.predicate_IC(kb, edge_noLiterals.getTriplePredicate());
		System.out.println("EdgeWeighting_IC.predicate_IC="+result);
		Assert.assertTrue(result>0);
	}
	
}
