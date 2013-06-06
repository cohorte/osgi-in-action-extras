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
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

/**
 * This component puts himself as a ServiceListener :
 * 
 * @author isandlaTech
 * @see http://felix.apache.org/site/how-to-use-ipojo-annotations.html#
 *      HowtouseiPOJOAnnotations-@Component
 * @see http://felix.apache.org/site/how-to-use-ipojo-annotations.html#
 *      HowtouseiPOJOAnnotations-@Instantiate
 */
@Component
@Instantiate
public class OsgiSpyServices implements ServiceListener {

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
	public OsgiSpyServices(BundleContext aBundleContext) {
		super();
		pBundleContext = aBundleContext;
	}

	/**
	 * @throws Exception
	 */
	@Invalidate
	public void invalidate() throws Exception {

		pBundleContext.removeServiceListener(this);
		pLogger.logInfo(this, "invalidate",
				"OsgiSpyServices removed as ServiceListener");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.ServiceListener#serviceChanged(org.osgi.framework.
	 * ServiceEvent)
	 */
	@Override
	public void serviceChanged(ServiceEvent aServiceEvent) {

		String wServiceEventSource = aServiceEvent.getSource().toString();

		String wServiceEventTypeName = EServiceEventType.fromType(
				aServiceEvent.getType()).name();

		pLogger.logInfo(this, "serviceChanged",
				"Event=[%s] Source=[%s] Properties=[%s]",
				wServiceEventTypeName, wServiceEventSource, UtilsOsgi
						.dumpServiceProperties(aServiceEvent
								.getServiceReference()));
	}

	/**
	 * @throws Exception
	 */
	@Validate
	public void validate() throws Exception {

		pBundleContext.addServiceListener(this);

		pLogger.logInfo(this, "validate",
				"OsgiSpyServices installed as ServiceListener. [%s]",
				this.toString());

	}
}
