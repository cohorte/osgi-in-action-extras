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

import org.osgi.framework.ServiceReference;

/**
 * @author isandlaTech
 * 
 */
public class UtilsOsgi {

	/**
	 * dumps the properties of a service reference
	 * 
	 * @param aServiceReference
	 * @return
	 */
	public static String dumpServiceProperties(
			ServiceReference<?> aServiceReference) {

		StringBuilder wSB = new StringBuilder();
		for (String wKey : aServiceReference.getPropertyKeys()) {
			wSB.append(String.format(" %s=[%s]", wKey, aServiceReference
					.getProperty(wKey).toString().replace('\n', '¤')));
		}
		return wSB.toString();
	}

}