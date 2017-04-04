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
package it.cnr.iasi.leks.bedspread.impl.policies;

import it.cnr.iasi.leks.bedspread.ExecutionPolicy;

/**
 * 
 * @author gulyx
 *
 */
public class SimpleExecutionPolicy extends ExecutionPolicy {

	private final int MAX_QUERIES_BEFORE_TRUE = 10;
	private int nQueries;
	
	public SimpleExecutionPolicy(){
		this.nQueries = MAX_QUERIES_BEFORE_TRUE;
	}
	
	public SimpleExecutionPolicy(int i){
		this.nQueries = i;
	}

	@Override
	public boolean terminationPolicyMet() {
		if (this.nQueries > 0){
			this.nQueries --;
			return false;
		}	
		return true;
	}

}
