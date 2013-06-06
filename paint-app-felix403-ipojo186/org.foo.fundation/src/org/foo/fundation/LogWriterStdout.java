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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;

/**
 * @author isandlaTech
 * 
 */
public class LogWriterStdout implements LogListener {

	// the width of the bundle column
	static final int LENGTH_BUNDLE = 20;

	private final static String PATTERN_TIMESTAMP = "DDD-hh:mm:ss.SSS";

	private final static SimpleDateFormat sTimeStampFormater = new SimpleDateFormat(
			PATTERN_TIMESTAMP);

	/**
	 * @param aTime
	 * @return a formated time stamp in a string "DDD-hh:mm:ss.SSS"
	 */
	public static String timeToFormatedTimeStamp(long aTime) {

		return sTimeStampFormater.format(new Date(aTime));
	}

	/**
	 * @param aLevel
	 * @return
	 */
	private String levelToStr(int aLevel) {

		return ELogLevel.fromValue(aLevel).getlabel();
	}

	/**
	 * @param aLevel
	 * @param aMessage
	 */
	public void logged(int aLevel, String aMessage) {
		logged(System.currentTimeMillis(), aLevel, "???", aMessage);

	}

	/*
	 * Invoked by the log service implementation for each log entry
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.service.log.LogListener#logged(org.osgi.service.log.LogEntry)
	 */
	@Override
	public void logged(LogEntry entry) {

		logged(entry.getTime(), entry.getLevel(), entry.getBundle()
				.getSymbolicName(), entry.getMessage());
	}

	/**
	 * @param aTime
	 * @param aLevel
	 * @param aMessage
	 */
	public void logged(long aTime, int aLevel, String aBundleName,
			String aMessage) {

		StringBuilder wSB = new StringBuilder();

		wSB.append(timeToFormatedTimeStamp(aTime));
		wSB.append(Logger.SEP_COLUMN_DELIM);
		wSB.append(UtilsString.strAdjustRight(aBundleName, LENGTH_BUNDLE));
		wSB.append(Logger.SEP_COLUMN_DELIM);
		wSB.append(levelToStr(aLevel));

		// to format the
		if (!aMessage.startsWith(Logger.SEP_COLUMN_DELIM)) {
			aMessage = Logger.buildLogLine(Thread.currentThread(), this, "*",
					aMessage);
		}
		wSB.append(aMessage);

		System.out.println(wSB.toString());
	}
}