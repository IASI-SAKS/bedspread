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
package it.cnr.iasi.leks.bedspread.main;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import it.cnr.iasi.leks.bedspread.config.PropertyUtil;

public class Main {

	public static void main (String args[]) throws IllegalArgumentException, IllegalAccessException{
		System.err.println("This Entry Point has not been defined yet!!!");

		PropertyUtil prop = PropertyUtil.getInstance();
		for (Field f : PropertyUtil.class.getFields()) {
			int fieldModifiers = f.getModifiers();
			if (Modifier.isFinal(fieldModifiers) && Modifier.isPublic(fieldModifiers)){
				String msg = "";
				String labelName = f.getName();
				String labelValue = f.get(prop).toString();
				String configuredValue = prop.getProperty(labelValue);
				if (configuredValue != null){
					msg = ", Configured Value: \'" + configuredValue +"\'";
				}
				System.err.println("Label: \'" + labelName + "\', Value: \'"+ labelValue +"\'" + msg);
			}
		}		
	}
	
}
