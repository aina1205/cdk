/* $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 * 
 * Copyright (C) 1997-2004  The Chemistry Development Kit (CDK) project
 * 
 * Contact: cdk-devel@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * All we ask is that proper credit is given for our work, which includes
 * - but is not limited to - adding the above copyright notice to the beginning
 * of your source code files, and to any copyright notice that you may distribute
 * with programs based on this work.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *  */
package org.openscience.cdk.tools;

import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;

/**
 * Tool to make projections from 3D to 2D
 *
 * @cdk.keyword projection in 2D
 */
public class Projector  {
  
  public static void project2D(AtomContainer container) {
    for (int i = 0; i < container.getAtomCount(); i++) {
      Atom atom = container.getAtomAt(i);
      if (atom.getPoint3D() != null) {
        atom.setX2D(atom.getX3D());
        atom.setY2D(atom.getY3D());
      } else {
        // should throw an exception
      }
    }
  }
}
