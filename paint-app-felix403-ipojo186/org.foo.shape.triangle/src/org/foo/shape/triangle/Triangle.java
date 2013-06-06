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
package org.foo.shape.triangle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.GeneralPath;
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
 * @author isandlaTech
 * @see http://felix.apache.org/site/how-to-use-ipojo-annotations.html#
 *      HowtouseiPOJOAnnotations-@Component
 * @see http://felix.apache.org/site/how-to-use-ipojo-annotations.html#
 *      HowtouseiPOJOAnnotations-@Provides
 * @see http://felix.apache.org/site/how-to-use-ipojo-annotations.html#
 *      HowtouseiPOJOAnnotations-@Instantiate
 */
@Component
@Provides
@Instantiate
public class Triangle implements ISimpleShape {

	public static final String ICON_PNG = "triangle.png";
	public static final String TRIANGLE_NAME = "TRIANGLE_NAME";

	private ImageIcon pIcon;

	@ServiceProperty(name = ISimpleShape.ICON_PROPERTY, value = ICON_PNG)
	private String pIconName;

	@Requires
	private ILogger pLogger;

	// @see
	// http://felix.apache.org/site/how-to-use-ipojo-annotations.html#HowtouseiPOJOAnnotations-@ServiceProperty
	@ServiceProperty(name = ISimpleShape.NAME_PROPERTY, value = "Square")
	private String pName;

	/**
 * 
 */
	public Triangle() {
		super();
	}

	/**
	 * Implements the <tt>SimpleShape.draw()</tt> method for painting the shape.
	 * 
	 * @param g2
	 *            The graphics object used for painting.
	 * @param p
	 *            The position to paint the triangle.
	 **/
	@Override
	public void draw(Graphics2D g2, Point p) {
		int x = p.x - 25;
		int y = p.y - 25;
		GradientPaint gradient = new GradientPaint(x, y, Color.GREEN, x + 50,
				y, Color.WHITE);
		g2.setPaint(gradient);
		int[] xcoords = { x + 25, x, x + 50 };
		int[] ycoords = { y, y + 50, y + 50 };
		GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD,
				xcoords.length);
		polygon.moveTo(x + 25, y);
		for (int i = 0; i < xcoords.length; i++) {
			polygon.lineTo(xcoords[i], ycoords[i]);
		}
		polygon.closePath();
		g2.fill(polygon);
		BasicStroke wideStroke = new BasicStroke(2.0f);
		g2.setColor(Color.black);
		g2.setStroke(wideStroke);
		g2.draw(polygon);
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

		ResourceBundle wRB = ResourceBundle
				.getBundle("org.foo.shape.triangle.resource.Localize");

		pName = wRB.getString(TRIANGLE_NAME);

		pIcon = new ImageIcon(this.getClass().getResource(ICON_PNG));

		pLogger.logInfo(this, "validate",
				"name=[%s] IconHeight=[%d] IconWidth=[%d]", pName,
				pIcon.getIconHeight(), pIcon.getIconWidth());

	}
}
