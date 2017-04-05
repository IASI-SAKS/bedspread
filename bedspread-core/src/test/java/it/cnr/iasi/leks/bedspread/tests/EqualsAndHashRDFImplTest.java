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

import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.BlankNode;
import it.cnr.iasi.leks.bedspread.rdf.Literal;
import it.cnr.iasi.leks.bedspread.rdf.URI;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFFactory;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author gulyx
 *
 */
public class EqualsAndHashRDFImplTest extends AbstractTest{

	private RDFFactory factory;

	public EqualsAndHashRDFImplTest (){
		this.factory = RDFFactory.getInstance();		
	}
	
	private boolean testIfSameResource(AnyResource r1){				
		AnyResource r2 = r1;

		boolean result = r1.equals(r2);
		return result; 
	}

	@Test
	public void testIfSameURI(){				
		URI u = factory.createURI();
		
		boolean result = this.testIfSameResource(u);
		Assert.assertTrue(result); 
	}

	@Test
	public void testIfSameLiteral(){				
		Literal l = factory.createLiteral();
		
		boolean result = this.testIfSameResource(l);
		Assert.assertTrue(result); 
	}
	
	@Test
	public void testIfSameBlankNode(){				
		BlankNode b = factory.createBlankNode();
		
		boolean result = this.testIfSameResource(b);
		Assert.assertTrue(result); 
	}

	private boolean testIfSameIdDifferentResourceButSameKind(AnyResource r1){
		String newID = r1.getResourceID();

		AnyResource r2;
		if (r1 instanceof URI){
			r2 = factory.createURI(newID);
		}else{
			if (r1 instanceof Literal){
				r2 = factory.createLiteral(newID);				
			}else{
				if (r1 instanceof BlankNode){
					r2 = factory.createBlankNode(newID);			
				} else {
					// the test must fail
					return false;
				}
			}	
		}
		
		boolean result = r1.equals(r2);
		return result; 		
	}

	@Test
	public void testIfSameIdDifferentURI(){
		URI u = factory.createURI();

		boolean result = this.testIfSameIdDifferentResourceButSameKind(u);
		Assert.assertTrue(result); 		
	}

	@Test
	public void testIfSameIdDifferentLiteral(){
		Literal l = factory.createLiteral();

		boolean result = this.testIfSameIdDifferentResourceButSameKind(l);
		Assert.assertTrue(result); 		
	}

	@Test
	public void testIfSameIdDifferentBlanckNode(){
		BlankNode bn = factory.createBlankNode();

		boolean result = this.testIfSameIdDifferentResourceButSameKind(bn);
		Assert.assertTrue(result); 		
	}

	private boolean testIfSameIdDifferentResource(AnyResource r1){
		String newID = r1.getResourceID();

		AnyResource r2;
		if (r1 instanceof URI){
			r2 = factory.createLiteral(newID);
		}else{
			if (r1 instanceof Literal){
				r2 = factory.createBlankNode(newID);				
			}else{
				if (r1 instanceof BlankNode){
					r2 = factory.createURI(newID);			
				} else {
					// the test must fail
					return false;
				}
			}	
		}
		
		boolean result = !(r1.equals(r2));
		return result; 		
	}

	@Test
	public void testIfSameIdDifferentThanURI(){
		URI u = factory.createURI();
		
		boolean result = this.testIfSameIdDifferentResource(u);
		Assert.assertTrue(result); 		
	}

	@Test
	public void testIfSameIdDifferentThanLiteral(){
		Literal l = factory.createLiteral();
		
		boolean result = this.testIfSameIdDifferentResource(l);
		Assert.assertTrue(result); 		
	}
	
	@Test
	public void testIfSameIdDifferentThanBlankNode(){
		BlankNode bn = factory.createBlankNode();
		
		boolean result = this.testIfSameIdDifferentResource(bn);
		Assert.assertTrue(result); 		
	}

	private boolean testIfDifferentIdResourceSameKind(AnyResource r1){
		String newID = "this_is_foo_"+r1.getResourceID();

		AnyResource r2;
		if (r1 instanceof URI){
			r2 = factory.createURI(newID);
		}else{
			if (r1 instanceof Literal){
				r2 = factory.createLiteral(newID);				
			}else{
				if (r1 instanceof BlankNode){
					r2 = factory.createBlankNode(newID);			
				} else {
					// the test must fail
					return false;
				}
			}	
		}
		
		boolean result = !(r1.equals(r2));
		return result; 		
	}

	@Test
	public void testIfDifferentURI(){
		URI u = factory.createURI();

		boolean result = this.testIfDifferentIdResourceSameKind(u);
		Assert.assertTrue(result); 		
	}

	@Test
	public void testIfDifferentLiteral(){
		Literal l = factory.createLiteral();

		boolean result = this.testIfDifferentIdResourceSameKind(l);
		Assert.assertTrue(result); 		
	}
	
	@Test
	public void testIfDifferentBlankNode(){
		BlankNode bn = factory.createBlankNode();

		boolean result = this.testIfDifferentIdResourceSameKind(bn);
		Assert.assertTrue(result); 		
	}

	@Test
	public void testContainedInSet_HashSet(){
		URI u1 = factory.createURI();
		String u1ID = u1.getResourceID();
		URI u2 = factory.createURI(u1ID+"this_is_uoo");
		URI u3 = factory.createURI(u1ID);
		
		BlankNode b1 = factory.createBlankNode();	
		String b1ID = b1.getResourceID();
		BlankNode b2 = factory.createBlankNode(b1ID+"this_is_boo");	
		BlankNode b3 = factory.createBlankNode(b1ID);	

		Literal l1 = factory.createLiteral();
		String l1ID = l1.getResourceID();
		Literal l2 = factory.createLiteral(l1ID+"this_is_loo");
		Literal l3 = factory.createLiteral(l1ID);
		
		URI u4 = factory.createURI(b1ID);
		URI u5 = factory.createURI(l1ID);

		BlankNode b4 = factory.createBlankNode(u1ID);	
		BlankNode b5 = factory.createBlankNode(l1ID);	
		
		Literal l4 = factory.createLiteral(u1ID);
		Literal l5 = factory.createLiteral(b1ID);

		Set<AnyResource> s = new HashSet<AnyResource>();
		s.add(u1);
		s.add(u4);
		s.add(u5);
		s.add(b1);
		s.add(b4);
		s.add(b5);
		s.add(l1);		
		s.add(l4);
		s.add(l5);
		
		
		boolean result = s.contains(u1);
		result = !s.contains(u2) && result;
		result = s.contains(u3) && result;
		result = s.contains(u4) && result;
		result = s.contains(u5) && result;
		result = s.contains(b1) && result;
		result = !s.contains(b2) && result;
		result = s.contains(b3) && result;
		result = s.contains(b4) && result;
		result = s.contains(b5) && result;
		result = s.contains(l1) && result;
		result = !s.contains(l2) && result;
		result = s.contains(l3) && result;
		result = s.contains(l4) && result;
		result = s.contains(l5) && result;
		Assert.assertTrue(result);
	}	

	@Test
	public void testMultipleInsertionInSet_HashSet(){
		URI u1 = factory.createURI();
		String u1ID = u1.getResourceID();
		URI u2 = factory.createURI(u1ID+"this_is_uoo");
		URI u3 = factory.createURI(u1ID);
		
		BlankNode b1 = factory.createBlankNode();	
		String b1ID = b1.getResourceID();
		BlankNode b2 = factory.createBlankNode(b1ID+"this_is_boo");	
		BlankNode b3 = factory.createBlankNode(b1ID);	

		Literal l1 = factory.createLiteral();
		String l1ID = l1.getResourceID();
		Literal l2 = factory.createLiteral(l1ID+"this_is_loo");
		Literal l3 = factory.createLiteral(l1ID);
		
		URI u4 = factory.createURI(b1ID);
		URI u5 = factory.createURI(l1ID);

		BlankNode b4 = factory.createBlankNode(u1ID);	
		BlankNode b5 = factory.createBlankNode(l1ID);	
		
		Literal l4 = factory.createLiteral(u1ID);
		Literal l5 = factory.createLiteral(b1ID);

		Set<AnyResource> s = new HashSet<AnyResource>();
		s.add(u1);
		s.add(u2);
			s.add(u3);
		s.add(u4);
		s.add(u5);
		s.add(b1);
		s.add(b2);
			s.add(b3);
		s.add(b4);
		s.add(b5);
		s.add(l1);
		s.add(l2);
			s.add(l3);
		s.add(l4);
		s.add(l5);
		
			s.add(u1);
			s.add(b1);
			s.add(l1);

		boolean result = (s.size() == 12);
		Assert.assertTrue(result);
	}
		
}
