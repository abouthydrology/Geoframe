package org.geoframe.util;

import java.io.File;


public class InfoFolder extends Folder{

public boolean exists;	
	/**
	 * This other constructor just create the space for information
	 */

public InfoFolder(String lworkingPath) {
		    this.workingPathName=lworkingPath;
			workingPath=new File(workingPathName);
			if(!workingPath.exists()){
				
				exists=false;
				System.out.println("File or directory does not exits");
			} else{
				exists=true;
			}
			if(!workingPath.isDirectory()){
				this.shortName=workingPath.getName();
		    //System.out.println("->"+shortName);
			} else {
				shortName =null;
			}
			

	}

	public static void main(String[] args) {
		System.out.println();
		String name=Folder.getFileName();
		InfoFolder trial=new InfoFolder(name);
        System.out.println("This ends computation");
	}

}
