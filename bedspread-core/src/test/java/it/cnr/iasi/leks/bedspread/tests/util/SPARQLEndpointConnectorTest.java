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
package it.cnr.iasi.leks.bedspread.tests.util;

import java.util.Vector;

import org.apache.jena.query.QuerySolution;
import org.junit.Assert;
import org.junit.Test;

import it.cnr.iasi.leks.bedspread.rdf.sparqlImpl.SPARQLEndpointConnector;

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
