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

import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.impl.LiteralImpl;
import it.cnr.iasi.leks.bedspread.rdf.impl.URIImpl;
import it.cnr.iasi.leks.bedspread.rdf.sparqlImpl.DBpediaKB;

public class DBPediaKBTest {

	private static final String HTTP_DBPEDIA_ORG_RESOURCE_INNOVATION = "http://dbpedia.org/resource/Innovation";
	private static final String HTTP_DBPEDIA_ORG_RESOURCE_REIGATE = "http://dbpedia.org/resource/Reigate";
	private static final String HTTP_DBPEDIA_ORG_ONTOLOGY_ACTOR = "http://dbpedia.org/ontology/Actor";

	@Test
	public void getIncomingNeighborhood() {
		DBpediaKB kb = DBpediaKB.getInstance();
		URIImpl node = new URIImpl(HTTP_DBPEDIA_ORG_ONTOLOGY_ACTOR); 
		Set<AnyResource> nodes = kb.getIncomingNeighborhood(node);
		Set<URIImpl> uris = new HashSet<URIImpl>();
		for(AnyResource n:nodes) {
				uris.add((URIImpl)n);
				System.out.println("incomingNeighbor: "+n.getResourceID());
		}
		System.out.println("incomingNeighborhood = "+uris.size());
				
		Assert.assertTrue(nodes.size()>0);
	}

	@Test
	public void getOutgoingNeighborhood() {
		DBpediaKB kb = DBpediaKB.getInstance();
		URIImpl node = new URIImpl(HTTP_DBPEDIA_ORG_ONTOLOGY_ACTOR); 
		Set<AnyResource> nodes = kb.getOutgoingNeighborhood(node);
		Set<URIImpl> uris = new HashSet<URIImpl>();
		Set<LiteralImpl> lits = new HashSet<LiteralImpl>();
		for(AnyResource n:nodes) {
			if(n instanceof URIImpl) {
				uris.add((URIImpl)n);
				System.out.println("outgoingNeighbor: "+n.getResourceID());
			}
			else if (n instanceof LiteralImpl) {
				lits.add((LiteralImpl)n);
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
		URIImpl node = new URIImpl(HTTP_DBPEDIA_ORG_ONTOLOGY_ACTOR); 
		Set<AnyResource> nodes = kb.getIncomingNeighborhood(node);
		nodes.addAll(kb.getOutgoingNeighborhood(node));

		Set<URIImpl> uris = new HashSet<URIImpl>();
		Set<LiteralImpl> lits = new HashSet<LiteralImpl>();
		for(AnyResource n:nodes) {
			if(n instanceof URIImpl) {
				uris.add((URIImpl)n);
				System.out.println("Resource: "+n.getResourceID());
			}
			else if (n instanceof LiteralImpl) {
				lits.add((LiteralImpl)n);
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
		URIImpl node = new URIImpl(HTTP_DBPEDIA_ORG_RESOURCE_INNOVATION);
		boolean membership = kb.isMemberof(node);
		Assert.assertTrue(membership == true);
	}
	
	@Test
	public void degree() {
		DBpediaKB kb = DBpediaKB.getInstance();
		URIImpl node = new URIImpl(HTTP_DBPEDIA_ORG_RESOURCE_INNOVATION);
		int degree = kb.degree(node);
		System.out.println("degree="+degree);
		Assert.assertTrue(degree>0);
	}
	
	@Test
	public void countAllTriples() {
		int result = 0;
		DBpediaKB kb = DBpediaKB.getInstance();
		result = kb.countAllTriples();
		System.out.println("countAllTriples="+result);
		Assert.assertTrue(result>0);
	}

}