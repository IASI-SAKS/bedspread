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
import java.util.Set;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import it.cnr.iasi.leks.bedspread.AbstractSemanticSpread;
import it.cnr.iasi.leks.bedspread.PolicentricSemanticSpread;
import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.exceptions.AbstractBedspreadException;
import it.cnr.iasi.leks.bedspread.impl.HT13PolicentricSemanticSpread;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFFactory;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFGraph;
import it.cnr.iasi.leks.bedspread.util.SetOfNodesFactory;
import it.cnr.iasi.leks.debspread.tests.util.PropertyUtilNoSingleton;

public class PolicentricSemanticSpreadTest extends AbstractTest{
	
	private RDFGraph rdfGraph;
	private static final String ORIGIN_PREFIX_LABEL = "origin";
	private static final int NUMBER_OF_ORIGINS = 2;

	private static final String INPUT_GRAPH_FILE = "src/test/resources/whiteboardTwoOriginsRDFGraph.csv";
	private static final String INPUT_TREE_FILE = "src/test/resources/whiteboardTwoOriginsRDFTree.csv";
	
	
	@Test
	@Ignore
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
	public void actualTestByComparingOverllActivationSpreadConf() throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InterruptedException, AbstractBedspreadException {
		KnowledgeBase kb = this.loadMinimalKB(0);
		Set<Node> resourceOriginSet = this.extractTrivialOriginSet();
		
		String testPropertyFile = "configTestPolicentricDefaultWeighConservative.properties";
		System.getProperties().setProperty(PropertyUtil.CONFIG_FILE_LOCATION_LABEL, testPropertyFile);
		PropertyUtilNoSingleton.getInstance();
		
		PolicentricSemanticSpread pool = new HT13PolicentricSemanticSpread(resourceOriginSet, kb);
		String fileNamePolicentric = this.getFlushFileName("actualTestByComparingOverllActivationSpreadConf_Policentric");
		Writer outPolicentric = new FileWriter(fileNamePolicentric);
		pool.startProcessingAndFlushData(outPolicentric);
		
		List<AbstractSemanticSpread> list = pool.getCompletedSemanticSpreadList();
		String semantiSpreadClassName = PropertyUtil.getInstance().getProperty(PropertyUtil.SEMANTIC_SPREAD_LABEL);
		boolean condition = ( semantiSpreadClassName != null );
		for (AbstractSemanticSpread ss : list) {
			String originID = ss.getOrigin().getResource().getResourceID();
			String fileName = this.getFlushFileName("actualTestByComparingOverllActivationSpreadConf_"+originID);
			Writer out = new FileWriter(fileName);
			ss.flushData(out);			

			condition = condition && (ss.getClass().getName().equalsIgnoreCase(semantiSpreadClassName));
			
			double score=0;
			for (Node n : ss.getExporationLeaves()) {
				score += n.getScore();
				System.out.print(n.getResource().getResourceID()+", ");
			}
			System.out.println(score);
		}
		
		System.getProperties().remove(PropertyUtil.CONFIG_FILE_LOCATION_LABEL);
		Assert.assertTrue(condition);				
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
