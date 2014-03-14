package org.geoframe.util;

import java.io.File;

public class GetFileName {
	
	public String fileName;
	
	public GetFileName(){
	fileName=CreateFolder.getFileName();	
	File inputFile=new File(fileName);
//	System.out.println(fileName);
	while(!inputFile.exists()){
		fileName=CreateFolder.getFileName();
//		System.out.println(fileName);
		inputFile=new File(fileName);
	}
	}
	
	public static void main(String[] args) {
		System.out.println("This is GetFileName main()");
		GetFileName gfn=new GetFileName();
		System.out.println("The file name is: "+gfn.fileName);
		//@see GoDownstreamExec for the use of the data got from GetFileName
        System.out.println("This ends computation");
	}

}
