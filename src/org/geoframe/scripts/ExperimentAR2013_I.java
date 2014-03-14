package org.geoframe.scripts;

import java.io.File;
import java.util.Random;

import org.geoframe.exceptions.EofException;
import org.geoframe.exceptions.EsriAsciiHeaderException;
import org.geoframe.io.TextIO;
import org.geoframe.ocn.Flow;
import org.geoframe.ocn.OCN;
import org.geoframe.ocn.OCNInfo;
import org.geoframe.ocn.POCN;
import org.geoframe.ocn.SimulationInfo;
import org.geoframe.ocn.TCA;
import org.geoframe.util.CreateFolder;
import org.geoframe.util.DateAndTime2String;
import org.geoframe.util.Folder;
import org.geoframe.util.InfoFolder;
import org.geoframe.util.PConsoleMonitor;

public class ExperimentAR2013_I {
	
	public OCN arExp;
	public String outputFilesName;
	public String outputFlowName;
	public String outputTCAName;

	//Constructors

	
/*	public ExperimentAR2013(Flow basin,TCA area){
		
		this.arExp=new POCN(basin,area);
	
	}*/
	//Letting to read just the names leaves you the freedom to get the names from StdIO or 
	//a file
	public ExperimentAR2013_I(String basinName,String areaName){
		
		this.arExp=new OCN(basinName,areaName);
		
	}
	

/**
 * Return a network by reading it from the specified files. I am using here this Java clas as a script  
 * @return a populated OCN type
 * 
 */
// Possibly it would be interesting to extend this main structure everywhere
// with initialize() - run() - print() 	methods
// This becomes interesting when the program is long and the main should become itself very long and complex	
	
	public static  ExperimentAR2013_I initialize(int netno){
	
	//Gets the file names for inputs	
	String name="/Users/riccardo/Dropbox/A-WorkSpace/ARPaper2013";
	InfoFolder trial=new InfoFolder(name);
	
	System.out.println("The working directory is:"+trial.workingPathName);
	//trial.shortName=trial.workingPath.getName();
	//System.out.println("The file is:"+trial.shortName);
	File test=new File(trial.workingPath+File.separator+netno+"_flow.asc");
	if(!test.exists()){
		//Throw Runtime error 
	}
	
	String outputDirName = trial.workingPathName+File.separator+netno;
	new CreateFolder(outputDirName);
	String basinName=outputDirName+File.separator+netno+"_flow.asc";
	String tcaName=outputDirName+File.separator+netno+"_tca.asc";
	//Create the Object for the experiment
	ExperimentAR2013_I experiment=new ExperimentAR2013_I(basinName,tcaName);
	//Set the name for the outputs
	OCN.outputFilesName=outputDirName+File.separator+netno+"_S_OCN";
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
	//Set the periodic conditions i.e. create a HALO at the boundary of the
	//basin, i.e. copy the first column into after the last, and the last before the first
	//using the empty columns
	
	//Set the simulation's parameters
	//System.out.println("Enter the number of iterations");
	experiment.arExp.pIterations=10000000;
//	System.out.println("Enter the exponent for energy expenditure calculation");
	experiment.arExp.pEnergyExpenditureExponent=0.5;	
	return experiment;
	}
		/**
	 * A standard print method
	 */
	/* public void print(){
		//TODO To be really improved
        this.basin.print();
        this.area.print();
	}
*/
   
   	
	

	
	
	
  //Save and Write the new network (the OCN)
  public void experimentDeploy(){
	  
	  //Write OCN on disk
	 this.outputFlowName=OCN.outputFilesName+"_flow.asc";
	 this.outputTCAName=OCN.outputFilesName+"_tca.asc";
	  //TextIO.writeFile(outputFlowName);
	  this.arExp.basin.asciiRasterWriter(outputFlowName);
	  //TextIO.readStandardInput();
      //TextIO.writeFile(outputTCAName);
	  this.arExp.area.asciiRasterWriter(outputTCAName);
	  //TextIO.readStandardInput();
	  
	  SimulationInfo sI=new SimulationInfo(this);
	  String logFileName=OCN.outputFilesName+"_S_log.txt";
	  System.out.println(logFileName);
	  TextIO.writeFile(logFileName);
	  sI.print();
	  OCNInfo ocnInfo=new OCNInfo.Builder(this.arExp.pEnergyExpenditureExponent,this.arExp.pIterations).workingDirectoryName(OCN.outputFilesName).inputFlowFileName("").inputTCAFileName("").outputDirectoryName(outputFlowName).build();		
	  ocnInfo.print();
	  TextIO.writeStandardOutput();

	  
  }
	
	public static void main(String[] args) throws EsriAsciiHeaderException, EofException {
		System.out.println("This is ExperimentAR2013 main");
		String wFile="/Users/riccardo/Dropbox/A-WorkSpace/ARPaper2013/ARExperiment.init";
		TextIO.readFile(wFile);
		int a=TextIO.getInt();
		int b=TextIO.getInt();
		TextIO.readStandardInput();
		for(int i=a;i< b;i++){
			System.out.println("Network no: "+i);
			ExperimentAR2013_I exp=initialize(i);
			//ocn.print();
			System.out.println("Optimizing the Network");
			exp.arExp.run();
			exp.experimentDeploy();
		}
		System.out.println("This ends computation");
	}
}
