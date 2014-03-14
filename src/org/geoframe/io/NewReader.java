package org.geoframe.io;

import java.io.File;

public class NewReader {
	
	//File inputFile;
	
	public String[] vnames,vunits;
	private String[] var;
	public int rows, cols;
	
	public double[][] vls;
	
	private void getParameters(String inputFileName){
			
			new GetComments();
			//first the number of rows and columns is retrieved
			String rowsN=TextIO.getWord();
			//if(!rowsN.equals("Rows:")){
				//throw new RuntimeException("The file format is not correct. Rows: keyword is absent");
			//}
			//System.out.println(rowsN);
			rows=TextIO.getlnInt();
			//System.out.println(rows);
			String colsN=TextIO.getWord();
			//if(!rowsN.equals("Cols:")){
				//throw new RuntimeException("The file format is not correct. Cols: keyword is absent");
			//}
			//System.out.println(colsN);
			cols=TextIO.getlnInt();
			//System.out.println(cols);
			//Here the variables names are retrieved
			String variablesNames=TextIO.getlnString();
			//Here the units are retrieved
			String variablesUnits=TextIO.getlnString();

			//The number of names must be equal to the number of cols + 1. The first field names followed by ':' (with no space)
			
			this.vnames=variablesNames.split(" ");
			for(int k=0;k<cols+1;k++){
				vnames[k]=vnames[k].replaceAll("\\s", "");
				//System.out.println("["+k+"] "+vnames[k]);
			}
			
			this.vunits=variablesUnits.split(" ",vnames.length);
			for(int k=0;k<cols+1;k++){
				vunits[k]=vunits[k].replaceAll("\\s", "");
				//System.out.println("["+k+"] "+vunits[k]);
			}
			
			String variables;
			this.vls=new double[rows][cols];
			
			
			//All the values are retrieved as strings
			for(int i=0;i<rows;i++){
				variables=TextIO.getlnString();
				this.var=variables.split(" ",variables.length());
				var[i]=var[i].replaceAll("\\s", "");
				//All the variables retrieved are converted to double
				for(int k=0;k< cols;k++){			
					vls[i][k]=Double.parseDouble(var[k]);
				}
				
				
			}
			
		}
	
	public void print(){
		
		for(int k=1;k<=cols;k++){
			System.out.print(vnames[k]+"\t");
		}
		System.out.print("\n");
		for(int k=1;k<=cols;k++){
			System.out.print(vunits[k]+"\t");
		}
		System.out.print("\n");
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				System.out.print(vls[i][j]+"\t");
			}
			System.out.print("\n");
		}
	}
	
	public void write(String outputFileName){
		
		TextIO.writeFile(outputFileName);
		
		
	}
	
	public NewReader(String inputFileName){
		
		
		try{
			TextIO.readFile(inputFileName);
		}catch(IllegalArgumentException ee){
			System.out.println(ee);
		}
		
		getParameters(inputFileName);
		
	}
	
public NewReader(){
		//Retrieve the name of the file
	    //Read the file
	    //getParameters(inputFileName);
	}
	
	public static void main(String[] args) {
		
		System.out.println("This is NewReader main()");
		String inputFileName="/Users/riccardo/Dropbox/A-WorkSpace/Simulations/TestOnJavaClasses/ExampleOfDataFrame.txt";
		NewReader aDataFrameb=new NewReader(inputFileName);
		aDataFrameb.print();
		String outputFileName="/Users/riccardo/Dropbox/A-WorkSpace/Simulations/TestOnJavaClasses/ExampleOfDataFrameOutput.txt";
		aDataFrameb.write(outputFileName);
		System.out.println("This ends computation");
		
	}

}
