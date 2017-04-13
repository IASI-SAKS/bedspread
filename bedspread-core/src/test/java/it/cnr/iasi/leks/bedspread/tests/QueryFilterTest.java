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

import java.util.Set;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.impl.URIImpl;
import it.cnr.iasi.leks.bedspread.rdf.sparqlImpl.DBpediaKB;
import it.cnr.iasi.leks.bedspread.rdf.sparqlImpl.Filters;

public class QueryFilterTest {


	private static final String HTTP_DBPEDIA_ORG_RESOURCE_CNR = "http://dbpedia.org/resource/National_Research_Council_(Italy)";
	
	protected final Logger logger = LoggerFactory.getLogger(QueryFilterTest.class);
	
	@Test
	public void generateFilterOutLiterals() {
		System.out.println("- generateFilterOutLiterals");
		
		DBpediaKB kb = DBpediaKB.getInstance();
		
		String resource = HTTP_DBPEDIA_ORG_RESOURCE_CNR;
		
		int degree = kb.degree(new URIImpl(resource), Filters.FILTER_NO);
		logger.info("{}", "degree("+resource+", FILTER_NO)="+degree);
		
		int degree_filtered = kb.degree(new URIImpl(resource), Filters.FILTER_OUT_LITERALS);
		logger.info("{}", "degree("+resource+", FILTER_OUT_LITERALS)="+degree_filtered);
		
		Assert.assertTrue(degree_filtered<=degree);
	}
	
	@Test
	public void generateFilterOutAll() {
		System.out.println("- generateFilterOutAll");
		
		DBpediaKB kb = DBpediaKB.getInstance();
		
		String resource = HTTP_DBPEDIA_ORG_RESOURCE_CNR;
		
		int degree = kb.degree(new URIImpl(resource), Filters.FILTER_NO);
		logger.info("{}", "degree("+resource+", FILTER_NO)="+degree);
		
		int degree_filtered = kb.degree(new URIImpl(resource), Filters.FILTER_OUT_BLACKLIST_PREDICATES);
		logger.info("{}", "degree("+resource+", FILTER_OUT_BLACKLIST_PREDICATES)="+degree_filtered);
		
		Assert.assertTrue(degree_filtered<=degree);
	}
	
	@Test
	public void generateFilterOutPredicates() {
		System.out.println("- generateFilterOutPredicates");
		
		DBpediaKB kb = DBpediaKB.getInstance();
		
		String resource = HTTP_DBPEDIA_ORG_RESOURCE_CNR;
		
		int degree = kb.degree(new URIImpl(resource), Filters.FILTER_NO);
		logger.info("{}", "degree("+resource+", FILTER_NO)="+degree);
		
		int degree_filtered = kb.degree(new URIImpl(resource), Filters.FILTER_OUT_ALL);
		logger.info("{}", "degree("+resource+", FILTER_OUT_ALL)="+degree_filtered);
		
		Assert.assertTrue(degree_filtered<=degree);
	}

	
	@Test
	public void generateFilterOutLiterals_neighbor() {
		System.out.println("- generateFilterOutLiterals");
		
		DBpediaKB kb = DBpediaKB.getInstance();
		
		String resource = HTTP_DBPEDIA_ORG_RESOURCE_CNR;
		
		Set<AnyResource> neighborhood = kb.getNeighborhood(new URIImpl(resource), Filters.FILTER_NO);
		logger.info("{}", "neighborhood("+resource+", FILTER_NO)="+neighborhood.size());
		
		Set<AnyResource> neighborhood_filtered = kb.getNeighborhood(new URIImpl(resource), Filters.FILTER_OUT_LITERALS);
		logger.info("{}", "neighborhood("+resource+", FILTER_OUT_LITERALS)="+neighborhood_filtered.size());
		
		Assert.assertTrue(neighborhood_filtered.size()<=neighborhood.size());
	}
	
	@Test
	public void generateFilterOutAll_neighbor() {
		System.out.println("- generateFilterOutAll");
		
		DBpediaKB kb = DBpediaKB.getInstance();
		
		String resource = HTTP_DBPEDIA_ORG_RESOURCE_CNR;
		
		Set<AnyResource> neighborhood = kb.getNeighborhood(new URIImpl(resource), Filters.FILTER_NO);
		logger.info("{}", "neighborhood("+resource+", FILTER_NO)="+neighborhood.size());
		
		Set<AnyResource> neighborhood_filtered = kb.getNeighborhood(new URIImpl(resource), Filters.FILTER_OUT_BLACKLIST_PREDICATES);
		logger.info("{}", "neighborhood("+resource+", FILTER_OUT_BLACKLIST_PREDICATES)="+neighborhood_filtered.size());
		
		Assert.assertTrue(neighborhood_filtered.size()<=neighborhood.size());
	}
	
	@Test
	public void generateFilterOutPredicates_neighbor() {
		System.out.println("- generateFilterOutPredicates");
		
		DBpediaKB kb = DBpediaKB.getInstance();
		
		String resource = HTTP_DBPEDIA_ORG_RESOURCE_CNR;
		
		Set<AnyResource> neighborhood = kb.getNeighborhood(new URIImpl(resource), Filters.FILTER_NO);
		logger.info("{}", "neighborhood("+resource+", FILTER_NO)="+neighborhood.size());
		
		Set<AnyResource> neighborhood_filtered = kb.getNeighborhood(new URIImpl(resource), Filters.FILTER_OUT_ALL);
		logger.info("{}", "neighborhood("+resource+", FILTER_OUT_ALL)="+neighborhood_filtered.size());
		
		Assert.assertTrue(neighborhood_filtered.size()<=neighborhood.size());
	}
}