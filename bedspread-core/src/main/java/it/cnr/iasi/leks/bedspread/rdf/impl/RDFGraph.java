/*
 * 	 This file is part of Bedspread, originally promoted and
 *	 developed at CNR-IASI. For more information visit:
 *	 https://github.com/IASI-LEKS/bedspread
 *	     
 *	 This is free software: you can redistribute it and/or modify
 *	 it under the terms of the GNU Lesser General Public License as 
 *	 published by the Free Software Foundation, either version 3 of the 
 *	 License, or (at your option) any later version.
 *	 
 *	 This software is distributed in the hope that it will be useful,
 *	 but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	 GNU Lesser General Public License for more details.
 * 
 *	 You should have received a copy of the GNU Lesser General Public License
 *	 along with this source.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.cnr.iasi.leks.bedspread.rdf.impl;

import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.AnyURI;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.rdf.URI;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.opencsv.CSVReader;

/**
 * 
 * @author gulyx
 *
 */
public class RDFGraph implements KnowledgeBase {

	private Set<RDFTriple> rdfTriples;
	private RDFFactory rdfFactory;

	public RDFGraph() {
		this((Set<RDFTriple>) null);
	}

	public RDFGraph(RDFTriple triple) {
		this(new HashSet<RDFTriple>());
	}

	public RDFGraph(Set<RDFTriple> s) {
		this.rdfFactory = RDFFactory.getInstance();

		if (s == null) {
			this.rdfTriples = Collections.synchronizedSet(new HashSet<RDFTriple>());
		} else {
			this.rdfTriples = Collections.synchronizedSet(s);
		}
	}

	public RDFGraph(Reader kbReader) throws IOException {
		this();

		CSVReader reader = new CSVReader(kbReader);
		List<String[]> myEntries = reader.readAll();

		AnyURI subject;
		URI predicate;
		AnyURI object;

		for (String[] line : myEntries) {
			subject = this.rdfFactory.createBlankNode(line[0]);
			predicate = this.rdfFactory.createURI(line[1]);
			object = this.rdfFactory.createBlankNode(line[2]);
			
			RDFTriple triple = new RDFTriple(subject, predicate, object);
			this.rdfTriples.add(triple);
		}
	}

	public int degree(AnyResource node) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int depth(AnyResource node) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int isMemberof(AnyResource node) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<AnyResource> getNeighborhood(AnyResource node) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Set<AnyResource> listOfResources (){
		Set<AnyResource> outputSet = Collections.synchronizedSet(new HashSet<AnyResource>());
		
		AnyResource item;
		for (RDFTriple triple : this.rdfTriples) {
			item = triple.getTripleSubject();
			outputSet.add(item);

			item = triple.getTripleObject();
			outputSet.add(item);			
		}
		return outputSet;
	}

}
