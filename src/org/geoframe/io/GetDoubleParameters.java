package org.geoframe.io;
/**
 * gets any number of parameters from the StdIO. It works by inputting in the calling
 * main the names of the desired parameters.  
 * The number of parameters to get is assumed to be equal to the number of names.
 * 
 * @author Riccardo Rigon 2013
 * @version 1/16
 *
 */
//Want to build a class that can manage an arbitrary number of parameters of type double
public class GetDoubleParameters {

	/**
	 * @param param is the variable which will contain the parameters
	 */
	private double params[];
	/**
	 * 
	 * @param name is the name of the parameter to get
	 * @return c is the returned parameter value
	 */
	private double setParameterFromStdIO(String name){
		double c;
		
		TextIO.put("Please insert "+name+": ");
		c = TextIO.getDouble();
        // TextIO is used also because it check that the parameter is a double and if it is not a double it ask it again. So it is robust. 
		return c;
	}

	/**
	 * returns the parameters' values stored in params
	 * @return
	 */
	public  double[] getParameters() {
		return params;
	}

//print the parameters and their names
	/**
	 * prints the parameters (and their names)
	 * @param parNames
	 */
	public void print(String[] parNames){
		for(int i=0;i<parNames.length;i++){
			TextIO.put("The "+parNames[i]+" parameter is:"+params[i]+"\n");
		}
	}
//Constructor	
	/**
	 * populates the parameters class with values
	 * @param prmsname
	 */
	public GetDoubleParameters(String[] prmsname){
		int n;
		n=prmsname.length;
		this.params=new double[n];
		for(int i=0;i<n;i++){
		this.getParameters()[i]=setParameterFromStdIO(prmsname[i]);
		}
	}
	
	public static void main(String[] args) {
	
		System.out.println("This is the GetDoubleParameters main()");
		String[] nms={"First","Second","Third","Fourth","Fifth"};
		GetDoubleParameters prmtrs=new GetDoubleParameters(nms);  
		int j;
		for(int i=0;i<nms.length;i++){
			j=i+1;
			TextIO.put("The parameter "+j+" is:"+prmtrs.getParameters()[i]+"\n");
		}
		System.out.println("This ends the computation");
	}



}
