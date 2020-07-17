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

import javax.swing.Icon;
import javax.swing.SwingUtilities;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.foo.fundation.ILogger;
import org.foo.shape.ISimpleShape;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Extends the <tt>ServiceTracker</tt> to create a tracker for
 * <tt>SimpleShape</tt> services. The tracker is responsible for listener for
 * the arrival/departure of <tt>SimpleShape</tt> services and informing the
 * application about the availability of shapes. This tracker forces all
 * notifications to be processed on the Swing event thread to avoid
 * synchronization and redraw issues.
 * 
 * @author Richard S. Hall, Karl Pauls, Stuart McCulloch, and David Savage
 * @author ogattaz (cohorte-technologies)
 * 
 * @see http://felix.apache.org/site/how-to-use-ipojo-annotations.html
 * @see //@see http://felix.apache.org/site/ipojo-reference-card.html
 */
@Component(name = "org.foo.paint.ShapeTracker-factory")
@Instantiate(name = "org.foo.paint.ShapeTracker")
public class ShapeTracker extends ServiceTracker<ISimpleShape, ISimpleShape> {

	/**
	 * Simple class used to process service notification handling on the Swing event
	 * thread.
	 **/
	private class ShapeRunnable implements Runnable {

		private final int pAction;
		private final ServiceReference<ISimpleShape> pServiceReference;
		private final ISimpleShape pShape;

		/**
		 * Constructs an object with the specified action, service reference, and
		 * service object for processing on the Swing event thread.
		 * 
		 * @param action            The type of action associated with the notification.
		 * @param aServiceReference The service reference of the corresponding service.
		 * @param aShape            The service object of the corresponding service.
		 **/
		public ShapeRunnable(int action, ServiceReference<ISimpleShape> aServiceReference, ISimpleShape aShape) {
			pAction = action;
			pServiceReference = aServiceReference;
			pShape = aShape;
		}

		/**
		 * Calls the <tt>processShape()</tt> method.
		 **/
		@Override
		public void run() {
			processShape(pAction, pServiceReference, pShape);
		}
	}

	// Flag indicating an added shape.
	private static final int ADDED = 1;

	// Flag indicating a modified shape.
	private static final int MODIFIED = 2;

	// Flag indicating a removed shape.
	private static final int REMOVED = 3;

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
	public ShapeTracker(BundleContext aBundleContext) {
		super(aBundleContext, ISimpleShape.class.getName(), null);
		pBundleContext = aBundleContext;
	}

	/**
	 * Overrides the <tt>ServiceTracker</tt> functionality to inform the application
	 * object about the added service.
	 * 
	 * @param aServiceReference The service reference of the added service.
	 * @return The service object to be used by the tracker.
	 **/
	@Override
	public ISimpleShape addingService(ServiceReference<ISimpleShape> aServiceReference) {

		ISimpleShape wShape = new DefaultShape(pBundleContext, aServiceReference);

		// SimpleShape wShape = pBundleContext.getService(ref);

		pLogger.logInfo(this, "addingService", "name=[%s]", aServiceReference.getProperty(ISimpleShape.NAME_PROPERTY));

		processShapeOnEventThread(ADDED, aServiceReference, wShape);
		return wShape;
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

		this.close();

		pLogger.logInfo(this, "invalidate");

	}

	/**
	 * Overrides the <tt>ServiceTracker</tt> functionality to inform the application
	 * object about the modified service.
	 * 
	 * @param aServiceReference The service reference of the modified service.
	 * @param aShape            The service object of the modified service.
	 **/
	@Override
	public void modifiedService(ServiceReference<ISimpleShape> aServiceReference, ISimpleShape aShape) {
		processShapeOnEventThread(MODIFIED, aServiceReference, aShape);
	}

	/**
	 * Actually performs the processing of the service notification. Invokes the
	 * appropriate callback method on the application object depending on the action
	 * type of the notification.
	 * 
	 * @param action            The type of action associated with the notification.
	 * @param aServiceReference The service reference of the corresponding service.
	 * @param aShape            The service object of the corresponding service.
	 **/
	private void processShape(int action, ServiceReference<ISimpleShape> aServiceReference, ISimpleShape aShape) {
		String name = (String) aServiceReference.getProperty(ISimpleShape.NAME_PROPERTY);

		switch (action) {
		case MODIFIED:
			pPaintMain.getPaintFrame().removeShape(name);
			// Purposely let this fall through to the 'add' case to
			// reload the service.

		case ADDED:
			Icon icon = aShape.getIcon();
			pPaintMain.getPaintFrame().addShape(name, icon, aShape);
			break;

		case REMOVED:
			pPaintMain.getPaintFrame().removeShape(name);
			break;
		}
	}

	/**
	 * Processes a received service notification from the <tt>ServiceTracker</tt>,
	 * forcing the processing of the notification onto the Swing event thread if it
	 * is not already on it.
	 * 
	 * @param action            The type of action associated with the notification.
	 * @param aServiceReference The service reference of the corresponding service.
	 * @param aShape            The service object of the corresponding service.
	 **/
	private void processShapeOnEventThread(int action, ServiceReference<ISimpleShape> aServiceReference,
			ISimpleShape aShape) {
		if ((pBundleContext.getBundle(0).getState() & (Bundle.STARTING | Bundle.ACTIVE)) == 0) {
			return;
		}

		try {
			if (SwingUtilities.isEventDispatchThread()) {
				processShape(action, aServiceReference, aShape);
			} else {
				SwingUtilities.invokeAndWait(new ShapeRunnable(action, aServiceReference, aShape));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Overrides the <tt>ServiceTracker</tt> functionality to inform the application
	 * object about the removed service.
	 * 
	 * @param aServiceReference The service reference of the removed service.
	 * @param aShape            The service object of the removed service.
	 **/
	@Override
	public void removedService(ServiceReference<ISimpleShape> aServiceReference, ISimpleShape aShape) {
		processShapeOnEventThread(REMOVED, aServiceReference, aShape);
		((DefaultShape) aShape).dispose();
	}

	/**
	 * 
	 */
	@Validate
	public void validate() {

		this.open();

		pLogger.logInfo(this, "validate");
	}
}
