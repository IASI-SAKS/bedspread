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
package it.cnr.iasi.leks.bedspread.rdf.impl;

import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.AnyURI;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;
import it.cnr.iasi.leks.bedspread.rdf.URI;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
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

	private HashMap<String, Set<RDFTriple>> subjectsMap;
	private HashMap<String, Set<RDFTriple>> objectsMap;
	private HashMap<String, Set<RDFTriple>> predicatesMap;
	
	private RDFFactory rdfFactory;
	private static final String TYPE_PREDICATE = "rdf:type";
	
	public RDFGraph() {
		this((Set<RDFTriple>) null);
	}

	public RDFGraph(RDFTriple triple) {
		this(new HashSet<RDFTriple>());
	}

	public RDFGraph(Set<RDFTriple> s) {
		this.rdfFactory = RDFFactory.getInstance();
		
		this.subjectsMap = new HashMap<String, Set<RDFTriple>>();
		this.objectsMap = new HashMap<String, Set<RDFTriple>>();
		this.predicatesMap = new HashMap<String, Set<RDFTriple>>();

		if (s != null) {
			for (RDFTriple rdfTriple : s) {
				String subject = rdfTriple.getTripleSubject().getResourceID();
				String object = rdfTriple.getTripleObject().getResourceID();
				String predicate = rdfTriple.getTriplePredicate().getResourceID();
				
				this.populateMap(this.subjectsMap, rdfTriple, subject);
				this.populateMap(this.objectsMap, rdfTriple, object);
				this.populateMap(this.predicatesMap, rdfTriple, predicate);

			}
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
			
			this.populateMap(this.subjectsMap, triple, subject.getResourceID());
			this.populateMap(this.objectsMap, triple, object.getResourceID());
			this.populateMap(this.predicatesMap, triple, predicate.getResourceID());
		}
		reader.close();
	}

	private void populateMap(HashMap<String, Set<RDFTriple>> map, RDFTriple rdfTriple, String term) {
		Set<RDFTriple> set = null;
		if (! map.containsKey(term)){
			set = Collections.synchronizedSet(new HashSet<RDFTriple>());
			map.put(term, set);
		}else{
			set = map.get(term);
		}
		set.add(rdfTriple);
	}

	public int degree(AnyResource resource) {
		int count = 0;
		String id = resource.getResourceID();
		Set<RDFTriple> s = this.subjectsMap.get(id);
		if (s != null){
			count = s.size();
		}
		s = this.objectsMap.get(id);
		if (s != null){
			count += s.size();
		}
		return count;
	}

	public int depth(AnyResource resource) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isMemberof(AnyResource resource) {
		String id = resource.getResourceID();
		if (this.subjectsMap.containsKey(id)){
			return true;
		}
		if (this.objectsMap.containsKey(id)){
			return true;
		}
		if (this.predicatesMap.containsKey(id)){
			return true;
		}
			
		return false;
	}

	public Set<AnyResource> getNeighborhood(AnyResource resource) {
		Set<AnyResource> outputSet = Collections.synchronizedSet(new HashSet<AnyResource>());		
		String id = resource.getResourceID();
		
		if (this.subjectsMap.containsKey(id)){
			for (RDFTriple triple : this.subjectsMap.get(id)) {
				if (! triple.getTriplePredicate().getResourceID().equalsIgnoreCase(TYPE_PREDICATE)){
					outputSet.add(triple.getTripleObject());
				}
			}
		}
		
		if (this.objectsMap.containsKey(id)){
			for (RDFTriple triple : this.objectsMap.get(id)) {
				if (! triple.getTriplePredicate().getResourceID().equalsIgnoreCase(TYPE_PREDICATE)){
					outputSet.add(triple.getTripleSubject());
				}
			}
		}

		return outputSet;
	}

	@Override
	public Set<AnyResource> getPredicatesBySubjectAndObject(AnyResource r1, AnyResource r2) {
		Set<AnyResource> result = new HashSet<AnyResource>();
		Set<RDFTriple> triplesBySubject = subjectsMap.get(r1.getResourceID());
		Set<RDFTriple> triplesByObject = objectsMap.get(r1.getResourceID());
		
		Set<String> preds = new HashSet<String>(); 
		
		for(RDFTriple t:triplesBySubject) 
			preds.add(t.getTriplePredicate().getResourceID());
		for(RDFTriple t:triplesByObject) 
			preds.add(t.getTriplePredicate().getResourceID());
			
		for(String p:preds) {
			URIImpl r = new URIImpl(p);
			result.add(r);
		}	
		
		return result;
	}

	@Override
	public int countAllTriples() {
		return subjectsMap.values().size();
	}

	@Override
	public int countTriplesByPredicate(AnyResource resource) {
		int result = 0;
		result = predicatesMap.get(resource.getResourceID()).size();
		return result;
	}

	@Override
	public int countTriplesBySubjectOrObject(AnyResource resource) {
		int result = 0; 
		
		Set<RDFTriple> triplesBySubject = subjectsMap.get(resource.getResourceID());
		Set<RDFTriple> triplesByObject = objectsMap.get(resource.getResourceID());
		
		result = triplesBySubject.size();
		for(RDFTriple t:triplesByObject) 
			if(!(triplesBySubject.contains(t)))
				result ++;
			
		return result;
	}

	@Override
	public int countTriplesByPredicateAndSubjectOrObject(AnyResource pred, AnyResource resource) {
		int result = 0;
		
		Set<RDFTriple> triplesByPredicates = objectsMap.get(resource.getResourceID());
		for(RDFTriple t:triplesByPredicates) 
			if((t.getTripleSubject().getResourceID().equals(resource.getResourceID())) ||
					(t.getTripleObject().getResourceID().equals(resource.getResourceID())))
				result ++;
		
		return result;
	}	
}
