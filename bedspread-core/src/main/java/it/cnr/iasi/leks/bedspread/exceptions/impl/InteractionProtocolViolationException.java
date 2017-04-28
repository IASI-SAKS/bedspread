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
package it.cnr.iasi.leks.bedspread.exceptions.impl;

import it.cnr.iasi.leks.bedspread.exceptions.AbstractBedspreadException;

public class InteractionProtocolViolationException extends AbstractBedspreadException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8993234614736446559L;
	
	public InteractionProtocolViolationException() {
		super();
	}

	public InteractionProtocolViolationException(String message) {
		super(message);
	}

	public InteractionProtocolViolationException(String message, Throwable cause) {
		super(message, cause);
	}

	public InteractionProtocolViolationException(Throwable cause) {
		super(cause);
	}
}
