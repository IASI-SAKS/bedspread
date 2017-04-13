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
import java.util.Iterator;

import it.cnr.iasi.leks.bedspread.AbstractSemanticSpread;
import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.ExecutionPolicy;
import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.exceptions.impl.InteractionProtocolViolationException;
import it.cnr.iasi.leks.bedspread.impl.HT13ConfSemanticSpread;
import it.cnr.iasi.leks.bedspread.impl.HT13ConfSemanticSpread_GreedyVariant;
import it.cnr.iasi.leks.bedspread.impl.policies.SimpleExecutionPolicy;
import it.cnr.iasi.leks.bedspread.impl.policies.SpreadingBound;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFFactory;
import it.cnr.iasi.leks.bedspread.rdf.sparqlImpl.DBpediaKB;
import it.cnr.iasi.leks.bedspread.tests.util.PropertyUtilNoSingleton;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;


/*
 * @author ftaglino
 */
public class SemanticSpreadOnDBpediaTest extends AbstractTest{

//	private static final String ORIGIN_LABEL = "http://dbpedia.org/resource/Innovation";
	private static final String ORIGIN_LABEL = "http://dbpedia.org/resource/Barack_Obama";
//    private static final String ORIGIN_LABEL = "http://dbpedia.org/resource/Gioia_dei_Marsi";
//    private static final String ORIGIN_LABEL = "http://dbpedia.org/resource/L'Aquila";

	@Ignore
	@Test
	public void testHT13ConfSemanticSpread() throws IOException, InteractionProtocolViolationException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		KnowledgeBase kb = DBpediaKB.getInstance();
		Node resourceOrigin = this.extractTrivialOrigin();
	
		ExecutionPolicy policy = new SimpleExecutionPolicy(2);
		
		String testPropertyFile = "configTestSemanticWeighting_IC_onDBpedia.properties";
		System.getProperties().put(PropertyUtil.CONFIG_FILE_LOCATION_LABEL, testPropertyFile);
		PropertyUtilNoSingleton.getInstance();
		
		AbstractSemanticSpread ss = new HT13ConfSemanticSpread(resourceOrigin,kb,policy);
		ss.run();
		
		String fileName = this.getFlushFileName("testHT13ConfSemanticSpread_onDBpedia_IC");
		Writer out = new FileWriter(fileName);
		ss.flushData(out);
				
		System.getProperties().remove(PropertyUtil.CONFIG_FILE_LOCATION_LABEL);

		Assert.assertTrue(true);		
	}
	
	@Ignore
	@Test
	public void testHT13GreedyConfSemanticSpread() throws IOException, InteractionProtocolViolationException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		KnowledgeBase kb = DBpediaKB.getInstance();
		Node resourceOrigin = this.extractTrivialOrigin();
	
//		ExecutionPolicy policy = new SimpleExecutionPolicy(2);
		ExecutionPolicy policy = new SpreadingBound(kb, 2);
		
		String testPropertyFile = "configTestSemanticWeighting_IC_onDBpedia.properties";
		System.getProperties().put(PropertyUtil.CONFIG_FILE_LOCATION_LABEL, testPropertyFile);
		PropertyUtilNoSingleton.getInstance();
		
		AbstractSemanticSpread ss = new HT13ConfSemanticSpread_GreedyVariant(resourceOrigin,kb,policy);
		ss.run();
		
		
		String fileName = this.getFlushFileName("testHT13ConfSemanticSpread_GreedyVariant_onDBpedia_IC");
		Writer out = new FileWriter(fileName);
		ss.flushData(out);
		
		boolean status = isAlwaysScoreLessThanOrigin(ss);
		
		System.getProperties().remove(PropertyUtil.CONFIG_FILE_LOCATION_LABEL);

		Assert.assertTrue(status);		
	}

	private boolean isAlwaysScoreLessThanOrigin(AbstractSemanticSpread ss) throws InteractionProtocolViolationException {
		Node resourceOrigin = ss.getOrigin();
		boolean status = true;
		for (Iterator iterator = ss.getSemanticSpreadForNode().iterator(); (iterator.hasNext()) && (status);) {
			Node n = (Node) iterator.next();
			if (! n.equals(resourceOrigin) ){
				status = n.getScore() < resourceOrigin.getScore();
			}			
		}
		return status;
	}

	private Node extractTrivialOrigin() {
		AnyResource resource = RDFFactory.getInstance().createURI(ORIGIN_LABEL);
		Node n = new Node(resource);
		return n;
	}
	
}
