package org.geoframe.scripts;

import java.io.File;

import org.geoframe.exceptions.EofException;
import org.geoframe.exceptions.EsriAsciiHeaderException;
import org.geoframe.io.TextIO;
import org.geoframe.ocn.Field;
import org.geoframe.ocn.Flow;
import org.geoframe.ocn.OCNInfo;
import org.geoframe.ocn.ROCN;
import org.geoframe.ocn.SimulationInfo;
import org.geoframe.ocn.TCA;
import org.geoframe.util.CreateFolder;
import org.geoframe.util.InfoFolder;

public class ExperimentAR2013_II {
	
	public ROCN arExp;
	public String outputFilesName;
	public String outputFlowName;
	public String outputTCAName;

	//Constructors

	
/*	public ExperimentAR2013(Flow basin,TCA area){
		
		this.arExp=new POCN(basin,area);
	
	}*/
	//Letting to read just the names leaves you the freedom to get the names from StdIO or 
	//a file
	public ExperimentAR2013_II(String basinName,String areaName){
		
		this.arExp=new ROCN(basinName,areaName);
		
	}
	

/**
 * Return a network by reading it from the specified files. I am using here this Java clas as a script  
 * @return a populated OCN type
 * 
 */
// Possibly it would be interesting to extend this main structure everywhere
// with initialize() - run() - print() 	methods
// This becomes interesting when the program is long and the main should become itself very long and complex	
	
	public static  ExperimentAR2013_II initialize(int netno){
	
	//Gets the file names for inputs	
	String name="/Users/riccardo/Dropbox/A-WorkSpace/ARPaper2013-II";
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
	//System.out.println("This is the basin"+basinName);
	String tcaName=outputDirName+File.separator+netno+"_tca.asc";
	//Create the Object for the experiment
	ExperimentAR2013_II experiment=new ExperimentAR2013_II(basinName,tcaName);
	//Set the name for the outputs
	ROCN.outputFilesName=outputDirName+File.separator+netno+"_R_OCN";
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
		Field.asciiRasterReader(tcaName);
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
   
   	
	

	
	
	
  //Save and Write the new network (the ROCN)
  public void experimentDeploy(){
	  
	  //Write OCN on disk
	 this.outputFlowName=ROCN.outputFilesName+"_flow.asc";
	 this.outputTCAName=ROCN.outputFilesName+"_tca.asc";
	  //TextIO.writeFile(outputFlowName);
	  this.arExp.basin.asciiRasterWriter(outputFlowName);
	  //TextIO.readStandardInput();
      //TextIO.writeFile(outputTCAName);
	  this.arExp.area.asciiRasterWriter(outputTCAName);
	  //TextIO.readStandardInput();
	  
	  SimulationInfo sI=new SimulationInfo(this);
	  String logFileName=ROCN.outputFilesName+"_R_log.txt";
	  System.out.println(logFileName);
	  TextIO.writeFile(logFileName);
	  sI.print();
	  OCNInfo ocnInfo=new OCNInfo.Builder(this.arExp.pEnergyExpenditureExponent,this.arExp.pIterations).workingDirectoryName(ROCN.outputFilesName).inputFlowFileName("").inputTCAFileName("").outputDirectoryName(outputFlowName).build();		
	  ocnInfo.print();
	  TextIO.writeStandardOutput();

	  
  }
	
	public static void main(String[] args) throws EsriAsciiHeaderException, EofException {
		System.out.println("This is ExperimentAR2013-II main");
		//Below is the number of networks to process
		String wFile="/Users/riccardo/Dropbox/A-WorkSpace/ARPaper2013-II/ARExperiment.init";
		TextIO.readFile(wFile);
		int a=TextIO.getInt();
		int b=TextIO.getInt();
		TextIO.readStandardInput();
		for(int i=a;i< b;i++){
			System.out.println("Network no: "+i);
			ExperimentAR2013_II exp=initialize(i);
			//ocn.print();
			System.out.println("Optimizing the Network");
			exp.arExp.run();
			exp.experimentDeploy();
		}
		System.out.println("This ends computation");
	}
}
