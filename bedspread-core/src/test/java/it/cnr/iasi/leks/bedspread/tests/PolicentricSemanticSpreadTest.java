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
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;

import it.cnr.iasi.leks.bedspread.AbstractSemanticSpread;
import it.cnr.iasi.leks.bedspread.PolicentricSemanticSpread;
import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.exceptions.AbstractBedspreadException;
import it.cnr.iasi.leks.bedspread.impl.HT13PolicentricSemanticSpread;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.AnyURI;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.rdf.URI;
import it.cnr.iasi.leks.bedspread.rdf.impl.KBFactory;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFFactory;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFGraph;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFTriple;
import it.cnr.iasi.leks.bedspread.tests.util.PropertyUtilNoSingleton;
import it.cnr.iasi.leks.bedspread.util.SetOfNodesFactory;

public class PolicentricSemanticSpreadTest extends AbstractTest{

	protected final Logger logger = LoggerFactory.getLogger(PolicentricSemanticSpreadTest.class);
	
	private RDFGraph rdfGraph;
	private static final String ORIGIN_PREFIX_LABEL = "origin";
	private static final int NUMBER_OF_ORIGINS = 2;

	private static final double INITIAL_STIMULUS = 1.0;
	private static final double STIMULUS_DELTA = 0.000000000000001;
	
	private static final String INPUT_GRAPH_FILE = "src/test/resources/whiteboardTwoOriginsRDFGraph.csv";
	private static final String INPUT_TREE_FILE = "src/test/resources/whiteboardTwoOriginsRDFTree.csv";
	
	private static final String EXPECTED_OUTPUT_FILE_ALFA = "src/test/resources/output_actualTestByComparingOverallActivationSpreadConfAndFilteredNodes_Policentric.csv";
	
	@Test
	public void firstMinimalTestConf() throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InterruptedException, AbstractBedspreadException {
		KnowledgeBase kb = this.loadMinimalKB(0);
		Set<Node> resourceOriginSet = this.extractTrivialOriginSet();
		
		String testPropertyFile = "configTestPolicentricDefaultWeigh.properties";
		System.getProperties().setProperty(PropertyUtil.CONFIG_FILE_LOCATION_LABEL, testPropertyFile);
		PropertyUtilNoSingleton.getInstance();
		
		PolicentricSemanticSpread pool = new HT13PolicentricSemanticSpread(resourceOriginSet, kb);
		String fileNamePolicentric = this.getFlushFileName("firstMinimalTestConf_Policentric");
		Writer outPolicentric = new FileWriter(fileNamePolicentric);
		pool.startProcessingAndFlushData(outPolicentric);
		
		List<AbstractSemanticSpread> list = pool.getCompletedSemanticSpreadList();
		String semantiSpreadClassName = PropertyUtil.getInstance().getProperty(PropertyUtil.SEMANTIC_SPREAD_LABEL);
		boolean condition = ( semantiSpreadClassName != null );
		for (AbstractSemanticSpread ss : list) {
			String originID = ss.getOrigin().getResource().getResourceID();
			String fileName = this.getFlushFileName("firstMinimalTestConf_"+originID);
			Writer out = new FileWriter(fileName);
			ss.flushData(out);			

			condition = condition && (ss.getClass().getName().equalsIgnoreCase(semantiSpreadClassName));				
		}

		System.getProperties().remove(PropertyUtil.CONFIG_FILE_LOCATION_LABEL);
		Assert.assertTrue(condition);				
	}

	@Test
	public void actualTestByComparingOverallActivationSpreadConf() throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InterruptedException, AbstractBedspreadException {
		KnowledgeBase kb = this.loadMinimalKB(1);
		Set<Node> resourceOriginSet = this.extractTrivialOriginSet();
		
		String testPropertyFile = "configTestPolicentricDefaultWeighConservative.properties";
		System.getProperties().setProperty(PropertyUtil.CONFIG_FILE_LOCATION_LABEL, testPropertyFile);
		PropertyUtilNoSingleton.getInstance();
		
		PolicentricSemanticSpread pool = new HT13PolicentricSemanticSpread(resourceOriginSet, kb);
		String fileNamePolicentric = this.getFlushFileName("actualTestByComparingOverallActivationSpreadConf_Policentric");
		Writer outPolicentric = new FileWriter(fileNamePolicentric);
		pool.startProcessingAndFlushData(outPolicentric);
		
		List<AbstractSemanticSpread> list = pool.getCompletedSemanticSpreadList();
		String semantiSpreadClassName = PropertyUtil.getInstance().getProperty(PropertyUtil.SEMANTIC_SPREAD_LABEL);
		boolean condition = ( semantiSpreadClassName != null );
		for (AbstractSemanticSpread ss : list) {
			String originID = ss.getOrigin().getResource().getResourceID();
			String fileName = this.getFlushFileName("actualTestByComparingOverallActivationSpreadConf_"+originID);
			Writer out = new FileWriter(fileName);
			ss.flushData(out);			

			condition = condition && (ss.getClass().getName().equalsIgnoreCase(semantiSpreadClassName));
			
			String loggerMsg = "Exploration Leaves from: " + ss.getOrigin().getResource().getResourceID() + " --> ";
			double score=0;
			for (Node n : ss.getExplorationLeaves()) {
				score += n.getScore();
				loggerMsg = loggerMsg + n.getResource().getResourceID() + ", ";
			}
			logger.info(loggerMsg);
			boolean equals = Math.abs(score - INITIAL_STIMULUS) <= STIMULUS_DELTA;
			condition = condition && equals;
//			System.out.println(score);
		}
		
		System.getProperties().remove(PropertyUtil.CONFIG_FILE_LOCATION_LABEL);
		Assert.assertTrue(condition);				
	}

	@Test
	public void actualTestByComparingOverallActivationSpreadConfAndFilteredNodes() throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InterruptedException, AbstractBedspreadException {
		Set<Node> resourceOriginSet = this.extractTrivialOriginSet();
		
		String testPropertyFile = "configTestPolicentricDefaultWeighConservativeAndFiltering.properties";
		System.getProperties().setProperty(PropertyUtil.CONFIG_FILE_LOCATION_LABEL, testPropertyFile);
		PropertyUtil prop = PropertyUtilNoSingleton.getInstance();
		
		KnowledgeBase kb = KBFactory.getInstance().getKnowledgeBase(prop);
		
		PolicentricSemanticSpread pool = new HT13PolicentricSemanticSpread(resourceOriginSet, kb);
		String fileNamePolicentric = this.getFlushFileName("actualTestByComparingOverallActivationSpreadConfAndFilteredNodes_Policentric");
		Writer outPolicentric = new FileWriter(fileNamePolicentric);
		pool.startProcessingAndFlushData(outPolicentric);
		
		List<AbstractSemanticSpread> list = pool.getCompletedSemanticSpreadList();
		String semantiSpreadClassName = PropertyUtil.getInstance().getProperty(PropertyUtil.SEMANTIC_SPREAD_LABEL);
		boolean condition = ( semantiSpreadClassName != null );
		for (AbstractSemanticSpread ss : list) {
			String originID = ss.getOrigin().getResource().getResourceID();
			String fileName = this.getFlushFileName("actualTestByComparingOverallActivationSpreadConfAndFilteredNodes_"+originID);
			Writer out = new FileWriter(fileName);
			ss.flushData(out);			

			condition = condition && (ss.getClass().getName().equalsIgnoreCase(semantiSpreadClassName));
			
			String loggerMsg = "Exploration Leaves from: " + ss.getOrigin().getResource().getResourceID() + " --> ";
			double score=0;
			for (Node n : ss.getExplorationLeaves()) {
				score += n.getScore();
				loggerMsg = loggerMsg + n.getResource().getResourceID() + ", ";
			}
			logger.info(loggerMsg);
			boolean equals = Math.abs(score - INITIAL_STIMULUS) <= STIMULUS_DELTA;
			condition = condition && equals;
//			System.out.println(score);
		}
		
		
		System.getProperties().remove(PropertyUtil.CONFIG_FILE_LOCATION_LABEL);
		if (condition){
			FileReader actualOutputReader = new FileReader(fileNamePolicentric);
			FileReader expectedOutputReader = new FileReader(EXPECTED_OUTPUT_FILE_ALFA);
			Assert.assertTrue(this.compareOutputFromLocalFile(actualOutputReader, expectedOutputReader));
		}else{
			Assert.assertTrue(false);
		}	
	}

	private boolean compareOutputFromLocalFile(Reader actualOutputReader, Reader expectedOutputReader) throws IOException {
		HashMap<String, Double> expectedValues = new HashMap<String, Double>();
		
		CSVReader reader = new CSVReader(expectedOutputReader);
		List<String[]> myEntries = reader.readAll();

		String resourceId;
		double score;
		
		for (String[] line : myEntries) {
			resourceId = line[0];
			score = Double.parseDouble(line[1]);
			
			expectedValues.put(resourceId, new Double(score));
		}
		reader.close();

		reader = new CSVReader(actualOutputReader);
		myEntries = reader.readAll();
		boolean valuesAreMatching = true;
		for (Iterator iterator = myEntries.iterator(); (iterator.hasNext()) && (valuesAreMatching);) {
			String[] line = (String[]) iterator.next();
			
			resourceId = line[0];
			score = Double.parseDouble(line[1]);

			valuesAreMatching = Math.abs(expectedValues.get(resourceId).doubleValue() - score) <= STIMULUS_DELTA;  
		}
		reader.close();
		
		return valuesAreMatching;
	}
	private Set<Node> extractTrivialOriginSet() {
		Set<Node> s = SetOfNodesFactory.getInstance().getSetOfNodesInstance();
		
		for(int i=0; i < NUMBER_OF_ORIGINS; i++){
			String originLabel = ORIGIN_PREFIX_LABEL + i;
			AnyResource resource = RDFFactory.getInstance().createBlankNode(originLabel);
			Node n = new Node(resource);
			s.add(n);
		}
		return s;
	}

	private KnowledgeBase loadMinimalKB(int i) throws IOException{
		String filename;
		switch (i) {
		case 0:
			filename = INPUT_GRAPH_FILE;
			break;
		case 1:
			filename = INPUT_TREE_FILE;			
			break;
		default:
			filename = INPUT_GRAPH_FILE;			
			break;
		}
		FileReader kbReader = new FileReader(filename);
		this.rdfGraph = new RDFGraph(kbReader);
		
		return this.rdfGraph;
	}

}
