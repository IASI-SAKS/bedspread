package it.cnr.iasi.leks.bedspread.rdf.impl;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;

public class SPARQLEndpointConnector {
	
	/**
	 * Execute a query on the specified Sparql endpoint
	 * @param url The URL of the SPARQL endpoint
	 * @param queryString The SPARQL query
	 * @return 
	 */	
	public static ResultSet execQuery(String endpointUrl, String queryString) {
		Query query = QueryFactory.create(queryString);        
		QueryExecution qexec = QueryExecutionFactory.sparqlService(endpointUrl, query);
		ResultSet results = null;
		try {
			results = qexec.execSelect();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		finally{
			qexec.close();
		}
		return results;
	}
}
