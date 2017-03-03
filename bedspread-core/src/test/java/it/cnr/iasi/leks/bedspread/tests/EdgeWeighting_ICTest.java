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
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.rdf.impl.DBpediaKB;
import it.cnr.iasi.leks.bedspread.rdf.impl.EdgeWeighting_IC;
import it.cnr.iasi.leks.bedspread.rdf.impl.LiteralImpl;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFTriple;
import it.cnr.iasi.leks.bedspread.rdf.impl.URIImpl;

public class EdgeWeighting_ICTest {

	private static final String HTTP_DBPEDIA_ORG_RESOURCE_INNOVATION = "http://dbpedia.org/resource/Innovation";
	
	private static DBpediaKB kb = DBpediaKB.getInstance();
	
	private static RDFTriple edge_noLiterals = 
			new RDFTriple(
					new URIImpl(""), 
					new URIImpl(""), 
					new URIImpl("")
					);

	@Test
	public void getEdgeWeight_IC() {
		double result = EdgeWeighting_IC.edgeWeight_IC(kb, edge_noLiterals);
				
		System.out.println("IC("+edge_noLiterals.

		Assert.assertTrue(nodes.size()>0);
	}

}
