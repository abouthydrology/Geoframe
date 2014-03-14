package org.geoframe.ocn;

import java.io.File;

import org.geoframe.exceptions.*;
import org.geoframe.io.TextIO;
//import org.geoframe.ocn.RiverBasin.esriHeader;
import org.geoframe.util.CreateFolder;

public class ReadEsriASCIIHeader {
    
    public int nCols,nRows,x,y,cellSize,noDataValue;
    //Probably you want to know also which type of coord you are dealing with
	private enum esriHeader {NCOLS,NROWS,XLLCORNER,XLLCENTER,YLLCORNER,YLLCENTER,CELLSIZE,NODATA_VALUE};

	public ReadEsriASCIIHeader(String inputFileName) throws EsriAsciiHeaderException{
	/*//Get the file name	
	String inputFileName=Folder.getFileName();
	//Rename the old file name if it exists;
	File inputFile=new File(inputFileName);
	while(!inputFile.exists()){
		inputFileName=Folder.getFileName();
		inputFile=new File(inputFileName);
	}*/
	//The file exists and now we read it:
	//Opens the File for reading
    TextIO.readFile(inputFileName);
	//Reads the the header and check its consistency. Two strategies of  programming are possible.
    //One is to read the whole file and store it before giving any error. The other is to read things step by step
    //The second strategy is chosen here.
    String nColsName=TextIO.getWord();
    if(!nColsName.equals(esriHeader.NCOLS.toString())){
    	//System.out.println("The files does not contains a proper header");
    	throw new EsriAsciiHeaderException("Columns number unspecified");
    }
    nCols=TextIO.getInt();
    String nRowsName=TextIO.getWord();
    if(!nRowsName.equals(esriHeader.NROWS.toString())){
    	throw new EsriAsciiHeaderException("Rows number unspecified");
    }
    nRows=TextIO.getInt();
    //System.out.println(nCols+" "+nRows);
    String xName=TextIO.getWord();
    if(!(xName.equals(esriHeader.XLLCORNER.toString()) || xName.equals(esriHeader.XLLCENTER.toString()))){
    	throw new EsriAsciiHeaderException("X corner unspecified");
    }
    x=TextIO.getInt();
    String yName=TextIO.getWord();
    if(!(yName.equals(esriHeader.YLLCORNER.toString()) || yName.equals(esriHeader.YLLCENTER.toString()))){
    	throw new EsriAsciiHeaderException("Y corner unspecified");
    }
    y=TextIO.getInt();
    //System.out.println(x+" "+y);
    String cellSizeName=TextIO.getWord();
    if(!cellSizeName.equals(esriHeader.CELLSIZE.toString())){
    	throw new EsriAsciiHeaderException("Cell SIZE unspecified");
    }
    cellSize=TextIO.getInt();
    String noDataValueName=TextIO.getWord();
    if(!noDataValueName.equals(esriHeader.NODATA_VALUE.toString())){
    	throw new EsriAsciiHeaderException("NO Data corner unspecified");
    }
    noDataValue=TextIO.getInt();
    //System.out.println(cellSize+" "+noDataValue);
	}
	
	public static void esriAsciiPrint(ReadEsriASCIIHeader ar){
		
		//Here we could have been more general using the ReadEsriACIIHeader enum field
		System.out.println("NCOLS "+ar.nCols);
		System.out.println("NROWS "+ar.nRows);
		System.out.println("XLL "+ar.x);
		System.out.println("YLL "+ar.y);
		System.out.println("CELLSIZE "+ar.cellSize);
		System.out.println("NO_DATAVALUE "+ar.noDataValue);
	}
	
	public static void main(String[] args) throws EsriAsciiHeaderException {
		
		//Get FileName
		String inputFileName=CreateFolder.getFileName();
		//Rename the old file name if it exists;
		File inputFile=new File(inputFileName);
		while(!inputFile.exists()){
			inputFileName=CreateFolder.getFileName();
			inputFile=new File(inputFileName);
		}
		System.out.println("This is EsriAsciiHeaderReader main()");
		ReadEsriASCIIHeader ar = new ReadEsriASCIIHeader(inputFileName);
		/*System.out.println("NCOLS "+ar.nCols);
		System.out.println("NROWS "+ar.nRows);
		System.out.println("XLL "+ar.x);
		System.out.println("YLL "+ar.y);
		System.out.println("CELLSIZE "+ar.cellSize);
		System.out.println("NO_DATAVALUE "+ar.noDataValue);
		System.out.println("This ends computation");*/
		esriAsciiPrint(ar);

	}

}
