package it.cnr.iasi.leks.bedspread.rdf.impl;

import java.math.BigInteger;
import java.security.SecureRandom;

import it.cnr.iasi.leks.bedspread.rdf.BlankNode;

public class BlankNodeImpl implements BlankNode {

	private String id;
	
	public BlankNodeImpl (){
		this.id = this.randomId();
	}

	public BlankNodeImpl (String id){
		this.id = id;
	}

	public String getResourceID() {
		return this.id;
	}
	
    private String randomId() {
    	SecureRandom random = new SecureRandom();
    	return new BigInteger(130, random).toString(32);
    }

}
