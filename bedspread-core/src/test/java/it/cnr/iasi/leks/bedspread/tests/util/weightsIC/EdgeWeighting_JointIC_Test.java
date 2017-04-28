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
package it.cnr.iasi.leks.bedspread.tests.util.weightsIC;

import it.cnr.iasi.leks.bedspread.impl.weights.ic.EdgeWeighting_JointIC;
import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.KnowledgeBase;

/**
 *  This class has been conceived strictly for testing purposes.
 *  NEVER RELY on it.
 *
 * @author gulyx
 *
 */
public class EdgeWeighting_JointIC_Test extends EdgeWeighting_JointIC {

	public EdgeWeighting_JointIC_Test(KnowledgeBase kb) {
		super(kb);
	}
	
	public double nodeProbabilityConditionalToPredicate(AnyResource pred, AnyResource node) {
		return super.nodeProbabilityConditionalToPredicate(pred, node);
	}

	public double nodeConditionalToPredicate_IC(AnyResource pred, AnyResource node) {
		return super.nodeConditionalToPredicate_IC(pred, node);
	}
	
}
