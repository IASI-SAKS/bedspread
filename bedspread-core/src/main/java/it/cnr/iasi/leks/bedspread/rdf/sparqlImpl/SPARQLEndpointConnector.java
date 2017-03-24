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
package it.cnr.iasi.leks.bedspread.rdf.sparqlImpl;

import java.util.Vector;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

/**
 * 
 * @author ftaglino
 *
 */
public class SPARQLEndpointConnector {
	private String endpointUrl = "";
	
	public SPARQLEndpointConnector(String endpointUrl) {
		super();
		this.endpointUrl = endpointUrl;
	}

	public String getEndpointUrl() {
		return endpointUrl;
	}

	public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}

	/**
	 * Execute a query on the configured Sparql endpoint
	 * @param url The address of the SPARQL endpoint
	 * @param queryString The SPARQL query
	 * @return Vector<QuerySolution> 
	 */	
	public Vector<QuerySolution> execQuery(String queryString) {	
		Vector<QuerySolution> qss = new Vector<QuerySolution>(); 
		Query query = QueryFactory.create(queryString);        
		QueryExecution qexec = QueryExecutionFactory.sparqlService(endpointUrl, query);
		try {
			ResultSet r = qexec.execSelect();
			while(r.hasNext())
				qss.add(r.next());
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			qexec.close();
		}
		return qss;
	}
	
	
	

}
