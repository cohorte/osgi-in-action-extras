/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.foo.paint;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Unbind;
import org.apache.felix.ipojo.annotations.Validate;
import org.foo.fundation.ILogger;
import org.foo.shape.ISimpleShape;
import org.osgi.framework.BundleContext;

/**
 * Use the "Bind" and "Unbind" iPOJO annotations to listen the creation and the
 * removing of the ISimpleShape services
 * 
 * This class will replace the ShapeTracker
 * 
 * @author ogattaz (cohorte-technologies)
 * 
 * @see http://felix.apache.org/site/how-to-use-ipojo-annotations.html
 * @see //@see http://felix.apache.org/site/ipojo-reference-card.html
 */
@Component(name = "org.foo.paint.ShapeBinder-factory")
@Instantiate(name = "org.foo.paint.ShapeBinder")
public class ShapeBinder {

	// The bundle context used for tracking.
	private final BundleContext pBundleContext;

	// @see http://felix.apache.org/site/ipojo-reference-card.html
	@Requires
	private final ILogger pLogger = null;

	// @see http://felix.apache.org/site/ipojo-reference-card.html
	@Requires
	private IPaintMain pPaintMain;

	/**
	 * Constructs a tracker that uses the specified bundle context to track services
	 * and notifies the specified application object about changes.
	 * 
	 * @param aBundleContext The bundle context to be used by the tracker.
	 * @param frame          The application object to notify about service changes.
	 **/
	public ShapeBinder(BundleContext aBundleContext) {
		super();
		pBundleContext = aBundleContext;
	}

	/**
	 * Overrides the <tt>ServiceTracker</tt> functionality to inform the application
	 * object about the added service.
	 * 
	 * @param aServiceReference The service reference of the added service.
	 * @return The service object to be used by the tracker.
	 **/
	@Bind(aggregate = true, optional = true, specification = ISimpleShape.class)
	public void addService(ISimpleShape aSimpleShape) {

		pLogger.logInfo(this, "addService", "name=[%s]", aSimpleShape);
	}

	/**
	 * @return
	 */
	BundleContext getContext() {
		return pBundleContext;
	}

	/**
	 * Stops service tracking and disposes of the application window.
	 * 
	 **/
	@Invalidate
	public void invalidate() {

		pLogger.logInfo(this, "invalidate");
	}

	/**
	 * Overrides the <tt>ServiceTracker</tt> functionality to inform the application
	 * object about the removed service.
	 * 
	 * @param aServiceReference The service reference of the removed service.
	 * @param aShape            The service object of the removed service.
	 **/
	@Unbind(aggregate = true, optional = true, specification = ISimpleShape.class)
	public void removeShape(ISimpleShape aSimpleShapee) {
		pLogger.logInfo(this, "removeShape", "name=[%s]", aSimpleShapee);

	}

	/**
	 * 
	 */
	@Validate
	public void validate() {

		pLogger.logInfo(this, "validate");
	}
}
