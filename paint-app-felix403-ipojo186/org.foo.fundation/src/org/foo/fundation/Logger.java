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

import java.util.Arrays;
import java.util.Dictionary;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Unbind;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.LogService;

/**
 * @author isandlaTech
 * @see http://felix.apache.org/site/how-to-use-ipojo-annotations.html#
 *      HowtouseiPOJOAnnotations-@Component
 * @see http://felix.apache.org/site/how-to-use-ipojo-annotations.html#
 *      HowtouseiPOJOAnnotations-@Provides
 * @see http://felix.apache.org/site/how-to-use-ipojo-annotations.html#
 *      HowtouseiPOJOAnnotations-@Instantiate
 */
@Component
@Provides
@Instantiate
public class Logger implements ILogger {

	// the name of the binding of array of EventAdmin service
	private static final String BINDIND_ID_LOGREADER = "LogReader";

	// the name of the binding of array of Http service
	private static final String BINDIND_ID_LOGSERVICE = "LogService";

	static String DUMMY_SHORT_HASHCODE = "0000";

	static final String EMPTY = "";

	// the width of the thread column
	static final int LENGTH_THREADNAME = 16;

	// the width of the what column
	static final int LENGTH_WHAT = 20;

	// the width of the who column
	static final int LENGTH_WHO = 25;

	static final String LIB_EMPTY = "empty";

	static final String LIB_NULL = "null";

	static final char REPLACE_COLUMN = '_';

	static final char SEP_COLUMN = '|';

	static final String SEP_COLUMN_DELIM = SEP_COLUMN + " ";

	/**
	 * @param aLevel
	 * @param aWho
	 * @param aWhat
	 * @param aInfos
	 */
	static String buildLogLine(final Thread aThread, final Object aWho,
			final CharSequence aWhat, final Object... aInfos) {

		String wLogText = buildLogText(aInfos);

		String wLogWho = buildWhoObjectId(aWho);

		String wLogWhat = (aWhat != null) ? aWhat.toString() : LIB_NULL;

		return formatLine(aThread.getName(), wLogWho, wLogWhat, wLogText);
	}

	/**
	 * @param aSB
	 *            a stringbuffer to be appended
	 * @param aObjects
	 *            a table of object
	 * @return the given StringBuffer
	 */
	static String buildLogText(final Object... aObjects) {

		if (aObjects == null || aObjects.length == 0) {
			return EMPTY;
		}

		StringBuilder wSB = new StringBuilder(128);

		// converts null of Thowable to strings
		Object wObj;
		for (int wI = 0; wI < aObjects.length; wI++) {
			wObj = aObjects[wI];
			if (wObj == null) {
				aObjects[wI] = LIB_NULL;
			} else if (wObj instanceof Throwable) {
				aObjects[wI] = formatThrowable((Throwable) wObj);
			} else if (aObjects[wI].getClass().isArray()) {
				aObjects[wI] = Arrays.toString((Object[]) wObj);
			}
		}

		// if there is only one info
		if (aObjects.length == 1) {
			return wSB.append(String.valueOf(aObjects[0])).toString();
		}

		// if the first object is a format, return the result of the
		// String.format() method
		if (aObjects[0].toString().indexOf('%') > -1) {
			return wSB.append(
					String.format(aObjects[0].toString(),
							UtilsArray.removeOneObject(aObjects, 0)))
					.toString();
		}

		// builds the text by appending the string value of each object.
		boolean wIsId = false;
		boolean wIsValue = false;
		String wStr;
		final int wMax = aObjects.length;
		for (int wI = 0; wI < wMax; wI++) {
			wIsValue = wIsId;
			wStr = String.valueOf(aObjects[wI]);
			wIsId = wStr.endsWith("=");

			if (wIsValue) {
				wSB.append('[');
			}

			wSB.append(wStr);

			if (wIsValue) {
				wSB.append(']');
			}
			if (!wIsId) {
				wSB.append(' ');
			}
		}
		return wSB.toString();
	}

	/**
	 * @param aWho
	 * @return
	 */
	static String buildWhoObjectId(final Object aWho) {

		if (aWho == null) {
			return LIB_NULL;
		}

		if (aWho instanceof Class) {
			return ((Class<?>) aWho).getName() + '_' + DUMMY_SHORT_HASHCODE;
		}

		return new StringBuffer().append(aWho.getClass().getName()).append('_')
				.append(UtilsString.strAdjustRight(aWho.hashCode(), 4))
				.toString();
	}

	/**
	 * @param aSourceClassName
	 * @param aSourceMethodName
	 * @param aText
	 * @return
	 */
	static String formatLine(final String aThreadName,
			final String aSourceClassName, final String aSourceMethodName,
			final String aText) {

		// clean the buffer
		StringBuilder wSB = new StringBuilder();

		wSB.append(SEP_COLUMN_DELIM);
		wSB.append(formatThreadName(aThreadName));

		wSB.append(SEP_COLUMN_DELIM);
		wSB.append(formatWho(aSourceClassName));

		wSB.append(SEP_COLUMN_DELIM);
		wSB.append(formatWhat(aSourceMethodName));

		wSB.append(SEP_COLUMN_DELIM);
		wSB.append(formatText(aText));

		return wSB.toString();
	}

	/**
	 * @param aText
	 * @return
	 */
	static String formatText(final String aText) {

		return (aText == null) ? LIB_NULL : aText;
	}

	/**
	 * @param aSB
	 * @param aThreadName
	 * @return
	 */
	static String formatThreadName(final String aThreadName) {

		return UtilsString.strAdjustRight(aThreadName, LENGTH_THREADNAME, ' ');
	}

	/**
	 * @param e
	 * @return
	 */
	static String formatThrowable(Throwable e) {
		// TODO
		return "todo ...";
	}

	/**
	 * @param aLevel
	 * @return
	 */
	static String formatWhat(final String aMethod) {

		return UtilsString.strAdjustRight(
				aMethod != null ? aMethod.replace(SEP_COLUMN, REPLACE_COLUMN)
						: EMPTY, LENGTH_WHAT, ' ');

	}

	/**
	 * @param aLevel
	 * @return
	 */
	static String formatWho(final String aWho) {

		return UtilsString
				.strAdjustRight(
						aWho != null ? aWho.replace(SEP_COLUMN, REPLACE_COLUMN)
								: EMPTY, LENGTH_WHO, ' ');

	}

	// The default level is INFO. The DEBUG logging is filtered.
	private int pCurrentLogLevel = LogService.LOG_INFO;

	// @see
	// http://felix.apache.org/site/how-to-use-ipojo-annotations.html#HowtouseiPOJOAnnotations-@Requires
	@Requires(id = BINDIND_ID_LOGREADER, optional = true)
	private final LogReaderService pLogReaderService = null;

	// @see
	// http://felix.apache.org/site/how-to-use-ipojo-annotations.html#HowtouseiPOJOAnnotations-@Requires
	@Requires(id = BINDIND_ID_LOGSERVICE, optional = true)
	private final LogService pLogService = null;

	private boolean pLogServiceAvailable;

	private final LogWriterStdout pLogWriterStdout = new LogWriterStdout();

	/**
	 * @param aLogReaderService
	 * @param properties
	 * @see http://felix.apache.org/site/how-to-use-ipojo-annotations.html#
	 *      HowtouseiPOJOAnnotations-@Bind
	 */
	@Bind(id = BINDIND_ID_LOGREADER)
	public void bindLogReaderService(LogReaderService aLogReaderService,
			Dictionary<?, ?> properties) {

		pLogReaderService.addLogListener(pLogWriterStdout);
		logInfo(this, "bindLogReaderService", "%s", properties);

	}

	/**
	 * @param aLogService
	 * @param properties
	 * @see http://felix.apache.org/site/how-to-use-ipojo-annotations.html#
	 *      HowtouseiPOJOAnnotations-@Bind
	 */
	@Bind(id = BINDIND_ID_LOGSERVICE)
	public void bindLogService(LogService aLogService,
			Dictionary<?, ?> properties) {

		setLogServiceAvailable(true);
		logInfo(this, "bindLogService", "%s", properties);
	}

	/**
	 * @throws Exception
	 * @see http://felix.apache.org/site/how-to-use-ipojo-annotations.html#
	 *      HowtouseiPOJOAnnotations-@Invalidate
	 */
	@Invalidate
	public void invalidate() throws Exception {

		logInfo(this, "invalidate", "isLogServiceAvailable=[%b]",
				isLogServiceAvailable());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.foo.fundation.ILogger#isLogDebugOn()
	 */
	@Override
	public boolean isLogDebugOn() {
		return isLoggable(LogService.LOG_DEBUG);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.foo.fundation.ILogger#isLogErrorOn()
	 */
	@Override
	public boolean isLogErrorOn() {
		return isLoggable(LogService.LOG_ERROR);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.foo.fundation.ILogger#isLoggable(int)
	 */
	@Override
	public boolean isLoggable(int aLevel) {

		return (aLevel <= pCurrentLogLevel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.foo.fundation.ILogger#isLogInfoOn()
	 */
	@Override
	public boolean isLogInfoOn() {
		return isLoggable(LogService.LOG_INFO);
	}

	/**
	 * @return
	 */
	private synchronized boolean isLogServiceAvailable() {
		return pLogServiceAvailable;
	}

	@Override
	public boolean isLogWarningOn() {
		return isLoggable(LogService.LOG_WARNING);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.foo.fundation.ILogger#log(int, java.lang.Object,
	 * java.lang.CharSequence, java.lang.Object[])
	 */
	@Override
	public void log(int aLevel, Object aWho, CharSequence aWhat,
			Object... aInfos) {

		if (isLogServiceAvailable())
			pLogService.log(aLevel,
					buildLogLine(Thread.currentThread(), aWho, aWhat, aInfos));
		else
			pLogWriterStdout.logged(aLevel,
					buildLogLine(Thread.currentThread(), aWho, aWhat, aInfos));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.foo.fundation.ILogger#logDebug(java.lang.Object,
	 * java.lang.CharSequence, java.lang.Object[])
	 */
	@Override
	public void logDebug(Object aWho, CharSequence aWhat, Object... aInfos) {

		log(LogService.LOG_DEBUG, aWho, aWhat, aInfos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.foo.fundation.ILogger#logInfo(java.lang.Object,
	 * java.lang.CharSequence, java.lang.Object[])
	 */
	@Override
	public void logInfo(Object aWho, CharSequence aWhat, Object... aInfos) {

		log(LogService.LOG_INFO, aWho, aWhat, aInfos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.foo.fundation.ILogger#logSevere(java.lang.Object,
	 * java.lang.CharSequence, java.lang.Object[])
	 */
	@Override
	public void logSevere(Object aWho, CharSequence aWhat, Object... aInfos) {

		log(LogService.LOG_ERROR, aWho, aWhat, aInfos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.foo.fundation.ILogger#logWarn(java.lang.Object,
	 * java.lang.CharSequence, java.lang.Object[])
	 */
	@Override
	public void logWarn(Object aWho, CharSequence aWhat, Object... aInfos) {

		log(LogService.LOG_WARNING, aWho, aWhat, aInfos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.foo.fundation.ILogger#setLogLevel(int)
	 */
	@Override
	public void setLogLevel(int aLevel) {

		pCurrentLogLevel = ELogLevel.fromValue(aLevel).getValue();
	}

	/**
	 * @param aAvailable
	 */
	private synchronized void setLogServiceAvailable(boolean aAvailable) {
		pLogServiceAvailable = aAvailable;
	}

	/**
	 * @param aHttpService
	 * @param properties
	 * @see http://felix.apache.org/site/how-to-use-ipojo-annotations.html#
	 *      HowtouseiPOJOAnnotations-@Unbind
	 */
	@Unbind(id = BINDIND_ID_LOGREADER)
	public void unbindLogReaderService(LogReaderService aLogReaderService,
			Dictionary<?, ?> properties) {

		logInfo(this, "unbindLogReaderService", "%s", properties);
		aLogReaderService.removeLogListener(pLogWriterStdout);
		setLogServiceAvailable(false);
	}

	/**
	 * @param aLogReaderService
	 * @param properties
	 * @see http://felix.apache.org/site/how-to-use-ipojo-annotations.html#
	 *      HowtouseiPOJOAnnotations-@Unbind
	 */
	@Unbind(id = BINDIND_ID_LOGSERVICE)
	public void unbindLogService(LogService aLogService,
			Dictionary<?, ?> properties) {

		logInfo(this, "unbindLogService", "%s", properties);
		setLogServiceAvailable(false);
	}

	/**
	 * @throws Exception
	 * @see http://felix.apache.org/site/how-to-use-ipojo-annotations.html#
	 *      HowtouseiPOJOAnnotations-@Validate
	 */
	@Validate
	public void validate() throws Exception {

		logInfo(this, "validate", "isLogServiceAvailable=[%b]",
				isLogServiceAvailable());
	}
}