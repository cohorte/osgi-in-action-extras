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
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;

/**
 * This component puts himself as a FrameworkListener :
 * 
 * @author isandlaTech
 * @see http://felix.apache.org/site/how-to-use-ipojo-annotations.html#
 *      HowtouseiPOJOAnnotations-@Component
 * @see http://felix.apache.org/site/how-to-use-ipojo-annotations.html#
 *      HowtouseiPOJOAnnotations-@Instantiate
 */
@Component
@Instantiate
public class OsgiSpyFramework implements FrameworkListener {

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
	public OsgiSpyFramework(BundleContext aBundleContext) {
		super();
		pBundleContext = aBundleContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.FrameworkListener#frameworkEvent(org.osgi.framework
	 * .FrameworkEvent)
	 */
	@Override
	public void frameworkEvent(FrameworkEvent aFrameworkEvent) {

		String wServiceEventSource = aFrameworkEvent.getSource().toString();

		String wServiceEventTypeName = EFrameworkEventType.fromType(
				aFrameworkEvent.getType()).name();

		pLogger.logInfo(this, "frameworkEvent", "Event=[%s] Source=[%s]",
				wServiceEventTypeName, wServiceEventSource);
	}

	/**
	 * @throws Exception
	 */
	@Invalidate
	public void invalidate() throws Exception {

		pBundleContext.removeFrameworkListener(this);

		pLogger.logInfo(this, "invalidate",
				"OsgiSpyFramework removed as FrameworkListener");
	}

	/**
	 * @throws Exception
	 */
	@Validate
	public void validate() throws Exception {

		pBundleContext.addFrameworkListener(this);

		pLogger.logInfo(this, "validate",
				"OsgiSpyFramework installed as FrameworkListener. [%s]",
				this.toString());
	}
}
