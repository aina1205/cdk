/* $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 *
 * Copyright (C) 2002-2004  The Chemistry Development Kit (CDK) project
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
 *
 */
package org.openscience.cdk.libio.joelib;

import joelib.molecule.JOEAtom;
import joelib.molecule.JOEBond;
import joelib.molecule.JOEMol;

import org.openscience.cdk.Atom;
import org.openscience.cdk.Bond;
import org.openscience.cdk.Molecule;

/**
 * Abstract class that provides convertor procedures to
 * convert CDK classes to JOELib classes and visa versa.
 *
 * <p>JOELib is a Java implementation of the OELib classes and
 * can be found at: http://joelib.sourceforge.net/
 *
 * @cdkPackage libio
 *
 * @author     egonw
 * @author     Joerg K. Wegner <wegnerj@informatik.uni-tuebingen.de>
 *
 * @cdk.keyword    JOELib
 * @cdk.keyword    class convertor
 */
public class Convertor {

     public static final int COORDINATES_3D = 3;
     public static final int COORDINATES_2D = 2;

    /**
     * Converts an org.openscience.cdk.Atom class into a
     * joelib.molecule.JOEAtom class.
     *
     * Conversion includes:
     *   - atomic number
     *   - coordinates
     *
     * @param   atom    class to be converted
     * @return          converted class in JOELib
     **/
    public static JOEAtom convert(Atom atom) {
         return convert(atom, -1);
     }

     /**
      * Converts an org.openscience.cdk.Atom class into a
      * joelib.molecule.JOEAtom class.
      *
      * Conversion includes:
      *   - atomic number
      *   - coordinates
      *
      * @param   atom      CDK atom to be converted
      * @param   coordType coordinates to use. if -1 this converter uses the available coordinates.
      *                    If 3D and 2D coordinates are available, the 3D coordinates are used
      * @return            converted JOELib atom
      *
      * @see #COORDINATES_3D
      * @see #COORDINATES_2D
      **/
     public static JOEAtom convert(Atom atom, int coordType) {
        if (atom != null) {
            JOEAtom convertedAtom = new JOEAtom();
            if (coordType == COORDINATES_3D ||
                (atom.getPoint3D() != null && coordType != -1)) {
                convertedAtom.setVector(
                    atom.getX3D(),
                    atom.getY3D(),
                    atom.getZ3D()
                );
            } else if (coordType == COORDINATES_2D ||
                       (atom.getPoint2D() != null && coordType != -1)) {
                convertedAtom.setVector(
                    atom.getX2D(),
                    atom.getY2D(),
                    0.0
                );
            } else {
                convertedAtom.setVector(0.0, 0.0, 0.0);
            }
            convertedAtom.setAtomicNum(atom.getAtomicNumber());
            return convertedAtom;
        } else {
            return null;
        }
    }

    /**
     * Converts an joelib.molecule.JOEAtom class into a
     * org.openscience.cdk.Atom class.
     *
     * Conversion includes:
     *   - atomic number
     *   - coordinates
     *
     * @param   atom    class to be converted
     * @return          converted class in CDK
     **/
    public static Atom convert(JOEAtom atom) {
        if (atom != null) {
            Atom convertedAtom = new Atom("C");
            try {
                // try to give the atom the correct symbol
                org.openscience.cdk.tools.IsotopeFactory ef =
                    org.openscience.cdk.tools.IsotopeFactory.getInstance();
                org.openscience.cdk.Element e = ef.getElement(atom.getAtomicNum());
                convertedAtom = new Atom(e.getSymbol());
            } catch (java.lang.Exception e) {
            }
            try {
                // try to give the atom its coordinates
                convertedAtom.setX3D(atom.getVector().x());
                convertedAtom.setY3D(atom.getVector().y());
                convertedAtom.setZ3D(atom.getVector().z());
            } catch (java.lang.Exception e) {
            }
            try {
                // try to give the atom its atomic number
                convertedAtom.setAtomicNumber(atom.getAtomicNum());
            } catch (java.lang.Exception e) {
                // System.out.println("AtomicNumber failed");
            }
            return convertedAtom;
        } else {
            return null;
        }
    }

    /**
     * Converts an org.openscience.cdk.Bond class into a
     * joelib.molecule.JOEBond class.
     *
     * Conversion includes:
     *   - atoms which it conects
     *   - bond order
     *
     * @param   bond    class to be converted
     * @return          converted class in JOELib
     **/
    public static JOEBond convert(Bond bond) {
        if (bond != null) {
            JOEBond convertedBond = new JOEBond();
            convertedBond.setBegin(convert(bond.getAtomAt(0)));
            convertedBond.setEnd(convert(bond.getAtomAt(1)));
            convertedBond.setBO((int)bond.getOrder());
            return convertedBond;
        } else {
            return null;
        }
    }

    /**
     * Converts an joelib.molecule.JOEBond class into a
     * org.openscience.cdk.Bond class.
     *
     * Conversion includes:
     *   - atoms which it conects
     *   - bond order
     *
     * @param   bond    class to be converted
     * @return          converted class in CDK
     **/
    public static Bond convert(JOEBond bond) {
        if (bond != null) {
            Bond convertedBond = new Bond(
                                    convert(bond.getBeginAtom()),
                                    convert(bond.getEndAtom()),
                                    (double)bond.getBondOrder());
            return convertedBond;
        } else {
            return null;
        }
    }

    /**
     * Converts an org.openscience.cdk.Molecule class into a
     * joelib.molecule.JOEMol class.
     *
     * Conversion includes:
     *   - atoms
     *   - bonds
     *
     * @param   mol     molecule to be converted
     * @return          converted JOELib molecule
     *
     * @see #COORDINATES_3D
     * @see #COORDINATES_2D
     **/
    public static JOEMol convert(Molecule mol) {
        return convert(mol, -1);
    }

    /**
     * Converts an org.openscience.cdk.Molecule class into a
     * joelib.molecule.JOEMol class.
     *
     * Conversion includes:
     *   - atoms
     *   - bonds
     *
     *
     * @param   mol       class to be converted
     * @param   coordType coordinates to use. if -1 this converter uses the available coordinates.
     *                    If 3D and 2D coordinates are available, the 3D coordinates are used
     * @return            converted class in JOELib
     *
     * @see #COORDINATES_3D
     * @see #COORDINATES_2D
     **/
    public static JOEMol convert(Molecule mol, int coordType) {
        if (mol != null) {
            JOEMol converted = new JOEMol();
            
            // start molecule modification
            converted.beginModify();

            int NOatoms = mol.getAtomCount();
            
            // add atoms
            converted.reserveAtoms(NOatoms);
            for (int i=0; i<NOatoms; i++) {
                converted.addAtom(convert(mol.getAtomAt(i), coordType));
            }
            
            // add bonds
            double[][] matrix = mol.getConnectionMatrix();
            for (int i=0; i<NOatoms-1; i++) {
                for (int j=i+1; j<NOatoms; j++) {
                    if (matrix[i][j] != 0.0) {
                        // atoms i,j are connected
                        /* JOEMol.addBond() needs atom ids [1,...] */
                        converted.addBond(i+1,j+1, (int)matrix[i][j]);
                    } else {
                    }
                }
            }
            return converted;
        } else {
            return null;
        }
    }

    /**
     * Converts an joelib.molecule.JOEMol class into a
     * org.openscience.cdk.Molecule class.
     *
     * Conversion includes:
     *   - atoms
     *   - bonds
     *
     * @param   mol     class to be converted
     * @return          converted class in CDK
     **/
    public static Molecule convert(JOEMol mol) {
        if (mol != null) {
            Molecule converted = new Molecule();
            int NOatoms = mol.numAtoms();
            for (int i=1; i<=NOatoms; i++) {
                /* JOEMol.getAtom() needs ids [1,...] */
                JOEAtom a = mol.getAtom(i);
                Atom cdka = convert(a);
                converted.addAtom(cdka);
            }
            int NObonds = mol.numBonds();
            for (int i=1; i<=NObonds; i++) {
                /* JOEMol.getBond() needs ids [0,...] */
                JOEBond b = mol.getBond(i-1);
                /* Molecule.addBond() need atom ids [0,...] */
                converted.addBond(b.getBeginAtomIdx()-1,
                                  b.getEndAtomIdx()-1,
                                  b.getBondOrder());
            }
            return converted;
        } else {
            return null;
        }
    }

}
