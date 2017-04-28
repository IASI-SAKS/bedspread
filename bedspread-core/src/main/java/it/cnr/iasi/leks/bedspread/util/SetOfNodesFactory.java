/*
 * 	 This file is part of Bedspread, originally promoted and
 *	 developed at CNR-IASI. For more information visit:
 *	 http://leks.iasi.cnr.it/tools/bedspread
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
package it.cnr.iasi.leks.bedspread.util;

import it.cnr.iasi.leks.bedspread.Node;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author gulyx
 *
 */
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
