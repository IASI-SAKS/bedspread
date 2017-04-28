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

public class DBPediaKBTest {


	private static final String RESOURCE = "http://dbpedia.org/resource/Category:Legal_education_in_the_United_States";
	private static final String RESOURCE_2 = "http://dbpedia.org/resource/Category:United_States_law";
	private static final String PREDICATE = "http://www.w3.org/2004/02/skos/core#prefLabel";
	private static final String PREDICATE_2 = "http://www.w3.org/2004/02/skos/core#broader";
	
	protected final Logger logger = LoggerFactory.getLogger(DBPediaKBTest.class);
	
	@Ignore
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
		
		Assert.assertTrue((result==38)&&(result_filterOutLiterals==34)&&(result_filterOutPredicates==30)&&(result_filterOutAll==27));
	}

	@Ignore
	@Test
	public void countAllTriples() {	
		DBpediaKB kb = DBpediaKB.getInstance();
		
		int result = kb.countAllTriples(Filters.FILTER_NO);
		logger.info("{}", "countAllTriples(FILTER_NO)="+result);
		
		int result_filterOutLiterals = kb.countAllTriples(Filters.FILTER_OUT_LITERALS);
		logger.info("{}", "countAllTriples(FILTER_OUT_LITERALS)="+result_filterOutLiterals);
		
		int result_filterOutPredicates = kb.countAllTriples(Filters.FILTER_OUT_BLACKLIST_PREDICATES);
		logger.info("{}", "countAllTriples(FILTER_OUT_PREDICATES)="+result_filterOutPredicates);
		
		int result_filterOutAll = kb.countAllTriples(Filters.FILTER_OUT_ALL);
		logger.info("{}", "countAllTriples(FILTER_OUT_ALL)="+result_filterOutAll);
		
		Assert.assertTrue((result==397831457)&&(result_filterOutLiterals==264585515)&&(result_filterOutPredicates==393627614)&&(result_filterOutAll==261749431));
	}
	
	@Ignore
	@Test
	public void countTriplesByObject() {	
		DBpediaKB kb = DBpediaKB.getInstance();
		
		int result = kb.countTriplesByObject(new URIImpl(RESOURCE), Filters.FILTER_NO);
		logger.info("{}", "countTriplesByObject("+RESOURCE+", FILTER_NO)="+result);
		
		int result_filterOutLiterals = kb.countTriplesByObject(new URIImpl(RESOURCE), Filters.FILTER_OUT_LITERALS);
		logger.info("{}", "countTriplesByObject("+RESOURCE+", FILTER_OUT_LITERALS)="+result_filterOutLiterals);
		
		int result_filterOutPredicates = kb.countTriplesByObject(new URIImpl(RESOURCE), Filters.FILTER_OUT_BLACKLIST_PREDICATES);
		logger.info("{}", "countTriplesByObject("+RESOURCE+", FILTER_OUT_PREDICATES)="+result_filterOutPredicates);
		
		int result_filterOutAll = kb.countTriplesByObject(new URIImpl(RESOURCE), Filters.FILTER_OUT_ALL);
		logger.info("{}", "countTriplesByObject("+RESOURCE+", FILTER_OUT_ALL)="+result_filterOutAll);
		
		Assert.assertTrue((result==25)&&(result_filterOutLiterals==25)&&(result_filterOutPredicates==23)&&(result_filterOutAll==23));
	}
	
	@Ignore
	@Test
	public void countTriplesByPredicate() {	
		DBpediaKB kb = DBpediaKB.getInstance();
				
		int result = kb.countTriplesByPredicate(new URIImpl(PREDICATE), Filters.FILTER_NO);
		logger.info("{}", "countTriplesByPredicate("+PREDICATE+", FILTER_NO)="+result);
		
		int result_filterOutLiterals = kb.countTriplesByPredicate(new URIImpl(PREDICATE), Filters.FILTER_OUT_LITERALS);
		logger.info("{}", "countTriplesByPredicate("+PREDICATE+", FILTER_OUT_LITERALS)="+result_filterOutLiterals);
		
		int result_filterOutPredicates = kb.countTriplesByPredicate(new URIImpl(PREDICATE), Filters.FILTER_OUT_BLACKLIST_PREDICATES);
		logger.info("{}", "countTriplesByPredicate("+PREDICATE+", FILTER_OUT_PREDICATES)="+result_filterOutPredicates);
		
		int result_filterOutAll = kb.countTriplesByPredicate(new URIImpl(PREDICATE), Filters.FILTER_OUT_ALL);
		logger.info("{}", "countTriplesByPredicate("+PREDICATE+", FILTER_OUT_ALL)="+result_filterOutAll);
		
		Assert.assertTrue((result==1367759)&&(result_filterOutLiterals==0)&&(result_filterOutPredicates==1367759)&&(result_filterOutAll==0));
	}
	
	@Ignore
	@Test
	public void countTriplesByPredicateAndObject() {	
		DBpediaKB kb = DBpediaKB.getInstance();

		int result = kb.countTriplesByPredicateAndObject(new URIImpl(PREDICATE_2), new URIImpl(RESOURCE));
		logger.info("{}", "countTriplesByPredicateAndObject("+PREDICATE_2+", "+RESOURCE+", FILTER_NO)="+result);
				
		Assert.assertTrue((result==2));
	}
	
	@Ignore
	@Test
	public void countTriplesByPredicateAndSubject() {	
		DBpediaKB kb = DBpediaKB.getInstance();

		int result = kb.countTriplesByPredicateAndSubject(new URIImpl(PREDICATE_2), new URIImpl(RESOURCE), Filters.FILTER_NO);
		logger.info("{}", "countTriplesByPredicateAndSubject("+PREDICATE_2+", "+RESOURCE+", FILTER_NO)="+result);
		
		int result_filterOutLiterals = kb.countTriplesByPredicateAndSubject(new URIImpl(PREDICATE_2), new URIImpl(RESOURCE), Filters.FILTER_OUT_LITERALS);
		logger.info("{}", "countTriplesByPredicateAndSubject("+PREDICATE_2+", "+RESOURCE+", FILTER_OUT_LITERALS)="+result_filterOutLiterals);
		
		int result_filterOutPredicates = kb.countTriplesByPredicateAndSubject(new URIImpl(PREDICATE_2), new URIImpl(RESOURCE), Filters.FILTER_OUT_BLACKLIST_PREDICATES);
		logger.info("{}", "countTriplesByPredicateAndSubject("+PREDICATE_2+", "+RESOURCE+", FILTER_OUT_PREDICATES)="+result_filterOutPredicates);
		
		int result_filterOutAll = kb.countTriplesByPredicateAndSubject(new URIImpl(PREDICATE_2), new URIImpl(RESOURCE), Filters.FILTER_OUT_ALL);
		logger.info("{}", "countTriplesByPredicateAndSubject("+PREDICATE_2+", "+RESOURCE+", FILTER_OUT_ALL)="+result_filterOutAll);

		Assert.assertTrue((result==5)&&(result_filterOutLiterals==5)&&(result_filterOutPredicates==5)&&(result_filterOutAll==5));
	}
	
	@Ignore
	@Test
	public void countTriplesByPredicateAndSubjectOrObject() {	
		DBpediaKB kb = DBpediaKB.getInstance();

		int result = kb.countTriplesByPredicateAndSubjectOrObject(new URIImpl(PREDICATE_2), new URIImpl(RESOURCE), Filters.FILTER_NO);
		logger.info("{}", "countTriplesByPredicateAndSubjectOrObject("+PREDICATE_2+", "+RESOURCE+", FILTER_NO)="+result);
		
		int result_filterOutLiterals = kb.countTriplesByPredicateAndSubjectOrObject(new URIImpl(PREDICATE_2), new URIImpl(RESOURCE), Filters.FILTER_OUT_LITERALS);
		logger.info("{}", "countTriplesByPredicateAndSubjectOrObject("+PREDICATE_2+", "+RESOURCE+", FILTER_OUT_LITERALS)="+result_filterOutLiterals);
		
		int result_filterOutPredicates = kb.countTriplesByPredicateAndSubjectOrObject(new URIImpl(PREDICATE_2), new URIImpl(RESOURCE), Filters.FILTER_OUT_BLACKLIST_PREDICATES);
		logger.info("{}", "countTriplesByPredicateAndSubjectOrObject("+PREDICATE_2+", "+RESOURCE+", FILTER_OUT_PREDICATES)="+result_filterOutPredicates);
		
		int result_filterOutAll = kb.countTriplesByPredicateAndSubjectOrObject(new URIImpl(PREDICATE_2), new URIImpl(RESOURCE), Filters.FILTER_OUT_ALL);
		logger.info("{}", "countTriplesByPredicateAndSubjectOrObject("+PREDICATE_2+", "+RESOURCE+", FILTER_OUT_ALL)="+result_filterOutAll);

		Assert.assertTrue((result==7)&&(result_filterOutLiterals==7)&&(result_filterOutPredicates==7)&&(result_filterOutAll==7));
	}
	
	@Ignore
	@Test
	public void countTriplesBySubject() {	
		DBpediaKB kb = DBpediaKB.getInstance();
		
		int result = kb.countTriplesBySubject(new URIImpl(RESOURCE), Filters.FILTER_NO);
		logger.info("{}", "countTriplesBySubject("+RESOURCE+", FILTER_NO)="+result);
		
		int result_filterOutLiterals = kb.countTriplesBySubject(new URIImpl(RESOURCE), Filters.FILTER_OUT_LITERALS);
		logger.info("{}", "countTriplesBySubject("+RESOURCE+", FILTER_OUT_LITERALS)="+result_filterOutLiterals);
		
		int result_filterOutPredicates = kb.countTriplesBySubject(new URIImpl(RESOURCE), Filters.FILTER_OUT_BLACKLIST_PREDICATES);
		logger.info("{}", "countTriplesBySubject("+RESOURCE+", FILTER_OUT_PREDICATES)="+result_filterOutPredicates);
		
		int result_filterOutAll = kb.countTriplesBySubject(new URIImpl(RESOURCE), Filters.FILTER_OUT_ALL);
		logger.info("{}", "countTriplesBySubject("+RESOURCE+", FILTER_OUT_ALL)="+result_filterOutAll);

		Assert.assertTrue((result==13)&&(result_filterOutLiterals==9)&&(result_filterOutPredicates==7)&&(result_filterOutAll==4));
	}
	
	@Ignore
	@Test
	public void countTriplesBySubjectOrObject() {	
		DBpediaKB kb = DBpediaKB.getInstance();
		
		int result = kb.countTriplesBySubjectOrObject(new URIImpl(RESOURCE), Filters.FILTER_NO);
		logger.info("{}", "countTriplesBySubjectOrObject("+RESOURCE+", FILTER_NO)="+result);
		
		int result_filterOutLiterals = kb.countTriplesBySubjectOrObject(new URIImpl(RESOURCE), Filters.FILTER_OUT_LITERALS);
		logger.info("{}", "countTriplesBySubjectOrObject("+RESOURCE+", FILTER_OUT_LITERALS)="+result_filterOutLiterals);
		
		int result_filterOutPredicates = kb.countTriplesBySubjectOrObject(new URIImpl(RESOURCE), Filters.FILTER_OUT_BLACKLIST_PREDICATES);
		logger.info("{}", "countTriplesBySubjectOrObject("+RESOURCE+", FILTER_OUT_PREDICATES)="+result_filterOutPredicates);
		
		int result_filterOutAll = kb.countTriplesBySubjectOrObject(new URIImpl(RESOURCE), Filters.FILTER_OUT_ALL);
		logger.info("{}", "countTriplesBySubjectOrObject("+RESOURCE+", FILTER_OUT_ALL)="+result_filterOutAll);
		
		Assert.assertTrue((result==38)&&(result_filterOutLiterals==34)&&(result_filterOutPredicates==30)&&(result_filterOutAll==27));
	}
	
	@Ignore
	@Test
	public void getAllPredicates() {	
		DBpediaKB kb = DBpediaKB.getInstance();
		
		Set<AnyResource> result = kb.getAllPredicates(Filters.FILTER_NO);
		logger.info("{}", "getAllPredicates(FILTER_NO)="+result.size());
		
		Set<AnyResource> result_filterOutLiterals = kb.getAllPredicates(Filters.FILTER_OUT_LITERALS);
		logger.info("{}", "getAllPredicates(FILTER_OUT_LITERALS)="+result_filterOutLiterals.size());
		
		Set<AnyResource> result_filterOutPredicates = kb.getAllPredicates(Filters.FILTER_OUT_BLACKLIST_PREDICATES);
		logger.info("{}", "getAllPredicates(FILTER_OUT_PREDICATES)="+result_filterOutPredicates.size());
		
		Set<AnyResource> result_filterOutAll = kb.getAllPredicates(Filters.FILTER_OUT_ALL);
		logger.info("{}", "getAllPredicates(FILTER_OUT_ALL)="+result_filterOutAll.size());

		Assert.assertTrue((result.size()==3555)&&(result_filterOutLiterals.size()==1943)&&(result_filterOutPredicates.size()==3553)&&(result_filterOutAll.size()==1942));
	}
	
	@Ignore
	@Test
	public void getIncomingNeighborhood() {	
		DBpediaKB kb = DBpediaKB.getInstance();
		
		Set<AnyResource> result = kb.getIncomingNeighborhood(new URIImpl(RESOURCE), Filters.FILTER_NO);
		logger.info("{}", "getIncomingNeighborhood(FILTER_NO)="+result.size());
		
		Set<AnyResource> result_filterOutLiterals = kb.getIncomingNeighborhood(new URIImpl(RESOURCE), Filters.FILTER_OUT_LITERALS);
		logger.info("{}", "getIncomingNeighborhood(FILTER_OUT_LITERALS)="+result_filterOutLiterals.size());
		
		Set<AnyResource> result_filterOutPredicates = kb.getIncomingNeighborhood(new URIImpl(RESOURCE), Filters.FILTER_OUT_BLACKLIST_PREDICATES);
		logger.info("{}", "getIncomingNeighborhood(FILTER_OUT_PREDICATES)="+result_filterOutPredicates.size());
		
		Set<AnyResource> result_filterOutAll = kb.getIncomingNeighborhood(new URIImpl(RESOURCE), Filters.FILTER_OUT_ALL);
		logger.info("{}", "getIncomingNeighborhood(FILTER_OUT_ALL)="+result_filterOutAll.size());

		Assert.assertTrue((result.size()==25)&&(result_filterOutLiterals.size()==25)&&(result_filterOutPredicates.size()==23)&&(result_filterOutAll.size()==23));
	}

	@Ignore
	@Test
	public void getIncomingPredicates() {	
		DBpediaKB kb = DBpediaKB.getInstance();
		
		Set<AnyResource> result = kb.getIncomingPredicates(new URIImpl(RESOURCE), Filters.FILTER_NO);
		logger.info("{}", "getIncomingPredicates(FILTER_NO)="+result.size());
		
		Set<AnyResource> result_filterOutLiterals = kb.getIncomingPredicates(new URIImpl(RESOURCE), Filters.FILTER_OUT_LITERALS);
		logger.info("{}", "getIncomingPredicates(FILTER_OUT_LITERALS)="+result_filterOutLiterals.size());
		
		Set<AnyResource> result_filterOutPredicates = kb.getIncomingPredicates(new URIImpl(RESOURCE), Filters.FILTER_OUT_BLACKLIST_PREDICATES);
		logger.info("{}", "getIncomingPredicates(FILTER_OUT_PREDICATES)="+result_filterOutPredicates.size());
		
		Set<AnyResource> result_filterOutAll = kb.getIncomingPredicates(new URIImpl(RESOURCE), Filters.FILTER_OUT_ALL);
		logger.info("{}", "getIncomingPredicates(FILTER_OUT_ALL)="+result_filterOutAll.size());

		Assert.assertTrue((result.size()==2)&&(result_filterOutLiterals.size()==2)&&(result_filterOutPredicates.size()==1)&&(result_filterOutAll.size()==1));
	}
	
	@Ignore
	@Test
	public void getNeighborhood() {	
		DBpediaKB kb = DBpediaKB.getInstance();
		
		Set<AnyResource> result = kb.getNeighborhood(new URIImpl(RESOURCE), Filters.FILTER_NO);
		logger.info("{}", "getNeighborhood("+RESOURCE+", FILTER_NO)="+result.size());
		
		Set<AnyResource> result_filterOutLiterals = kb.getNeighborhood(new URIImpl(RESOURCE), Filters.FILTER_OUT_LITERALS);
		logger.info("{}", "getNeighborhood("+RESOURCE+", FILTER_OUT_LITERALS)="+result_filterOutLiterals.size());
		
		Set<AnyResource> result_filterOutPredicates = kb.getNeighborhood(new URIImpl(RESOURCE), Filters.FILTER_OUT_BLACKLIST_PREDICATES);
		logger.info("{}", "getNeighborhood("+RESOURCE+", FILTER_OUT_PREDICATES)="+result_filterOutPredicates.size());
		
		Set<AnyResource> result_filterOutAll = kb.getNeighborhood(new URIImpl(RESOURCE), Filters.FILTER_OUT_ALL);
		logger.info("{}", "getNeighborhood("+RESOURCE+", FILTER_OUT_ALL)="+result_filterOutAll.size());

		Assert.assertTrue((result.size()==37)&&(result_filterOutLiterals.size()==34)&&(result_filterOutPredicates.size()==30)&&(result_filterOutAll.size()==27));
	}
	
	@Ignore
	@Test
	public void getOutgoingNeighborhood() {	
		DBpediaKB kb = DBpediaKB.getInstance();
		
		Set<AnyResource> result = kb.getOutgoingNeighborhood(new URIImpl(RESOURCE), Filters.FILTER_NO);
		logger.info("{}", "getOutgoingNeighborhood("+RESOURCE+", FILTER_NO)="+result.size());
		
		Set<AnyResource> result_filterOutLiterals = kb.getOutgoingNeighborhood(new URIImpl(RESOURCE), Filters.FILTER_OUT_LITERALS);
		logger.info("{}", "getOutgoingNeighborhood("+RESOURCE+", FILTER_OUT_LITERALS)="+result_filterOutLiterals.size());
		
		Set<AnyResource> result_filterOutPredicates = kb.getOutgoingNeighborhood(new URIImpl(RESOURCE), Filters.FILTER_OUT_BLACKLIST_PREDICATES);
		logger.info("{}", "getOutgoingNeighborhood("+RESOURCE+", FILTER_OUT_PREDICATES)="+result_filterOutPredicates.size());
		
		Set<AnyResource> result_filterOutAll = kb.getOutgoingNeighborhood(new URIImpl(RESOURCE), Filters.FILTER_OUT_ALL);
		logger.info("{}", "getOutgoingNeighborhood("+RESOURCE+", FILTER_OUT_ALL)="+result_filterOutAll.size());

		Assert.assertTrue((result.size()==12)&&(result_filterOutLiterals.size()==9)&&(result_filterOutPredicates.size()==7)&&(result_filterOutAll.size()==4));
	}
	
	@Ignore
	@Test
	public void getOutgoingPredicates() {	
		DBpediaKB kb = DBpediaKB.getInstance();
		
		Set<AnyResource> result = kb.getOutgoingPredicates(new URIImpl(RESOURCE), Filters.FILTER_NO);
		logger.info("{}", "getOutgoingPredicates("+RESOURCE+", FILTER_NO)="+result.size());
		
		Set<AnyResource> result_filterOutLiterals = kb.getOutgoingPredicates(new URIImpl(RESOURCE), Filters.FILTER_OUT_LITERALS);
		logger.info("{}", "getOutgoingPredicates("+RESOURCE+", FILTER_OUT_LITERALS)="+result_filterOutLiterals.size());
		
		Set<AnyResource> result_filterOutPredicates = kb.getOutgoingPredicates(new URIImpl(RESOURCE), Filters.FILTER_OUT_BLACKLIST_PREDICATES);
		logger.info("{}", "getOutgoingPredicates("+RESOURCE+", FILTER_OUT_PREDICATES)="+result_filterOutPredicates.size());
		
		Set<AnyResource> result_filterOutAll = kb.getOutgoingPredicates(new URIImpl(RESOURCE), Filters.FILTER_OUT_ALL);
		logger.info("{}", "getOutgoingPredicates("+RESOURCE+", FILTER_OUT_ALL)="+result_filterOutAll.size());

		Assert.assertTrue((result.size()==8)&&(result_filterOutLiterals.size()==4)&&(result_filterOutPredicates.size()==6)&&(result_filterOutAll.size()==3));
	}
	
	@Ignore
	@Test
	public void getPredicatesBySubjectAndObject() {	
		DBpediaKB kb = DBpediaKB.getInstance();
		
		Set<AnyResource> result = kb.getPredicatesBySubjectAndObject(new URIImpl(RESOURCE), new URIImpl(RESOURCE_2), Filters.FILTER_NO);
		logger.info("{}", "getPredicatesBySubjectAndObject("+RESOURCE+", "+RESOURCE_2+" FILTER_NO)="+result.size());
		
		Set<AnyResource> result_filterOutLiterals = kb.getPredicatesBySubjectAndObject(new URIImpl(RESOURCE), new URIImpl(RESOURCE_2), Filters.FILTER_OUT_LITERALS);
		logger.info("{}", "getPredicatesBySubjectAndObject("+RESOURCE+", "+RESOURCE_2+" FILTER_OUT_LITERALS)="+result_filterOutLiterals.size());
		
		Set<AnyResource> result_filterOutPredicates = kb.getPredicatesBySubjectAndObject(new URIImpl(RESOURCE), new URIImpl(RESOURCE_2), Filters.FILTER_OUT_BLACKLIST_PREDICATES);
		logger.info("{}", "getPredicatesBySubjectAndObject("+RESOURCE+", "+RESOURCE_2+" FILTER_OUT_PREDICATES)="+result_filterOutPredicates.size());
		
		Set<AnyResource> result_filterOutAll = kb.getPredicatesBySubjectAndObject(new URIImpl(RESOURCE), new URIImpl(RESOURCE_2), Filters.FILTER_OUT_ALL);
		logger.info("{}", "getPredicatesBySubjectAndObject("+RESOURCE+", FILTER_OUT_ALL)="+result_filterOutAll.size());

		Assert.assertTrue((result.size()==1)&&(result_filterOutLiterals.size()==1)&&(result_filterOutPredicates.size()==0)&&(result_filterOutAll.size()==0));
	}
	
	@Ignore
	@Test
	public void isMemberof() {	
		DBpediaKB kb = DBpediaKB.getInstance();
		
		boolean result = kb.isMemberof(new URIImpl(RESOURCE_2), Filters.FILTER_NO);
		logger.info("{}", "isMemberof("+RESOURCE_2+", FILTER_NO)="+result);
		
		boolean result_filterOutLiterals = kb.isMemberof(new URIImpl(RESOURCE_2), Filters.FILTER_OUT_LITERALS);
		logger.info("{}", "isMemberof("+RESOURCE_2+", FILTER_OUT_LITERALS)="+result_filterOutLiterals);
		
		boolean result_filterOutPredicates = kb.isMemberof(new URIImpl(RESOURCE_2), Filters.FILTER_OUT_BLACKLIST_PREDICATES);
		logger.info("{}", "isMemberof("+RESOURCE_2+", FILTER_OUT_PREDICATES)="+result_filterOutPredicates);
		
		boolean result_filterOutAll = kb.isMemberof(new URIImpl(RESOURCE_2), Filters.FILTER_OUT_ALL);
		logger.info("{}", "isMemberof("+RESOURCE_2+", FILTER_OUT_ALL)="+result_filterOutAll);

		Assert.assertTrue((result==true)&&(result_filterOutLiterals==true)&&(result_filterOutPredicates==true)&&(result_filterOutAll==true));
	}
}