package org.geoframe.scripts;
import java.io.File;

import org.geoframe.io.TextIO;
import org.geoframe.util.GenericAsciiFileReader;

/**
 * This is a script to convert Alberto Bellin's Hydro_gen outputs into a lognormal field
 * with assigned mean and variance
 * 
 * @author riccardo
 *
 */
public class LogNormalize {
	
    double meanDesired,varianceDesired,meanActual,varianceActual;
    //These are the variance  and the mean of the gaussian field as tranformed from the desired statistics
    private double yVar,yM;
    private final static double NO_VALUE=-1.0;
    
    //Constructor. Initializes  the fields
    
     //Read the mean and the variance required and desired from StdIO
     public void initialize(){
    	 System.out.println("Enter the actual (realization) mean");
    	 this.meanActual=TextIO.getDouble();
    	 System.out.println("Enter the actual (realization) variance");
    	 this.varianceActual=TextIO.getDouble();
    	 System.out.println("Enter the target lognornal mean");
    	 this.meanDesired=TextIO.getDouble();
       	 System.out.println("Enter the target lognormal variance");
    	 this.varianceDesired=TextIO.getDouble();
 
     }
     // \langle Y \rangle  = \log \left[ \frac{\langle Z \rangle}{1 + \frac{\sigma_z^2}{\langle Z \rangle}} \right]
     private double yMean(double z,double varZ){
    	 
    	 return Math.log(z/Math.sqrt((1+varZ/(z*z))));
     }
     //\sigma_Y^2 = \log \left[ 1 + \frac{\sigma^2_Z}{\langle Z \rangle}\right]
     private double yVariance(double z,double varZ){
    	 return Math.log(1+varZ/(z*z));
     }
    //Y(x) = (Y_g(x) - \langle Y_g \rangle)\, \frac{\sigma_Y}{\sigma_{Y_g}}+\langle Y \rangle
    //Shift the mean and the variance of a field of whatsoever distribution
      @SuppressWarnings("unused")
	private double rescaledField(double yG){
    	 return (yG-this.meanActual)*(this.varianceDesired/this.varianceActual)+this.meanDesired;
     }
    
  // Z(x) = exp[Y(x)]   
     private double newLogNormalField(double yG){
    	 return Math.exp((yG-this.meanActual)*Math.sqrt(this.yVar/this.varianceActual)+this.yM);
     }
   //Estimates the mean of the field in input, excluding the boundaries  
   //-2 as well as -3 to correct the dimensions
     private double meanField(double[][] data){
    	double sum=0;
    	
    	for(int i=1;i<data.length-2;i++){
    		for(int j=1;j<data[0].length-2;j++){
    			sum+=data[i][j];
    		}
    	}
    	return sum/((data.length-3)*(data[0].length-3));
    }
    //Estimates the unbiased variance of the field in input, excluding the boundaries
    private double varOfField(double[][] data, double mean){
    	double sum=0;
    	
    	for(int i=1;i<data.length-2;i++){
    		for(int j=1;j<data[0].length-2;j++){
    			sum+=(data[i][j]-mean)*(data[i][j]-mean);
    		}
    	}
    	return sum/((data.length-3)*(data[0].length-3)-1);
    }   
     
    private void write(String outputFileName,double data[][]){
		File outputFile=new File(outputFileName);
		if(outputFile.exists()){
			File oldFile=new File(outputFileName+".old");
			outputFile.renameTo(oldFile);
		}
		//Write the header: This is not so general, however, should take
		//variable inputs
		//-1 here for correcting Alberto's error
	    TextIO.writeFile(outputFileName);
	    TextIO.putf("NCOLS %d\n", data[0].length-1);
	    TextIO.putf("NROWS %d\n",data.length-1);
	    TextIO.putf("XLLCORNER %d\n", 0);
	    TextIO.putf("YLLCORNER %d\n", 0);
	    TextIO.putf("CELLSIZE %d\n", 1000);
	    TextIO.putf("NODATA_VALUE %f\n", NO_VALUE);
	    for(int i=0;i<data.length-1;i++){
	    	for(int j=0; j<data[0].length-1;j++){
			 TextIO.putf("%f ",data[i][j]);
	    	}
	    	TextIO.putln();
	    }
	    TextIO.writeStandardOutput();
    }
    
	public static void main(String[] args) {
		System.out.println("This is DeNormalize main");
		String fName="/Users/riccardo/Dropbox/A-Workspace/ARPaper2013-IV/2_2.dat";
        String oFName="/Users/riccardo/Dropbox/A-Workspace/ARPaper2013-IV/weights_2_2.dat";
        
		GenericAsciiFileReader aA=new GenericAsciiFileReader(fName);
		if(!aA.initialize()){
			System.out.println("It was not possible to initialize the reading of the file");
		}else{
			System.out.println("File ready to be read");
		}
		
		if(!aA.readHeader()){
			System.out.println("It was not possible to read the file");
		}else{
			aA.print();
		}
		if(!aA.readData()){
			System.out.println("It was not possible to read the data");
		}else{
			System.out.println("Data read");
		}
		//After reading the data, produce a lognormal file with the desired mean and variance
		LogNormalize bB=new LogNormalize();
		bB.initialize();
		
		double mn=bB.meanField(aA.dataStore);
		double vr=bB.varOfField(aA.dataStore, mn);
		
		bB.meanActual=mn;
		bB.varianceActual=vr;
		
		System.out.println("The mean and the variance of the data are: "+mn+" and: "+vr);
		//The following should be fixed inside the constructor or something similar, in order not to have wrong values
		bB.yM=bB.yMean(bB.meanDesired, bB.varianceDesired);
		bB.yVar=bB.yVariance(bB.meanDesired, bB.varianceDesired);
		System.out.println("The desired mean and the variance of the gaussian derived field are: "+bB.yM+" and:"+bB.yVar);
		// -1 to correct an error by Alberto who gave me a 131*131 field
		for(int i=0; i< aA.rows-1; i++){
			for(int j=0;j<aA.cols-1;j++){
				aA.dataStore[i][j]=bB.newLogNormalField(aA.dataStore[i][j]);
			}
		}
		//Now check the mean of the lognormal field
		mn=bB.meanField(aA.dataStore);
		vr=bB.varOfField(aA.dataStore, mn);
        bB.meanActual=mn;
        bB.varianceActual=vr;
		System.out.println("The mean and the variance of the lognormal field are: "+mn+" and: "+vr);
 /*       System.out.println("Now rescaling the mean and the variance");
        System.out.println("Actual mean: "+bB.meanActual);
        System.out.println("Actual Variance: "+bB.varianceActual);
        System.out.println("Desired mean: "+bB.meanDesired);
        System.out.println("Desired Variance: "+bB.varianceDesired);

		for(int i=0; i< aA.rows-1; i++){
			for(int j=0;j<aA.cols-1;j++){
				aA.dataStore[i][j]=bB.rescaledField(aA.dataStore[i][j]);
			}
		}
		mn=bB.meanField(aA.dataStore);
		vr=bB.varOfField(aA.dataStore, mn);
		System.out.println("The mean and the variance of the rescaled lognormal field are: "+mn+" and: "+vr);*/
		//Now write to an ESRII ASCII file to be subsequently read by the OCNs programs
		
	    bB.write(oFName,aA.dataStore);
		
		System.out.println("This ends computation");

	}

}
