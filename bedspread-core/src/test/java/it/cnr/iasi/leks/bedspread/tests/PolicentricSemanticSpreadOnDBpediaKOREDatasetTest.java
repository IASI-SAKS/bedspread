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

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.opencsv.CSVReader;

import it.cnr.iasi.leks.bedspread.AbstractSemanticSpread;
import it.cnr.iasi.leks.bedspread.PolicentricSemanticSpread;
import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.exceptions.AbstractBedspreadException;
import it.cnr.iasi.leks.bedspread.impl.HT13PolicentricSemanticSpread;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.rdf.impl.KBFactory;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFFactory;
import it.cnr.iasi.leks.bedspread.tests.util.PropertyUtilNoSingleton;
import it.cnr.iasi.leks.bedspread.util.SetOfNodesFactory;

public class PolicentricSemanticSpreadOnDBpediaKOREDatasetTest extends AbstractTest{
	
	private static final String BDPEDIA_PREFIX = "http://dbpedia.org/resource/";
	
	private static final char KORE_FILE_SEPARATOR = ';';
//	private static final int KORE_DATASET_MAX_ITEMS = 1;
	private static final int KORE_DATASET_MAX_ITEMS = 2;
	private static final int KORE_DATASET_MAX_ORIGINS = 4;
	private static final int KORE_DATASET_FIRST_ORIGIN_INDEX = 4;

//	private static final String INPUT_KORE_DATASET_FILE = "src/test/resources/datasets-KORE/0_KORE_modified _Apple.txt";
	private static final String INPUT_KORE_DATASET_FILE = "src/test/resources/datasets-KORE/1_KORE_modified _ITCompanies.txt";
//	private static final String INPUT_KORE_DATASET_FILE = "src/test/resources/datasets-KORE/2_KORE_modified _Hollywood.txt";
//	private static final String INPUT_KORE_DATASET_FILE = "src/test/resources/datasets-KORE/3_KORE_modified _VideoGames.txt";
//	private static final String INPUT_KORE_DATASET_FILE = "src/test/resources/datasets-KORE/4_KORE_modified _TelevisionSeries.txt";
//	private static final String INPUT_KORE_DATASET_FILE = "src/test/resources/datasets-KORE/5_KORE_modified _ChuckNorris.txt";

	private static HashMap<String, List<String>> KORE_DATASET_MAP;
	
	private static final String OUT_FILENAME_PREFIX = "firstMinimalTestConf_PolicentricDBpediaKORE";
	
	@BeforeClass
	public static void readKoreDataset () throws IOException{
		KORE_DATASET_MAP = new HashMap<String, List<String>>();
		
		FileReader datasetReader = new FileReader(INPUT_KORE_DATASET_FILE);

		CSVReader reader = new CSVReader(datasetReader,KORE_FILE_SEPARATOR);
		List<String[]> myEntries = reader.readAll();
		reader.close();

		String key;
		String value;

		for (String[] line : myEntries) {
			key = BDPEDIA_PREFIX + line[0];
			value = BDPEDIA_PREFIX + line[1];
			
			List<String> lst = KORE_DATASET_MAP.get(key);
			if (lst == null){
				lst = new Vector<String>();
				KORE_DATASET_MAP.put(key, lst);
			}
			lst.add(value);
		}		
	}
	
	@Ignore
	@Test
	public void firstMinimalTestConf() throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InterruptedException, AbstractBedspreadException {
		boolean condition = false;
		
		String testPropertyFile = "configTestPolicentricSemanticWeighting_IC_onDBpediaKORE.properties";
		System.getProperties().setProperty(PropertyUtil.CONFIG_FILE_LOCATION_LABEL, testPropertyFile);
		PropertyUtil prop = PropertyUtilNoSingleton.getInstance();

		KnowledgeBase kb = KBFactory.getInstance().getKnowledgeBase(prop);

		int nKeyItems = 0;
		Set<String> ketSet = KORE_DATASET_MAP.keySet();
		if (ketSet != null){
			for (Iterator<String> keysIterator = ketSet.iterator(); ((keysIterator.hasNext()) && (nKeyItems < KORE_DATASET_MAX_ITEMS)); nKeyItems++) {
				String key = (String) keysIterator.next();				
				Set<Node> resourceOriginSet = this.extractOriginSet(key);

				String outFileNamePrefixByKey = OUT_FILENAME_PREFIX+"_"+key.replaceFirst(BDPEDIA_PREFIX,"");
				
				PolicentricSemanticSpread pool = new HT13PolicentricSemanticSpread(resourceOriginSet, kb);
				String fileNamePolicentric = this.getFlushFileName(outFileNamePrefixByKey);
				Writer outPolicentric = new FileWriter(fileNamePolicentric);
				pool.startProcessingAndFlushData(outPolicentric);

				List<AbstractSemanticSpread> list = pool.getCompletedSemanticSpreadList();
				String semantiSpreadClassName = PropertyUtil.getInstance().getProperty(PropertyUtil.SEMANTIC_SPREAD_LABEL);

				condition = (semantiSpreadClassName != null);
				for (AbstractSemanticSpread ss : list) {
					String originFileSuffix = ss.getOrigin().getResource().getResourceID().replaceFirst(BDPEDIA_PREFIX,"");
					String fileName = this.getFlushFileName(outFileNamePrefixByKey + originFileSuffix);
					Writer out = new FileWriter(fileName);
					ss.flushData(out);

					condition = condition && (ss.getClass().getName().equalsIgnoreCase(semantiSpreadClassName));
				}
			}
		}
		
		System.getProperties().remove(PropertyUtil.CONFIG_FILE_LOCATION_LABEL);
		Assert.assertTrue(condition);				
	}

	private Set<Node> extractOriginSet(String key) {
		Set<Node> s = SetOfNodesFactory.getInstance().getSetOfNodesInstance();
		
		List<String> lst = KORE_DATASET_MAP.get(key);
		if (lst != null){

			
			for (int nItemsAsOrigin = KORE_DATASET_FIRST_ORIGIN_INDEX, finalIndex = KORE_DATASET_FIRST_ORIGIN_INDEX+KORE_DATASET_MAX_ORIGINS; ((nItemsAsOrigin < lst.size()) && (nItemsAsOrigin < finalIndex)); nItemsAsOrigin++) {
				String itemAsOrigin = lst.get(nItemsAsOrigin);
				AnyResource resource = RDFFactory.getInstance().createURI(itemAsOrigin);
				Node n = new Node(resource);
				s.add(n);				
			}			
		}
		
		return s;
	}
	
}
