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

import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.BlankNode;
import it.cnr.iasi.leks.bedspread.rdf.Literal;
import it.cnr.iasi.leks.bedspread.rdf.URI;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFFactory;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFGraph;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFTriple;
import it.cnr.iasi.leks.bedspread.util.SetOfNodesFactory;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author gulyx
 *
 */
public class BasicTest extends AbstractTest{

	private RDFFactory factory;

	public BasicTest (){
		this.factory = RDFFactory.getInstance();		
	}
	
	@Test
	public void testIfNodeEquals_SameURI(){				
		URI u1 = factory.createURI();

		Node n1 = new Node(u1);
		Node n2 = new Node(u1);
		
		boolean result = n1.equals(n2);
		Assert.assertTrue(result); 
	}

	@Test
	public void testIfNodesEqual_SameIdDifferentURI(){
		URI u1 = factory.createURI();
		String id = u1.getResourceID();
		URI u2 = factory.createURI(id);
		
		Node n1 = new Node(u1);
		Node n2 = new Node(u2);

		boolean result = n1.equals(n2);
		Assert.assertTrue(result); 		
	}

	@Test
	public void testIfNodesDifferent(){
		URI u1 = factory.createURI();
		URI u2 = factory.createURI();
		
		Node n1 = new Node(u1);
		Node n2 = new Node(u2);

		boolean result = n1.equals(n2);
		Assert.assertFalse(result); 		
	}

	@Test
	public void testContainedInSet(){
		URI u1 = factory.createURI();
		URI u2 = factory.createURI();
		BlankNode b = factory.createBlankNode();	
		Literal l = factory.createLiteral();
		
		Node n1 = new Node(u1);
		Node n2 = new Node(u2);
		Node n3 = new Node(b);
		Node n4 = new Node(l);
	
//		Set<Node> s = Collections.synchronizedSet(new HashSet<Node>());
//		Set<Node> s = new RevisedHashSet();
		Set<Node> s = SetOfNodesFactory.getInstance().getOtherSetOfNodesInstance();
		s.add(n1);
		s.add(n3);
		s.add(n4);
		
		boolean result = s.contains(n1);
		result = !s.contains(n2);
		result = s.contains(n1) && !s.contains(n2);
		Assert.assertTrue(result);
	}	

	@Test
	public void testMultipleInsertionInSet_HashSet(){
		SetOfNodesFactory factory = SetOfNodesFactory.getInstance();
		Set<Node> s1 = factory.getNaviteSetOfNodesInstance();
		Set<Node> s2 = factory.getNaviteSetOfNodesInstance();
		boolean result = this.implMultipleInsertionInSet(s1, s2);
		Assert.assertTrue(result);
	}
	
	@Test
	public void testMultipleInsertionInSet_RevisedHashSet(){
		SetOfNodesFactory factory = SetOfNodesFactory.getInstance();
		Set<Node> s1 = factory.getOtherSetOfNodesInstance();
		Set<Node> s2 = factory.getOtherSetOfNodesInstance();
		boolean result = this.implMultipleInsertionInSet(s1, s2);
		Assert.assertTrue(result);
	}

	private boolean implMultipleInsertionInSet (Set<Node> s1, Set<Node> s2){
		URI u1 = factory.createURI();
		
		Node n1 = new Node(u1);
		Node n2 = new Node(u1);
	
		s1.add(n1);
		s1.add(n2);
		
		s2.add(n1);
		s2.add(n1);

		boolean result = (s1.size() == 1) && (s2.size() == 1);
		return result;
	}
	
	@Test
	public void testNullOrEmptyRDFGraphCreation(){
		RDFGraph g = new RDFGraph();
		this.queryGraph(g);
		
		g = new RDFGraph((RDFTriple)null);
		this.queryGraph(g);
		
		g = new RDFGraph((Set<RDFTriple>)null);
		this.queryGraph(g);
		
		g = new RDFGraph(new HashSet<RDFTriple>());
		this.queryGraph(g);

		Assert.assertTrue(true);
	}

	private void queryGraph(RDFGraph g) {
		AnyResource resource = this.factory.createBlankNode("thisIsFoo");
		g.isMemberof(resource);
	}
}
