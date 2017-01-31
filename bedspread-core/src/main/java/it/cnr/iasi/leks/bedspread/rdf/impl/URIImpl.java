package it.cnr.iasi.leks.bedspread.rdf.impl;

import it.cnr.iasi.leks.bedspread.rdf.URI;

public class URIImpl implements URI {

	private String id;
	
	public URIImpl (String id){
		this.id = id;
	}

	public String getResourceID() {
		return this.id;
	}

}
