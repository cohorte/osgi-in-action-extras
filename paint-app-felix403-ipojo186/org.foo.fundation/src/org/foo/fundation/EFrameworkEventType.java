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
 * <pre>
 * STARTED = 0x00000001;
 * ERROR = 0x00000002;
 * PACKAGES_REFRESHED = 0x00000004;
 * STARTLEVEL_CHANGED = 0x00000008;
 * WARNING = 0x00000010;
 * INFO = 0x00000020;
 * STOPPED = 0x00000040;
 * STOPPED_UPDATE = 0x00000080;
 * STOPPED_BOOTCLASSPATH_MODIFIED = 0x00000100;
 * WAIT_TIMEDOUT = 0x00000200;
 * </pre>
 * 
 * @author isandlaTech
 * 
 */
public enum EFrameworkEventType {
	STARTED(0x00000001), ERROR(0x00000002), PACKAGES_REFRESHED(0x00000004), STARTLEVEL_CHANGED(
			0x00000008), WARNING(0x00000010), INFO(0x00000020), STOPPED(
			0x00000040), STOPPED_UPDATE(0x00000080), STOPPED_BOOTCLASSPATH_MODIFIED(
			0x00000100), WAIT_TIMEDOUT(0x00000200);

	/**
	 * @param aType
	 * @return
	 * @throws EnumConstantNotPresentException
	 */
	public static EFrameworkEventType fromType(int aType)
			throws EnumConstantNotPresentException {

		for (EFrameworkEventType wServiceEventType : EFrameworkEventType
				.values()) {
			if (wServiceEventType.getType() == aType)
				return wServiceEventType;
		}
		throw new EnumConstantNotPresentException(EFrameworkEventType.class,
				String.format("type=%d", aType));
	}

	private final int pType;

	/**
	 * @param aType
	 */
	EFrameworkEventType(int aType) {
		pType = aType;
	}

	/**
	 * @return
	 */
	public int getType() {
		return pType;
	}

}
