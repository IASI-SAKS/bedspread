package it.cnr.iasi.leks.bedspread.rdf.impl;

import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.AnyURI;
import it.cnr.iasi.leks.bedspread.rdf.URI;

public class RDFTriple {

	private AnyURI tripleSubject;
	private URI triplePredicate;
	private AnyResource tripleObject;
	
	public RDFTriple (AnyURI tripleSubject, URI triplePredicate, AnyResource tripleObject){
		this.tripleSubject = tripleSubject;
		this.triplePredicate = triplePredicate;
		this.tripleObject = tripleObject;
	}
	
	
}
