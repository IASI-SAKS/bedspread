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
package it.cnr.iasi.leks.bedspread;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import it.cnr.iasi.leks.bedspread.policies.TerminationPolicy;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;

/**
 * 
 * @author gulyx
 *
 */
public class PolicentricSemanticSpread {

	private Map<String, AbstractSemanticSpread> semanticSpreadMap;
	private SecureRandom random;
	
	public PolicentricSemanticSpread (Set<Node> originSet, KnowledgeBase kb, TerminationPolicy term) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		this.random = new SecureRandom();

		this.semanticSpreadMap = Collections.synchronizedMap(new HashMap<String, AbstractSemanticSpread>());
		
		for (Node origin : originSet) {
			AbstractSemanticSpread semSpread = SematicSpreadFactory.getInstance().getSemanticSpread(origin, kb, term);
			this.semanticSpreadMap.put(key, value)
		}
		
	}
	
	private String randomId() {
	    return new BigInteger(130, random).toString(32);
	}
	
}
