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
import it.cnr.iasi.leks.bedspread.rdf.impl.DBpediaKB;
import it.cnr.iasi.leks.bedspread.rdf.impl.LiteralImpl;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFTriple;
import it.cnr.iasi.leks.bedspread.rdf.impl.URIImpl;

public class RDFTripleToStringTest {

	private static final String HTTP_DBPEDIA_ORG_RESOURCE_INNOVATION = "http://dbpedia.org/resource/Innovation";

	@Test
	public void RDFTriple_toStringTest() {
		String subject_id = "http://dbpedia.org/resource/Barack_Obama";
		URIImpl subject = new URIImpl(subject_id); 
		String predicate_id = "http://dbpedia.org/ontology/residence";
		URIImpl predicate = new URIImpl(predicate_id);
		String object_id = "http://dbpedia.org/resource/Washington,_D.C.";
		URIImpl object = new URIImpl(object_id);
		
		RDFTriple edge = new RDFTriple(subject, predicate, object); 
		
		String edge_String = edge.toString();
		System.out.println(edge_String);
		
		Assert.assertTrue(edge_String.contentEquals("<http://dbpedia.org/resource/Barack_Obama, http://dbpedia.org/ontology/residence, http://dbpedia.org/resource/Washington,_D.C.>"));
	}

}
