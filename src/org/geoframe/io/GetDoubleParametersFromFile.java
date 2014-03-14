/*
 * This file is part of GEOframe (http://abouthydrology.blogspot.com)
 * (C) Riccardo Rigon - http://www.ing.unitn.it/dica/hp/index_eng.php?user=rigon 
 * 
 * GEOframe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geoframe.io;
import java.io.File;

import org.geoframe.util.CreateFolder;

/**
 * gets a dataframe of double from a file. The structure of the file is commented at the end of the source code
 * of this class. The file cannot be huge since all is loaded in memory. 
 * @author riccardo rigon
 *
 */
public class GetDoubleParametersFromFile {
/**
 * The file to be read
 */
	String inputFileName;

/**
 * vnames is the vector of the name of the variables, vunits is the name of the vector containing the 
 * units in which the quantities are stored
 * 
 */
	public String[] vnames,vunits;
	private String[] var;
/**
 * The numbers of rows and columns to be read. One line is supposed to contain one row, since a row is read as
 * a whole.	
 */
	public int rows, cols;
/**
 * the data are stored here.
 *	
 */
	public double[][] vls;
	
	/**
	 * gets the parameters by giving a file Name explicitly. The core of Constructors action
	 * 
	 */
	
	private void getParameters(String inputFileName){
		
		
	/*	try{
			TextIO.readFile(inputFileName);
		}catch(IllegalArgumentException ee){
			
		}*/
		
		//GetComments assume to know how comments are written in the file. I use the ! (bang) symbol for them
		//For the moment, I do not store the comments
		new GetComments();
		//first the number of rows and columns is retrieved
		String rowsN=TextIO.getWord();
		//System.out.println(rowsN);
		rows=TextIO.getlnInt();
		//System.out.println(rows);
		String colsN=TextIO.getWord();
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
	/**
	 * Constructor. Since the file name is not given explicitle as input, it ask for it.
	 * 
	 */
	
	public GetDoubleParametersFromFile(){
        //In this example the file name is hard-coded. Any user has to change it.	
		String inputFileName=CreateFolder.getFileName();
		//String inputFileName="/Users/riccardo/Dropbox/A-WorkSpace/Simulations/TestOnJavaClasses/ExampleOfDataFrame.txt";
		getParameters(inputFileName);

	}
	
	/**
	 * Constructor. The file name is given explicitly as input here.
	 * 
	 */
	public GetDoubleParametersFromFile(String inputFileName){
        
		try{
			TextIO.readFile(inputFileName);
		}catch(IllegalArgumentException ee){
			
		}
		
		getParameters(inputFileName);

	}
	/**
	 * prints the Data frame
	 * 
	 */
	public void print(){
		for(int k=0;k<=cols;k++){
			System.out.print(vnames[k]+"\t");
		}
		System.out.print("\n");
		for(int k=0;k<=cols;k++){
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
	
	/**
	 * write the Data frame to a specified file
	 * 
	 */
	public void write(String outputFileName){
		TextIO.writeFile(outputFileName);
		TextIO.putf("!This is a copy of the file %s\n", this.inputFileName);
		TextIO.putf("!We could have done something more complex than this\n");
		TextIO.putf("Rows: %d\n",this.rows);
		TextIO.putf("Cols: %d\n",this.cols);
		//prints names
		for(int i=0;i<=cols;i++){
			TextIO.putf("%s ", this.vnames[i]);
		}
		TextIO.putf("\n");
		//prints units
		for(int i=0;i<=cols;i++){
			TextIO.putf("%s ", this.vunits[i]);
		}
		TextIO.putf("\n");
		//writes data
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				TextIO.putf(vls[i][j]+"\t");
			}
			TextIO.putf("\n");
		}
	}
	
	
	
	public static void main(String[] args) {

		System.out.println("This is the GetDoubleParametersFromFiles");
		
		//GetDoubleParametersFromFile aDataFrame=new GetDoubleParametersFromFile();
		//aDataFrame.print();
		System.out.println("Now using the second Constructor");
		String inputFileName="/Users/riccardo/Dropbox/A-WorkSpace/Simulations/TestOnJavaClasses/ExampleOfDataFrame.txt";
		GetDoubleParametersFromFile aDataFrameb=new GetDoubleParametersFromFile(inputFileName);
		aDataFrameb.inputFileName = inputFileName;
		aDataFrameb.print();
		//Writing to a new file
		String outputFileName="/Users/riccardo/Dropbox/A-WorkSpace/Simulations/TestOnJavaClasses/ExampleOfDataFrame_copy.txt";
		aDataFrameb.write(outputFileName);
		System.out.println("This ends computation");	
			}
	

}

/*
*This is how the file temporarily located at "/Users/riccardo/Dropbox/A-WorkSpace/Simulations/TestOnJavaClasses/ExampleOfDataFrame.txt" is:
*Just cat and paste it to a new file, if necessary and change the path file. File contents start below with the "!" and no "*" was used
!This is a file containing two numbers. But it could contain 2*Rows number of data
! It is assumed to be a 2D matrix of data. So the next lines contain the numer of rows and columns
Rows: 1 !No space between “Rows” and “:” This comment is discarded by default because it is known it has just one integer value. All of it is assumed to be in the same line. 
Cols: 2 !Again this comment is discarded by default. No space is allowed between “Cols” and “:” as above. The bang “!” is pleonastic here. 
Names: a b  !This comment is discarded by default.
Units: [-] [-]  !This comment is discarded by default.
1 -3  !Also this is discarded by default. As the above ones is read but subsequently discarded
2 4.76
*File ends the line above with the number 4.76. Please notice that: Comments starts with a "!" bang sign
*/
