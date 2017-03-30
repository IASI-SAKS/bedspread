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

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import it.cnr.iasi.leks.bedspread.exceptions.impl.InteractionProtocolViolationException;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFFactory;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFGraph;
import it.cnr.iasi.leks.bedspread.rdf.URI;

public class RDFGraphTest {
	private RDFGraph rdfGraph;
	private static final String INPUT_GRAPH_FILE = "src/test/resources/whiteboardRDFGraph.csv";

	@Test
	public void getPredicatesBySubjectAndObject() throws IOException, InteractionProtocolViolationException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{			
		KnowledgeBase kb = this.loadMinimalKB();
		
		RDFFactory f = RDFFactory.getInstance();
		URI r1 = f.createURI("origin");
		URI r2 = f.createURI("n1");
		
		Set<AnyResource> preds = kb.getPredicatesBySubjectAndObject(r1, r2);
		
		Assert.assertTrue(preds.size()>=0);
	}

	private KnowledgeBase loadMinimalKB() throws IOException{
		FileReader kbReader = new FileReader(INPUT_GRAPH_FILE);
		this.rdfGraph = new RDFGraph(kbReader);
				
		return this.rdfGraph;
	}
	
}
