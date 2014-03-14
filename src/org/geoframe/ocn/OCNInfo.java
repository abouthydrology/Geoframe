package org.geoframe.ocn;

import org.geoframe.io.TextIO;

public class OCNInfo{
	
	String workingDirectoryName;
	String inputFlowFileName;
	String inputTCAFileName;
    String outputDirectoryName;
    double energyExpenditureExponent;
    long   numberOfIterations;
    
	SimulationInfo simInfo;
	
    //Using the Builder Pattern (JB - Item 2)
	//Well, this is a little hybrid
	public static class Builder{
		
		//Just the OCNs variables
		String workingDirectoryName;
		String inputFlowFileName;
		String inputTCAFileName;
	    String outputDirectoryName;
	    //Defaults: the other information can be deduced from the location of the files ... unless they were moved
	    double energyExpenditureExponent;
	    long   numberOfIterations;
	    
         public Builder(double eE,long itr){
        	this.energyExpenditureExponent=eE;
        	this.numberOfIterations=itr;
         }
		public Builder workingDirectoryName(String wd){
			workingDirectoryName=wd;
			return this;
		}
		public Builder inputFlowFileName(String iFF){
			inputFlowFileName=iFF;
			return this;
		}
		public Builder inputTCAFileName(String iTCA){
			inputTCAFileName=iTCA;
			return this;
		}
		public Builder outputDirectoryName(String oDN){
			outputDirectoryName=oDN;
			return this;
		}
		
		public OCNInfo build(){
			return new OCNInfo(this);
		}
	}
	
	public OCNInfo(Builder builder){
		
		workingDirectoryName=builder.workingDirectoryName;
		inputFlowFileName=builder.inputFlowFileName;
		inputTCAFileName=builder.inputTCAFileName;
	    outputDirectoryName=builder.outputDirectoryName;
	    energyExpenditureExponent=builder.energyExpenditureExponent;
	    numberOfIterations=builder.numberOfIterations;
	    
	}
	
	public static void main(String[] args) {
		System.out.println("This is OCNInfo main");
		
		ROCN ocn=ROCN.initialize();
		//Maybe to move back to non-static method
		SimulationInfo sI=new SimulationInfo(ocn);
		sI.print();		
		OCNInfo ocnInfo=new OCNInfo.Builder(ocn.pEnergyExpenditureExponent,ocn.pIterations).workingDirectoryName("").inputFlowFileName("").inputTCAFileName("").outputDirectoryName("").build();					
		ocnInfo.print();
		
		System.out.println("This ends computation");

	}

	
	public void print(){
		if(!workingDirectoryName.isEmpty()){
			TextIO.putln("The working directory is: "+workingDirectoryName);
		} else{
			TextIO.putln("The working directory name is not available");
		}
		if(!inputFlowFileName.isEmpty()){
			TextIO.putln("The input flow file name is: "+ inputFlowFileName);
		} else{
			TextIO.putln("The input flow file name is not available");
		}
		if(!inputTCAFileName.isEmpty()){
			TextIO.putln("The input TCA file name is: "+ inputTCAFileName);
		}else{
			TextIO.putln("The input TCA file name is not available");
		}
		if(!outputDirectoryName.isEmpty()){
			TextIO.putln("The output Directory is: "+outputDirectoryName);
		}else{
			TextIO.putln("The input output directory name is not available");
		}
		
		TextIO.putln("The Energy Expenditure Exponent is: "+energyExpenditureExponent);
		TextIO.putln("The number of iterations is: "+numberOfIterations);
	}
}


