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

import org.foo.shape.ISimpleShape;

/**
 * This class is used to record the various information pertaining to an
 * available shape.
 * 
 * @author Richard S. Hall, Karl Pauls, Stuart McCulloch, and David Savage
 * @author isandlaTech
 **/
public class ShapeInfo {

	private final ISimpleShape pShape;
	private final Icon pIcon;
	private final String pName;

	/**
	 * @param name
	 * @param icon
	 * @param shape
	 */
	public ShapeInfo(String name, Icon icon, ISimpleShape shape) {
		pName = name;
		pIcon = icon;
		pShape = shape;
	}

	/**
	 * @return
	 */
	public Icon getIcon() {
		return pIcon;
	}

	/**
	 * @return
	 */
	public String getName() {
		return pName;
	}

	/**
	 * @return
	 */
	public ISimpleShape getShape() {
		return pShape;
	}
}
