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

import org.osgi.service.log.LogService;

/**
 * <pre>
 * LOG_ERROR = 1;
 * LOG_WARNING = 2;
 * LOG_INFO = 3;
 * LOG_DEBUG = 4;
 * </pre>
 * 
 * @author isandlaTech
 * 
 */
public enum ELogLevel {
	DEBUG(LogService.LOG_DEBUG), ERROR(LogService.LOG_ERROR), INFO(
			LogService.LOG_INFO), WARNING(LogService.LOG_WARNING);

	/**
	 * @param aType
	 * @return
	 * @throws EnumConstantNotPresentException
	 */
	public static ELogLevel fromValue(int aType)
			throws EnumConstantNotPresentException {

		for (ELogLevel wLogLevel : ELogLevel.values()) {
			if (wLogLevel.getValue() == aType)
				return wLogLevel;
		}
		throw new EnumConstantNotPresentException(ELogLevel.class,
				String.format("unknonw level=[%d]", aType));
	}

	private final String pLabel;
	private final int pValue;

	/**
	 * @param aValue
	 */
	ELogLevel(int aValue) {
		pLabel = UtilsString.strAdjustLeft(this.name(), 7, ' ');
		pValue = aValue;
	}

	/**
	 * @return
	 */
	public String getlabel() {
		return pLabel;
	}

	/**
	 * @return
	 */
	public int getValue() {
		return pValue;
	}
}
