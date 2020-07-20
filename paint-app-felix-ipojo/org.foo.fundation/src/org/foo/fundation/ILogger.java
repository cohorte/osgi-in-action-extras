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
 * @author isandlatech
 * 
 */
public interface ILogger {

	/**
	 * @return
	 */
	public boolean isLogDebugOn();

	/**
	 * @return
	 */
	public boolean isLogErrorOn();

	/**
	 * @param aLevel
	 * @return
	 */
	public boolean isLoggable(int aLevel);

	/**
	 * @return true if the logger accept
	 */
	public boolean isLogInfoOn();

	/**
	 * @return
	 */
	public boolean isLogWarningOn();

	/**
	 * @param aLevel
	 * @param aWho
	 * @param aWhat
	 * @param aLine
	 */
	public void log(int aLevel, Object aWho, CharSequence aWhat,
			Object... aInfos);

	/**
	 * @param aWho
	 * @param aWhat
	 * @param aInfos
	 */
	public void logDebug(Object aWho, CharSequence aWhat, Object... aInfos);

	/**
	 * @param aWho
	 * @param aWhat
	 * @param aInfos
	 */
	public void logInfo(Object aWho, CharSequence aWhat, Object... aInfos);

	/**
	 * @param aWho
	 * @param aWhat
	 * @param aInfos
	 */
	public void logSevere(Object aWho, CharSequence aWhat, Object... aInfos);

	/**
	 * @param aWho
	 * @param aWhat
	 * @param aInfos
	 */
	public void logWarn(Object aWho, CharSequence aWhat, Object... aInfos);

	/**
	 * @param aLevel
	 */
	public void setLogLevel(int aLevel);
}
