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
package it.cnr.iasi.leks.bedspread.rdf.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.RDFNode;

import it.cnr.iasi.leks.bedspread.rdf.AnyResource;

/**
 * 
 * @author ftaglino
 *
 */
public class SPARQLQueryCollector {

	public static Vector<AnyResource> getIncomingPredicates(DBpediaKB kb, AnyResource resource) {
		Vector<AnyResource> result = new Vector<AnyResource>(); 
		
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
		
		// Search for incoming predicates
		String queryString = "SELECT ?p FROM <"+kb.getGraph()+"> WHERE {"
				+ "?s ?p <"+resource.getResourceID()+">"
				+ "}";
		
		Vector<QuerySolution> query_results = sec.execQuery(queryString);
		for(QuerySolution qs:query_results)
			result.add(new URIImpl(qs.getResource("p").getURI().toString()));
		
		return result;
	}

	
	public static Vector<AnyResource> getOutgoingPredicates(DBpediaKB kb, AnyResource resource) {
		Vector<AnyResource> result = new Vector<AnyResource>(); 
		
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
		
		// Search for outgoing predicates
		String queryString = "SELECT ?p FROM <"+kb.getGraph()+"> WHERE {"
				+ "<"+resource.getResourceID()+"> ?p ?o"
				+ "}";
		
		Vector<QuerySolution> query_results = sec.execQuery(queryString);
		for(QuerySolution qs:query_results)
			result.add(new URIImpl(qs.getResource("p").getURI().toString()));
		
		return result;
	}
	
	public static boolean isPredicate(DBpediaKB kb, AnyResource resource) {
		boolean result = false;

		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
		Vector<QuerySolution> query_results;
		if(resource instanceof URIImpl) {
			String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE {"
					+ "?s <"+resource.getResourceID()+"> ?o"
					+ "}";
			query_results = sec.execQuery(queryString);
			if(query_results.size()>0)
				if(query_results.elementAt(0).getLiteral("count").asLiteral().getInt()>0)
					result = true;
		}
		
		return result;
	}
	
	/**
	 * Return all the resources that play the role of SUBJECT in the triples having the passed resource as the OBJECT
	 * If more than a triple having the same SUBJECT and OBJECT exists, this is not recorded (the returning type is a Set and not a Vector. 
	 * @param resource
	 * @return 
	 */
	public static boolean isNode(DBpediaKB kb, AnyResource resource) {
		boolean result = false;

		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
		Vector<QuerySolution> query_results;
		if(resource instanceof URIImpl) {
			String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE {"
					+ "{?s1 ?p1 <"+resource.getResourceID()+">} "
					+ "UNION "
					+ "{<"+resource.getResourceID()+"> ?p2 ?o2}"
					+ "}";
			query_results = sec.execQuery(queryString);
			if(query_results.size()>0)
				if(query_results.elementAt(0).getLiteral("count").asLiteral().getInt()>0)
					result = true;
		}
		
		return result;
	}
	
	/**
	 * Return all the resources that play the role of OBJECT in the triples having the passed resource as the SUBJECT
	 * If more than a triple having the same SUBJECT and OBJECT exists, this is not recorded (the returning type is a Set and not a Vector. 
	 * @param resource
	 * @return 
	 */
	public static Set<AnyResource> getIncomingNeighborhood(DBpediaKB kb, AnyResource resource) {
		Set<AnyResource> result = new HashSet<AnyResource>();
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
		
		String queryString = "SELECT DISTINCT ?s FROM <"+kb.getGraph()+"> WHERE {"
				+ "?s ?p <"+resource.getResourceID()+"> "
				+ "}";
		
		Vector<QuerySolution> query_results = sec.execQuery(queryString);
		
		for(int i=0; i<query_results.size(); i++) {
			String r = query_results.elementAt(i).getResource("s").getURI().toString();
			if(!(r.equals(resource.getResourceID()))) {
				String neighboor = query_results.elementAt(i).getResource("s").getURI().toString();  
				result.add(new URIImpl(neighboor)); 
			}
 		}		
		return result;
	}
	
	
	public static Set<AnyResource> getOutgoingNeighborhood(DBpediaKB kb, AnyResource resource) {
		Set<AnyResource> result = new HashSet<AnyResource>();
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
		
		String queryString = "SELECT ?o FROM <"+kb.getGraph()+"> WHERE {"
				+ "<"+resource.getResourceID()+"> ?p ?o "
				+ "}";
		
		Vector<QuerySolution> query_results = sec.execQuery(queryString);
		
		for(int i=0; i<query_results.size(); i++) {
			String r = query_results.elementAt(i).getResource("o").getURI().toString();
			if(!(r.equals(resource.getResourceID()))) {
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
 		}		
		return result;
	}
	
	public static int countTotalTriples(DBpediaKB kb) {
		int result = 0;
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
		
		Vector<QuerySolution> query_results;
			String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE {"
					+ "?s ?p ?o"
					+ "}";
			query_results = sec.execQuery(queryString);
			if(query_results.size()>0)
				result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
		
		return result;
		
	}
	
	public static int countTriplesByPredicate(DBpediaKB kb, AnyResource resource) {
		int result = 0;
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
		
		Vector<QuerySolution> query_results;
		if(resource instanceof URIImpl) {
			String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE {"
					+ "?s <"+resource.getResourceID()+"> ?o"
					+ "}";
			query_results = sec.execQuery(queryString);
			if(query_results.size()>0)
				result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
		}		
		
		return result;
	}
		
	public static int countTriplesBySubject(DBpediaKB kb, AnyResource resource) {
		int result = 0;
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
		
		Vector<QuerySolution> query_results;
		if(resource instanceof URIImpl) {
			String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE {"
					+ "<"+resource.getResourceID()+"> ?p ?o"
					+ "}";
			query_results = sec.execQuery(queryString);
			if(query_results.size()>0)
				result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
		}		
		
		return result;
	}
	
	
	public static int countTriplesByObject(DBpediaKB kb, AnyResource resource) {
		int result = 0;
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
		
		Vector<QuerySolution> query_results;
		if(resource instanceof URIImpl) {
			String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE {"
					+ "?s ?p <"+resource.getResourceID()+">"
					+ "}";
			query_results = sec.execQuery(queryString);
			if(query_results.size()>0)
				result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
		}		
		
		return result;
	}

	//TO BE TESTED
	public static int countTriplesByNode(DBpediaKB kb, AnyResource resource) {
		int result = 0;
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
		
		Vector<QuerySolution> query_results;
		if(resource instanceof URIImpl) {
			String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE {"
					+ "{<"+resource.getResourceID()+"> ?p ?o} "
					+ "UNION "
					+ "{?s ?p <"+resource.getResourceID()+">} "
					+ "}";
			query_results = sec.execQuery(queryString);
			if(query_results.size()>0)
				result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
		}		
		else {}
		
		return result;
	}
	
	public static int countTriplesByPredicateAndObject(DBpediaKB kb, AnyResource pred, AnyResource node) {
		int result = 0;

		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());

		Vector<QuerySolution> query_results;
		if(pred instanceof URIImpl) {
			if(node instanceof URIImpl) {
				String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE { "
						+ "?s <"+pred.getResourceID()+"> <"+node.getResourceID()+"> "
						+ "}";
				query_results = sec.execQuery(queryString);
				if(query_results.size()>0)
					result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
			}
			else {}
		}
		
		return result;
	}
	
	public static int countTriplesByPredicateAndSubject(DBpediaKB kb, AnyResource pred, AnyResource node) {
		int result = 0;

		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());

		Vector<QuerySolution> query_results;
		if((pred instanceof URIImpl) && (node instanceof URIImpl)) {
			String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE { "
					+ "?s <"+node.getResourceID()+"> <"+pred.getResourceID()+"> "
					+ "}";
			query_results = sec.execQuery(queryString);
			if(query_results.size()>0)
				result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
		}
		
		return result;
	}
	
	public static int countTriplesByPredicateAndNode(DBpediaKB kb, AnyResource pred, AnyResource node) {
		int result = 0;
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
		
		Vector<QuerySolution> query_results;
		if(pred instanceof URIImpl) {
			if(node instanceof URIImpl) {
				String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE {"
						+ "{<"+node.getResourceID()+"> <"+pred.getResourceID()+"> ?o} "
						+ "UNION "
						+ "{?s <"+pred.getResourceID()+ "> <"+node.getResourceID()+">} "
						+ "}";
				query_results = sec.execQuery(queryString);
				if(query_results.size()>0)
					result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
			}
			else {}
		}		
		
		return result;
	}
	
	public static Set<AnyResource> getPredicatesBySubjectAndObject(DBpediaKB kb, AnyResource s, AnyResource o) {
		Set<AnyResource> result = new HashSet<AnyResource>();

		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());

		Vector<QuerySolution> query_results;
		String queryString = "SELECT ?p FROM <"+kb.getGraph()+"> WHERE { "
				+ "<"+s.getResourceID()+"> ?p <"+o.getResourceID()+"> "
				+ "}";
		
		query_results = sec.execQuery(queryString);
		
		for(QuerySolution qs:query_results)
			result.add(new URIImpl(qs.getResource("p").getURI().toString()));
		
		return result;
	}
	
	public static Set<AnyResource> getBow(DBpediaKB kb) {
		Set<AnyResource> result = new HashSet<AnyResource>();

		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());

		Vector<QuerySolution> query_results;
		String queryString = "SELECT ?x FROM <"+kb.getGraph()+"> WHERE { "
				+ "?x ?p ?x"
				+ "}";
		query_results = sec.execQuery(queryString);
		
		for(QuerySolution qs:query_results)
			result.add(new URIImpl(qs.getResource("x").getURI().toString()));
		
		return result;
	}
}
