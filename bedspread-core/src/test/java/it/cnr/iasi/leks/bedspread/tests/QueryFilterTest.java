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
import it.cnr.iasi.leks.bedspread.rdf.AnyURI;
import it.cnr.iasi.leks.bedspread.rdf.Literal;
import it.cnr.iasi.leks.bedspread.rdf.URI;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFFactory;
import it.cnr.iasi.leks.bedspread.rdf.impl.URIImpl;
import it.cnr.iasi.leks.bedspread.rdf.sparqlImpl.DBpediaKB;
import it.cnr.iasi.leks.bedspread.rdf.sparqlImpl.Filters;
import it.cnr.iasi.leks.bedspread.rdf.sparqlImpl.SPARQLQueryCollector;

public class QueryFilterTest {

	private static final String HTTP_DBPEDIA_ORG_RESOURCE_INNOVATION = "http://dbpedia.org/resource/Innovation";
	private static final String HTTP_DBPEDIA_ORG_RESOURCE_REIGATE = "http://dbpedia.org/resource/Reigate";
	private static final String HTTP_DBPEDIA_ORG_ONTOLOGY_ACTOR = "http://dbpedia.org/ontology/Actor";
	private static final String HTTP_DBPEDIA_ORG_RESOURCE_BARACK_OBAMA = "http://dbpedia.org/resource/Barack_Obama";
	
	@Test
	public void generateFilterOutLiterals() {
		System.out.println("- generateFilterOutLiterals");
		
		DBpediaKB kb = DBpediaKB.getInstance();
		
		String resource = HTTP_DBPEDIA_ORG_RESOURCE_BARACK_OBAMA;
		
		int degree = kb.degree(new URIImpl(resource), Filters.FILTER_NO);
		System.out.println("degree("+resource+", FILTER_NO)="+degree);
		
		degree = kb.degree(new URIImpl(resource), Filters.FILTER_OUT_LITERALS);
		System.out.println("degree("+resource+", FILTER_OUT_LITERALS)="+degree);
		
		Assert.assertTrue(true);
	}
	
	@Test
	public void generateFilterOutAll() {
		System.out.println("- generateFilterOutAll");
		
		DBpediaKB kb = DBpediaKB.getInstance();
		
		String resource = HTTP_DBPEDIA_ORG_RESOURCE_BARACK_OBAMA;
		
		int degree = kb.degree(new URIImpl(resource), Filters.FILTER_NO);
		System.out.println("degree("+resource+", FILTER_NO)="+degree);
		
		degree = kb.degree(new URIImpl(resource), Filters.FILTER_OUT_BLACKLIST_PREDICATES);
		System.out.println("degree("+resource+", FILTER_OUT_BLACKLIST_PREDICATES)="+degree);
		
		Assert.assertTrue(true);
	}
	
	@Test
	public void generateFilterOutPredicates() {
		System.out.println("- generateFilterOutPredicates");
		
		DBpediaKB kb = DBpediaKB.getInstance();
		
		String resource = HTTP_DBPEDIA_ORG_RESOURCE_BARACK_OBAMA;
		
		int degree = kb.degree(new URIImpl(resource), Filters.FILTER_NO);
		System.out.println("degree("+resource+", FILTER_NO)="+degree);
		
		degree = kb.degree(new URIImpl(resource), Filters.FILTER_OUT_ALL);
		System.out.println("degree("+resource+", FILTER_OUT_ALL)="+degree);
		
		Assert.assertTrue(true);
	}

}