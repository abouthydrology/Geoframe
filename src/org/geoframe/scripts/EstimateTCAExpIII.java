/*
 * This file is part of Geofram 
 * (C) AboutHydrology - http://aboutHydrology.blogspot.com
 * 
 * Geoframe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geoframe.scripts;

import java.io.File;

import org.geoframe.exceptions.EofException;
import org.geoframe.exceptions.EsriAsciiHeaderException;
import org.geoframe.ocn.EstimateTCA;
import org.geoframe.ocn.Flow;
import org.geoframe.ocn.TCA;

/**
 * Calculates the contributing area of OCNs for ARPAper2013. This treats Flow and TCA symmetrically
 * It is a script
 * @author Riccardo Rigon 2013
 * @version 1/8
 *
 */
public class EstimateTCAExpIII {
 
	public static void main(String[] args) {
		
		System.out.println("This is EstimateTCAExpIII main()");
        double ee;
        String workingDirectory="/Users/riccardo/Dropbox/A-Workspace/ARPaper2013-III";
		String edn= "fileName"; //The name of the file to be read
		String odn="flNm"; //The name of the file to be written
		for(int i=1; i<128;i++){
			System.out.println("Estimating TCA of network "+i);
			edn=workingDirectory+File.separator+i+File.separator+i+"_flow.asc";			
			EstimateTCA bTCA=new EstimateTCA(edn);
			odn=workingDirectory+File.separator+i+File.separator+i+"_itca.asc";
			bTCA.area.asciiRasterWriter(odn);
			ee=bTCA.area.energyExpenditure(0.5);
			System.out.println("Energy Expenditure = "+ee);
		}


		System.out.println("This is ends calculations");	

	}

}
