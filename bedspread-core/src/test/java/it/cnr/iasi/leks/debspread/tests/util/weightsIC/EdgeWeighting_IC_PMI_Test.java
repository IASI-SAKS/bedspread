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
package it.cnr.iasi.leks.debspread.tests.util.weightsIC;

import it.cnr.iasi.leks.bedspread.impl.weights.ic.EdgeWeighting_IC_PMI;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;

/**
 *  This class has been conceived strictly for testing purposes.
 *  NEVER RELY on it.
 *
 * @author gulyx
 *
 */
public class EdgeWeighting_IC_PMI_Test extends EdgeWeighting_IC_PMI{

	public EdgeWeighting_IC_PMI_Test(KnowledgeBase kb) {
		super(kb);
	}
	
	public double nodeProbability(AnyResource resource) {
		return super.nodeProbability(resource);
	}
	
	
	public double nodeAndPredicateProbability(AnyResource pred, AnyResource node) {
		return super.nodeAndPredicateProbability(pred, node);
	}

	public double pmi(AnyResource pred, AnyResource node) {
		return super.pmi(pred, node);
	}
	
}
