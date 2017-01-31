package it.cnr.iasi.leks.bedspread.rdf.impl;

import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RDFGraph implements KnowledgeBase{
	
	private Set rdfTriples;
	
	public RDFGraph(){
		this((Set<RDFTriple>) null);
	}
	
	public RDFGraph(RDFTriple triple){
		this(new HashSet<RDFTriple>());
	}
	
	public RDFGraph(Set<RDFTriple> s){
		if ( s == null ){
			this.rdfTriples = Collections.synchronizedSet(new HashSet<RDFTriple>());
		}else{
			this.rdfTriples = Collections.synchronizedSet(s);			
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

}
