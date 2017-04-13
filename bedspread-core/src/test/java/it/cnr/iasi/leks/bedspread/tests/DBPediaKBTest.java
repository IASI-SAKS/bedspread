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
import org.junit.Ignore;
import org.junit.Test;

import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.Literal;
import it.cnr.iasi.leks.bedspread.rdf.URI;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFFactory;
import it.cnr.iasi.leks.bedspread.rdf.sparqlImpl.DBpediaKB;

public class DBPediaKBTest {

	private static final String HTTP_DBPEDIA_ORG_RESOURCE_CNR = "http://dbpedia.org/page/National_Research_Council_(Italy)";

	@Ignore
	@Test
	public void getIncomingNeighborhood() {
		DBpediaKB kb = DBpediaKB.getInstance();
		URI node = RDFFactory.getInstance().createURI(HTTP_DBPEDIA_ORG_RESOURCE_CNR); 
		Set<AnyResource> nodes = kb.getIncomingNeighborhood(node);
		Set<URI> uris = new HashSet<URI>();
		for(AnyResource n:nodes) {
				uris.add((URI)n);
				System.out.println("incomingNeighbor: "+n.getResourceID());
		}
		System.out.println("incomingNeighborhood = "+uris.size());
				
		Assert.assertTrue(nodes.size()>0);
	}

	@Ignore
	@Test
	public void getOutgoingNeighborhood() {
		DBpediaKB kb = DBpediaKB.getInstance();
		URI node = RDFFactory.getInstance().createURI(HTTP_DBPEDIA_ORG_RESOURCE_CNR); 
		Set<AnyResource> nodes = kb.getOutgoingNeighborhood(node);
		Set<URI> uris = new HashSet<URI>();
		Set<Literal> lits = new HashSet<Literal>();
		for(AnyResource n:nodes) {
			if(n instanceof URI) {
				uris.add((URI)n);
				System.out.println("outgoingNeighbor: "+n.getResourceID());
			}
			else if (n instanceof Literal) {
				lits.add((Literal)n);
				System.out.println("Literal: "+n.getResourceID());
			}
		}
		System.out.println("outgoiingNeighborhood(URIs) = "+uris.size());
		System.out.println("outgoingNeighborhood(Literals) = "+lits.size());
		Assert.assertTrue(nodes.size()>0);
	}
	
	@Test
	public void getNeighborhood() {
		DBpediaKB kb = DBpediaKB.getInstance();
		URI node = RDFFactory.getInstance().createURI(HTTP_DBPEDIA_ORG_RESOURCE_CNR); 
		Set<AnyResource> nodes = kb.getIncomingNeighborhood(node);
		nodes.addAll(kb.getOutgoingNeighborhood(node));

		Set<URI> uris = new HashSet<URI>();
		Set<Literal> lits = new HashSet<Literal>();
		for(AnyResource n:nodes) {
			if(n instanceof URI) {
				uris.add((URI)n);
				System.out.println("Resource: "+n.getResourceID());
			}
			else if (n instanceof Literal) {
				lits.add((Literal)n);
				System.out.println("Literal: "+n.getResourceID());
			}
		}
		System.out.println("Total URIs = "+uris.size());
		System.out.println("Total Literals = "+lits.size());
		Assert.assertTrue(nodes.size()>0);
	}
	
	@Test
	public void isMemberOf() {
		DBpediaKB kb = DBpediaKB.getInstance();
		URI node = RDFFactory.getInstance().createURI(HTTP_DBPEDIA_ORG_RESOURCE_CNR);
		boolean membership = kb.isMemberof(node);
		Assert.assertTrue(membership == true);
	}
	
	@Test
	public void degree() {
		DBpediaKB kb = DBpediaKB.getInstance();
		URI node = RDFFactory.getInstance().createURI(HTTP_DBPEDIA_ORG_RESOURCE_CNR);
		int degree = kb.degree(node);
		System.out.println("degree="+degree);
		Assert.assertTrue(degree>0);
	}
	
	@Ignore
	@Test
	public void countAllTriples() {
		int result = 0;
		DBpediaKB kb = DBpediaKB.getInstance();
		result = kb.countAllTriples();
		System.out.println("countAllTriples="+result);
		Assert.assertTrue(result>0);
	}

}