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

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;

import com.opencsv.CSVWriter;

import it.cnr.iasi.leks.bedspread.AbstractSemanticSpread;
import it.cnr.iasi.leks.bedspread.ComputationStatus;
import it.cnr.iasi.leks.bedspread.Node;
import it.cnr.iasi.leks.bedspread.ExecutionPolicy;
import it.cnr.iasi.leks.bedspread.WeightingFunction;
import it.cnr.iasi.leks.bedspread.config.PropertyUtil;
import it.cnr.iasi.leks.bedspread.exceptions.impl.InteractionProtocolViolationException;
import it.cnr.iasi.leks.bedspread.impl.weights.WeightingFunctionFactory;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;

/**
 * 
 * @author ftaglino
 * 
 * The implemented spreading strategy is the following.
 * Given a spreadingNode and a targetNode, the score of the latter is computed by 
 * multiplying the score of the spreadingNode by the greatest weight among the weights 
 * of the arcs existing between the spreadingNode and the targetNode.     
 * 
 * The rest of the strategy is implemented by the AbstractSemanticSpread class.
 *
 */
public class BasicSemanticSpread extends AbstractSemanticSpread {

	private WeightingFunction weightingModule; 
	
	public BasicSemanticSpread(Node origin, KnowledgeBase kb) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		super(origin, kb);
		this.weightingModule = WeightingFunctionFactory.getInstance().getWeightingFunction(this.kb);		
	}

	public BasicSemanticSpread(Node origin, KnowledgeBase kb, ExecutionPolicy policy) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		super(origin, kb, policy);
		this.weightingModule = WeightingFunctionFactory.getInstance().getWeightingFunction(this.kb);
	}

	@Override
	protected double computeScore(Node spreadingNode, Node targetNode) {		
		double score = spreadingNode.getScore();

		score = score * this.weightingModule.weight(targetNode, spreadingNode);
		return score;
	}

	@Override
	protected void filterCurrenltyActiveNode() {
		// TODO  IT DOES NOTHING FOR THE MOMENT!!
	}

	@Override
	public void flushData(Writer out) throws IOException, InteractionProtocolViolationException {
		if (this.getStatus() != ComputationStatus.Completed){
			InteractionProtocolViolationException ex = new InteractionProtocolViolationException(PropertyUtil.INTERACTION_PROTOCOL_ERROR_MESSAGE);
			throw ex;
		}
		
		CSVWriter writer = new CSVWriter(out);
	    String[] csvEntry = new String[2];
	     
	    for (Node n : this.getActiveNodes()) {
	    	 csvEntry[0] = n.getResource().getResourceID();
	    	 csvEntry[1] = String.valueOf(n.getScore());
		     writer.writeNext(csvEntry);
	    }
	     
	    writer.close();
	}

}
