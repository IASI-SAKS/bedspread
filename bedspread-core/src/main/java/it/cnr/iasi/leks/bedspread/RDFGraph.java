package it.cnr.iasi.leks.bedspread;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RDFGraph {
	
	private Set interalSet;
	
	public RDFGraph(){
		this((Set<RDFTriple>) null);
	}
	
	public RDFGraph(RDFTriple triple){
		this(new HashSet<RDFTriple>());
	}
	
	public RDFGraph(Set<RDFTriple> s){
		if ( s == null ){
			this.interalSet = Collections.synchronizedSet(new HashSet<RDFTriple>());
		}else{
			this.interalSet = Collections.synchronizedSet(s);			
		}
	}

}
