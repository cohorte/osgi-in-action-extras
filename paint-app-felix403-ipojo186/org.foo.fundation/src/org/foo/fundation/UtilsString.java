/*******************************************************************************
 * Copyright (c) 2013 www.isandlatech.com (www.isandlatech.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    thomas calmant (isandlaTech)
 *    olivier gattaz (isandlaTech)
 *******************************************************************************/
package org.foo.fundation;

/**
 * @author ogattaz (cohorte-technologies)
 * 
 */
public class UtilsString {

	public static final String EMPTY = "";

	/**
	 * @param aValue
	 * @param aLen
	 * @param aLeadingChar
	 * @return
	 */
	public static String strAdjustLeft(String aValue, final int aLen, final char aLeadingChar) {

		if (aValue == null) {
			aValue = EMPTY;
		}

		int wLen = aValue.length();
		if (wLen < aLen) {
			return aValue + strFromChar(aLeadingChar, aLen - wLen);
		} else if (wLen > aLen) {
			return aValue.substring(0, aLen);
		} else {
			return aValue;
		}
	}

	/**
	 * @param aValue
	 * @param aLen
	 * @return
	 */
	public static String strAdjustRight(long aValue, int aLen) {
		return strAdjustRight(String.valueOf(aValue), aLen, '0');
	}

	/**
	 * @param aValue
	 * @param aLen
	 * @return
	 */
	public static String strAdjustRight(String aValue, int aLen) {

		return strAdjustRight(aValue, aLen, ' ');
	}

	/**
	 * @param aValue
	 * @param aLen
	 * @param aLeadingChar
	 * @return
	 */
	public static String strAdjustRight(String aValue, int aLen, char aLeadingChar) {

		if (aValue == null) {
			aValue = EMPTY;
		}
		int wLen = aValue.length();
		if (wLen < aLen)
			return strFromChar(aLeadingChar, aLen - wLen) + aValue;
		else if (wLen > aLen)
			return aValue.substring(aValue.length() - aLen);
		else
			return aValue;
	}

	/**
	 * @param aChar
	 * @param aLen
	 * @return
	 */
	public static String strFromChar(char aChar, int aLen) {
		if (aLen < 1)
			return "";
		if (aLen == 1)
			return String.valueOf(aChar);
		char[] wBuffer = new char[aLen];
		for (int wI = 0; wI < aLen; wI++) {
			wBuffer[wI] = aChar;
		}
		return String.valueOf(wBuffer);
	}
}
