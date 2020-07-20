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
 * INSTALLED = 0x00000001;
 * STARTED = 0x00000002;
 * STOPPED = 0x00000004;
 * UPDATED = 0x00000008;
 * UNINSTALLED = 0x00000010;
 * RESOLVED = 0x00000020;
 * UNRESOLVED = 0x00000040;
 * STARTING = 0x00000080;
 * STOPPING = 0x00000100;
 * LAZY_ACTIVATION = 0x00000200;
 * </pre>
 * 
 * @author isandlaTech
 * 
 */
public enum EBundleEventType {
	INSTALLED(0x00000001), LAZY_ACTIVATION(0x00000200), RESOLVED(0x00000020), STARTED(
			0x00000002), STARTING(0x00000080), STOPPED(0x00000004), STOPPING(
			0x00000100), UNINSTALLED(0x00000010), UNKNOWN(999999), UNRESOLVED(
			0x00000040), UPDATED(0x00000008);

	/**
	 * @param aType
	 * @return
	 * @throws EnumConstantNotPresentException
	 */
	public static EBundleEventType fromType(int aType)
			throws EnumConstantNotPresentException {

		for (EBundleEventType wServiceEventType : EBundleEventType.values()) {
			if (wServiceEventType.getType() == aType)
				return wServiceEventType;
		}
		throw new EnumConstantNotPresentException(EBundleEventType.class,
				String.format("type=%d", aType));
	}

	private final int pType;

	/**
	 * @param aType
	 */
	EBundleEventType(int aType) {
		pType = aType;
	}

	/**
	 * @return
	 */
	public int getType() {
		return pType;
	}

}
