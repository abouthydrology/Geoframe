package org.geoframe.util;

import org.geoframe.io.TextIO;

import java.io.File;

/**
 * 
 * A generic ASCII File reader. Now a quick and dirty solution waiting to apply the Strategy
 * pattern to accomodate different file structures (e.g. http://en.wikipedia.org/wiki/Strategy_pattern)
 * 
 * @author riccardo
 *
 */

public class GenericAsciiFileReader {
	
	File inputFile;
	String inputFileName;
	String[] fileHeader;
	public int lines,cols;
	public int rows;
	public double[][] dataStore;
	
	public boolean initialize()
	{
	
		System.out.println("Enter the Number of headers' lines");
		this.lines=TextIO.getInt();
		System.out.println("Enter the Number of rows");
		this.rows=TextIO.getInt();
		System.out.println("Enter the Number of columns");
		this.cols=TextIO.getInt();
		this.dataStore= new double[rows][cols];
	
		return true;
	}
	
	public boolean readHeader(){
		
		//This assume that the user has seen the file
		fileHeader=new String[this.lines];
		TextIO.readFile(this.inputFileName);
		for(int i=0;i<lines;i++){
			fileHeader[i]=TextIO.getlnString();
		}
		//Warning Not Closing Yet ! TextIO.readStandardInput();
		
	    return true;
		
	}
	
	public boolean readData(){
     int count=0;
		for(int i=0;i<rows;i++){
			for(int j=0; j<cols; j++){
				this.dataStore[i][j]=TextIO.getDouble();
				count++;
				System.out.println(count+": "+this.dataStore[i][j]);
				
			}		  
		}
		TextIO.readStandardInput();
		return true;
	}
	
	public void print(){
		for(int i=0;i<fileHeader.length;i++){
			System.out.println(fileHeader[i]);
		}
	}
	
	public void printRows(int init,int end){
		for(int i=init;i<=end;i++){
			for(int j=0;j<this.cols;j++){
				System.out.print(this.dataStore[i][j]+"\t");
			}
			System.out.print("\n");
		}
	}
	
	//Constructor
	public GenericAsciiFileReader(String inputFileName){
		this.inputFileName=inputFileName;
		this.inputFile=new File(this.inputFileName);
	}
	
	public static void main(String[] args){
		System.out.println("This is GenericAsciiFileReader main()");
		String fName="/Users/riccardo/Dropbox/A-Workspace/ARPaper2013-II/RF_Ric/ric1.dat";
		GenericAsciiFileReader aGenericFileReader=new GenericAsciiFileReader(fName);
		if(!aGenericFileReader.initialize()){
			System.out.println("It was not possible to initialize the reading of the file");
		}else{
			System.out.println("File ready to be read");
		}
		
		if(!aGenericFileReader.readHeader()){
			System.out.println("It was not possible to read the file");
		}else{
			aGenericFileReader.print();
		}
		if(!aGenericFileReader.readData()){
			System.out.println("It was not possible to read the data");
		}else{
			System.out.println("Data read");
		}
		aGenericFileReader.printRows(0, 10);
		System.out.println("This ends computation");
	}

}
