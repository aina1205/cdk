/*
 *  $RCSfile$
 *  $Author$
 *  $Date$
 *  $Revision$
 *
 *  Copyright (C) 1997-2005  The Chemistry Development Kit (CDK) project
 *
 *  Contact: cdk-devel@lists.sourceforge.net
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1
 *  of the License, or (at your option) any later version.
 *  All I ask is that proper credit is given for my work, which includes
 *  - but is not limited to - adding the above copyright notice to the beginning
 *  of your source code files, and to any copyright notice that you may distribute
 *  with programs based on this work.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */
package org.openscience.cdk.controller;

import org.openscience.cdk.layout.*;
import org.openscience.cdk.renderer.*;
import org.openscience.cdk.geometry.*;
import org.openscience.cdk.*;
import org.openscience.cdk.event.*;
import org.openscience.cdk.tools.LoggingTool;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.vecmath.*;

/**
 * Class that acts on MouseEvents and KeyEvents.
 *
 * @author         egonw
 * @cdk.created    2004-03-26
 * @cdk.keyword    mouse events
 * @cdk.keyword    popup menus
 */
public class PopupController2D extends Controller2D {

	private LoggingTool logger;

	private static Hashtable popupMenus = null;

	public PopupController2D(ChemModel chemModel, Renderer2DModel r2dm, Controller2DModel c2dm) {
        super(chemModel, r2dm, c2dm);
		logger = new LoggingTool(this);

		if (this.popupMenus == null) {
			this.popupMenus = new Hashtable();
		}
	}

	public PopupController2D(ChemModel chemModel, Renderer2DModel r2dm) {
		super(chemModel, r2dm);
	}

	/**
	 *  manages all actions that will be invoked when a mouse button is pressed
	 *
	 *@param  event  MouseEvent object
	 */
	public void mousePressed(MouseEvent event)
	{
    isUndoableChange = false;
		int[] screenCoords = {event.getX(), event.getY()};
		int[] mouseCoords = getWorldCoordinates(screenCoords);
		int mouseX = mouseCoords[0];
		int mouseY = mouseCoords[1]+c2dm.getOffsetY();

		logger.debug("MousePressed Event Props: mode=", c2dm.getDrawModeString());
        if (logger.isDebugEnabled()) {
            logger.debug("   trigger=" + event.isPopupTrigger() +
				/* ", Button number: " + event.getButton() + */
				", Click count: " + event.getClickCount());
        }

		if (event.isPopupTrigger() || 
            (event.getModifiers() & MouseEvent.BUTTON3_MASK) != 0)
		{
			logger.info("Popup menu triggered...");
			popupMenuForNearestChemObject(mouseX, mouseY, event);
		} else {
            super.mousePressed(event);
		}
	}

	/**
	 *  Sets the popupMenu attribute of the Controller2D object
	 *
	 *@param  chemObject  The new popupMenu value
	 *@param  menu        The new popupMenu value
	 */
	public void setPopupMenu(ChemObject chemObject, CDKPopupMenu menu) {
		this.popupMenus.put(chemObject.getClass().getName(), menu);
	}


	/**
	 *  Returns the popup menu for this ChemObject if it is set, and null
	 *  otherwise.
	 *
	 *@param  chemObject  Description of the Parameter
	 *@return             The popupMenu value
	 */
	public CDKPopupMenu getPopupMenu(ChemObject chemObject) {
        Class classSearched = chemObject.getClass();
        logger.debug("Searching popup for: ", classSearched.getName());
        while (classSearched.getName().startsWith("org.openscience.cdk")) {
            logger.debug("Searching popup for: ", classSearched.getName());
            if (this.popupMenus.containsKey(classSearched.getName())) {
                return (CDKPopupMenu) this.popupMenus.get(classSearched.getName());
            } else {
                logger.debug("  recursing into super class");
                classSearched = classSearched.getSuperclass();
            }
		}
        return null;
	}

	/**
	 *  Description of the Method
	 *
	 *@param  mouseX  Description of the Parameter
	 *@param  mouseY  Description of the Parameter
	 *@param  event   Description of the Parameter
	 */
	private void popupMenuForNearestChemObject(int mouseX, int mouseY, MouseEvent event)
	{
		ChemObject objectInRange = getChemObjectInRange(mouseX, mouseY);
		CDKPopupMenu popupMenu = getPopupMenu(objectInRange);
		if (popupMenu != null)
		{
			popupMenu.setSource(objectInRange);
			logger.debug("Set popup menu source to: ", objectInRange);
			popupMenu.show(event.getComponent(), event.getX(), event.getY());
		} else
		{
			logger.warn("Popup menu is null! Could not set source!");
		}
	}

}

