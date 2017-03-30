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
package it.cnr.iasi.leks.bedspread.tests;

import org.junit.Assert;
import org.junit.Test;

import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.impl.weights.ic.Abstract_EdgeWeighting_IC;
import it.cnr.iasi.leks.bedspread.impl.weights.ic.EdgeWeightingFactory;
import it.cnr.iasi.leks.bedspread.impl.weights.ic.SemanticWeighting_IC;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFFactory;
import it.cnr.iasi.leks.bedspread.rdf.URI;
import it.cnr.iasi.leks.bedspread.rdf.sparqlImpl.DBpediaKB;

public class SemanticWeighting_ICTest {

	private static final String HTTP_DBPEDIA_ORG_RESOURCE_BARACK_OBAMA = "http://dbpedia.org/resource/Barack_Obama";
	private static final String HTTP_DBPEDIA_ORG_RESOURCE_JOE_BIDEN = "http://dbpedia.org/resource/Joe_Biden";
	
	
	@Test
	public void weight() {
		DBpediaKB kb = DBpediaKB.getInstance();
		RDFFactory f = RDFFactory.getInstance();
		URI r1 = f.createURI(HTTP_DBPEDIA_ORG_RESOURCE_BARACK_OBAMA);
		URI r2 = f.createURI(HTTP_DBPEDIA_ORG_RESOURCE_JOE_BIDEN);
		
		Node n1 = new Node(r1);
		Node n2 = new Node(r2);
		
		Abstract_EdgeWeighting_IC ew = EdgeWeightingFactory.getInstance().getEdgeWeighting_IC(kb);
		
		SemanticWeighting_IC sw = new SemanticWeighting_IC(kb,ew);
		double w = sw.weight(n1, n2);
		
		System.out.println("SemanticWeighting_IC.weight(="+r1.getResourceID()+", "+r2.getResourceID()+")="+w);
				
		Assert.assertTrue(w>0);
	}


}
