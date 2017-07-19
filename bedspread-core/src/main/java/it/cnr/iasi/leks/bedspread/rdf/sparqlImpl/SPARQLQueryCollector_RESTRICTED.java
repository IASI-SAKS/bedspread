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
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFFactory;
import it.cnr.iasi.leks.bedspread.rdf.impl.URIImpl;

/**
 * 
 * @author ftaglino
 *
 */
public class SPARQLQueryCollector_RESTRICTED {

	final static String queryPieceForSelectingObjectProperties= " ?p <"+Constants.RDF_TYPE+"> <"+Constants.OWL_OBJECTPROPERTY+"> "; 
	
	public static int getDegree(DBpediaKB kb, AnyResource resource, Filters filter) {
		int result = 0;
		
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
		
		String filterOutPredicates = generateFilterOut_Predicates(filter, "p", kb.getPredicatesBlackList());
		
		String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE "
				+ "{"
				+ 	"{"
				+ 		queryPieceForSelectingObjectProperties + " . "
				+ 		"<"+resource.getResourceID()+"> ?p ?o " + filterOutPredicates
				+	"} "
				+ 	"UNION "
				+ 	"{"
				+		queryPieceForSelectingObjectProperties + " . "
				+ 		"?s ?p <"+resource.getResourceID()+"> " + filterOutPredicates
				+ 	"}"
				+ 	"UNION "
				+ 	"{"
				+ 		"<"+resource.getResourceID()+"> <"+Constants.DCT_SUBJECT+"> ?o "
				+	"} "
				+ 	"UNION "
				+ 	"{"
				+ 		"?s <"+Constants.DCT_SUBJECT+"> <"+resource.getResourceID()+">"
				+ 	"}"
				+ "}";
		
		System.out.println(queryString);
		
		Vector<QuerySolution> query_results = sec.execQuery(queryString);
		if(query_results.size()>0)
			result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();

		return result;
	} 
	
	
	public static boolean isPredicate(DBpediaKB kb, AnyResource resource, Filters filter) {
		boolean result = false;

		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
		
		String queryString = "ASK FROM <"+kb.getGraph()+"> "
				+ "{"
				+ 	"?s <"+resource.getResourceID()+"> ?o "
				+ "}";
		
		result = sec.execAsk(queryString);

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
		String filterOutPredicates = generateFilterOut_Predicates(filter, "p", kb.getPredicatesBlackList());

		String queryString = "";
		queryString = "ASK FROM <"+kb.getGraph()+"> "
			+ "{ "
			+	"{ "
			+   	queryPieceForSelectingObjectProperties + " . "
			+		"<"+resource.getResourceID()+"> ?p ?o " + filterOutPredicates
			+	"} "
			+	"UNION "
			+	"{ "
			+   	queryPieceForSelectingObjectProperties + " . "
			+		"?s ?p <"+resource.getResourceID()+"> " 
			+	"} "
			+ 	"UNION "
			+ 	"{"
			+ 		"?s <"+Constants.DCT_SUBJECT+"> <"+resource.getResourceID()+"> "
			+ 	"}"
			+ 	"UNION "
			+ 	"{"
			+ 		"<"+resource.getResourceID()+"> <"+Constants.DCT_SUBJECT+"> ?o"
			+ 	"}"
			+ "}";

		result = sec.execAsk(queryString);

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
		
		String filterOutPredicates = generateFilterOut_Predicates(filter, "p", kb.getPredicatesBlackList());
		
		String queryString = "SELECT DISTINCT ?x FROM <"+kb.getGraph()+"> WHERE "
				+ "{"
				+ 	"{"
				+ 		queryPieceForSelectingObjectProperties + " . "
				+ 		"?x ?p <" + resource.getResourceID() + ">" + filterOutPredicates
				+	"} "
				+ 	"UNION "
				+ 	"{"
				+		queryPieceForSelectingObjectProperties + " . "
				+ 		"<"+resource.getResourceID()+"> ?p ?x " + filterOutPredicates
				+ 	"}"
				+ 	"UNION "
				+ 	"{"
				+ 		"<"+resource.getResourceID()+"> <"+Constants.DCT_SUBJECT+"> ?x "
				+	"} "
				+ 	"UNION "
				+ 	"{"
				+ 		"?x <"+Constants.DCT_SUBJECT+"> <"+resource.getResourceID()+"> "
				+ 	"}"
				+ "}";
								
		Vector<QuerySolution> query_results = sec.execQuery(queryString);
		
		for(int i=0; i<query_results.size(); i++) {
			RDFNode node = query_results.elementAt(i).get("x");
			String neighboor = node.asResource().getURI().toString();  
			result.add(RDFFactory.getInstance().createURI(neighboor));
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
				+ 	"{"
				+ 		queryPieceForSelectingObjectProperties + " . "
				+ 		"?s ?p <" + resource.getResourceID() + ">" + filterOutPredicates
				+	"} "
				+ 	"UNION "
				+ 	"{"
				+ 		"?s <"+Constants.DCT_SUBJECT+"> <"+resource.getResourceID()+"> "
				+ 	"}"
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
		
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
		
		String filterOutPredicates = generateFilterOut_Predicates(filter, "p", kb.getPredicatesBlackList());
		
		String queryString = "SELECT DISTINCT ?o FROM <"+kb.getGraph()+"> WHERE "
				+ "{"
				+ 	"{"
				+		queryPieceForSelectingObjectProperties + " . "
				+ 		"<"+resource.getResourceID()+"> ?p ?o " + filterOutPredicates
				+ 	"}"
				+ 	"UNION "
				+ 	"{"
				+ 		"<"+resource.getResourceID()+"> <"+Constants.DCT_SUBJECT+"> ?o "
				+	"} "
				+ "}";			
		Vector<QuerySolution> query_results = sec.execQuery(queryString);
		
		for(int i=0; i<query_results.size(); i++) {
			RDFNode node = query_results.elementAt(i).get("o");
			String neighboor = node.asResource().getURI().toString();  
			result.add(RDFFactory.getInstance().createURI(neighboor));
		}
		return result;
	}
	
	public static int countTotalTriples(DBpediaKB kb, Filters filter) {
		int result = 0;
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
		
		String filterOutPredicates = generateFilterOut_Predicates(filter, "p", kb.getPredicatesBlackList());

		Vector<QuerySolution> query_results;
		String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE "
				+ "{"
				+ 	"{"
				+ 		queryPieceForSelectingObjectProperties + " . "
				+ 		"?s ?p ?o " + filterOutPredicates
				+	"} "
				+ 	"UNION "
				+ 	"{"
				+ 		"?s <"+Constants.DCT_SUBJECT+"> ?o "
				+	"} "
				+ "}";
		query_results = sec.execQuery(queryString);
		if(query_results.size()>0)
			result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
		
		return result;
		
	}
	
	public static int countTriplesByPredicate(DBpediaKB kb, AnyResource resource, Filters filter) {
		int result = 0;
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());

		Vector<QuerySolution> query_results;
		String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE "
				+ "{"
				+ 	"?s <"+resource.getResourceID()+"> ?o "
				+ "}";
		query_results = sec.execQuery(queryString);
		if(query_results.size()>0)
			result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
		
		return result;
	}
		
	public static int countTriplesBySubject(DBpediaKB kb, AnyResource resource, Filters filter) {
		int result = 0;
		
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());

		String filterOutPredicates = generateFilterOut_Predicates(filter, "p", kb.getPredicatesBlackList());

		Vector<QuerySolution> query_results;
		if(resource instanceof URIImpl) {
			String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE "
					+ "{"
					+ 	"{"
					+		queryPieceForSelectingObjectProperties + " . "
					+ 		"<"+resource.getResourceID()+"> ?p ?o " + filterOutPredicates
					+ 	"}"
					+ 	"UNION "
					+ 	"{"
					+ 		"<"+resource.getResourceID()+"> <"+Constants.DCT_SUBJECT+"> ?o "
					+	"} "
					+ "}";
			query_results = sec.execQuery(queryString);
			if(query_results.size()>0)
				result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
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
				+ 	"{"
				+ 		queryPieceForSelectingObjectProperties + " . "
				+ 		"?s ?p <" + resource.getResourceID() + ">" + filterOutPredicates
				+	"} "
				+ 	"UNION "
				+ 	"{"
				+ 		"?s <"+Constants.DCT_SUBJECT+"> <"+resource.getResourceID()+"> "
				+ 	"}"
				+ "}";		

			
		query_results = sec.execQuery(queryString);
		if(query_results.size()>0)
			result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
		return result;
	}

	public static int countTriplesBySubjectOrObject(DBpediaKB kb, AnyResource resource, Filters filter) {
		int result = 0;
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());

		String filterOutPredicates = generateFilterOut_Predicates(filter, "p", kb.getPredicatesBlackList());

		Vector<QuerySolution> query_results;
		String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE "
					+ "{"
					+ 	"{"
					+ 		queryPieceForSelectingObjectProperties + " . "
					+ 		"?s ?p <" + resource.getResourceID() + ">" + filterOutPredicates
					+	"} "
					+ 	"UNION "
					+ 	"{"
					+		queryPieceForSelectingObjectProperties + " . "
					+ 		"<"+resource.getResourceID()+"> ?p ?o " + filterOutPredicates
					+ 	"}"
					+ 	"UNION "
					+ 	"{"
					+ 		"<"+resource.getResourceID()+"> <"+Constants.DCT_SUBJECT+"> ?o "
					+	"} "
					+ 	"UNION "
					+ 	"{"
					+ 		"?s <"+Constants.DCT_SUBJECT+"> <"+resource.getResourceID()+"> "
					+ 	"}"
					+ "}";
					
		query_results = sec.execQuery(queryString);
		if(query_results.size()>0)
			result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
			
		return result;
	}
	
/*	
	public static Vector<AnyResource> getArcsBySubjectOrObject_(DBpediaKB kb, AnyResource resource, Filters filter) {
		Vector<AnyResource> result = new Vector<AnyResource>();
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());

		String filterOutLiterals = generateFilterOut_Literals(filter, "o");
		String filterOutPredicates = generateFilterOut_Predicates(filter, "p", kb.getPredicatesBlackList());

		Vector<QuerySolution> query_results;
		String queryString = "";
		if(resource instanceof URIImpl) {
			queryString = "SELECT ?p FROM <"+kb.getGraph()+"> WHERE "
					+ "{"
					+ 	"{"
					+ 		queryPieceForSelectingObjectProperties + " . "
					+ 		"?s ?p <" + resource + ">" + filterOutPredicates
					+	"} "
					+ 	"UNION "
					+ 	"{"
					+		queryPieceForSelectingObjectProperties + " . "
					+ 		"<"+resource.getResourceID()+"> ?p ?o " + filterOutPredicates
					+ 	"}"
					+ 	"UNION "
					+ 	"{"
					+ 		"<"+resource.getResourceID()+"> <"+Constants.DCT_SUBJECT+"> ?x "
					+	"} "
					+ 	"UNION "
					+ 	"{"
					+ 		"?x <"+Constants.DCT_SUBJECT+"> <"+resource+"> "
					+ 	"}"
					+ "}";
					
			query_results = sec.execQuery(queryString);
			
			for(int i=0; i<query_results.size(); i++) {
				RDFNode node = query_results.elementAt(i).get("p");
				String neighboor = node.asResource().getURI().toString();  
				result.add(RDFFactory.getInstance().createURI(neighboor));
	 		}			
		}		
		else { 
			queryString = "SELECT ?p FROM <"+kb.getGraph()+"> WHERE "
				+ "{"
				+ 	"?s ?p " + adjustObject(resource) + filterOutPredicates
				+ "}";
		
			query_results = sec.execQuery(queryString);
			for(int i=0; i<query_results.size(); i++) {
				RDFNode node = query_results.elementAt(i).get("p");
				String neighboor = node.asResource().getURI().toString();  
				result.add(RDFFactory.getInstance().createURI(neighboor));
	 		}
		}
			
		return result;
	}
*/	
	public static int countTriplesByPredicateAndObject(DBpediaKB kb, AnyResource predicate, AnyResource resource) {
		int result = 0;

		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
				
		Vector<QuerySolution> query_results;
		String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE "
				+ "{ "
				+ 	"?s <"+predicate.getResourceID()+"> <"+resource.getResourceID()+"> "
				+ "}";
		query_results = sec.execQuery(queryString);

		if(query_results.size()>0)
			result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();

		
		return result;
	}
	
	public static int countTriplesByPredicateAndSubject(DBpediaKB kb, AnyResource predicate, AnyResource resource, Filters filter) {
		int result = 0;

		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());

		Vector<QuerySolution> query_results;
		String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE "
				+ "{ "
				+ 	"<"+resource.getResourceID()+"> <"+predicate.getResourceID()+"> ?o "
				+ "}";
		query_results = sec.execQuery(queryString);
		if(query_results.size()>0)
			result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
		
		return result;
	}
	
	public static int countTriplesByPredicateAndSubjectOrObject(DBpediaKB kb, AnyResource predicate, AnyResource resource, Filters filter) {
		int result = 0;
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
		
		Vector<QuerySolution> query_results;
		String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+kb.getGraph()+"> WHERE "
				+ "{"
				+ 	"{"
				+ 		"?s <"+predicate.getResourceID()+"> <" + resource.getResourceID() + ">" 
				+	"} "
				+ 	"UNION "
				+ 	"{"
				+ 		"<"+resource.getResourceID()+"> <"+predicate.getResourceID()+"> ?o " 
				+ 	"}"
				+ "}";
		query_results = sec.execQuery(queryString);

		if(query_results.size()>0)
			result = query_results.elementAt(0).getLiteral("count").asLiteral().getInt();
		
		return result;
	}
	
	public static Set<AnyResource> getPredicatesBySubjectAndObject(DBpediaKB kb, AnyResource s, AnyResource o, Filters filter) {
		Set<AnyResource> result = new HashSet<AnyResource>();

		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
		String filterOutPredicates = generateFilterOut_Predicates(filter, "p", kb.getPredicatesBlackList());
		
		Vector<QuerySolution> query_results;
		String queryString = "SELECT ?p FROM <"+kb.getGraph()+"> WHERE "
				+ "{"
				+ 	"{"
				+ 		queryPieceForSelectingObjectProperties + " . "
				+ 		"<"+s.getResourceID()+"> ?p <" + o.getResourceID() + "> " + filterOutPredicates
				+	"} "
				+ "}";
		
		query_results = sec.execQuery(queryString);
		
		for(QuerySolution qs:query_results)
			result.add(RDFFactory.getInstance().createURI(qs.getResource("p").getURI().toString()));
		
		queryString = "ASK "
				+ "{ "
				+ 	"<"+s.getResourceID()+"> <"+Constants.DCT_SUBJECT+"> <"+o.getResourceID()+"> "
				+ "} ";
		
		boolean r = sec.execAsk(queryString);
		
		if(r==true) {
			AnyResource dct_dubject = RDFFactory.getInstance().createURI(Constants.DCT_SUBJECT);
			result.add(dct_dubject);
		}
	
		//AGGIUNGERE ASK su DCT_SUBJECT
		
		return result;
	}
		
	public static Set<AnyResource> getAllPredicates(DBpediaKB kb, Filters filter) {
		Set<AnyResource> result = new HashSet<AnyResource>();

			SPARQLEndpointConnector sec = new SPARQLEndpointConnector(kb.getEndpoint());
			String filterOutPredicates = generateFilterOut_Predicates(filter, "p", kb.getPredicatesBlackList());
			
			Vector<QuerySolution> query_results;
			String queryString = "SELECT ?p FROM <"+kb.getGraph()+"> WHERE "
					+ "{ "
					+ 	queryPieceForSelectingObjectProperties
					+ 		filterOutPredicates
					+ "}";
			
			query_results = sec.execQuery(queryString);
			
			for(QuerySolution qs:query_results)
				result.add(RDFFactory.getInstance().createURI(qs.getResource("p").getURI().toString()));
			
			AnyResource dct_dubject = RDFFactory.getInstance().createURI(Constants.DCT_SUBJECT);
			result.add(dct_dubject);
		return result;
	}
		
	public static String generateFilterOut_Predicates(Filters filter, String var, Set<AnyURI> blackList) {
		String result = "";
		if((filter == Filters.FILTER_ALL) || filter == Filters.FILTER_OUT_BLACKLIST_PREDICATES)
			for(AnyURI uri:blackList)
				result = result + " . FILTER (?"+var+" != <"+uri.getResourceID()+">) ";
		return result;
	}
	

}