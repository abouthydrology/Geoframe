package org.geoframe.ocn;

import org.geoframe.exceptions.EofException;
import org.geoframe.exceptions.EsriAsciiHeaderException;
import org.geoframe.io.TextIO;
import org.geoframe.util.GetFileName;

// This class  cannot extend IRiverBasin since it cannot implements the 
//print() and the Writer because is missing the data field
public class Raster {
	
	String basinName;
	//Can I do the next without declaring it static ?
	public  ReadEsriASCIIHeader esriHeader=null;
	public int rows,cols;

	/*//ESRI Ascii rasterReader
		*//**
		 * asciiRasterReader reads a File (containing int) in ESRI ASCII format
		 * @throws EsriAsciiHeaderException 
		 * @throws EofException 
		 * 
		 *//*
		//Declared static because it is weird to instantiate a new river when you do not know its size
		//The alternative is to put it in another class. For instance a class EsriAsciiRf
		//Is it a "static factory method ? - Item no 1 in Effective Java ?
		//Reading a int it is not so general. So it needs to be generalised.
		public  static Flow asciiRasterReader() throws EsriAsciiHeaderException, EofException{
		
				
			//Get the file name	
			GetFileName ifn=new GetFileName();
			//The file exists and now we read it:
			//Reads the data. Again two strategies: one is to read the file to the end of file, the other to read character by character the whole thing
			//I cannot avoid to instantiate a new variable for the header (unless I do it static: but it is meaningless)
			ReadEsriASCIIHeader  ar = new ReadEsriASCIIHeader(ifn.fileName);
			Flow asciiEsri=new Flow(ar.nRows,ar.nCols);
			//Now referencing the field inside the flow class
			asciiEsri.esriHeader = ar;
			
			asciiEsri.basinName = ifn.fileName;
		    for(int i=0;i<ar.nRows;i++){
		    	for(int j=0;j<ar.nCols;j++){
		    		asciiEsri.flow[i][j]=TextIO.getInt();
		    	}
		    }
		    TextIO.skipBlanks();
		    
		     Trying to control if there are other data
			do{
				lst=TextIO.getAnyChar();
			}while(lst=='\n');
				
			boolean eof=TextIO.eof();
			if(!eof){
		    	throw new EofException();
		    } else {
		    	//Terminate
		    }
		    
		    TextIO.readStandardInput();
		    return asciiEsri;
		}	
		*/

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


}
