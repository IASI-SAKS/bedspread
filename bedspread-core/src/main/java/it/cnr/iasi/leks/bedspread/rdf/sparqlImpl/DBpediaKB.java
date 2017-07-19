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
package it.cnr.iasi.leks.bedspread.rdf.sparqlImpl;
/**
 * 
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.opencsv.CSVReader;

import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.AnyURI;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.rdf.impl.URIImpl;

/**
 * 
 * @author ftaglino
 *
 */
public class DBpediaKB implements KnowledgeBase {

	private String endpoint;
	private String graph;
	
	private DBpediaKBCache cache = new DBpediaKBCache();

	private Set<AnyURI> predicatesBlackList = new HashSet<AnyURI>();
//	private Set<AnyURI> predicatesIn = new HashSet<AnyURI>();
	private Set<AnyResource> validPredicates = new HashSet<AnyResource>();
	
	private static DBpediaKB instance = null;

	private final boolean caching = false;
	/**
	 * 
	 */
	private DBpediaKB() {
		PropertyUtil p = PropertyUtil.getInstance();
		this.endpoint = p.getProperty(PropertyUtil.KB_ENDPOINT_LABEL, PropertyUtil.KB_ENDPOINT_DEFAULT);
		this.graph = p.getProperty(PropertyUtil.KB_ENDPOINT_GRAPH_LABEL, PropertyUtil.KB_ENDPOINT_GRAPH_DEFAULT);
		initPredicatesBlackList(p.getProperty(PropertyUtil.KB_PREDICATES_BLACKLIST_FILE, PropertyUtil.DEFAULT_KB_PREDICATES_BLACKLIST_FILE));
	}
	
    public String getEndpoint() {
		return endpoint;
	}



	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}



	public String getGraph() {
		return graph;
	}



	public void setGraph(String graph) {
		this.graph = graph;
	}



	public synchronized static DBpediaKB getInstance(){
    	if (instance == null){
    		instance = new DBpediaKB();
    	}
    	return instance;
    }
	
	/* (non-Javadoc)
	 * @see it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase#degree(it.cnr.iasi.leks.bedspread.rdf.AnyResource)
	 */
	@Override
	public int degree(AnyResource resource) {
		return this.degree(resource, Filters.FILTER_ALL);  
	}
	
	public int degree(AnyResource resource, Filters filter) {
		int result = 0; 
		result =  SPARQLQueryCollector_RESTRICTED.getDegree(this, resource, filter);
		return result;
	}
	
	/* (non-Javadoc)
	 * @see it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase#depth(it.cnr.iasi.leks.bedspread.rdf.AnyResource)
	 */
	@Override
	public int depth(AnyResource resource) {
		// TODO Auto-generated method stub
		return -1;
	}

	/* (non-Javadoc)
	 * @see it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase#isMemberof(it.cnr.iasi.leks.bedspread.rdf.AnyResource)
	 */
	@Override
	public boolean isMemberof(AnyResource resource) {
		return  this.isMemberof(resource, Filters.FILTER_ALL);
	}

	public boolean isMemberof(AnyResource resource, Filters filter) {
		boolean result = false;

		boolean isPredicate = SPARQLQueryCollector_RESTRICTED.isPredicate(this, resource, filter);
		boolean isSubjectOrObject = SPARQLQueryCollector_RESTRICTED.isSubjectOrObject(this, resource, filter);
		
		if(isPredicate || isSubjectOrObject )
			result = true;
		
		return result;
	}
	/* (non-Javadoc)
	 * @see it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase#getNeighborhood(it.cnr.iasi.leks.bedspread.rdf.AnyResource)
	 */
	@Override
	public Set<AnyResource> getNeighborhood(AnyResource resource) {
		return this.getNeighborhood(resource, Filters.FILTER_ALL);
	}
	
	public Set<AnyResource> getNeighborhood(AnyResource resource, Filters filter) {
		Set<AnyResource> result = new HashSet<AnyResource>();
		result = SPARQLQueryCollector_RESTRICTED.getNeighborhood(this, resource, filter); 
		return result;
	}
	
	public Set<AnyResource> getIncomingNeighborhood(AnyResource resource) {
		return this.getIncomingNeighborhood(resource, Filters.FILTER_OUT_BLACKLIST_PREDICATES);
	}
	
	public Set<AnyResource> getIncomingNeighborhood(AnyResource resource, Filters filter) {
		Set<AnyResource> result = new HashSet<AnyResource>();
		result = SPARQLQueryCollector_RESTRICTED.getIncomingNeighborhood(this, resource, filter);
		return result;
	}
	
	public Set<AnyResource> getOutgoingNeighborhood(AnyResource resource) {
		return  this.getOutgoingNeighborhood(resource, Filters.FILTER_ALL);
	}
	
	public Set<AnyResource> getOutgoingNeighborhood(AnyResource resource, Filters filter) {
		Set<AnyResource> result = new HashSet<AnyResource>();
		result = SPARQLQueryCollector_RESTRICTED.getOutgoingNeighborhood(this, resource, filter);
		return result;
	}
	
	public int countAllTriples() {
		return this.countAllTriples(Filters.FILTER_ALL);
	}
	
	public int countAllTriples(Filters filter) {
		int result = 0;
		
		
		if(caching) {
			if(this.getCache().num_total_triple!=0)
				result = this.getCache().num_total_triple;
			else {
				result = SPARQLQueryCollector_RESTRICTED.countTotalTriples(this, filter);
				this.getCache().num_total_triple = result;
			}
		}
		else result = SPARQLQueryCollector_RESTRICTED.countTotalTriples(this, filter);
		
		return result;
	}
	
	public int countTriplesByPredicate(AnyResource resource) {
		return this.countTriplesByPredicate(resource, Filters.FILTER_OUT_LITERALS);
	}
	
	public int countTriplesByPredicate(AnyResource resource, Filters filter) {
		int result = 0;
		
		if(caching) {
			if(this.getCache().num_triples_by_predicate.containsKey(resource.getResourceID()))
				result = this.getCache().num_triples_by_predicate.get(resource.getResourceID());
			else {
				result = SPARQLQueryCollector_RESTRICTED.countTriplesByPredicate(this, resource, filter);
				this.getCache().num_triples_by_predicate.put(resource.getResourceID(), result);
			}
		}
		else result = SPARQLQueryCollector_RESTRICTED.countTriplesByPredicate(this, resource, filter);
		
		return result;
	}


	public int countTriplesBySubject(AnyResource resource) {
		return this.countTriplesBySubject(resource, Filters.FILTER_ALL);
	}

	
	public int countTriplesBySubject(AnyResource resource, Filters filter) {
		int result = 0;
		
		if(caching) {
			if(this.getCache().num_triples_by_subject.containsKey(resource.getResourceID()))
				result = this.getCache().num_triples_by_subject.get(resource.getResourceID());
			else {
				result = SPARQLQueryCollector_RESTRICTED.countTriplesBySubject(this, resource, filter);
				this.getCache().num_triples_by_subject.put(resource.getResourceID(), result);
			}
		}
		else result = SPARQLQueryCollector_RESTRICTED.countTriplesBySubject(this, resource, filter);

		return result;
	}
	
	public int countTriplesByObject(AnyResource resource) {
		return this.countTriplesByObject(resource, Filters.FILTER_OUT_BLACKLIST_PREDICATES);
	}
	
	public int countTriplesByObject(AnyResource resource, Filters filter) {
		int result = 0;
		
		if(caching) {
			if(this.getCache().num_triples_by_object.containsKey(resource.getResourceID()))
				result = this.getCache().num_triples_by_object.get(resource.getResourceID());
			else {
				result = SPARQLQueryCollector_RESTRICTED.countTriplesByObject(this, resource, filter);
				this.getCache().num_triples_by_object.put(resource.getResourceID(), result);
			}
		}
		else result = SPARQLQueryCollector_RESTRICTED.countTriplesByObject(this, resource, filter);
		
		return result;
	}
	
	public int countTriplesBySubjectOrObject(AnyResource resource) {
		return this.countTriplesBySubjectOrObject(resource, Filters.FILTER_ALL);
	}
	
	public int countTriplesBySubjectOrObject(AnyResource resource, Filters filter) {
		int result = 0;
		
		if(caching) {
			if(this.getCache().num_triples_by_subject_or_object.containsKey(resource.getResourceID()))
				result = this.getCache().num_triples_by_subject_or_object.get(resource.getResourceID());
			else {
				result = SPARQLQueryCollector_RESTRICTED.countTriplesBySubjectOrObject(this, resource, filter);
				this.getCache().num_triples_by_subject_or_object.put(resource.getResourceID(), result);
			}
		}
		else
			result = SPARQLQueryCollector_RESTRICTED.countTriplesBySubjectOrObject(this, resource, filter);
		
		return result;
	}
	
	
	public int countTriplesByPredicateAndObject(AnyResource predicate, AnyResource resource) {
		int result = 0;
		Vector<String> pair = new Vector<String>();
		pair.add(predicate.getResourceID());
		pair.add(resource.getResourceID());
		
		if(caching) {
			if(this.getCache().num_triples_by_predicate_and_object.containsKey(pair))
				result = this.getCache().num_triples_by_predicate_and_object.get(pair);
			else {
				result = SPARQLQueryCollector_RESTRICTED.countTriplesByPredicateAndObject(this, predicate, resource);
				this.getCache().num_triples_by_predicate_and_object.put(pair, result);
			}
		}
		else result = SPARQLQueryCollector_RESTRICTED.countTriplesByPredicateAndObject(this, predicate, resource);
		
		return result;
	}

	public int countTriplesByPredicateAndSubject(AnyResource predicate, AnyResource resource) {
		return this.countTriplesByPredicateAndSubject(predicate, resource, Filters.FILTER_OUT_LITERALS);
	}

	
	public int countTriplesByPredicateAndSubject(AnyResource predicate, AnyResource resource, Filters filter) {
		int result = 0;
		Vector<String> pair = new Vector<String>();
		pair.add(predicate.getResourceID());
		pair.add(resource.getResourceID());
		
		if(caching) {
			if(this.getCache().num_triples_by_predicate_and_subject.containsKey(pair))
				result = this.getCache().num_triples_by_predicate_and_subject.get(pair);
			else {
				result = SPARQLQueryCollector_RESTRICTED.countTriplesByPredicateAndSubject(this, predicate, resource, filter);
				this.getCache().num_triples_by_predicate_and_subject.put(pair, result);
			}
		}
		else result = SPARQLQueryCollector_RESTRICTED.countTriplesByPredicateAndSubject(this, predicate, resource, filter);
		
		return result;
	}
	
	public int countTriplesByPredicateAndSubjectOrObject(AnyResource predicate, AnyResource resource) {
		return this.countTriplesByPredicateAndSubjectOrObject(predicate, resource, Filters.FILTER_OUT_LITERALS);
	}
	
	public int countTriplesByPredicateAndSubjectOrObject(AnyResource predicate, AnyResource resource, Filters filter) {
		int result = 0;
		Vector<String> pair = new Vector<String>();
		pair.add(predicate.getResourceID());
		pair.add(resource.getResourceID());
		
		if(caching) {
			if(this.getCache().num_triples_by_predicate_and_subject_or_object.containsKey(pair))
				result = this.getCache().num_triples_by_predicate_and_subject_or_object.get(pair);
			else {
				result = SPARQLQueryCollector_RESTRICTED.countTriplesByPredicateAndSubjectOrObject(this, predicate, resource, filter);
				this.getCache().num_triples_by_predicate_and_subject_or_object.put(pair, result);
			}
		}
		else result = SPARQLQueryCollector_RESTRICTED.countTriplesByPredicateAndSubjectOrObject(this, predicate, resource, filter);
		return result;
		
	}
	
	public Set<AnyResource> getPredicatesBySubjectAndObject(AnyResource r1, AnyResource r2) {
		return this.getPredicatesBySubjectAndObject(r1, r2, Filters.FILTER_OUT_BLACKLIST_PREDICATES);
	}
	
	public Set<AnyResource> getPredicatesBySubjectAndObject(AnyResource r1, AnyResource r2, Filters filter) {
		Set<AnyResource> result = new HashSet<AnyResource>();
		result = SPARQLQueryCollector_RESTRICTED.getPredicatesBySubjectAndObject(this, r1, r2, filter);
		return result;
	}
/*
	public Set<AnyResource> getAllPredicates() {
		return this.getAllPredicates(Filters.FILTER_ALL);
	}
	
	public Set<AnyResource> getAllPredicates(Filters filter) {
		Set<AnyResource> result = new HashSet<AnyResource>();
		result = SPARQLQueryCollector_RESTRICTED.getAllPredicates(this, filter);
		return result;
	}
*/	
	public Set<AnyResource> getAllPredicates() {
		return this.getAllPredicates(Filters.FILTER_ALL);
	}
	
	public Set<AnyResource> getAllPredicates(Filters filter) {
		Set<AnyResource> result = new HashSet<AnyResource>();
		result = SPARQLQueryCollector_RESTRICTED.getAllPredicates(this, filter);
		return result;
	}
	
	
	private void initPredicatesBlackList(String predicatesBlackList_filename) {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream s = classloader.getResourceAsStream(predicatesBlackList_filename);
		InputStreamReader isr = new InputStreamReader(s); 
		
		CSVReader reader = new CSVReader(isr);

		String [] nextLine;
	    try {
			while ((nextLine = reader.readNext()) != null) {
				// nextLine[] is an array of values from the line
				if(!(nextLine[0].trim().startsWith("#")))
					this.getPredicatesBlackList().add(new URIImpl(nextLine[0]));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	}
		
	public Set<AnyURI> getPredicatesBlackList() {
		return predicatesBlackList;
	}

	public DBpediaKBCache getCache() {
		return cache;
	}

	public Set<AnyResource> getValidPredicates() {
		Set<AnyResource> result = new HashSet<AnyResource>();
		if(this.validPredicates.size()>0)
			result = validPredicates;
		else 
			result = this.getAllPredicates();
		return result;
	}

	public void setValidPredicates(Set<AnyResource> validPredicates) {
		this.validPredicates = validPredicates;
	}
	
	private Vector<AnyResource> filterValidArcs(Vector<AnyResource> arcs) {
		Vector<AnyResource> result = new Vector<AnyResource>();
		for(AnyResource arc:arcs) {
			if(this.getValidPredicates().contains(arc))
				result.add(arc);
		}
		return result;
	}
	
}
