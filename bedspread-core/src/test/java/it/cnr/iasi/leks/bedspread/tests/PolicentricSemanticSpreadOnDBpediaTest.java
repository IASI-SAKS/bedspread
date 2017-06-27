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
package it.cnr.iasi.leks.bedspread.tests;

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
import it.cnr.iasi.leks.bedspread.exceptions.impl.InteractionProtocolViolationException;
import it.cnr.iasi.leks.bedspread.impl.HT13PolicentricSemanticSpread;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.rdf.impl.KBFactory;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFFactory;
import it.cnr.iasi.leks.bedspread.tests.util.PropertyUtilNoSingleton;
import it.cnr.iasi.leks.bedspread.util.SetOfNodesFactory;

public class PolicentricSemanticSpreadOnDBpediaTest extends AbstractTest{
	
	private static final String BDPEDIA_PREFIX = "http://dbpedia.org/resource/";
	
//	private static final String[] ORIGIN_LABEL_SET = {BDPEDIA_PREFIX+"Barack_Obama",
//													  BDPEDIA_PREFIX+"Innovation",
//													  BDPEDIA_PREFIX+"Gioia_dei_Marsi",
//													  BDPEDIA_PREFIX+"L'Aquila"};
	
	private static final String[] ORIGIN_LABEL_SET = {BDPEDIA_PREFIX+"Ignazio_Silone",
			  										  BDPEDIA_PREFIX+"L'Aquila"};	

//	private static final String[] ORIGIN_LABEL_SET = {BDPEDIA_PREFIX+"Gioia_dei_Marsi"};	
	
//	private static final String[] ORIGIN_LABEL_SET = {BDPEDIA_PREFIX+"Diego_Maradona",
//			  										  BDPEDIA_PREFIX+"Fidel_Castro",
//			  										  BDPEDIA_PREFIX+"Pope_Francis",};	

	private static final String OUT_FILENAME_PREFIX = "firstMinimalTestConf_PolicentricDBpedia";
	
	@Ignore
	@Test
	public void firstMinimalTestConf() throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InterruptedException, AbstractBedspreadException {
		String testPropertyFile = "configTestPolicentricSemanticWeighting_IC_onDBpedia.properties";
		boolean condition = executeMinimalTest(testPropertyFile);

		Assert.assertTrue(condition);				
	}

	@Ignore
	@Test
	public void discreteTestConf() throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InterruptedException, AbstractBedspreadException {
		String testPropertyFile = "configTestPolicentricSemanticWeighting_IC_Discrete_onDBpedia.properties";
		boolean condition = executeMinimalTest(testPropertyFile);

		Assert.assertTrue(condition);				
	}

	@Ignore
	@Test
	public void power2TestConf() throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InterruptedException, AbstractBedspreadException {
		String testPropertyFile = "configTestPolicentricSemanticWeighting_IC_Power2_onDBpedia.properties";
		boolean condition = executeMinimalTest(testPropertyFile);

		Assert.assertTrue(condition);				
	}

	private boolean executeMinimalTest(String testPropertyFile) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, NoSuchMethodException, InvocationTargetException, InteractionProtocolViolationException, AbstractBedspreadException {
		Set<Node> resourceOriginSet = this.extractTrivialOriginSet();
		
		System.getProperties().setProperty(PropertyUtil.CONFIG_FILE_LOCATION_LABEL, testPropertyFile);
		PropertyUtil prop = PropertyUtilNoSingleton.getInstance();
		
		KnowledgeBase kb = KBFactory.getInstance().getKnowledgeBase(prop);

		PolicentricSemanticSpread pool = new HT13PolicentricSemanticSpread(resourceOriginSet, kb);
		String fileNamePolicentric = this.getFlushFileName(OUT_FILENAME_PREFIX);
		Writer outPolicentric = new FileWriter(fileNamePolicentric);
		pool.startProcessingAndFlushData(outPolicentric);
		
		List<AbstractSemanticSpread> list = pool.getCompletedSemanticSpreadList();
		String semantiSpreadClassName = PropertyUtil.getInstance().getProperty(PropertyUtil.SEMANTIC_SPREAD_LABEL);
		boolean condition = ( semantiSpreadClassName != null );
		for (AbstractSemanticSpread ss : list) {
			String originFileSuffix = ss.getOrigin().getResource().getResourceID().replaceFirst(BDPEDIA_PREFIX, "");
			String fileName = this.getFlushFileName(OUT_FILENAME_PREFIX+originFileSuffix);
			Writer out = new FileWriter(fileName);
			ss.flushData(out);			

			condition = condition && (ss.getClass().getName().equalsIgnoreCase(semantiSpreadClassName));				
		}

		System.getProperties().remove(PropertyUtil.CONFIG_FILE_LOCATION_LABEL);

		return condition;
	}

	private Set<Node> extractTrivialOriginSet() {
		Set<Node> s = SetOfNodesFactory.getInstance().getSetOfNodesInstance();
		
		for(int i=0; i < ORIGIN_LABEL_SET.length; i++){
			String originLabel = ORIGIN_LABEL_SET[i];
			AnyResource resource = RDFFactory.getInstance().createURI(originLabel);
			Node n = new Node(resource);
			s.add(n);
		}
		return s;
	}
	
}
