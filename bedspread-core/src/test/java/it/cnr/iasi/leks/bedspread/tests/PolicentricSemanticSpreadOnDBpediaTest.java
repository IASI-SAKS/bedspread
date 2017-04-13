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

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
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
import it.cnr.iasi.leks.bedspread.rdf.sparqlImpl.DBpediaKB;
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
	
	private static final String OUT_FILENAME_PREFIX = "firstMinimalTestConf_PolicentricDBpedia";
	
	@Test
	public void firstMinimalTestConf() throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InterruptedException, AbstractBedspreadException {
		KnowledgeBase kb = DBpediaKB.getInstance();
		Set<Node> resourceOriginSet = this.extractTrivialOriginSet();
		
		String testPropertyFile = "configTestPolicentricSemanticWeighting_IC_onDBpedia.properties";
		System.getProperties().setProperty(PropertyUtil.CONFIG_FILE_LOCATION_LABEL, testPropertyFile);
		PropertyUtilNoSingleton.getInstance();
		
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
		Assert.assertTrue(condition);				
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
