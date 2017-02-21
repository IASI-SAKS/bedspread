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
package it.cnr.iasi.leks.bedspread.exceptions;

/**
 * 
 * @author gulyx
 *
 */
public abstract class AbstractBedspreadException extends Exception {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6773211644270881856L;

	public AbstractBedspreadException() {
		super();
	}

	public AbstractBedspreadException(String message) {
		super(message);
	}

	public AbstractBedspreadException(String message, Throwable cause) {
		super(message, cause);
	}

	public AbstractBedspreadException(Throwable cause) {
		super(cause);
	}
}
