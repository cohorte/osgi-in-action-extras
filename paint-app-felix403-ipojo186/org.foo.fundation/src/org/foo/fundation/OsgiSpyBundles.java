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

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

/**
 * This component puts himself as a BundleListener :
 * 
 * @author isandlaTech
 * @see http://felix.apache.org/site/how-to-use-ipojo-annotations.html#
 *      HowtouseiPOJOAnnotations-@Component
 * @see http://felix.apache.org/site/how-to-use-ipojo-annotations.html#
 *      HowtouseiPOJOAnnotations-@Instantiate
 */
@Component
@Instantiate
public class OsgiSpyBundles implements BundleListener {

	private final BundleContext pBundleContext;

	@Requires
	private ILogger pLogger;

	/**
	 * A POJO can receive its bundle context as an argument of its constructor
	 * 
	 * @param aBundleContext
	 * @see http://felix.apache.org/site/ipojo-faq.html#iPOJOFAQ-
	 *      InjectingthebundlecontextinaPOJO
	 */
	public OsgiSpyBundles(BundleContext aBundleContext) {
		super();
		pBundleContext = aBundleContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleListener#bundleChanged(org.osgi.framework.
	 * BundleEvent)
	 */
	@Override
	public void bundleChanged(BundleEvent aBundleEvent) {

		String wBundleEventTypeName = EBundleEventType.fromType(
				aBundleEvent.getType()).name();
		long wBundleId = aBundleEvent.getBundle().getBundleId();
		String wBundleName = aBundleEvent.getBundle().getSymbolicName();

		pLogger.logInfo(this, "bundleChanged",
				"Event=[%s] BundleId=[%d] BundleName=[%s]",
				wBundleEventTypeName, wBundleId, wBundleName);
	}

	/**
	 * @throws Exception
	 */
	@Invalidate
	public void invalidate() throws Exception {

		pBundleContext.removeBundleListener(this);
		pLogger.logInfo(this, "invalidate",
				"OsgiSpyBundles removed as BundleListener");
	}

	/**
	 * @throws Exception
	 */
	@Validate
	public void validate() throws Exception {

		pBundleContext.addBundleListener(this);

		pLogger.logInfo(this, "validate",
				"OsgiSpyBundles installed as BundleListener. [%s]",
				this.toString());

	}
}