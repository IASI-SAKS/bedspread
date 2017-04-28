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

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.RDFNode;

import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.AnyURI;
import it.cnr.iasi.leks.bedspread.rdf.URI;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFFactory;
import it.cnr.iasi.leks.bedspread.rdf.impl.URIImpl;

/**
 * 
 * @author ftaglino
 *
 */
public class SPARQLQueryCollector {

	public static int getDegree(DBpediaKB kb, AnyResource resource, Filters filter) {
		int result = 0;
		
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());

		String filterOutLiterals = generateFilterOut_Literals(filter, "o");
		String filterOutPredicates = generateFilterOut_Predicates(filter, "p", kb.getPredicatesBlackList());
		
		String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE "
				+ "{"
				+ 	"{"
				+ 		"<"+resource.getResourceID()+"> ?p ?o "+filterOutLiterals+filterOutPredicates
				+	"} "
				+ 	"UNION "
				+ 	"{"
				+ 		"?s ?p "+adjustObject(resource)+filterOutPredicates 
				+ 	"}"
				+ "}";
				
		Vector<QuerySolution> query_results = sec.execQuery(queryString);
		if(query_results.size()>0)
			result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();

		return result;
	} 
	
	
	public static Set<AnyResource> getIncomingPredicates(DBpediaKB kb, AnyResource resource, Filters filter) {
		Set<AnyResource> result = new HashSet<AnyResource>(); 
		
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
		
		String filterOutPredicates = generateFilterOut_Predicates(filter, "p", kb.getPredicatesBlackList());
		
		// Search for incoming predicates
		String queryString = "SELECT ?p FROM <"+kb.getGraph()+"> WHERE "
						+ "{"
						+ 	"?s ?p "+adjustObject(resource)+filterOutPredicates
						+ "}";
		
		Vector<QuerySolution> query_results = sec.execQuery(queryString);
		for(QuerySolution qs:query_results)
			result.add(RDFFactory.getInstance().createURI(qs.getResource("p").getURI().toString()));
		return result;
	}

	
	public static Set<AnyResource> getOutgoingPredicates(DBpediaKB kb, AnyResource resource, Filters filter) {
		Set<AnyResource> result = new HashSet<AnyResource>(); 
		
		if(resource instanceof URIImpl) {
			SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
			
			String filterOutLiterals = generateFilterOut_Literals(filter, "o");
			String filterOutPredicates = generateFilterOut_Predicates(filter, "p", kb.getPredicatesBlackList());
			
			// Search for outgoing predicates
			String queryString = "SELECT DISTINCT ?p FROM <"+kb.getGraph()+"> WHERE "
					+ "{"
					+ 	"<"+resource.getResourceID()+"> ?p ?o "+filterOutLiterals+filterOutPredicates 
					+ "}";
			
			Vector<QuerySolution> query_results = sec.execQuery(queryString);
			for(QuerySolution qs:query_results)
				result.add(RDFFactory.getInstance().createURI(qs.getResource("p").getURI().toString()));
		}
		return result;
	}
	
	public static boolean isPredicate(DBpediaKB kb, AnyResource resource, Filters filter) {
		boolean result = false;

		if(resource instanceof URIImpl) {
			SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
			
			String filterOutLiterals = generateFilterOut_Literals(filter, "o");
			
			Vector<QuerySolution> query_results;
			String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE "
					+ "{"
					+ 	"?s <"+resource.getResourceID()+"> ?o "+filterOutLiterals
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
	public static boolean isSubjectOrObject(DBpediaKB kb, AnyResource resource, Filters filter) {
		boolean result = false;

		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());

		String filterOutLiterals = generateFilterOut_Literals(filter, "o");
		String filterOutPredicates = generateFilterOut_Predicates(filter, "p", kb.getPredicatesBlackList());

		Vector<QuerySolution> query_results;
		String queryString = "";
		if(resource instanceof URIImpl) { 
			queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE "
				+ "{"
				+ 	"<"+resource.getResourceID()+"> ?p ?o "+filterOutLiterals+filterOutPredicates
				+ "}";
			query_results = sec.execQuery(queryString);
			if(query_results.size()>0)
				if(query_results.elementAt(0).getLiteral("count").asLiteral().getInt()>0)
					result = true;
		}
		if(result == false)
			queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE "
					+ "{"
					+ 	"?s ?p "+adjustObject(resource)+filterOutPredicates
					+ "}";
			query_results = sec.execQuery(queryString);
			if(query_results.size()>0)
				if(query_results.elementAt(0).getLiteral("count").asLiteral().getInt()>0)
					result = true;
		
		return result;
	}
	
	/**
	 * Return all the resources that play the role of OBJECT in the triples having the passed resource as the SUBJECT
	 * If more than a triple having the same SUBJECT and OBJECT exists, this is not recorded (the returning type is a Set and not a Vector. 
	 * @param resource
	 * @return 
	 */
	public static Set<AnyResource> getNeighborhood(DBpediaKB kb, AnyResource resource, Filters filter) {
		Set<AnyResource> result = new HashSet<AnyResource>();
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
		
		String queryString = "";
		String filterOutLiterals = generateFilterOut_Literals(filter, "x");
		String filterOutPredicates = generateFilterOut_Predicates(filter, "p", kb.getPredicatesBlackList());
		
		if(resource instanceof URIImpl) 
			queryString = "SELECT DISTINCT ?x FROM <"+kb.getGraph()+"> WHERE "
				+ "{"
				+ 	"{"
				+ 		"?x ?p "+adjustObject(resource)+filterOutPredicates
				+	"} " 
				+ 	"UNION "
				+ 	"{"
				+ 		"<"+resource.getResourceID()+"> ?p ?x "+filterOutLiterals+filterOutPredicates
				+ 	"} "
				+ "}";
		else
			queryString = "SELECT DISTINCT ?x FROM <"+kb.getGraph()+"> WHERE "
					+ "{"
					+ 	"?x ?p "+adjustObject(resource)+filterOutPredicates
					+"}";
		
		Vector<QuerySolution> query_results = sec.execQuery(queryString);
		
		for(int i=0; i<query_results.size(); i++) {
			RDFNode node = query_results.elementAt(i).get("x");
			if(node.isResource() && !(query_results.elementAt(i).getResource("x").getURI().toString().equals(resource.getResourceID()))) {
				String neighboor = node.asResource().getURI().toString();  
				result.add(RDFFactory.getInstance().createURI(neighboor));
			}
			if(node.isLiteral()) {
				String neighboor = node.asLiteral().toString();
				result.add(RDFFactory.getInstance().createLiteral(neighboor));
			}
 		}				
		return result;
	}
	
	/**
	 * Return all the resources that play the role of OBJECT in the triples having the passed resource as the SUBJECT
	 * If more than a triple having the same SUBJECT and OBJECT exists, this is not recorded (the returning type is a Set and not a Vector. 
	 * @param resource
	 * @return 
	 */
	public static Set<AnyResource> getIncomingNeighborhood(DBpediaKB kb, AnyResource resource, Filters filter) {
		Set<AnyResource> result = new HashSet<AnyResource>();
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());

		String filterOutPredicates = generateFilterOut_Predicates(filter, "p", kb.getPredicatesBlackList());
		
		String queryString = "SELECT DISTINCT ?s FROM <"+kb.getGraph()+"> WHERE "
				+ "{"
				+ 	"?s ?p "+adjustObject(resource)+filterOutPredicates
				+ "}";
		
		Vector<QuerySolution> query_results = sec.execQuery(queryString);
		
		for(int i=0; i<query_results.size(); i++) {
			String neighboor = query_results.elementAt(i).getResource("s").getURI().toString();
			if(!(neighboor.equals(resource.getResourceID())))  
				result.add(RDFFactory.getInstance().createURI(neighboor));
 		}		
		return result;
	}
	
	
	public static Set<AnyResource> getOutgoingNeighborhood(DBpediaKB kb, AnyResource resource, Filters filter) {
		Set<AnyResource> result = new HashSet<AnyResource>();
		
		if(resource instanceof URIImpl) {
			SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
			
			String filterOutLiterals = generateFilterOut_Literals(filter, "o");
			String filterOutPredicates = generateFilterOut_Predicates(filter, "p", kb.getPredicatesBlackList());
			
			String queryString = "SELECT DISTINCT ?o FROM <"+kb.getGraph()+"> WHERE "
					+ "{"
					+ 	"<"+resource.getResourceID()+"> ?p ?o "+filterOutLiterals+filterOutPredicates
					+ "}";
			
			Vector<QuerySolution> query_results = sec.execQuery(queryString);
			
			for(int i=0; i<query_results.size(); i++) {
				RDFNode node = query_results.elementAt(i).get("o");
				if(node.isResource() && !(query_results.elementAt(i).getResource("o").getURI().toString().equals(resource.getResourceID()))) {
					String neighboor = node.asResource().getURI().toString();  
					result.add(RDFFactory.getInstance().createURI(neighboor));
				}
				if(node.isLiteral()) {
					String neighboor = node.asLiteral().toString();
					result.add(RDFFactory.getInstance().createLiteral(neighboor));
				}
	 		}		
		}
		return result;
	}
	
	public static int countTotalTriples(DBpediaKB kb, Filters filter) {
		int result = 0;
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
		
		String filterOutLiterals = generateFilterOut_Literals(filter, "o");
		String filterOutPredicates = generateFilterOut_Predicates(filter, "p", kb.getPredicatesBlackList());

		Vector<QuerySolution> query_results;
		String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE "
				+ "{"
				+ 	"?s ?p ?o "+filterOutLiterals+filterOutPredicates 
				+ "}";
		query_results = sec.execQuery(queryString);
		if(query_results.size()>0)
			result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
		
		return result;
		
	}
	
	public static int countTriplesByPredicate(DBpediaKB kb, AnyResource resource, Filters filter) {
		int result = 0;
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
		
		String filterOutLiterals = generateFilterOut_Literals(filter, "o");

		Vector<QuerySolution> query_results;
		if(resource instanceof URIImpl) {
			String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE "
					+ "{"
					+ 	"?s <"+resource.getResourceID()+"> ?o "+filterOutLiterals
					+ "}";
			query_results = sec.execQuery(queryString);
			if(query_results.size()>0)
				result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
		}		
		
		return result;
	}
		
	public static int countTriplesBySubject(DBpediaKB kb, AnyResource resource, Filters filter) {
		int result = 0;
		
		if(resource instanceof URIImpl) {
			SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
	
			String filterOutLiterals = generateFilterOut_Literals(filter, "o");
			String filterOutPredicates = generateFilterOut_Predicates(filter, "p", kb.getPredicatesBlackList());

			Vector<QuerySolution> query_results;
			if(resource instanceof URIImpl) {
				String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE "
						+ "{"
						+ 	"<"+resource.getResourceID()+"> ?p ?o "+filterOutLiterals+filterOutPredicates
						+ "}";
				query_results = sec.execQuery(queryString);
				if(query_results.size()>0)
					result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
			}		
		}
		
		return result;
	}
	
	
	public static int countTriplesByObject(DBpediaKB kb, AnyResource resource, Filters filter) {
		int result = 0;
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());

		String filterOutPredicates = generateFilterOut_Predicates(filter, "p", kb.getPredicatesBlackList());
		
		Vector<QuerySolution> query_results;
		String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE "
				+ "{"
				+ 	"?s ?p "+adjustObject(resource)+filterOutPredicates
				+ "}";
			
		query_results = sec.execQuery(queryString);
		if(query_results.size()>0)
			result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
		return result;
	}

	public static int countTriplesBySubjectOrObject(DBpediaKB kb, AnyResource resource, Filters filter) {
		int result = 0;
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());

		String filterOutLiterals = generateFilterOut_Literals(filter, "o");
		String filterOutPredicates = generateFilterOut_Predicates(filter, "p", kb.getPredicatesBlackList());

		Vector<QuerySolution> query_results;
		String queryString = "";
		if(resource instanceof URIImpl) {
			queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE "
					+ "{"
					+ 	"SELECT ?s ?p ?o WHERE "
					+ 		"{"
					+ 			"{"
					+ 				"<"+resource.getResourceID()+"> ?p ?o"+filterOutLiterals+filterOutPredicates
					+ 			"} "
					+			"UNION "
					+ 			"{"
					+ 				"?s ?p "+adjustObject(resource)+filterOutPredicates
					+			"} "
					+		"} "
					+ "}";
					
			query_results = sec.execQuery(queryString);
			if(query_results.size()>0)
				result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
		}		
		else { 
			queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE "
				+ "{"
				+ 	"?s ?p "+adjustObject(resource)+filterOutPredicates
				+ "}";
		
			query_results = sec.execQuery(queryString);
			if(query_results.size()>0)
				result = result + query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
		}
			
		return result;
	}
	
	public static int countTriplesByPredicateAndObject(DBpediaKB kb, AnyResource predicate, AnyResource resource) {
		int result = 0;

		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
				
		Vector<QuerySolution> query_results;
		if(predicate instanceof URIImpl) {			
			String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE "
					+ "{ "
					+ 	"?s <"+predicate.getResourceID()+"> "+adjustObject(resource)
					+ "}";
			query_results = sec.execQuery(queryString);
			if(query_results.size()>0)
				result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
		}
		
		return result;
	}
	
	public static int countTriplesByPredicateAndSubject(DBpediaKB kb, AnyResource predicate, AnyResource resource, Filters filter) {
		int result = 0;

		if(resource instanceof URIImpl && predicate instanceof URIImpl) {
			SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());

			String filterOutLiterals = generateFilterOut_Literals(filter, "o");

			Vector<QuerySolution> query_results;
			String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE "
					+ "{ "
					+ 	"<"+resource.getResourceID()+"> <"+predicate.getResourceID()+"> ?o "+filterOutLiterals
					+ "}";
			query_results = sec.execQuery(queryString);
			if(query_results.size()>0)
				result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
		}
		
		return result;
	}
	
	public static int countTriplesByPredicateAndSubjectOrObject(DBpediaKB kb, AnyResource predicate, AnyResource resource, Filters filter) {
		int result = 0;
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());

		String filterOutLiterals = generateFilterOut_Literals(filter, "o");
		
		Vector<QuerySolution> query_results;
		if(predicate instanceof URIImpl) {
			if(resource instanceof URIImpl) {
				String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE "
						+ "{"
						+ 	"<"+resource.getResourceID()+"> <"+predicate.getResourceID()+"> ?o "+filterOutLiterals
						+ "}";
				query_results = sec.execQuery(queryString);
				if(query_results.size()>0)
					result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
			}
			String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE "
					+ "{"
					+ 	"?s <"+predicate.getResourceID()+"> "+adjustObject(resource)
					+ "}";
			query_results = sec.execQuery(queryString);
			if(query_results.size()>0)
				result = result + query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
		}		
		
		return result;
	}
	
	public static Set<AnyResource> getPredicatesBySubjectAndObject(DBpediaKB kb, AnyResource s, AnyResource o, Filters filter) {
		Set<AnyResource> result = new HashSet<AnyResource>();

		if(s instanceof URIImpl) {
		
			SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
			String filterOutPredicates = generateFilterOut_Predicates(filter, "p", kb.getPredicatesBlackList());
			
			Vector<QuerySolution> query_results;
			String queryString = "SELECT ?p FROM <"+kb.getGraph()+"> WHERE "
					+ "{ "
					+ 	"<"+s.getResourceID()+"> ?p "+adjustObject(o)+filterOutPredicates
					+ "}";
			
			query_results = sec.execQuery(queryString);
			
			for(QuerySolution qs:query_results)
				result.add(RDFFactory.getInstance().createURI(qs.getResource("p").getURI().toString()));
		}
		
		return result;
	}
	
	public static Set<AnyResource> getBow(DBpediaKB kb, Filters filter) {
		Set<AnyResource> result = new HashSet<AnyResource>();

		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
		String filterOutPredicates = generateFilterOut_Predicates(filter, "p", kb.getPredicatesBlackList());
		
		Vector<QuerySolution> query_results;
		String queryString = "SELECT ?x FROM <"+kb.getGraph()+"> WHERE "
				+ "{"
				+ 	"?x ?p ?x"+filterOutPredicates
				+ "}";
		query_results = sec.execQuery(queryString);
		
		for(QuerySolution qs:query_results)
			result.add(RDFFactory.getInstance().createURI(qs.getResource("x").getURI().toString()));
		
		return result;
	}

	public static Set<AnyResource> getAllPredicates(DBpediaKB kb, Filters filter) {
		Set<AnyResource> result = new HashSet<AnyResource>();

			SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
			String filterOutLiterals = generateFilterOut_Literals(filter, "o");
			String filterOutPredicates = generateFilterOut_Predicates(filter, "p", kb.getPredicatesBlackList());

			
			Vector<QuerySolution> query_results;
			String queryString = "SELECT DISTINCT ?p FROM <"+kb.getGraph()+"> WHERE "
					+ "{ "
					+ 	"?s ?p ?o"+filterOutLiterals+filterOutPredicates
					+ "}";
			
			query_results = sec.execQuery(queryString);
			
			for(QuerySolution qs:query_results)
				result.add(RDFFactory.getInstance().createURI(qs.getResource("p").getURI().toString()));
		return result;
	}
	
	
	private static String adjustObject(AnyResource resource) {
		String result = "";
		if(resource.getResourceID().contains("@")) {
			result = "\""+resource.getResourceID().substring(0, resource.getResourceID().indexOf("@")) +"\"@"+resource.getResourceID().substring(resource.getResourceID().indexOf("@")+1);
		}
		else if(resource.getResourceID().contains("^^")) {
			result = "\""+resource.getResourceID().substring(0, resource.getResourceID().indexOf("^^")) +"\"^^<"+resource.getResourceID().substring(resource.getResourceID().indexOf("^^")+2)+">";
		}
		else if(resource instanceof URI){
			result = "<"+resource.getResourceID()+">";
		}
		else
			result = "'"+resource.getResourceID()+"'";
		
		return result+" ";
	}

	public static String generateFilterOut_Literals(Filters filter, String var) {
		String result = "";
		if((filter == Filters.FILTER_OUT_ALL) || filter == Filters.FILTER_OUT_LITERALS)
			result = " . FILTER (isLiteral(?"+var+") != true) ";
		return result;
	}
	
	public static String generateFilterOut_Predicates(Filters filter, String var, Set<AnyURI> blackList) {
		String result = "";
		if((filter == Filters.FILTER_OUT_ALL) || filter == Filters.FILTER_OUT_BLACKLIST_PREDICATES)
			for(AnyURI uri:blackList)
				result = result + " . FILTER (?"+var+" != <"+uri.getResourceID()+">) ";
		return result;
	}

	public static String generateFilterOut_Subjects(Filters filter, String var, Set<AnyURI> blackList) {
		String result = "";
		if((filter == Filters.FILTER_OUT_ALL) || filter == Filters.FILTER_OUT_BLACKLIST_SUBJECTS)
			for(AnyURI uri:blackList)
				result = result + " . FILTER (?"+var+" != <"+uri.getResourceID()+">) ";
		return result;
	}
	
	public static String generateFilterOut_Objects(Filters filter, String var, Set<AnyResource> blackList) {
		String result = "";
		if((filter == Filters.FILTER_OUT_ALL) || filter == Filters.FILTER_OUT_BLACKLIST_OBJECTS)
			for(AnyResource res:blackList)
				result = result + " . FILTER (?"+var+" != "+adjustObject(res)+") ";
		return result;
	}
}