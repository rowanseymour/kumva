/**
 * Copyright 2011 Rowan Seymour
 * 
 * This file is part of Kumva.
 *
 * Kumva is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kumva is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Kumva. If not, see <http://www.gnu.org/licenses/>.
 */

package com.ijuru.kumva.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

/**
 * General utility methods
 */
public class Utils {
	
	/**
	 * Converts a ISO-639 language code to a language name
	 * @param code the code
	 * @return the name
	 */
	public static String getLanguageName(String code) {
		return new Locale(code).getDisplayLanguage();
	}
	
	/**
	 * Makes a CSV string from a collection of objects
	 * @param vals the objects
	 * @return the CSV string
	 */
	public static String makeCSV(Collection<?> vals) {
		return StringUtils.join(vals, ", ");
	}
	
	/**
	 * Parses a CSV string into a list of strings, removing spaces and empty strings
	 * @param csv the string
	 * @return the list of strings
	 */
	public static List<String> parseCSV(String csv) {
		String[] vals = csv.split(",");
		List<String> strs = new ArrayList<String>();
		for (String val : vals) {
			String v = val.trim();
			if (v.length() > 0)
				strs.add(v);
		}
		return strs;
	}
	
	/**
	 * Parses a CSV string into a list of integers
	 * @param csv the string
	 * @return the list of integers
	 * @throws NumberFormatException if one of the numbers is invalid
	 */
	public static List<Integer> parseCSVIntegers(String csv) {
		String[] vals = csv.split(",");
		List<Integer> ints = new ArrayList<Integer>();
		for (String val : vals) {
			String v = val.trim();
			if (v.length() > 0) {
				Integer n = Integer.parseInt(v);
				if (n != null)
					ints.add(n);
			}
		}
		return ints;
	}
}
