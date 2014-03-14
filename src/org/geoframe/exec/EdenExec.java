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
package org.geoframe.exec;

import java.util.ArrayList;

import org.geoframe.io.GetDoubleParameters;
import org.geoframe.io.TextIO;
import org.geoframe.ocn.EdenAL;
import org.geoframe.ocn.EstimateTCA;
import org.geoframe.ocn.Flow;
import org.geoframe.ocn.TCA;
import org.geoframe.util.InfoFolder;

/**
 * builts a riven networks by using Eden growth and prints a flow and the tca files
 * 
 * @author Riccardo Rigon 2013
 *
 */
public class EdenExec {
	
	Flow flow;
	TCA tca;
	//EdenAL edn;

	public static void main(String[] args) {
		
		
		System.out.println("This is EdenExec main()");
		InfoFolder workingFolder=null;
		
		//Input of the river dimensions
		String[] nms={"the number of rows","the number of columns"};
		GetDoubleParameters prmtrs=new GetDoubleParameters(nms);
		//System.out.println((int)prmtrs.getParameters()[0]);
		//System.out.println((int)prmtrs.getParameters()[1]);
		// Provisional casting
		//Put check that the numer of rows and cols should be greater than 3
		EdenAL edenBasinFlow = new EdenAL((int)prmtrs.getParameters()[0],(int)prmtrs.getParameters()[1]);
        //System.out.println(edenBasinFlow.rows+" " +edenBasinFlow.cols+"->"+ (edenBasinFlow.flow==null));
		//Setting the outlets
	    EdenAL.outlets = new ArrayList<int[]>();
		System.out.println("Enter the number of oulets");		
		int pNumberOfOutlets=TextIO.getInt();
		int[][] ots=new int[pNumberOfOutlets][2];
		for(int k=0;k< pNumberOfOutlets;k++){
			//System.out.println("Outlet #"+(k+1));
			System.out.println("Enter the first coordinate (row)");
			ots[k][0]=TextIO.getInt();
			System.out.println("Enter the second coordinate (col)");
			ots[k][1]=TextIO.getInt();
			//System.out.println("> "+ots[k][0]+ " "+ots[k][1]);
			EdenAL.outlets.add(ots[k]);
		}
        
		System.out.println("Outlets:");
		int count=1;
		for(int[] item : EdenAL.outlets){
			System.out.println("outlet no #"+count+" is "+item[0]+" "+item[1]);
			count++;
		}
	try{
		//Populate the basin
		edenBasinFlow.populateRiverBasin();
		System.out.println("****************************");
		//Write the basin on disk. Ask for "name" and then save as "name_flow.asc"
		//edenBasinFlow.asciiRasterWriter();
		workingFolder=edenBasinFlow.asciiRasterWriterGF();
		System.out.println("The name of the working directory is "+workingFolder.workingPathName);
		System.out.println("The name of the file is "+workingFolder.shortName);
		}catch(Error ee){
			ee.getStackTrace();
		}
	//Estimate the contributing area
	EstimateTCA bTCA=new EstimateTCA(edenBasinFlow);
	//bTCA.area.print();
	//Save in the very same directory where flow is with the same name plus "_tca.asc"
	bTCA.area.asciiRasterWriter(workingFolder);
	bTCA.area.energyExpenditure=bTCA.area.energyExpenditure(0.5);
	System.out.println("Energy Expenditure of the basin is: "+bTCA.area.energyExpenditure);
	System.out.println("This ends computations");
	}

}
