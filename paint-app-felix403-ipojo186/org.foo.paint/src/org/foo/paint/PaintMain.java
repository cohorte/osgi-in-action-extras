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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.foo.fundation.ILogger;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

/**
 * The activator of the host application bundle. The activator creates the main
 * application <tt>JFrame</tt> and starts tracking <tt>SimpleShape</tt>
 * services. All activity is performed on the Swing event thread to avoid
 * synchronization and repainting issues. Closing the application window will
 * result in <tt>Bundle.stop()</tt> being called on the system bundle, which
 * will cause the framework to shutdown and the JVM to exit.
 * 
 * @author Richard S. Hall, Karl Pauls, Stuart McCulloch, and David Savage
 * @author isandlaTech
 * 
 * @see http://felix.apache.org/site/how-to-use-ipojo-annotations.html
 * @see //@see http://felix.apache.org/site/ipojo-reference-card.html
 */
@Component
@Provides(specifications = { IPaintMain.class })
@Instantiate
public class PaintMain implements Runnable, IPaintMain {

	private final BundleContext pBundleContext;

	// @see http://felix.apache.org/site/ipojo-reference-card.html
	@Requires
	private final ILogger pLogger = null;

	private PaintFrame pPaintFrame = null;

	/**
	 * @param aBundleContext
	 */
	public PaintMain(BundleContext aBundleContext) {
		super();
		pBundleContext = aBundleContext;
	}

	/**
	 * @return
	 */
	BundleContext getContext() {
		return pBundleContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.foo.paint.IPaintMain#getPaintFrame()
	 */
	@Override
	public PaintFrame getPaintFrame() {
		return pPaintFrame;
	}

	/**
	 * Stops service tracking and disposes of the application window.
	 * 
	 **/
	@Invalidate
	public void invalidate() {

		final PaintFrame wPaintFrame = pPaintFrame;
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					pLogger.logInfo(this, "windowClosing",
							"set the frame invisible");
					wPaintFrame.setVisible(false);
					wPaintFrame.dispose();
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		pPaintFrame = null;

		pLogger.logInfo(this, "invalidate", "bundle=[%s]", getContext()
				.getBundle().getSymbolicName());

	}

	/**
	 * This method actually performs the creation of the application window. It
	 * is intended to be called by the Swing event thread and should not be
	 * called directly.
	 **/
	@Override
	public void run() {

		pLogger.logInfo(this, "run", "START");

		pPaintFrame = new PaintFrame(pLogger);

		pPaintFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		pPaintFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {

				pLogger.logInfo(this, "windowClosing", "stop the framework");

				try {
					getContext().getBundle(0).stop();
				} catch (BundleException ex) {
					ex.printStackTrace();
				}
			}
		});

		pPaintFrame.setVisible(true);
	}

	/**
	 * Displays the applications window and starts service tracking; everything
	 * is done on the Swing event thread to avoid synchronization and repainting
	 * issues.
	 * 
	 **/
	@Validate
	public void validate() {

		pLogger.logInfo(this, "validate",
				"Thread=[%s] isEventDispatchThread=[%b]", Thread
						.currentThread().getName(), SwingUtilities
						.isEventDispatchThread());

		if (SwingUtilities.isEventDispatchThread()) {
			run();
		} else {
			try {
				javax.swing.SwingUtilities.invokeAndWait(this);
			} catch (Throwable ex) {
				ex.printStackTrace();
			}
		}

		pLogger.logInfo(this, "validate", "bundle=[%s]", getContext()
				.getBundle().getSymbolicName());
	}
}
