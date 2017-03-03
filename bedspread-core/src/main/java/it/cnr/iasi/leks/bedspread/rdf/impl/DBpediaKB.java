package it.cnr.iasi.leks.bedspread.rdf.impl;
/**
 * 
 */

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;

/**
 * 
 * @author ftaglino
 *
 */
public class DBpediaKB implements KnowledgeBase {

	private String endpoint;
	private String graph;
	
	DBpediaKBCache cache = new DBpediaKBCache();
	
	private static DBpediaKB instance = null;
	 
	/**
	 * 
	 */
	private DBpediaKB() {
		PropertyUtil p = PropertyUtil.getInstance();
		this.endpoint = p.getProperty(PropertyUtil.KB_ENDPOINT_LABEL, PropertyUtil.KB_ENDPOINT_DEFAULT);
		this.graph = p.getProperty(PropertyUtil.KB_ENDPOINT_GRAPH_LABEL, PropertyUtil.KB_ENDPOINT_GRAPH_DEFAULT);
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
		int result = 0; 	
		Vector<AnyResource> incoming_predicates = SPARQLQueryCollector.getIncomingPredicates(this, resource);
		Vector<AnyResource> outgoing_predicates = SPARQLQueryCollector.getOutgoingPredicates(this, resource);

		result = incoming_predicates.size() + outgoing_predicates.size(); 
		
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
		boolean result = false;

		boolean isPredicate = SPARQLQueryCollector.isPredicate(this, resource);
		boolean isNode = SPARQLQueryCollector.isNode(this, resource);
		
		if(isPredicate || isNode )
			result = true;
		
		return result;
	}

	/* (non-Javadoc)
	 * @see it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase#getNeighborhood(it.cnr.iasi.leks.bedspread.rdf.AnyResource)
	 */
	@Override
	public Set<AnyResource> getNeighborhood(AnyResource resource) {
		Set<AnyResource> result = getIncomingNeighborhood(resource);
		result.addAll(getOutgoingNeighborhood(resource));
		return result;
	}
	
	public Set<AnyResource> getIncomingNeighborhood(AnyResource resource) {
		Set<AnyResource> result = new HashSet<AnyResource>();
		result = SPARQLQueryCollector.getIncomingNeighborhood(this, resource);
		return result;
	}


	public Set<AnyResource> getOutgoingNeighborhood(AnyResource resource) {
		Set<AnyResource> result = new HashSet<AnyResource>();
		result = SPARQLQueryCollector.getOutgoingNeighborhood(this, resource);
		return result;
	}
	
	
	public int countAllTriples() {
		int result = 0;
		
		if(this.cache.num_total_triple!=0)
			result = this.cache.num_total_triple;
		else {
			result = SPARQLQueryCollector.countTotalTriples(this);
			this.cache.num_total_triple = result;
		}
		
		return result;
	}
	
	
	public int countTriplesByPredicate(AnyResource resource) {
		int result = 0;
		
		if(this.cache.num_triples_by_predicate.containsKey(resource.getResourceID()))
			result = this.cache.num_triples_by_predicate.get(resource.getResourceID());
		else {
			result = SPARQLQueryCollector.countTriplesByPredicate(this, resource);
			this.cache.num_triples_by_predicate.put(resource.getResourceID(), result);
		}
			
		return result;
	}

	public int countTriplesBySubject(AnyResource resource) {
		int result = 0;
		
		if(this.cache.num_triples_by_subject.containsKey(resource.getResourceID()))
			result = this.cache.num_triples_by_subject.get(resource.getResourceID());
		else {
			result = SPARQLQueryCollector.countTriplesBySubject(this, resource);
			this.cache.num_triples_by_subject.put(resource.getResourceID(), result);
		}

		return result;
	}
	
	public int countTriplesByObject(AnyResource resource) {
		int result = 0;
		
		if(this.cache.num_triples_by_object.containsKey(resource.getResourceID()))
			result = this.cache.num_triples_by_object.get(resource.getResourceID());
		else {
			result = SPARQLQueryCollector.countTriplesByObject(this, resource);
			this.cache.num_triples_by_object.put(resource.getResourceID(), result);
		}
		
		return result;
	}
	
	public int countTriplesByNode(AnyResource resource) {
		int result = 0;
		
		if(this.cache.num_triples_by_node.containsKey(resource.getResourceID()))
			result = this.cache.num_triples_by_node.get(resource.getResourceID());
		else {
			result = SPARQLQueryCollector.countTriplesByNode(this, resource);
			this.cache.num_triples_by_node.put(resource.getResourceID(), result);
		}
		
		return result;
	}
	
	public int countTriplesByPredicateAndObject(DBpediaKB kb, AnyResource pred, AnyResource node) {
		int result = 0;
		Vector<String> pair = new Vector<String>();
		pair.add(pred.getResourceID());
		pair.add(node.getResourceID());
		if(this.cache.num_triples_by_predicate_and_object.containsKey(pair))
			result = this.cache.num_triples_by_predicate_and_object.get(pair);
		else {
			result = SPARQLQueryCollector.countTriplesByPredicateAndObject(this, pred, node);
			this.cache.num_triples_by_predicate_and_object.put(pair, result);
		}
		
		return result;
	}
	
	public int countTriplesByPredicateAndSubject(DBpediaKB kb, AnyResource pred, AnyResource node) {
		int result = 0;
		Vector<String> pair = new Vector<String>();
		pair.add(pred.getResourceID());
		pair.add(node.getResourceID());
		if(this.cache.num_triples_by_predicate_and_subject.containsKey(pair))
			result = this.cache.num_triples_by_predicate_and_subject.get(pair);
		else {
			result = SPARQLQueryCollector.countTriplesByPredicateAndSubject(this, pred, node);
			this.cache.num_triples_by_predicate_and_subject.put(pair, result);
		}
		
		return result;
	}
	
	public int countTriplesByPredicateAndNode(DBpediaKB kb, AnyResource pred, AnyResource node) {
		int result = 0;
		Vector<String> pair = new Vector<String>();
		pair.add(pred.getResourceID());
		pair.add(node.getResourceID());
		if(this.cache.num_triples_by_predicate_and_node.containsKey(pair))
			result = this.cache.num_triples_by_predicate_and_node.get(pair);
		else {
			result = SPARQLQueryCollector.countTriplesByPredicateAndNode(this, pred, node);
			this.cache.num_triples_by_predicate_and_node.put(pair, result);
		}
		
		return result;
		
	}
}
