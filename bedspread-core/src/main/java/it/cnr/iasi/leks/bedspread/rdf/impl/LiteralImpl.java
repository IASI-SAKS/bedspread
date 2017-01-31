package it.cnr.iasi.leks.bedspread.rdf.impl;

import it.cnr.iasi.leks.bedspread.rdf.Literal;

public class LiteralImpl implements Literal {

	private String id;
	
	public LiteralImpl (String id){
		this.id = id;
	}

	public String getResourceID() {
		return this.id;
	}

}
