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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import it.cnr.iasi.leks.bedspread.AbstractSemanticSpread;
import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.ExecutionPolicy;
import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.exceptions.impl.InteractionProtocolViolationException;
import it.cnr.iasi.leks.bedspread.impl.BasicSemanticSpread;
import it.cnr.iasi.leks.bedspread.impl.policies.SpreadingBound;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFFactory;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFGraph;
import it.cnr.iasi.leks.bedspread.rdf.sparqlImpl.DBpediaKB;
import it.cnr.iasi.leks.bedspread.tests.util.PropertyUtilNoSingleton;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author ftaglino
 */
public class BorderTest extends AbstractTest{

	private RDFGraph rdfGraph;
	private static final String ORIGIN_LABEL = "origin";
	private static final String INPUT_GRAPH_FILE = "src/test/resources/LocalRDFGraph.csv";

	protected final Logger logger = LoggerFactory.getLogger(AbstractSemanticSpread.class);
	
	//@Ignore
	@Test
	public void testBorderWithSemanticWeighting_IC_onLocalGraph() throws IOException, InteractionProtocolViolationException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		KnowledgeBase kb = loadMinimalKB();
		Node resourceOrigin = this.extractTrivialOrigin();
	
		//ExecutionPolicy policy = new SimpleExecutionPolicy(2);
		ExecutionPolicy policy = new SpreadingBound(kb, 2);
		
		String testPropertyFile = "configTestSemanticWeighting_IC_onLocalGraph.properties";
		System.getProperties().put(PropertyUtil.CONFIG_FILE_LOCATION_LABEL, testPropertyFile);
		PropertyUtilNoSingleton.getInstance();
		
		AbstractSemanticSpread ss = new BasicSemanticSpread(resourceOrigin,kb,policy);
		ss.run();
		
		String fileName = this.getFlushFileName("testBorderWithSemanticWeighting_IC_onLocalGraph");
		Writer out = new FileWriter(fileName);
		ss.flushData(out);
		
		this.logger.info("{}", "LEAVES");
		for(Node leaf:ss.getExplorationLeaves()) {
			this.logger.info("{}", leaf.getResource().getResourceID());
		}
		
		
		
		System.getProperties().remove(PropertyUtil.CONFIG_FILE_LOCATION_LABEL);

		Set<Node> expected_results = new HashSet<Node>(); 
		Node n = new Node(RDFFactory.getInstance().createBlankNode("x1"));
		//n.updateScore(0.44996553731885225);
		expected_results.add(n);
		
		n = new Node(RDFFactory.getInstance().createBlankNode("x3"));
		//n.updateScore(0.44996553731885225);
		expected_results.add(n);
	
		n = new Node(RDFFactory.getInstance().createBlankNode("x21"));
		//n.updateScore(0.8132474286295223);
		expected_results.add(n);
		
		n = new Node(RDFFactory.getInstance().createBlankNode("x22"));
		//n.updateScore(0.44996553731885225);
		expected_results.add(n);
		
		n = new Node(RDFFactory.getInstance().createBlankNode("x23"));
		//n.updateScore(0.44996553731885225);
		expected_results.add(n);
		
		boolean check = true;
		for(Node nx:expected_results)
			if(!(ss.getExplorationLeaves().contains(nx))) check = false;
		
		Assert.assertTrue(check);		
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
