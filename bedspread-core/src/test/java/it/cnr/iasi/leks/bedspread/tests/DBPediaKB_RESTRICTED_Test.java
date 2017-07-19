/*
 * 	 This file is part of Bedspread, originally promoted and
 *	 developed at CNR-IASI. For more information visit:
 *	 http://leks.iasi.cnr.it/tools/bedspread
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.impl.URIImpl;
import it.cnr.iasi.leks.bedspread.rdf.sparqlImpl.DBpediaKB;
import it.cnr.iasi.leks.bedspread.rdf.sparqlImpl.Filters;

/**
 * 
 * @author ftaglino
 *
 */
public class DBPediaKB_RESTRICTED_Test {


	//private static final String RESOURCE = "http://dbpedia.org/resource/Category:Legal_education_in_the_United_States";
	private static final String RESOURCE = "http://dbpedia.org/resource/Barack_Obama";
	private static final String RESOURCE_2 = "http://dbpedia.org/resource/Category:United_States_law";
	private static final String PREDICATE = "http://www.w3.org/2004/02/skos/core#prefLabel";
	private static final String PREDICATE_2 = "http://www.w3.org/2004/02/skos/core#broader";
	
	protected final Logger logger = LoggerFactory.getLogger(DBPediaKB_RESTRICTED_Test.class);
	
	//@Ignore
	@Test
	public void degree() {	
		DBpediaKB kb = DBpediaKB.getInstance();
			
		int result = kb.degree(new URIImpl(RESOURCE), Filters.FILTER_NO);
		logger.info("{}", "degree("+RESOURCE+", FILTER_NO)="+result);
		
		int result_filterOutLiterals = kb.degree(new URIImpl(RESOURCE), Filters.FILTER_OUT_LITERALS);
		logger.info("{}", "degree("+RESOURCE+", FILTER_OUT_LITERALS)="+result_filterOutLiterals);
		
		int result_filterOutPredicates = kb.degree(new URIImpl(RESOURCE), Filters.FILTER_OUT_BLACKLIST_PREDICATES);
		logger.info("{}", "degree("+RESOURCE+", FILTER_OUT_PREDICATES)="+result_filterOutPredicates);
		
		int result_filterOutAll = kb.degree(new URIImpl(RESOURCE), Filters.FILTER_OUT_ALL);
		logger.info("{}", "degree("+RESOURCE+", FILTER_OUT_ALL)="+result_filterOutAll);
		
		Assert.assertTrue(true);
	}

	//@Ignore
	@Test
	public void countAllTriples() {	
		DBpediaKB kb = DBpediaKB.getInstance();
		
		int result = kb.countAllTriples(Filters.FILTER_NO);
		logger.info("{}", "countAllTriples(FILTER_NO)="+result);
				
		int result_filterOutAll = kb.countAllTriples(Filters.FILTER_OUT_ALL);
		logger.info("{}", "countAllTriples(FILTER_OUT_ALL)="+result_filterOutAll);
		
		Assert.assertTrue(true);
	}
	
	//@Ignore
	@Test
	public void countTriplesByObject() {	
		DBpediaKB kb = DBpediaKB.getInstance();
		
		int result = kb.countTriplesByObject(new URIImpl(RESOURCE), Filters.FILTER_NO);
		logger.info("{}", "countTriplesByObject("+RESOURCE+", FILTER_NO)="+result);
				
		int result_filterOutAll = kb.countTriplesByObject(new URIImpl(RESOURCE), Filters.FILTER_OUT_ALL);
		logger.info("{}", "countTriplesByObject("+RESOURCE+", FILTER_OUT_ALL)="+result_filterOutAll);
		
		Assert.assertTrue(true);
	}
	
	//@Ignore
	@Test
	public void countTriplesByPredicate() {	
		DBpediaKB kb = DBpediaKB.getInstance();
				
		int result = kb.countTriplesByPredicate(new URIImpl(PREDICATE), Filters.FILTER_NO);
		logger.info("{}", "countTriplesByPredicate("+PREDICATE+", FILTER_NO)="+result);
				
		int result_filterOutAll = kb.countTriplesByPredicate(new URIImpl(PREDICATE), Filters.FILTER_OUT_ALL);
		logger.info("{}", "countTriplesByPredicate("+PREDICATE+", FILTER_OUT_ALL)="+result_filterOutAll);
		
		Assert.assertTrue(true);
	}
	
	//@Ignore
	@Test
	public void countTriplesByPredicateAndObject() {	
		DBpediaKB kb = DBpediaKB.getInstance();

		int result = kb.countTriplesByPredicateAndObject(new URIImpl(PREDICATE_2), new URIImpl(RESOURCE));
		logger.info("{}", "countTriplesByPredicateAndObject("+PREDICATE_2+", "+RESOURCE+", FILTER_NO)="+result);
				
		Assert.assertTrue(true);
	}
	
	//@Ignore
	@Test
	public void countTriplesByPredicateAndSubject() {	
		DBpediaKB kb = DBpediaKB.getInstance();

		int result = kb.countTriplesByPredicateAndSubject(new URIImpl(PREDICATE_2), new URIImpl(RESOURCE), Filters.FILTER_NO);
		logger.info("{}", "countTriplesByPredicateAndSubject("+PREDICATE_2+", "+RESOURCE+", FILTER_NO)="+result);
		
		int result_filterOutAll = kb.countTriplesByPredicateAndSubject(new URIImpl(PREDICATE_2), new URIImpl(RESOURCE), Filters.FILTER_OUT_ALL);
		logger.info("{}", "countTriplesByPredicateAndSubject("+PREDICATE_2+", "+RESOURCE+", FILTER_OUT_ALL)="+result_filterOutAll);

		Assert.assertTrue(true);
	}
	
	//@Ignore
	@Test
	public void countTriplesByPredicateAndSubjectOrObject() {	
		DBpediaKB kb = DBpediaKB.getInstance();

		int result = kb.countTriplesByPredicateAndSubjectOrObject(new URIImpl(PREDICATE_2), new URIImpl(RESOURCE), Filters.FILTER_NO);
		logger.info("{}", "countTriplesByPredicateAndSubjectOrObject("+PREDICATE_2+", "+RESOURCE+", FILTER_NO)="+result);
		
		int result_filterOutAll = kb.countTriplesByPredicateAndSubjectOrObject(new URIImpl(PREDICATE_2), new URIImpl(RESOURCE), Filters.FILTER_OUT_ALL);
		logger.info("{}", "countTriplesByPredicateAndSubjectOrObject("+PREDICATE_2+", "+RESOURCE+", FILTER_OUT_ALL)="+result_filterOutAll);

		Assert.assertTrue(true);
	}
	
	//@Ignore
	@Test
	public void countTriplesBySubject() {	
		DBpediaKB kb = DBpediaKB.getInstance();
		
		int result = kb.countTriplesBySubject(new URIImpl(RESOURCE), Filters.FILTER_NO);
		logger.info("{}", "countTriplesBySubject("+RESOURCE+", FILTER_NO)="+result);
				
		int result_filterOutAll = kb.countTriplesBySubject(new URIImpl(RESOURCE), Filters.FILTER_OUT_ALL);
		logger.info("{}", "countTriplesBySubject("+RESOURCE+", FILTER_OUT_ALL)="+result_filterOutAll);

		Assert.assertTrue(true);
	}
	
	//@Ignore
	@Test
	public void countTriplesBySubjectOrObject() {	
		DBpediaKB kb = DBpediaKB.getInstance();
		
		int result = kb.countTriplesBySubjectOrObject(new URIImpl(RESOURCE), Filters.FILTER_NO);
		logger.info("{}", "countTriplesBySubjectOrObject("+RESOURCE+", FILTER_NO)="+result);
		
		int result_filterOutAll = kb.countTriplesBySubjectOrObject(new URIImpl(RESOURCE), Filters.FILTER_OUT_ALL);
		logger.info("{}", "countTriplesBySubjectOrObject("+RESOURCE+", FILTER_OUT_ALL)="+result_filterOutAll);
		
		Assert.assertTrue(true);
	}
	
	//@Ignore
	@Test
	public void getAllPredicates() {	
		DBpediaKB kb = DBpediaKB.getInstance();
		
		Set<AnyResource> result_filterOutAll = kb.getAllPredicates(Filters.FILTER_OUT_ALL);
		logger.info("{}", "getAllPredicates(FILTER_OUT_ALL)="+result_filterOutAll.size());

		Assert.assertTrue(true);	
	}
	
	//@Ignore
	@Test
	public void getIncomingNeighborhood() {	
		DBpediaKB kb = DBpediaKB.getInstance();
		
		Set<AnyResource> result = kb.getIncomingNeighborhood(new URIImpl(RESOURCE), Filters.FILTER_NO);
		logger.info("{}", "getIncomingNeighborhood(FILTER_NO)="+result.size());
		
		Set<AnyResource> result_filterOutAll = kb.getIncomingNeighborhood(new URIImpl(RESOURCE), Filters.FILTER_OUT_ALL);
		logger.info("{}", "getIncomingNeighborhood(FILTER_OUT_ALL)="+result_filterOutAll.size());

		Assert.assertTrue(true);
	}

	
	//@Ignore
	@Test
	public void getNeighborhood() {	
		DBpediaKB kb = DBpediaKB.getInstance();
		
		Set<AnyResource> result = kb.getNeighborhood(new URIImpl(RESOURCE), Filters.FILTER_NO);
		logger.info("{}", "getNeighborhood("+RESOURCE+", FILTER_NO)="+result.size());

		Set<AnyResource> result_filterOutAll = kb.getNeighborhood(new URIImpl(RESOURCE), Filters.FILTER_OUT_ALL);
		logger.info("{}", "getNeighborhood("+RESOURCE+", FILTER_OUT_ALL)="+result_filterOutAll.size());

		Assert.assertTrue(true);
	}
	
	//@Ignore
	@Test
	public void getOutgoingNeighborhood() {	
		DBpediaKB kb = DBpediaKB.getInstance();
		
		Set<AnyResource> result = kb.getOutgoingNeighborhood(new URIImpl(RESOURCE), Filters.FILTER_NO);
		logger.info("{}", "getOutgoingNeighborhood("+RESOURCE+", FILTER_NO)="+result.size());
				
		Set<AnyResource> result_filterOutAll = kb.getOutgoingNeighborhood(new URIImpl(RESOURCE), Filters.FILTER_OUT_ALL);
		logger.info("{}", "getOutgoingNeighborhood("+RESOURCE+", FILTER_OUT_ALL)="+result_filterOutAll.size());

		Assert.assertTrue(true);
	}
		
	//@Ignore
	@Test
	public void getPredicatesBySubjectAndObject() {	
		DBpediaKB kb = DBpediaKB.getInstance();
		
		Set<AnyResource> result = kb.getPredicatesBySubjectAndObject(new URIImpl(RESOURCE), new URIImpl(RESOURCE_2), Filters.FILTER_NO);
		logger.info("{}", "getPredicatesBySubjectAndObject("+RESOURCE+", "+RESOURCE_2+" FILTER_NO)="+result.size());
		
		Set<AnyResource> result_filterOutAll = kb.getPredicatesBySubjectAndObject(new URIImpl(RESOURCE), new URIImpl(RESOURCE_2), Filters.FILTER_OUT_ALL);
		logger.info("{}", "getPredicatesBySubjectAndObject("+RESOURCE+", "+RESOURCE_2+" FILTER_OUT_ALL)="+result_filterOutAll.size());

		Assert.assertTrue(true);
	}
	
	//@Ignore
	@Test
	public void isMemberof() {	
		DBpediaKB kb = DBpediaKB.getInstance();
		
		boolean result = kb.isMemberof(new URIImpl(RESOURCE_2), Filters.FILTER_NO);
		logger.info("{}", "isMemberof("+RESOURCE_2+", FILTER_NO)="+result);
		
		boolean result_filterOutAll = kb.isMemberof(new URIImpl(RESOURCE_2), Filters.FILTER_OUT_ALL);
		logger.info("{}", "isMemberof("+RESOURCE_2+", FILTER_OUT_ALL)="+result_filterOutAll);

		Assert.assertTrue(true);
	}
}