package org.geoframe.ocn;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.geoframe.exceptions.EofException;
import org.geoframe.exceptions.EsriAsciiHeaderException;
import org.geoframe.io.TextIO;
import org.geoframe.util.DateAndTime2String;
//import org.geoframe.util.CreateFolder;
//import org.geoframe.util.DateAndTime2String;
import org.geoframe.util.Folder;
import org.geoframe.util.InfoFolder;

public class ToMathematicaFormat {
	
	private static String outputFileName;
	private static long[][] data;
	
	public static OCN read(){
		
		//Gets the file names for inputs	
		String name=Folder.getDirectory();
		InfoFolder trial=new InfoFolder(name);
		
		System.out.println("The working directory is:"+trial.workingPathName);
		trial.shortName=trial.workingPath.getName();
		System.out.println("The file is:"+trial.shortName);
		File test=new File(trial.workingPath+File.separator+trial.shortName+"_flow.asc");
		while(!test.exists()){
			System.out.println("Re-enter the files prefix");
			trial.shortName=TextIO.getlnWord();
			test=new File(trial.workingPath+File.separator+trial.shortName+"_flow.asc");			
		}
		System.out.print("The files to open are:\n"+trial.workingPath+File.separator+trial.shortName+"_flow.asc\n");
		System.out.println(trial.workingPath+File.separator+trial.shortName+"_tca.asc\n");
		String basinName=trial.workingPath+File.separator+trial.shortName+"_flow.asc";
		String tcaName=trial.workingPath+File.separator+trial.shortName+"_tca.asc";
		OCN ocn=new OCN(basinName,tcaName);
		//Set the name for the outputs
		ToMathematicaFormat.outputFileName=trial.workingPath+File.separator+trial.shortName+".mat";
		//Read the flow
		try {
			Flow.asciiRasterReader(basinName);
		} catch (EsriAsciiHeaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EofException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Read the TCA
		try {
			TCA.asciiRasterReader(tcaName);
		} catch (EsriAsciiHeaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EofException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ocn;
		}
	
	public static long[][] toMathematica(OCN ocn){
		
		int size=(ocn.basin.cols-2)*(ocn.basin.rows-2);
		//System.out.println(size+" "+ocn.basin.rows+" "+ocn.basin.rows);
		long[][] dataFrame= new long[size][5];
		int count=0;
		int k;
		for(int i=1;i<ocn.basin.rows-1;i++){
			for(int j=1;j<ocn.basin.cols-1;j++){
				//System.out.println(count);
				dataFrame[count][0]=j;
				dataFrame[count][1]=ocn.basin.rows-i;
				k=ocn.basin.flow[i][j];
				if(k!=10){
					dataFrame[count][2]=j+Flow.neighbors[k][1];
					dataFrame[count][3]=ocn.basin.rows-(i+Flow.neighbors[k][0]);
					
				} else {
					dataFrame[count][2]=j;
					dataFrame[count][3]=ocn.basin.rows-i;
				
				}
				dataFrame[count][4]=ocn.area.tca[i][j];	
				count++;
			}
		}
		
		return dataFrame;
	
	}
	
	public static void writeMath(String outputfileName){
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		   //get current date time with Date()
		Date date = new Date();
		TextIO.writeFile(outputFileName);
		TextIO.putln("/** This is a turtle file created on "+dateFormat.format(date)+" by ToMathematicaFormat");
		TextIO.putln("* ");
		TextIO.putln("*/");
		TextIO.putln("index{2,XY}");
		TextIO.putln("1: float array pixel sizes and threshold {10.000000,10.000000,0.000000}");
		TextIO.putln("2: long matrix channel network coordinates and weights {"+data.length+",5}");
		for(int k=0;k<data.length;k++){
			TextIO.putln(data[k][0]+"\t"+data[k][1]+"\t"+data[k][2]+"\t"+data[k][3]+"\t"+data[k][4]+"\t");
		}
		TextIO.writeStandardOutput();
	}
	
	public static void main(String[] args) {
		System.out.println("This is ToMathematicaFormat main");
		//Get Working Directory and names
		//Read Flow And TCA
	    OCN ocn=read();		
		//Write a Mathematica File
        //ocn.print();
	    ToMathematicaFormat.data=ToMathematicaFormat.toMathematica(ocn);
	    ToMathematicaFormat.writeMath(ToMathematicaFormat.outputFileName);
	    System.out.println("This ends computation");
	}

}
