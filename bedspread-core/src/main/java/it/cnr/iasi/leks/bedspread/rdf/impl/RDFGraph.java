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
