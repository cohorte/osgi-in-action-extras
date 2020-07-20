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
package org.foo.shape.square;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.apache.felix.ipojo.annotations.Validate;
import org.foo.fundation.ILogger;
import org.foo.shape.ISimpleShape;

/**
 * @author Richard S. Hall, Karl Pauls, Stuart McCulloch, and David Savage
 * @author ogattaz (cohorte-technologies)
 * 
 * @see http://felix.apache.org/site/how-to-use-ipojo-annotations.html#
 *      HowtouseiPOJOAnnotations-@Component
 * @see http://felix.apache.org/site/how-to-use-ipojo-annotations.html#
 *      HowtouseiPOJOAnnotations-@Provides
 * @see http://felix.apache.org/site/how-to-use-ipojo-annotations.html#
 *      HowtouseiPOJOAnnotations-@Instantiate
 */
@Component(name = "org.foo.shape.square.Square-factory")
@Instantiate(name = "rg.foo.shape.square.Square")
@Provides(specifications = { ISimpleShape.class })
public class Square implements ISimpleShape {

	public static final String ICON_PNG = "square.png";
	public static final String SQUARE_NAME = "Square";

	private ImageIcon pIcon = null;

	@ServiceProperty(name = ISimpleShape.ICON_PROPERTY, value = ICON_PNG)
	private String pIconName;

	@Requires
	private ILogger pLogger;

	// @see
	// http://felix.apache.org/site/how-to-use-ipojo-annotations.html#HowtouseiPOJOAnnotations-@ServiceProperty
	@ServiceProperty(name = ISimpleShape.NAME_PROPERTY, value = SQUARE_NAME)
	private String pName;

	/**
	 * 
	 */
	public Square() {
		super();
	}

	/**
	 * Implements the <tt>SimpleShape.draw()</tt> method for painting the shape.
	 * 
	 * @param g2 The graphics object used for painting.
	 * @param p  The position to paint the triangle.
	 **/
	@Override
	public void draw(Graphics2D g2, Point p) {
		int x = p.x - 25;
		int y = p.y - 25;
		GradientPaint gradient = new GradientPaint(x, y, Color.BLUE, x + 50, y, Color.WHITE);
		g2.setPaint(gradient);
		g2.fill(new Rectangle2D.Double(x, y, 50, 50));
		BasicStroke wideStroke = new BasicStroke(2.0f);
		g2.setColor(Color.black);
		g2.setStroke(wideStroke);
		g2.draw(new Rectangle2D.Double(x, y, 50, 50));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.foo.shape.SimpleShape#getIcon()
	 */
	@Override
	public ImageIcon getIcon() {
		return pIcon;
	}

	/**
	 * 
	 */
	@Invalidate
	public void invalidate() {
		pName = null;
		pIcon = null;
		pLogger.logInfo(this, "invalidate");
	}

	/**
	 * 
	 */
	@Validate
	public void validate() {

		ResourceBundle wRB = ResourceBundle.getBundle("org.foo.shape.square.resource.Localize");

		pName = wRB.getString(RB_PROPERTY_SHAPE_NAME);

		pIcon = new ImageIcon(this.getClass().getResource(ICON_PNG));

		pLogger.logInfo(this, "validate", "name=[%s] IconHeight=[%d] IconWidth=[%d]", pName, pIcon.getIconHeight(),
				pIcon.getIconWidth());
	}
}
