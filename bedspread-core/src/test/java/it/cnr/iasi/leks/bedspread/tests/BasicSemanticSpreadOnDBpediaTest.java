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

import it.cnr.iasi.leks.bedspread.AbstractSemanticSpread;
import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.ExecutionPolicy;
import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.exceptions.impl.InteractionProtocolViolationException;
import it.cnr.iasi.leks.bedspread.impl.BasicSemanticSpread;
import it.cnr.iasi.leks.bedspread.impl.policies.SimpleTerminationPolicy;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFFactory;
import it.cnr.iasi.leks.bedspread.rdf.impl.RDFGraph;
import it.cnr.iasi.leks.bedspread.rdf.sparqlImpl.DBpediaKB;
import it.cnr.iasi.leks.debspread.tests.util.PropertyUtilNoSingleton;

import org.junit.Assert;
import org.junit.Test;

/*
 * @author gulyx
 */
public class BasicSemanticSpreadOnDBpediaTest extends AbstractTest{

	private RDFGraph rdfGraph;
private static final String ORIGIN_LABEL = "http://dbpedia.org/resource/Innovation";

	@Test
	public void testBasicSemanticSpread() throws IOException, InteractionProtocolViolationException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		KnowledgeBase kb = DBpediaKB.getInstance();
		Node resourceOrigin = this.extractTrivialOrigin();
	
		ExecutionPolicy policy = new SimpleTerminationPolicy(1);
		
		String testPropertyFile = "configTestSemanticWeighting_IC_onDBpedia.properties";
		System.getProperties().put(PropertyUtil.CONFIG_FILE_LOCATION_LABEL, testPropertyFile);
		PropertyUtilNoSingleton.getInstance();
		
		AbstractSemanticSpread ss = new BasicSemanticSpread(resourceOrigin,kb,policy);
		ss.run();
		
		String fileName = this.getFlushFileName("testBasicSemanticSpread_onDBpedia_IC");
		Writer out = new FileWriter(fileName);
		ss.flushData(out);
				
		System.getProperties().remove(PropertyUtil.CONFIG_FILE_LOCATION_LABEL);

		Assert.assertTrue(true);		
	}
		
	private Node extractTrivialOrigin() {
		AnyResource resource = RDFFactory.getInstance().createURI(ORIGIN_LABEL);
		Node n = new Node(resource);
		return n;
	}
	
}
