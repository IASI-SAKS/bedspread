package it.cnr.iasi.leks.bedspread.util;

import it.cnr.iasi.leks.bedspread.Node;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SetOfNodesFactory {

	protected static SetOfNodesFactory FACTORY = null;
	
	protected SetOfNodesFactory(){		
	}
	
	public static synchronized SetOfNodesFactory getInstance(){
		if (FACTORY == null){
			FACTORY = new SetOfNodesFactory();
		}
		return FACTORY;
	}
	
	public Set<Node> getSetOfNodesInstance() {
		Set<Node> s1 = this.getNaviteSetOfNodesInstance();
//		Set<Node> s1 = this.getOtherSetOfNodesInstance();
		return s1;
	}

	public Set<Node> getNaviteSetOfNodesInstance() {
		Set<Node> s1 = Collections.synchronizedSet(new HashSet<Node>());
		return s1;
	}

	public Set<Node> getOtherSetOfNodesInstance() {
		Set<Node> s1 = new RevisedHashSet();
		return s1;
	}
}
