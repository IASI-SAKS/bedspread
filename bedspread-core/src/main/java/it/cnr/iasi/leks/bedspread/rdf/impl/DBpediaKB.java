package it.cnr.iasi.leks.bedspread.rdf.impl;
/**
 * 
 */


import java.util.Set;
import java.util.Vector;

import org.apache.jena.query.QuerySolution;

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
		// TODO Auto-generated method stub
		return 0;
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
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase#getNeighborhood(it.cnr.iasi.leks.bedspread.rdf.AnyResource)
	 */
	@Override
	public Set<AnyResource> getNeighborhood(AnyResource resource) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Return all the resources that play the role of SUBJECT in the triples having the passed resource as the OBJECT
	 * @param resource
	 * @return THINK ABOUT A VECTOR OR A SET
	 */
	public Vector<AnyResource> getIncomingNeighborhood(AnyResource resource) {
		Vector<AnyResource> result = new Vector<AnyResource>();
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(DBPEDIA_ENDPOINT);
		
		String queryString = "SELECT ?s FROM <"+DBPEDIA_GRAPH+"> WHERE {"
				+ "?s ?p <"+resource.getResourceID()+">"
				+ "}";
		
		Vector<QuerySolution> qss = sec.execQuery(queryString);
		
		for(int i=0; i<qss.size(); i++) {
			String neighboor = qss.elementAt(i).getResource("s").getURI().toString();  
			result.add(new URIImpl(neighboor)); 
 		}		
		return result;
	}

}
