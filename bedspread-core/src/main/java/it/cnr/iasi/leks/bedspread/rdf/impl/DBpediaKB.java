package it.cnr.iasi.leks.bedspread.rdf.impl;
/**
 * 
 */

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.RDFNode;

import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;

/**
 * 
 * @author ftaglino
 *
 */
public class DBpediaKB implements KnowledgeBase {

	String DBPEDIA_ENDPOINT = "http://dbpedia.org/sparql";
	String DBPEDIA_GRAPH = "http://dbpedia.org";
	
	/**
	 * 
	 */
	public DBpediaKB() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase#degree(it.cnr.iasi.leks.bedspread.rdf.AnyResource)
	 */
	@Override
	public int degree(AnyResource resource) {
		int result = 0; 
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(DBPEDIA_ENDPOINT);
		
		// Search for incoming predicates
		String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+DBPEDIA_GRAPH+"> WHERE {"
				+ "{?s ?p <"+resource.getResourceID()+">} "
				+ "UNION "
				+ "{<"+resource.getResourceID()+"> ?p ?o}"
				+ "}";
		
		Vector<QuerySolution> query_results = sec.execQuery(queryString);
		if(query_results.size()>0)
			result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
		
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
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(DBPEDIA_ENDPOINT);
		Vector<QuerySolution> query_results;
		if(resource instanceof URIImpl) {
			String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+DBPEDIA_GRAPH+"> WHERE {"
					+ "{?s1 ?p1 <"+resource.getResourceID()+">} "
					+ "UNION "
					+ "{<"+resource.getResourceID()+"> ?p2 ?o2}"
					+ "UNION "
					+ "{ ?s3 <"+resource.getResourceID()+"> ?o3}"
					+ "}";
			query_results = sec.execQuery(queryString);
			if(query_results.size()>0)
				if(query_results.elementAt(0).getLiteral("count").asLiteral().getInt()>0)
					result = true;
		}
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
	
	/**
	 * Return all the resources that play the role of SUBJECT in the triples having the passed resource as the OBJECT
	 * If more than a triple having the same SUBJECT and OBJECT exists, this is not recorded (the returning type is a Set and not a Vector. 
	 * @param resource
	 * @return 
	 */
	public Set<AnyResource> getIncomingNeighborhood(AnyResource resource) {
		Set<AnyResource> result = new HashSet<AnyResource>();
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(DBPEDIA_ENDPOINT);
		
		String queryString = "SELECT ?s FROM <"+DBPEDIA_GRAPH+"> WHERE {"
				+ "?s ?p <"+resource.getResourceID()+">"
				+ "}";
		
		Vector<QuerySolution> query_results = sec.execQuery(queryString);
		
		for(int i=0; i<query_results.size(); i++) {
			String neighboor = query_results.elementAt(i).getResource("s").getURI().toString();  
			result.add(new URIImpl(neighboor)); 
 		}		
		return result;
	}

	/**
	 * Return all the resources that play the role of OBJECT in the triples having the passed resource as the SUBJECT
	 * If more than a triple having the same SUBJECT and OBJECT exists, this is not recorded (the returning type is a Set and not a Vector. 
	 * @param resource
	 * @return 
	 */
	public Set<AnyResource> getOutgoingNeighborhood(AnyResource resource) {
		Set<AnyResource> result = new HashSet<AnyResource>();
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(DBPEDIA_ENDPOINT);
		
		String queryString = "SELECT ?o FROM <"+DBPEDIA_GRAPH+"> WHERE {"
				+ "<"+resource.getResourceID()+"> ?p ?o "
				+ "}";
		
		Vector<QuerySolution> query_results = sec.execQuery(queryString);
		
		for(int i=0; i<query_results.size(); i++) {
			RDFNode node = query_results.elementAt(i).get("o");
			if(node.isResource()) {
				String neighboor = node.asResource().getURI().toString();  
				result.add(new URIImpl(neighboor));
			}
			if(node.isLiteral()) {
				String neighboor = node.asLiteral().toString();
				result.add(new LiteralImpl(neighboor));
			}
 		}		
		return result;
	}
	
	
}
