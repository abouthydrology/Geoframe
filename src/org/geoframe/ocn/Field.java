package org.geoframe.ocn;

import java.io.File;

import org.geoframe.exceptions.EofException;
import org.geoframe.exceptions.EsriAsciiHeaderException;
import org.geoframe.io.*;
import org.geoframe.util.CreateFolder;
import org.geoframe.util.GetFileName;
import org.geoframe.util.InfoFolder;

/**
 * Stores a raster field related to a river basin. This field, can be the contributing area
 * or anything else.
 * This assumes that the field is a real number.
 * @author Riccardo Rigon 2013
 * @version 1/16
 * @see Raster
 * @see IRiverBasin
 * 
 */
public class Field extends Raster implements IRiverBasin {
/**
 * NO_VALUE is set to -1 for TCA
 * tca is a "matrix" of long instead than of int	
 */
	static long NO_VALUE=-1;	
	public double[][] field=null;
	
	
	public double energyExpenditure;
//Constructor
/**
 * sets and initializes a tca object.	
 * @param rows
 * @param cols
 */
	public Field(int rows,int cols){
		this.rows=rows;
		this.cols=cols;
		field=new double[rows][cols];
		//Feed the objects
		for(int j=0;j<field[0].length;j++){
			field[0][j]=NO_VALUE;
		}
		for(int j=0;j<field[0].length;j++){
			field[field.length-1][j]=NO_VALUE;
		}
		for(int i=0;i<field.length;i++){
			field[i][0]=NO_VALUE;
		}
		for(int i=0;i<field.length;i++){
			field[i][field[0].length-1]=NO_VALUE;
		}
		//The core of the basin (Useless indeed since that the matrix has already been initialised to 0
		for(int i=1;i<field.length-1;i++){
			for(int j=1;j<field[0].length-1;j++){
				field[i][j]=0;
			}
		}


	}
	//ESRI Ascii rasterReader
	/**
		 * asciiRasterReader reads a File (containing int) in ESRI ASCII format
		 * @throws EsriAsciiHeaderException 
		 * @throws EofException 
		 * 
		 */
		//Declared static because it is weird to instantiate a new river when you do not know its size
		//The alternative is to put it in another class. For instance a class EsriAsciiRf
		//Is it a "static factory method ? - Item no 1 in Effective Java ?
		//Reading a int it is not so general. So it needs to be generalized.
		public  static Field asciiRasterReader() throws EsriAsciiHeaderException, EofException{
		
				
			//Get the file name	
			GetFileName ifn=new GetFileName();
			//The file exists and now we read it:
			//Reads the data. Again two strategies: one is to read the file to the end of file, the other to read character by character the whole thing
			//I cannot avoid to instantiate a new variable for the header (unless I do it static: but it is meaningless)
			ReadEsriASCIIHeader  ar = new ReadEsriASCIIHeader(ifn.fileName);
			Field asciiEsri=new Field(ar.nRows,ar.nCols);
			//Now referencing the field inside the flow class
			asciiEsri.esriHeader = ar;
			
			asciiEsri.basinName = ifn.fileName;
		    for(int i=0;i<ar.nRows;i++){
		    	for(int j=0;j<ar.nCols;j++){
		    		//This has been changed from the original
		    		asciiEsri.field[i][j]=TextIO.getDouble();
		    	}
		    }
		    TextIO.skipBlanks();
		    //Necessary to reset the buffer at the right place
		    TextIO.readStandardInput();
		    return asciiEsri;
		}
		
		/**
		 * asciiRasterReader(fileName) reads a File (containing long) in ESRI ASCII format.
		 * However, it get as input the filename and does not find it internally as 
		 * asciiRasterReader()
		 * 
		 * @throws EsriAsciiHeaderException 
		 * @throws EofException 
		 * @par fileName the name of the file to be read
		 */	
		public static  Field asciiRasterReader(String fileName) throws EsriAsciiHeaderException, EofException{
			
			
			
			//Reads the data. Again two strategies: one is to read the file to the end of file, the other to read character by character the whole thing
			//I cannot avoid to instantiate a new variable for the header (unless I do it static: but it is meaningless)
			ReadEsriASCIIHeader  ar = new ReadEsriASCIIHeader(fileName);
			Field asciiEsri=new Field(ar.nRows,ar.nCols);
			//Now referencing the field inside the flow class
			asciiEsri.esriHeader = ar;
			
			asciiEsri.basinName = fileName;
		    for(int i=0;i<ar.nRows;i++){
		    	for(int j=0;j<ar.nCols;j++){
		    		asciiEsri.field[i][j]=TextIO.getDouble();
		    	}
		    }
		    TextIO.skipBlanks();
		    //Necessary to reset the buffer at the right place
		    TextIO.readStandardInput();
		    return asciiEsri;
		}
		
		public double energyExpenditure(double exp){
			
			double pEnergyExpenditure=0;
			
			for(int i=1;i<this.field.length-1;i++){
				for(int j=1;j<this.field[0].length-1;j++){
					pEnergyExpenditure+=Math.pow(this.field[i][j],exp);
					
				}
				
			}
			return pEnergyExpenditure;
		}
		
	public void halo(){
		for(int j=1;j<this.field.length-1;j++){
			this.field[j][0]=this.field[j][this.field.length-2];
		}
		for(int j=1;j<this.field.length-1;j++){
			this.field[j][this.field.length-1]=this.field[j][1];
		}
	}
	public void rehalo(){
		for(int j=1;j<this.field.length-1;j++){
			this.field[j][0]=-1;
		}
		for(int j=1;j<this.field.length-1;j++){
			this.field[j][this.field.length-1]=-1;
		}
	}		
	public static void main(String[] args) {
		System.out.println("This is Field main()");
		
		System.out.println("Please enter the name of the river:");
		String nm=TextIO.getln();
		System.out.println("Please enter the number of rows:");
		int rws=TextIO.getInt();
		System.out.println("Please enters the number of columns:");
		int cls=TextIO.getInt();
		Field field=new Field(rws,cls);
		field.basinName=nm;
		field.rows=rws;
		field.cols=cls;
		
		field.print();
        System.out.println("This ends computation");
	}
/**
 * prints the data in a field object
 */
	@Override
	public void print() {
		
		for(int i=0;i<this.field.length;i++){
			for(int j=0;j<this.field[0].length;j++){
				System.out.print(this.field[i][j]);
				System.out.print("\t");
			}
			System.out.print("\n");
		}
	}
	/**
	 * writes an Esri ascii file containing the values of the total contributing area.
	 * 
	 */
	@Override
	public void asciiRasterWriter() {
		
		String outputFileName=CreateFolder.getFileName();
		//Rename the old file name if it exists;
		File outputFile=new File(outputFileName);
		if(outputFile.exists()){
			File oldFile=new File(outputFileName+".old");
			outputFile.renameTo(oldFile);
		}
		//Write the header: This is not so general, however, should take
		//variable inputs
	    TextIO.writeFile(outputFileName);
	    TextIO.putf("NCOLS %d\n", cols);
	    TextIO.putf("NROWS %d\n",rows);
	    TextIO.putf("XLLCORNER %d\n", 0);
	    TextIO.putf("YLLCORNER %d\n", 0);
	    TextIO.putf("CELLSIZE %d\n", 1000);
	    TextIO.putf("NODATA_VALUE %d\n", NO_VALUE);
	    for(int i=0;i<rows;i++){
	    	for(int j=0; j<cols;j++){
			 TextIO.putf("%f ",this.field[i][j]);
	    	}
	    	TextIO.putln();
	    }
	}
public void asciiRasterWriter(String outputFileName) {
		
		
		File outputFile=new File(outputFileName);
		if(outputFile.exists()){
			File oldFile=new File(outputFileName+".old");
			outputFile.renameTo(oldFile);
		}
		//Write the header: This is not so general, however, should take
		//variable inputs
	    TextIO.writeFile(outputFileName);
	    TextIO.putf("NCOLS %d\n", cols);
	    TextIO.putf("NROWS %d\n",rows);
	    TextIO.putf("XLLCORNER %d\n", 0);
	    TextIO.putf("YLLCORNER %d\n", 0);
	    TextIO.putf("CELLSIZE %d\n", 1000);
	    TextIO.putf("NODATA_VALUE %d\n", NO_VALUE);
	    for(int i=0;i<rows;i++){
	    	for(int j=0; j<cols;j++){
			 TextIO.putf("%f ",this.field[i][j]);
	    	}
	    	TextIO.putln();
	    }
	}
/**
 * Writes a file with the same name that flow has
 * @param info
 */

public void asciiRasterWriter(InfoFolder info) {
	
	String outputFileName=info.workingPathName+File.separator+info.shortName+"_field.asc";
	//Write the header: This is not so general, however, should take
	//variable inputs
    TextIO.writeFile(outputFileName);
    TextIO.putf("NCOLS %d\n", cols);
    TextIO.putf("NROWS %d\n",rows);
    TextIO.putf("XLLCORNER %d\n", 0);
    TextIO.putf("YLLCORNER %d\n", 0);
    TextIO.putf("CELLSIZE %d\n", 1000);
    TextIO.putf("NODATA_VALUE %d\n", NO_VALUE);
    for(int i=0;i<rows;i++){
    	for(int j=0; j<cols;j++){
		 TextIO.putf("%f ",this.field[i][j]);
    	}
    	TextIO.putln();
    }
}



}
