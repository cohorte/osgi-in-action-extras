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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.foo.fundation.ILogger;
import org.foo.shape.ISimpleShape;

/**
 * This class represents the main application class, which is a JFrame subclass
 * that manages a toolbar of shapes and a drawing canvas.
 * 
 * This class does not directly interact with the underlying OSGi framework;
 * instead, it is injected with the available <tt>SimpleShape</tt> instances to
 * eliminate any dependencies on the OSGi application programming interfaces.
 * 
 * @author Richard S. Hall, Karl Pauls, Stuart McCulloch, and David Savage
 * @author isandlaTech
 **/
public class PaintFrame extends JFrame implements MouseListener,
		MouseMotionListener {

	/**
	 * Simple action listener for shape tool bar buttons that sets the drawing
	 * frame's currently selected shape when receiving an action event.
	 **/
	private class ShapeActionListener implements ActionListener {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
		@Override
		public void actionPerformed(ActionEvent evt) {
			selectShape(evt.getActionCommand());
		}
	}

	private static final int BOX = 54;
	private static final long serialVersionUID = 1L;

	private final Map<String, ShapeInfo> m_shapes = new HashMap<String, ShapeInfo>();
	private final ISimpleShape pDefaultShape = new DefaultShape();
	// injected by the constructor
	private final ILogger pLogger;
	private final JPanel pPanelMain;
	private final ActionListener pReusableActionListener = new ShapeActionListener();

	private String pSelected;

	private ShapeComponent pSelectedComponent;

	private final JToolBar pToolbar;

	/**
	 * Default constructor that populates the main window.
	 **/
	public PaintFrame(ILogger aLogger) {
		super("Paint Frame");
		pLogger = aLogger;

		pToolbar = new JToolBar("Paint Toolbar");
		pPanelMain = new JPanel();
		pPanelMain.setBackground(Color.WHITE);
		pPanelMain.setLayout(null);
		pPanelMain.setMinimumSize(new Dimension(400, 400));
		pPanelMain.addMouseListener(this);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(pToolbar, BorderLayout.NORTH);
		getContentPane().add(pPanelMain, BorderLayout.CENTER);
		setSize(400, 400);
	}

	/**
	 * Injects an available <tt>SimpleShape</tt> into the drawing frame.
	 * 
	 * @param aName
	 *            The name of the injected <tt>SimpleShape</tt>.
	 * @param aIcon
	 *            The icon associated with the injected <tt>SimpleShape</tt>.
	 * @param aShape
	 *            The injected <tt>SimpleShape</tt> instance.
	 **/
	public void addShape(String aName, Icon aIcon, ISimpleShape aShape) {

		pLogger.logInfo(this, "addShape",
				"name=[%s] IconHeight=[%d] IconWidth=[%d]", aName,
				aIcon.getIconHeight(), aIcon.getIconWidth());

		m_shapes.put(aName, new ShapeInfo(aName, aIcon, aShape));
		JButton button = new JButton(aIcon);
		button.setActionCommand(aName);
		button.setToolTipText(aName);
		button.addActionListener(pReusableActionListener);

		if (pSelected == null) {
			button.doClick();
		}

		pToolbar.add(button);
		pToolbar.validate();
		repaint();
	}

	/**
	 * Retrieves the available <tt>SimpleShape</tt> associated with the given
	 * name.
	 * 
	 * @param aName
	 *            The name of the <tt>SimpleShape</tt> to retrieve.
	 * @return The corresponding <tt>SimpleShape</tt> instance if available or
	 *         <tt>null</tt>.
	 **/
	public ISimpleShape getShape(String aName) {
		ShapeInfo info = m_shapes.get(aName);
		if (info == null) {
			return pDefaultShape;
		}

		return info.getShape();
	}

	/**
	 * Implements method for the <tt>MouseListener</tt> interface to draw the
	 * selected shape into the drawing canvas.
	 * 
	 * @param aMouseEvent
	 *            The associated mouse event.
	 **/
	@Override
	public void mouseClicked(MouseEvent aMouseEvent) {
		if (pSelected == null) {
			return;
		}

		if (pPanelMain.contains(aMouseEvent.getX(), aMouseEvent.getY())) {
			ShapeComponent wShapeComponent = new ShapeComponent(this, pSelected);
			wShapeComponent.setBounds(aMouseEvent.getX() - BOX / 2,
					aMouseEvent.getY() - BOX / 2, BOX, BOX);
			pPanelMain.add(wShapeComponent, 0);
			pPanelMain.validate();
			pPanelMain.repaint(wShapeComponent.getBounds());
		}
	}

	/**
	 * Implements method for the <tt>MouseMotionListener</tt> interface to move
	 * a dragged shape.
	 * 
	 * @param aMouseEvent
	 *            The associated mouse event.
	 **/
	@Override
	public void mouseDragged(MouseEvent aMouseEvent) {
		pSelectedComponent.setBounds(aMouseEvent.getX() - BOX / 2,
				aMouseEvent.getY() - BOX / 2, BOX, BOX);
	}

	/**
	 * Implements an empty method for the <tt>MouseListener</tt> interface.
	 * 
	 * @param aMouseEvent
	 *            The associated mouse event.
	 **/
	@Override
	public void mouseEntered(MouseEvent aMouseEvent) {
	}

	/**
	 * Implements an empty method for the <tt>MouseListener</tt> interface.
	 * 
	 * @param aMouseEvent
	 *            The associated mouse event.
	 **/
	@Override
	public void mouseExited(MouseEvent aMouseEvent) {
	}

	/**
	 * Implements an empty method for the <tt>MouseMotionListener</tt>
	 * interface.
	 * 
	 * @param aMouseEvent
	 *            The associated mouse event.
	 **/
	@Override
	public void mouseMoved(MouseEvent aMouseEvent) {
	}

	/**
	 * Implements method for the <tt>MouseListener</tt> interface to initiate
	 * shape dragging.
	 * 
	 * @param aMouseEvent
	 *            The associated mouse event.
	 **/
	@Override
	public void mousePressed(MouseEvent aMouseEvent) {
		Component c = pPanelMain.getComponentAt(aMouseEvent.getPoint());
		if (c instanceof ShapeComponent) {
			pSelectedComponent = (ShapeComponent) c;
			pPanelMain
					.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			pPanelMain.addMouseMotionListener(this);
			pSelectedComponent.repaint();
		}
	}

	/**
	 * Implements method for the <tt>MouseListener</tt> interface to complete
	 * shape dragging.
	 * 
	 * @param aMouseEvent
	 *            The associated mouse event.
	 **/
	@Override
	public void mouseReleased(MouseEvent aMouseEvent) {
		if (pSelectedComponent != null) {
			pPanelMain.removeMouseMotionListener(this);
			pPanelMain.setCursor(Cursor
					.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			pSelectedComponent.setBounds(aMouseEvent.getX() - BOX / 2,
					aMouseEvent.getY() - BOX / 2, BOX, BOX);
			pSelectedComponent.repaint();
			pSelectedComponent = null;
		}
	}

	/**
	 * Removes a no longer available <tt>SimpleShape</tt> from the drawing
	 * frame.
	 * 
	 * @param aMouseEvent
	 *            The name of the <tt>SimpleShape</tt> to remove.
	 **/
	public void removeShape(String aMouseEvent) {
		m_shapes.remove(aMouseEvent);

		if ((pSelected != null) && pSelected.equals(aMouseEvent)) {
			pSelected = null;
		}

		for (int i = 0; i < pToolbar.getComponentCount(); i++) {
			JButton sb = (JButton) pToolbar.getComponent(i);
			if (sb.getActionCommand().equals(aMouseEvent)) {
				pToolbar.remove(i);
				pToolbar.invalidate();
				validate();
				repaint();
				break;
			}
		}

		if ((pSelected == null) && (pToolbar.getComponentCount() > 0)) {
			((JButton) pToolbar.getComponent(0)).doClick();
		}
	}

	/**
	 * This method sets the currently selected shape to be used for drawing on
	 * the canvas.
	 * 
	 * @param aName
	 *            The name of the shape to use for drawing on the canvas.
	 **/
	public void selectShape(String aName) {
		pSelected = aName;
	}
}
