package it.cnr.iasi.leks.bedspread.tests;

import java.util.Set;

import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.rdf.BlankNode;
import it.cnr.iasi.leks.bedspread.rdf.Literal;
import it.cnr.iasi.leks.bedspread.rdf.URI;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFFactory;
import it.cnr.iasi.leks.bedspread.util.RevisedHashSet;

import org.junit.Assert;
import org.junit.Test;

public class BasicTest {

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
		Set<Node> s = new RevisedHashSet();
		s.add(n1);
		s.add(n3);
		s.add(n4);
		
		boolean result = s.contains(n1);
		result = !s.contains(n2);
		result = s.contains(n1) && !s.contains(n2);
		Assert.assertTrue(result);
	}	

	@Test
	public void testMultipleInsertionInSet(){
		URI u1 = factory.createURI();
		
		Node n1 = new Node(u1);
		Node n2 = new Node(u1);
	
//		Set<Node> s1 = Collections.synchronizedSet(new HashSet<Node>());
		Set<Node> s1 = new RevisedHashSet();
		s1.add(n1);
		s1.add(n2);
		
//		Set<Node> s2 = Collections.synchronizedSet(new HashSet<Node>());
		Set<Node> s2 = new RevisedHashSet();
		s2.add(n1);
		s2.add(n1);

		boolean result = (s1.size() == 1) && (s2.size() == 1);
		Assert.assertTrue(result);
	}	
}
