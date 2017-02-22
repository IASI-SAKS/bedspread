package it.cnr.iasi.leks.debspread.tests.util;

import static org.junit.Assert.*;

import java.util.Vector;

import com.hp.hpl.jena.query.QuerySolution;
import org.junit.Assert;
import org.junit.Test;

import it.cnr.iasi.leks.bedspread.rdf.impl.SPARQLEndpointConnector;

public class SPARQLEndpointConnectorTest {

	@Test
	public void executeQuery() {
		int result = 0;
		String DBPEDIA_ENDPOINT = "http://dbpedia.org/sparql";
		String DBPEDIA_GRAPH = "http://dbpedia.org";
		String queryString = "SELECT (COUNT(*) AS ?count) FROM <"+DBPEDIA_GRAPH+"> WHERE {"
				+ "?s <http://dbpedia.org/property/birthPlace> ?o"
				+ "}";
		
		SPARQLEndpointConnector sec = new SPARQLEndpointConnector(DBPEDIA_ENDPOINT); 
		Vector<QuerySolution> qss = sec.execQuery(queryString);
		
		result = qss.elementAt(0).getLiteral("count").asLiteral().getInt();
		
		System.out.println("triples="+result);
		Assert.assertTrue(result>0);
	}

}
