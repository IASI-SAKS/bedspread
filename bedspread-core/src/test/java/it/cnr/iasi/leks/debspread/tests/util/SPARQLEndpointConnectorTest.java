package it.cnr.iasi.leks.debspread.tests.util;

import static org.junit.Assert.*;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.junit.Assert;
import org.junit.Test;

import it.cnr.iasi.leks.bedspread.rdf.impl.SPARQLEndpointConnector;

public class SPARQLEndpointConnectorTest {

	@Test
	public void executeQuery() {
		int result = 0;
		String DBPEDIA_ENDPOINT = "http://dbpedia.org/sparql";
		String queryString = "SELECT (COUNT(*) AS ?count) FROM <http://dbpedia.org> WHERE {"
				+ "?s <http://dbpedia.org/property/birthPlace> ?o"
				+ "}"
				+ "GROUP BY ?s";
		
		ResultSet results = SPARQLEndpointConnector.execQuery(DBPEDIA_ENDPOINT, queryString);
		
		while(results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			result = soln.getLiteral("count").asLiteral().getInt();
		}
		Assert.assertTrue(result>0);
//		this.closeQueryExecution();
	}

}
