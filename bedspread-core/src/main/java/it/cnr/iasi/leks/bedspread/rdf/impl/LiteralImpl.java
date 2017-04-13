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
package it.cnr.iasi.leks.bedspread.rdf.impl;

import it.cnr.iasi.leks.bedspread.rdf.AnyResource;
import it.cnr.iasi.leks.bedspread.rdf.Literal;

/**
 * 
 * @author gulyx
 *
 */
public class LiteralImpl implements Literal {

	private String id;
	
	public LiteralImpl (String id){
		this.id = id;
	}

	public String getResourceID() {
		return this.id;
	}

	@Override
	public boolean equals (Object obj){
		if (obj instanceof AnyResource) {
			AnyResource r = (AnyResource) obj;
			return this.equals(r);
		}	
		return false;		
	}

	@Override
	public boolean equals (AnyResource r){
		if (r instanceof LiteralImpl) {
			LiteralImpl literal = (LiteralImpl) r;
			String literalID = literal.getResourceID();
			String currentID = this.getResourceID();
			return currentID.equals(literalID);
		}	
		return false;		
	}
	
	@Override
	public int hashCode(){
		return this.getResourceID().hashCode();
	}
	
}
