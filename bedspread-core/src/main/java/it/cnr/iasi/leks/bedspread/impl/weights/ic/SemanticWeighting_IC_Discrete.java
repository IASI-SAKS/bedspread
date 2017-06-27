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
package it.cnr.iasi.leks.bedspread.impl.weights.ic;

import java.lang.reflect.InvocationTargetException;

import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.exceptions.impl.UndefinedPropertyException;
import it.cnr.iasi.leks.bedspread.exceptions.impl.UnexpectedValueException;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;

/**
 * 
 * @author gulyx
 *
 */
public class SemanticWeighting_IC_Discrete extends SemanticWeighting_IC {
	
	public SemanticWeighting_IC_Discrete(KnowledgeBase kb) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, UndefinedPropertyException {
		super(kb);
	}

	public SemanticWeighting_IC_Discrete(KnowledgeBase kb, Abstract_EdgeWeighting_IC ew) {
		super(kb, ew);
	}
	
	/**
	 * Compute the weight between adjacent nodes, as the maximum information content (IC) of the predicates linking n1 and n2,
	 * and power to ..... such a max value. 
	 * The method first finds the predicates from n1 to n2. For each predicate p of such predicates, builds the triple (or edge) <n1, p, n2> and computes the weight of the edge as its IC.
	 * Then the same is performed exchanging n1 and n2.
	 * The result is the maximum computed weight over all the built edges.
	 * The max is then classified as follow:
	 *  + [0-0.25] --> 0.25
	 *  + (0.25-0.50] --> 0.50
	 *  + (0.50-0.75] --> 0.75
	 *  + (0.75-1] --> 1
	 * The values range is [0, 1] 
	 * param n1
	 * param n2
	 * return
	 * @throws UnexpectedValueException 
	 */
	@Override
	public double weight(Node n1, Node n2) throws UnexpectedValueException {		
		double result = super.weight(n1, n2);
		double discreteResult = 0;
		
		if ((result >= 0) && (result <= 0.25)){
			discreteResult = 0.25;
		}else{
			if (result <= 0.5){
				discreteResult = 0.50;
			}else{
				if (result <= 0.75){
					discreteResult = 0.75;
				}else{
					discreteResult = 1.0;					
				}					
			}	
		}
		
		return discreteResult;
	}
	
}