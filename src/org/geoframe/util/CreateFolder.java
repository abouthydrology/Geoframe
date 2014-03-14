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
package org.geoframe.util;
import java.io.File;

import org.geoframe.io.TextIO;
/**
 * It simplifies the creation of folders
 * 
 *  
 * @author Riccardo Rigon 2013
 * @version 1/16
 * @see CreateFolder
 * @see InfoFolder
 *
 */
final public class CreateFolder extends Folder{
	
	
//Constructor
/**
 * It actually builds the working paths directories if needed. for a proper choice
 * the names of the directory should be known in advance, and the static methods above
 * (that do not need instantiation of the class can be used for the purposes).
 * 
 * @param lworkingPath
 */
public CreateFolder(String lworkingPath){
    workingPathName=lworkingPath;
	workingPath=new File(workingPathName);
	if(!workingPath.exists()){
		//Create it
		boolean success = (new File(workingPathName)).mkdirs();
		if(success){
			 System.out.println("The directory"+workingPathName+" was created");
		}else{
			System.out.println("The directory"+workingPathName+" cannot be created");		
			throw new RuntimeException("\nThe directory"+workingPathName+" cannot be created\n");
		}
	}

}

	public static void main(String[] args){
		
String fl2;

System.out.println("This starts Folder class computation");		
		TextIO.putln("User home is: "+userHome);	
		fl2=userHome;
		fl2=IteratedFolderRequest(fl2);
		new CreateFolder(fl2);
		
		//To see how the method folder request works, see GetDoubleParametersFromFile
		
		System.out.println("This ends computation");
	}
}
//Lesson learned here: You can start from the skeleton class  (with constructor and the essential getter and setter - remind
// to avoid them, when possible - then long procedural operations are replaced by methods. 
//The Class can be extended to get its data from a file, which needs to be a standard file in a standard place