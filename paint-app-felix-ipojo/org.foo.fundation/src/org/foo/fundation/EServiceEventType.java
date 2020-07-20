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
 * REGISTERED = 0x00000001;
 * MODIFIED = 0x00000002;
 * UNREGISTERING = 0x00000004;
 * MODIFIED_ENDMATCH = 0x00000008;
 * </pre>
 * 
 * @author isandlaTech
 * 
 */
public enum EServiceEventType {
	MODIFIED(0x00000002), MODIFIED_ENDMATCH(0x00000008), REGISTERED(0x00000001), UNREGISTERING(
			0x00000004);

	/**
	 * @param aType
	 * @return
	 * @throws EnumConstantNotPresentException
	 */
	public static EServiceEventType fromType(int aType)
			throws EnumConstantNotPresentException {

		for (EServiceEventType wServiceEventType : EServiceEventType.values()) {
			if (wServiceEventType.getType() == aType)
				return wServiceEventType;
		}
		throw new EnumConstantNotPresentException(EServiceEventType.class,
				String.format("type=%d", aType));
	}

	private final int pType;

	/**
	 * @param aType
	 */
	EServiceEventType(int aType) {
		pType = aType;
	}

	/**
	 * @return
	 */
	public int getType() {
		return pType;
	}

}
