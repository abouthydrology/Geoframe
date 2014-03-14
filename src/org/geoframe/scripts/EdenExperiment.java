/*
 * This file is part of Geoframe 
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
import java.util.ArrayList;

import org.geoframe.io.GetDoubleParameters;
import org.geoframe.io.TextIO;
import org.geoframe.ocn.EdenAL;
import org.geoframe.ocn.EstimateTCA;
import org.geoframe.ocn.Flow;
import org.geoframe.ocn.TCA;
import org.geoframe.util.InfoFolder;

/**
 * builts 128 river networks by using Eden growth and prints a flow and the tca files
 * in separate directories. Used for experiment in paper Rinaldo et al., 2014
 * Note: here I am using Java as a scripting language. I do not pretend any generality
 * or re-usability. It creates a Working Directory called A-WorkingDirectory/RinaldoPaper (if it does not exists)
 * and 128 subdirectories (1 to 128). Each one will contain an Eden grown network to be subsequently optimized
 * 
 * @author Riccardo Rigon, October 2013
 *
 */
public class EdenExperiment {
	
	Flow flow;
	TCA tca;
	static String relativeWorkingPath="/Users/riccardo/DropBox/A-WorkSpace";

	public static void main(String[] args) {
		
		//Create the main directory
		System.out.println("This is EdenExperiment main()");
		InfoFolder workingFolder=new InfoFolder(relativeWorkingPath+File.separator+"ARPaper2013");
		if(!workingFolder.exists){
			//Create it
			workingFolder.workingPath=new File(workingFolder.workingPathName);
			boolean success = workingFolder.workingPath.mkdirs();
			if(success){
				 System.out.println("The directory "+workingFolder.workingPathName+" was created");
			}else{
				System.out.println("The directory"+workingFolder.workingPathName+" cannot be created");		
				throw new RuntimeException("\nThe directory"+workingFolder.workingPathName+" cannot be created\n");
			}
		}

		//Create the networks
		for(int l=0;l<128;l++){
		//Create the directory
		String fls=workingFolder.workingPathName+File.separator+(l+1);	
		File thisSimulation= new File(fls);
		boolean success = thisSimulation.mkdirs();	
		EdenAL edenBasinFlow = new EdenAL(130,130);
	    EdenAL.outlets = new ArrayList<int[]>();
		//Here we use just one outlet
		int pNumberOfOutlets=1;
		int[][] ots=new int[pNumberOfOutlets][2];
		for(int k=0;k< pNumberOfOutlets;k++){
			ots[k][0]=128;
			ots[k][1]=l+1;			
			EdenAL.outlets.add(ots[k]);
		}
        

	try{
		//Populate the basin
		edenBasinFlow.populateRiverBasin();
		//Write the basin on disk. Ask for "name" and then save as "name_flow.asc"
//The following three lines have to be moved 
     	edenBasinFlow.asciiRasterWriter(fls+File.separator+(l+1)+"_flow.asc");
//		System.out.println("The name of the working directory is "+workingFolder.workingPathName);
//		System.out.println("The name of the file is "+workingFolder.shortName);
		}catch(Error ee){
			ee.getStackTrace();
		}
	//Estimate the contributing area
	EstimateTCA bTCA=new EstimateTCA(edenBasinFlow);
	//bTCA.area.print();
	//Save in the very same directory where flow is with the same name plus "_tca.asc"
//The following three lines have to be moved or changed
	bTCA.area.asciiRasterWriter(fls+File.separator+(l+1)+"_tca.asc");
    bTCA.area.energyExpenditure=bTCA.area.energyExpenditure(0.5);
//	System.out.println("Energy Expenditure of the basin is: "+bTCA.area.energyExpenditure);
		}//end of for
	System.out.println("This ends computations");
	}

}
