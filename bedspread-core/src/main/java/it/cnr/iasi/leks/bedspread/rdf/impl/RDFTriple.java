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
import it.cnr.iasi.leks.bedspread.rdf.URI;

/**
 * 
 * @author gulyx
 *
 */
public class RDFTriple {

	private AnyURI tripleSubject;
	private URI triplePredicate;
	private AnyResource tripleObject;
	
	public RDFTriple (AnyURI tripleSubject, URI triplePredicate, AnyResource tripleObject){
		this.tripleSubject = tripleSubject;
		this.triplePredicate = triplePredicate;
		this.tripleObject = tripleObject;
	}
	
	public AnyURI getTripleSubject() {
		return this.tripleSubject;
	}

	public URI getTriplePredicate() {
		return this.triplePredicate;
	}

	public AnyResource getTripleObject() {
		return this.tripleObject;
	}

/*
 * Note that this method changed.
 * Now the implementation strictly relay on the override of "equals" that any implementation
 * of AnyResource must provide.
 * 
 * @see	it.cnr.iasi.leks.bedspread.rdf.URIImpl
 * @see	it.cnr.iasi.leks.bedspread.rdf.BlankNodeImpl
 * @see	it.cnr.iasi.leks.bedspread.rdf.LiteralImpl
 * 
 */
	public boolean equals (RDFTriple t){

		boolean sameSubject = (this.tripleSubject.equals(t.tripleSubject)); 
		boolean sameObject = (this.tripleObject.equals(t.tripleObject)); 
		boolean samePredicate = (this.triplePredicate.equals(t.triplePredicate)); 

		return ( sameSubject && sameObject && samePredicate );
	}
	
	/*
	 * Note that this method changed.
	 * Now the implementation strictly relay on the override of "equals" that any implementation
	 * of AnyResource must provide.
	 * 
	 * @see	it.cnr.iasi.leks.bedspread.rdf.URIImpl
	 * @see	it.cnr.iasi.leks.bedspread.rdf.BlankNodeImpl
	 * @see	it.cnr.iasi.leks.bedspread.rdf.LiteralImpl
	 * 
	 */
	@Override
	public boolean equals (Object obj){
		if (obj instanceof RDFTriple) {
			RDFTriple triple = (RDFTriple) obj;
			return this.equals(triple);
		}	
		return false;		
	}
	
	/*
	 * Note that this method changed.
	 * Now the implementation strictly relay on the override of "hashCode" that any implementation
	 * of AnyResource must provide.
	 * 
	 * @see	it.cnr.iasi.leks.bedspread.rdf.URIImpl
	 * @see	it.cnr.iasi.leks.bedspread.rdf.BlankNodeImpl
	 * @see	it.cnr.iasi.leks.bedspread.rdf.LiteralImpl
	 * 
	 */
	@Override
	public int hashCode(){		
		int newHash = this.tripleSubject.hashCode() + this.tripleObject.hashCode() + this.triplePredicate.hashCode(); 
		
		return newHash;
	}

	@Override
	public String toString(){
		return 	"<" 
				+this.tripleSubject.getResourceID() + ", "
				+this.triplePredicate.getResourceID() +", "
				+this.tripleObject.getResourceID()
				+">";
	}
}
