/*
 * 	 This file is part of Bedspread, originally promoted and
 *	 developed at CNR-IASI. For more information visit:
 *	 https://github.com/IASI-LEKS/bedspread
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
package it.cnr.iasi.leks.bedspread.impl;

import java.lang.reflect.InvocationTargetException;

import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.TerminationPolicy;
import it.cnr.iasi.leks.bedspread.WeightingFunction;
import it.cnr.iasi.leks.bedspread.impl.weights.WeightingFunctionFactory;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;

/**
 * 
 * @author gulyx
 *
 */
public class SimpleSemanticWeightedSpread extends SimpleSemanticSpread {

	private WeightingFunction weightingModule; 
	
	public SimpleSemanticWeightedSpread(Node origin, KnowledgeBase kb) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		super(origin, kb);

		this.weightingModule = WeightingFunctionFactory.getInstance().getWeightingFunction(this.kb);
	}

	public SimpleSemanticWeightedSpread(Node origin, KnowledgeBase kb, TerminationPolicy term) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		super(origin, kb, term);

		this.weightingModule = WeightingFunctionFactory.getInstance().getWeightingFunction(this.kb);
	}

	public void updateWeightFunction(WeightingFunction weightingModule){
		this.weightingModule = weightingModule;
	}
	
	@Override
	protected double computeScore(Node spreadingNode, Node targetNode) {
		AnyResource resource = spreadingNode.getResource();
		int n = this.kb.getNeighborhood(resource).size();
		double score = spreadingNode.getScore();
		if (n != 0){
			double weight = this.weightingModule.weight(targetNode, this.getOrigin());
			score = weight * score/n;
		}
		return score;
	}

}
