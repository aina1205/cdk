/* $Revision$ $Author$ $Date$
 * 
 * Copyright (C) 2008  Egon Willighagen <egonw@users.sf.net>
 * 
 * Contact: cdk-devel@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA. 
 */
package org.openscience.cdk.tools.diff;

import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.interfaces.IChemObject;

/**
 * @cdk.module test-diff
 */
public class ChemObjectDiffTest {

    @Test public void testMatchAgainstItself() {
        IChemObject atom1 = new ChemObject();
        String result = ChemObjectDiff.diff(atom1, atom1);
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.length());
    }
    
    @Test public void testDiff() {
        IChemObject atom1 = new ChemObject();
        IChemObject atom2 = new ChemObject();
        atom2.setFlag(CDKConstants.ISAROMATIC, true);
        
        String result = ChemObjectDiff.diff( atom1, atom2 );
        Assert.assertNotNull(result);
        Assert.assertNotSame(0, result.length());
        Assert.assertTrue(result.contains("ChemObjectDiff"));
        Assert.assertTrue(result.contains("F/T"));
    }

}