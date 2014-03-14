package org.geoframe.ocn;

import java.io.File;

import org.geoframe.exceptions.EofException;
import org.geoframe.exceptions.EsriAsciiHeaderException;
import org.geoframe.io.*;
import org.geoframe.util.CreateFolder;
import org.geoframe.util.GetFileName;
import org.geoframe.util.InfoFolder;

/**
 * Calculates  and stores the total contributing areas of a river basin.
 * @author Riccardo Rigon 2013
 * @version 1/16
 * @see Raster
 * @see IRiverBasin
 * 
 */
public class TCA extends Raster implements IRiverBasin {
/**
 * NO_VALUE is set to -1 for TCA
 * tca is a "matrix" of long instead than of int	
 */
	final static long NO_VALUE=-1;	
	public long[][] tca=null;
	public double energyExpenditure;
//Constructor
/**
 * sets and initializes a tca object.	
 * @param rows
 * @param cols
 */
	public TCA(int rows,int cols){
		this.rows=rows;
		this.cols=cols;
		tca=new long[rows][cols];
		//Feed the object
		for(int j=0;j<tca[0].length;j++){
			tca[0][j]=NO_VALUE;
		}
		for(int j=0;j<tca[0].length;j++){
			tca[tca.length-1][j]=NO_VALUE;
		}
		for(int i=0;i<tca.length;i++){
			tca[i][0]=NO_VALUE;
		}
		for(int i=0;i<tca.length;i++){
			tca[i][tca[0].length-1]=NO_VALUE;
		}
		//The core of the basin (Useless indeed since that the matrix has already been initialised to 0
		for(int i=1;i<tca.length-1;i++){
			for(int j=1;j<tca[0].length-1;j++){
				tca[i][j]=0;
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
		//Reading a int it is not so general. So it needs to be generalised.
		public  static TCA asciiRasterReader() throws EsriAsciiHeaderException, EofException{
		
				
			//Get the file name	
			GetFileName ifn=new GetFileName();
			//The file exists and now we read it:
			//Reads the data. Again two strategies: one is to read the file to the end of file, the other to read character by character the whole thing
			//I cannot avoid to instantiate a new variable for the header (unless I do it static: but it is meaningless)
			ReadEsriASCIIHeader  ar = new ReadEsriASCIIHeader(ifn.fileName);
			TCA asciiEsri=new TCA(ar.nRows,ar.nCols);
			//Now referencing the field inside the flow class
			asciiEsri.esriHeader = ar;
			
			asciiEsri.basinName = ifn.fileName;
		    for(int i=0;i<ar.nRows;i++){
		    	for(int j=0;j<ar.nCols;j++){
		    		asciiEsri.tca[i][j]=TextIO.getLong();
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
		public static  TCA asciiRasterReader(String fileName) throws EsriAsciiHeaderException, EofException{
			
			
			
			//Reads the data. Again two strategies: one is to read the file to the end of file, the other to read character by character the whole thing
			//I cannot avoid to instantiate a new variable for the header (unless I do it static: but it is meaningless)
			ReadEsriASCIIHeader  ar = new ReadEsriASCIIHeader(fileName);
			TCA asciiEsri=new TCA(ar.nRows,ar.nCols);
			//Now referencing the field inside the flow class
			asciiEsri.esriHeader = ar;
			
			asciiEsri.basinName = fileName;
		    for(int i=0;i<ar.nRows;i++){
		    	for(int j=0;j<ar.nCols;j++){
		    		asciiEsri.tca[i][j]=TextIO.getInt();
		    	}
		    }
		    TextIO.skipBlanks();
		    //Necessary to reset the buffer at the right place
		    TextIO.readStandardInput();
		    return asciiEsri;
		}
		
		public double energyExpenditure(double exp){
			
			double pEnergyExpenditure=0;
			
			for(int i=1;i<this.tca.length-1;i++){
				for(int j=1;j<this.tca[0].length-1;j++){
					pEnergyExpenditure+=Math.pow(this.tca[i][j],exp);
					
				}
				
			}
			return pEnergyExpenditure;
		}
		
	public void halo(){
		for(int j=1;j<this.tca.length-1;j++){
			this.tca[j][0]=this.tca[j][this.tca.length-2];
		}
		for(int j=1;j<this.tca.length-1;j++){
			this.tca[j][this.tca.length-1]=this.tca[j][1];
		}
	}
	public void rehalo(){
		for(int j=1;j<this.tca.length-1;j++){
			this.tca[j][0]=-1;
		}
		for(int j=1;j<this.tca.length-1;j++){
			this.tca[j][this.tca.length-1]=-1;
		}
	}		
	public static void main(String[] args) {
		System.out.println("This is TCA main()");
		
		System.out.println("Please enter the name of the river:");
		String nm=TextIO.getln();
		System.out.println("Please enter the number of rows:");
		int rws=TextIO.getInt();
		System.out.println("Please enters the number of columns:");
		int cls=TextIO.getInt();
		TCA tca=new TCA(rws,cls);
		tca.basinName=nm;
		tca.rows=rws;
		tca.cols=cls;
		
		tca.print();
        System.out.println("This ends computation");
	}
/**
 * prints the data in a tca object
 */
	@Override
	public void print() {
		
		for(int i=0;i<this.tca.length;i++){
			for(int j=0;j<this.tca[0].length;j++){
				System.out.print(this.tca[i][j]);
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
			 TextIO.putf("%d ",this.tca[i][j]);
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
			 TextIO.putf("%d ",this.tca[i][j]);
	    	}
	    	TextIO.putln();
	    }
	}
/**
 * Writes a file with the same name that flow has
 * @param info
 */

public void asciiRasterWriter(InfoFolder info) {
	
	String outputFileName=info.workingPathName+File.separator+info.shortName+"_tca.asc";
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
		 TextIO.putf("%d ",this.tca[i][j]);
    	}
    	TextIO.putln();
    }
}



}
