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

import it.cnr.iasi.leks.bedspread.rdf.BlankNode;
import it.cnr.iasi.leks.bedspread.rdf.Literal;
import it.cnr.iasi.leks.bedspread.rdf.URI;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RDFFactory {

	private static SecureRandom RANDOM = new SecureRandom();
	
	protected static RDFFactory FACTORY = null; 
	
	protected  RDFFactory(){
	}
	   	
	private String randomId() {
    	return new BigInteger(130, RANDOM).toString(32);
    }

    public static synchronized RDFFactory getInstance(){
    	if (FACTORY == null){
    		FACTORY = new RDFFactory();
    	}
    	return FACTORY;
    }
	
	public BlankNode createBlankNode(){
		String randomID = this.randomId();
		return new BlankNodeImpl(randomID);
	}
	
	public BlankNode createBlankNode(String id){
		return new BlankNodeImpl(id);
	}
    
	public Literal createLiteral(){
		String randomID = this.randomId();
		return new LiteralImpl(randomID);
	}
	
	public Literal createLiteral(String id){
		return new LiteralImpl(id);
	}
    
	
	public URI createURI(){
		String randomID = this.randomId();
		return new URIImpl(randomID);
	}
	
	public URI createURI(String id){
		return new URIImpl(id);
	}
}
