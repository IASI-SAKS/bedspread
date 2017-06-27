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
public class SemanticWeighting_IC_Power extends SemanticWeighting_IC {
	
	private int power;
	
	public SemanticWeighting_IC_Power(KnowledgeBase kb) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, UndefinedPropertyException {
		super(kb);
		PropertyUtil p = PropertyUtil.getInstance();
		this.power = p.getProperty(PropertyUtil.WEIGHTING_FUNCTOIN_POWER_LABEL, 1);
	}

	public SemanticWeighting_IC_Power(KnowledgeBase kb, Abstract_EdgeWeighting_IC ew) {
		super(kb, ew);
	}
	
	/**
	 * Compute the weight between adjacent nodes, as the maximum information content (IC) of the predicates linking n1 and n2,
	 * and power to ..... such a max value. 
	 * The method first finds the predicates from n1 to n2. For each predicate p of such predicates, builds the triple (or edge) <n1, p, n2> and computes the weight of the edge as its IC.
	 * Then the same is performed exchanging n1 and n2.
	 * The result is the maximum computed weight over all the built edges.
	 * The max is then powered by 2 in order to enlarge differences in the computation.
	 * The values range is [0, 1] 
	 * param n1
	 * param n2
	 * return
	 * @throws UnexpectedValueException 
	 */
	@Override
	public double weight(Node n1, Node n2) throws UnexpectedValueException {
		if (this.power <= 0 ){
			String message = PropertyUtil.WEIGHTING_FUNCTOIN_POWER_LABEL + "found as: " + this.power + ", but it cannot be less-equal than 1";
			throw new UnexpectedValueException(message);
		}		
		
		double result = super.weight(n1, n2);
		double powerResult = this.power;
		for (int i = 0; i < this.power; i++) {
			powerResult = powerResult * result;
		}
		
		return powerResult;
	}

}