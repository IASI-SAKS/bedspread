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
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import it.cnr.iasi.leks.bedspread.AbstractSemanticSpread;
import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.TerminationPolicy;
import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.exceptions.impl.InteractionProtocolViolationException;
import it.cnr.iasi.leks.bedspread.impl.HT13ConfSemanticSpread;
import it.cnr.iasi.leks.bedspread.impl.SemanticSpreadFactory;
import it.cnr.iasi.leks.bedspread.impl.SimpleSemanticSpread;
import it.cnr.iasi.leks.bedspread.impl.policies.SimpleTerminationPolicy;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.rdf.impl.DBpediaKB;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFFactory;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFGraph;
import it.cnr.iasi.leks.debspread.tests.util.PropertyUtilNoSingleton;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.opencsv.CSVReader;

/*
 * @author gulyx
 */
public class SemanticSpreadOnWhiteboardRDFGraph_modifiedTest extends AbstractTest{

	private RDFGraph rdfGraph;
	private static final String ORIGIN_LABEL = "origin";
	private static final String INPUT_GRAPH_FILE = "src/test/resources/whiteboardRDFGraph_modified.csv";
	

	@Test
	public void testHT13ConfSemanticSpread() throws IOException, InteractionProtocolViolationException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		KnowledgeBase kb = this.loadMinimalKB();
		Node resourceOrigin = this.extractTrivialOrigin();
	
		TerminationPolicy term = new SimpleTerminationPolicy(5);
		
		String testPropertyFile = "configTestSemanticWeighting_IC_onLocalGraph.properties";
		System.getProperties().put(PropertyUtil.CONFIG_FILE_LOCATION_LABEL, testPropertyFile);
		PropertyUtilNoSingleton.getInstance();
		
		AbstractSemanticSpread ss = new HT13ConfSemanticSpread(resourceOrigin,kb,term);
		ss.run();
		
		String fileName = this.getFlushFileName("testHT13ConfSemanticSpread_onWhiteboardRDFGraph_modified_IC");
		Writer out = new FileWriter(fileName);
		ss.flushData(out);
				
		System.getProperties().remove(PropertyUtil.CONFIG_FILE_LOCATION_LABEL);

		Assert.assertTrue(true);		
	}
		
	private Node extractTrivialOrigin() {
		AnyResource resource = RDFFactory.getInstance().createBlankNode(ORIGIN_LABEL);
		Node n = new Node(resource);
		return n;
	}

	private KnowledgeBase loadMinimalKB() throws IOException{
		FileReader kbReader = new FileReader(INPUT_GRAPH_FILE);
		this.rdfGraph = new RDFGraph(kbReader);
				
		return this.rdfGraph;
	}
		
}
