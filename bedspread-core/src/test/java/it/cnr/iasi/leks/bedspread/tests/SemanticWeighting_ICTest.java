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

import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.impl.weights.SemanticWeighting_IC;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.impl.DBpediaKB;
import it.cnr.iasi.leks.bedspread.rdf.impl.LiteralImpl;
import it.cnr.iasi.leks.bedspread.rdf.impl.URIImpl;

public class SemanticWeighting_ICTest {

	private static final String HTTP_DBPEDIA_ORG_RESOURCE_BARACK_OBAMA = "http://dbpedia.org/resource/Barack_Obama";
	private static final String HTTP_DBPEDIA_ORG_RESOURCE_JOE_BIDEN = "http://dbpedia.org/resource/Joe_Biden";
	

	@Test
	public void weight() {
		DBpediaKB kb = DBpediaKB.getInstance();
		URIImpl r1 = new URIImpl(HTTP_DBPEDIA_ORG_RESOURCE_BARACK_OBAMA);
		URIImpl r2 = new URIImpl(HTTP_DBPEDIA_ORG_RESOURCE_JOE_BIDEN);
		
		Node n1 = new Node(r1);
		Node n2 = new Node(r2);
		
		SemanticWeighting_IC sw = new SemanticWeighting_IC(kb);
		double w = sw.weight(n1, n2);
		
		System.out.println("SemanticWeighting_IC.weight(="+r1.getResourceID()+", "+r2.getResourceID()+")="+w);
				
		Assert.assertTrue(w>0);
	}


}
