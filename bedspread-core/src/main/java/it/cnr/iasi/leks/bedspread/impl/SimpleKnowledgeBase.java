package it.cnr.iasi.leks.bedspread.impl;

import java.util.List;

import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;

public class SimpleKnowledgeBase implements KnowledgeBase{

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
	
	private void thisIsFoo(){
		for (int i = 0; i < 10; i++) {
			System.out.println("This is foo");
		}
	}
	
}
