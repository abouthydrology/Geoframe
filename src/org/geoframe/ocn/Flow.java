package org.geoframe.ocn;
import java.io.File;

import org.geoframe.exceptions.*;
import org.geoframe.io.TextIO;
import org.geoframe.util.CreateFolder;
import org.geoframe.util.GetFileName;
import org.geoframe.util.InfoFolder;
//import org.geoframe.util.GetFileName;
/**
 * River Basin class contains the structure of the river basins as encoded according to the D8 flow. It also contains methods
 * for working on river networks and it is the superclass of EdenAL and OCNs, two types of intresting "river" networks 
 * 
 * @author Riccardo Rigon
 * 
 * @reference "Rodriguez-Iturbe, I. and Rinaldo R., Fractal River Networks, chance and self-organization, CUP 1997" 
 *
 */
public class Flow extends Raster implements IRiverBasin {
	
	static final int NO_VALUE=9;	
	public int[][] flow;
	//Private 
	protected static int[][] nextPossibilities=new int[9][3];
    //the next to avoid useless loops for searching the next possibilities;
    protected static int no;
	/**
 * RiverBasin constructor. It takes the number of rows and colums of the "matrix" embedding the river network structure.
 * Please reminds that first and last row and column are marked by "novalues", and therefore the river network uses effectively
 * rows-2 rows and cols-2 columns.
 * 
 * @param rows
 * @param cols
 */
	//Constructor
	public Flow(int rows,int cols){
		this.rows=rows;
		this.cols=cols;
		flow=new int[rows][cols];
		//Feed the object
		for(int j=0;j<flow[0].length;j++){
			flow[0][j]=NO_VALUE;
		}
		for(int j=0;j<flow[0].length;j++){
			flow[flow.length-1][j]=NO_VALUE;
		}
		for(int i=0;i<flow.length;i++){
			flow[i][0]=NO_VALUE;
		}
		for(int i=0;i<flow.length;i++){
			flow[i][flow[0].length-1]=NO_VALUE;
		}
		//The core of the basin (Useless indeed since that the matrix has already been initialised to 0
		for(int i=1;i<flow.length-1;i++){
			for(int j=1;j<flow[0].length-1;j++){
				flow[i][j]=0;
			}
		}
	}
	

/* (non-Javadoc)
 * @see org.geoframe.ocn.RiverBasin#flowPrint()
 */
	@Override
	public void print(){
		for(int i=0;i<this.flow.length;i++){
			for(int j=0;j<this.flow[0].length;j++){
				System.out.print(this.flow[i][j]);
				System.out.print("\t");
			}
			System.out.print("\n");
		}
	}
	//With a static method (there will be one for all the instances)
/**
 * Prints the river network to the standard output. Differently from the above method it is static.
 * @param rb is the river basin to print
 */
	public static void print(Flow rb){
		System.out.println(rb.rows+" "+rb.cols);
		for(int i=0;i<rb.flow.length;i++){
			for(int j=0;j<rb.flow[0].length;j++){
				System.out.print(rb.flow[i][j]);
				System.out.print("\t");
			}
			System.out.print("\n");
		}
	}	
	//Move down along the river network of one step
/* (non-Javadoc)
 * @see org.geoframe.ocn.RiverBasin#goDownstream(int[])
 */
	
	public int[] goDownstream(int[] pos){
//Maybe we can move the following 'flow' to a private static field So we do not
//have to allocate any time ?	
		int[] newFlowPos=new int[2];
		//If it is not an outlet
		if(this.flow[pos[0]][pos[1]]!=10){
		newFlowPos[0]=pos[0]+neighbors[this.flow[pos[0]][pos[1]]][0];
		newFlowPos[1]=pos[1]+neighbors[this.flow[pos[0]][pos[1]]][1];	
		}else {
			newFlowPos=pos;
		}
		return newFlowPos;
	}
	public int[] goDownstream(int[] pos, boolean isPeriodic){
		
		//Maybe we can move the following 'flow' to a private static field So we do not
		//have to allocate any time ?	
				int[] newFlowPos=new int[2];
				//If it is not an outlet
				//System.out.println("$$"+pos[0]+" "+pos[1]);
				if(this.flow[pos[0]][pos[1]]!=10){
					newFlowPos[0]=pos[0]+neighbors[this.flow[pos[0]][pos[1]]][0];
					newFlowPos[1]=pos[1]+neighbors[this.flow[pos[0]][pos[1]]][1];	
				//To test what if isPeriodic is true, is not even necessary
					//System.out.println("$$$"+newFlowPos[0]+" "+newFlowPos[1]);
					if(newFlowPos[1]==0){
						newFlowPos[1]=this.flow[0].length-2;
					} else if(newFlowPos[1]==this.flow[0].length-1){
						newFlowPos[1]=1;
					}
					//System.out.println("XXXXXXXX");
				}else {
					newFlowPos=pos;
				}
				return newFlowPos;
			}
    /** 
     * Select the possible directions of flow for a given basin point. The method scan the next neighbors
     * according to D8 (CIT) and populates an ArrayList with values of the directions that are allowed.
     * @param nextFlow
     */

	//Warning: the next method is strongly dependent on neighbors. It the neighbors matrix is changed, everything
	//is screwed up. Probably not a good example of design.
	protected void selectPossibleFlow(int[] nextFlow) {
		
		int i,j;
		    no=0;
		    //First scan the odd directions in neighbors
			for(int k=1;k<9;k+=2){

				i=nextFlow[0]+neighbors[k][0];
				j=nextFlow[1]+neighbors[k][1];
				if(this.flow[i][j] >0 && this.flow[i][j] < 9){
					nextPossibilities[no][0]=i;
					nextPossibilities[no][1]=j;
					no++;
				}else if(this.flow[i][j]==10){
					nextPossibilities[no][0]=i;
					nextPossibilities[no][1]=j;
					no++;
				}
				
			}
			//The even directions in neighbors must be checked for crossings

			int k=2;
			
				i=nextFlow[0]+neighbors[k][0];
				j=nextFlow[1]+neighbors[k][1];

				if(this.flow[i][j] >0 && this.flow[i][j] < 9){
					if(this.flow[nextFlow[0]+neighbors[3][0]][nextFlow[1]+neighbors[3][1]] == 8 || this.flow[nextFlow[0]+neighbors[1][0]][nextFlow[1]+neighbors[1][1]]==4){
						//Do nothing
					}else{
					nextPossibilities[no][0]=i;
					nextPossibilities[no][1]=j;
					no++;
					}
				}else if(this.flow[i][j]==10){
					nextPossibilities[no][0]=i;
					nextPossibilities[no][1]=j;
					no++;
				}

				k=4;
				
				i=nextFlow[0]+neighbors[k][0];
				j=nextFlow[1]+neighbors[k][1];

				if(this.flow[i][j] >0 && this.flow[i][j] < 9){
					if(this.flow[nextFlow[0]+neighbors[3][0]][nextFlow[1]+neighbors[3][1]] == 6 || this.flow[nextFlow[0]+neighbors[5][0]][nextFlow[1]+neighbors[5][1]]==2){
						//Do nothing
					}else{
					nextPossibilities[no][0]=i;
					nextPossibilities[no][1]=j;
					no++;
					}
				}else if(this.flow[i][j]==10){
					nextPossibilities[no][0]=i;
					nextPossibilities[no][1]=j;
					no++;
				}
				
				k=6;
				
				i=nextFlow[0]+neighbors[k][0];
				j=nextFlow[1]+neighbors[k][1];

				if(this.flow[i][j] >0 && this.flow[i][j] < 9){
					if(this.flow[nextFlow[0]+neighbors[5][0]][nextFlow[1]+neighbors[5][1]] == 8 || this.flow[nextFlow[0]+neighbors[7][0]][nextFlow[1]+neighbors[7][1]]==4){
						//Do nothing
					}else{
					nextPossibilities[no][0]=i;
					nextPossibilities[no][1]=j;
					no++;
					}
				}else if(this.flow[i][j]==10){
					nextPossibilities[no][0]=i;
					nextPossibilities[no][1]=j;
					no++;
				}					
				k=8;
				
				i=nextFlow[0]+neighbors[k][0];
				j=nextFlow[1]+neighbors[k][1];

				if(this.flow[i][j] >0 && this.flow[i][j] < 9){
					if(this.flow[nextFlow[0]+neighbors[7][0]][nextFlow[1]+neighbors[7][1]] == 2 || this.flow[nextFlow[0]+neighbors[1][0]][nextFlow[1]+neighbors[1][1]]==6){
						//Do nothing
					}else{
					nextPossibilities[no][0]=i;
					nextPossibilities[no][1]=j;
					no++;
					}
				}else if(this.flow[i][j]==10){
					nextPossibilities[no][0]=i;
					nextPossibilities[no][1]=j;
					no++;
				}					
			
			no--;
		}
	//Warning: the next method is strongly dependent on neighbors. It the neighbors matrix is changed, everything
	//is screwed up. Probably not a good example of design.
	protected int[][] setPossibleFlow(int[] nextFlow) {
		
		int i,j;
		int no=0;
		int[][] nextPossibilities=new int[neighbors.length][2];
		
		    //First scan the odd directions in neighbors
			for(int k=1;k<neighbors.length;k+=2){

				i=nextFlow[0]+neighbors[k][0];
				j=nextFlow[1]+neighbors[k][1];
				if(this.flow[i][j] >0 && this.flow[i][j] < 9){
					nextPossibilities[no][0]=i;
					nextPossibilities[no][1]=j;
					no++;
				}else if(this.flow[i][j]==10){
					nextPossibilities[no][0]=i;
					nextPossibilities[no][1]=j;
					no++;
				}
				
			}
			//The even directions in neighbors must be checked for crossings

			int k=2;
			
				i=nextFlow[0]+neighbors[k][0];
				j=nextFlow[1]+neighbors[k][1];

				if(this.flow[i][j] >0 && this.flow[i][j] < neighbors.length){
					if(this.flow[nextFlow[0]+neighbors[3][0]][nextFlow[1]+neighbors[3][1]] == 8 || this.flow[nextFlow[0]+neighbors[1][0]][nextFlow[1]+neighbors[1][1]]==4){
						//Do nothing
					}else{
					nextPossibilities[no][0]=i;
					nextPossibilities[no][1]=j;
					no++;
					}
				}else if(this.flow[i][j]==10){
					nextPossibilities[no][0]=i;
					nextPossibilities[no][1]=j;
					no++;
				}

				k=4;
				
				i=nextFlow[0]+neighbors[k][0];
				j=nextFlow[1]+neighbors[k][1];

				if(this.flow[i][j] >0 && this.flow[i][j] < neighbors.length){
					if(this.flow[nextFlow[0]+neighbors[3][0]][nextFlow[1]+neighbors[3][1]] == 6 || this.flow[nextFlow[0]+neighbors[5][0]][nextFlow[1]+neighbors[5][1]]==2){
						//Do nothing
					}else{
					nextPossibilities[no][0]=i;
					nextPossibilities[no][1]=j;
					no++;
					}
				}else if(this.flow[i][j]==10){
					nextPossibilities[no][0]=i;
					nextPossibilities[no][1]=j;
					no++;
				}
				
				k=6;
				
				i=nextFlow[0]+neighbors[k][0];
				j=nextFlow[1]+neighbors[k][1];

				if(this.flow[i][j] >0 && this.flow[i][j] < neighbors.length){
					if(this.flow[nextFlow[0]+neighbors[5][0]][nextFlow[1]+neighbors[5][1]] == 8 || this.flow[nextFlow[0]+neighbors[7][0]][nextFlow[1]+neighbors[7][1]]==4){
						//Do nothing
					}else{
					nextPossibilities[no][0]=i;
					nextPossibilities[no][1]=j;
					no++;
					}
				}else if(this.flow[i][j]==10){
					nextPossibilities[no][0]=i;
					nextPossibilities[no][1]=j;
					no++;
				}					
				k=8;
				
				i=nextFlow[0]+neighbors[k][0];
				j=nextFlow[1]+neighbors[k][1];

				if(this.flow[i][j] >0 && this.flow[i][j] < neighbors.length){
					if(this.flow[nextFlow[0]+neighbors[7][0]][nextFlow[1]+neighbors[7][1]] == 2 || this.flow[nextFlow[0]+neighbors[1][0]][nextFlow[1]+neighbors[1][1]]==6){
						//Do nothing
					}else{
					nextPossibilities[no][0]=i;
					nextPossibilities[no][1]=j;
					no++;
					}
				}else if(this.flow[i][j]==10){
					nextPossibilities[no][0]=i;
					nextPossibilities[no][1]=j;
					no++;
				}					
			
			no--;
			
			return nextPossibilities;
		
		}
	//ESRI Ascii rasterWriter
	/* (non-Javadoc)
	 * @see org.geoframe.ocn.RiverBasin#asciiRasterWriter()
	 */
	//@Override
	public void asciiRasterWriter(){
		
		String outputFileName=CreateFolder.getFileName();
		//Rename the old file name if it exists;
		File outputFile=new File(outputFileName);
		if(outputFile.exists()){
			File oldFile=new File(outputFileName+".old");
			outputFile.renameTo(oldFile);
		}
		//Write the header
	    TextIO.writeFile(outputFileName);
	    TextIO.putf("NCOLS %d\n", cols);
	    TextIO.putf("NROWS %d\n",rows);
	    TextIO.putf("XLLCORNER %d\n", 0);
	    TextIO.putf("YLLCORNER %d\n", 0);
	    TextIO.putf("CELLSIZE %d\n", 1000);
	    TextIO.putf("NODATA_VALUE %d\n", NO_VALUE);
	    for(int i=0;i<rows;i++){
	    	for(int j=0; j<cols;j++){
			 TextIO.putf("%d ",this.flow[i][j]);
	    	}
	    	TextIO.putln();
	    }
		
	}
	
public void asciiRasterWriter(String outputFileName){
		
		File outputFile=new File(outputFileName);
		if(outputFile.exists()){
			File oldFile=new File(outputFileName+".old");
			outputFile.renameTo(oldFile);
		}
		//Write the header
	    TextIO.writeFile(outputFileName);
	    TextIO.putf("NCOLS %d\n", cols);
	    TextIO.putf("NROWS %d\n",rows);
	    TextIO.putf("XLLCORNER %d\n", 0);
	    TextIO.putf("YLLCORNER %d\n", 0);
	    TextIO.putf("CELLSIZE %d\n", 1000);
	    TextIO.putf("NODATA_VALUE %d\n", NO_VALUE);
	    for(int i=0;i<rows;i++){
	    	for(int j=0; j<cols;j++){
			 TextIO.putf("%d ",this.flow[i][j]);
	    	}
	    	TextIO.putln();
	    }
		
	}
	/**
	 * gets the file name for writing  Esri Ascii files containin flow (suffix _asc) and tca (suffix _tca)
	 * TODO Also creates a directory containing the two files and a README FILES
	 * @return the @ see infoFolder object containing various information 
	 */
	public InfoFolder asciiRasterWriterGF(){
		
		//If we allocate a new Folder, we could, later,
		//retrieve several information, not only the file name
		
		String oFileName=CreateFolder.getDirectoryName();
		//System.out.println("@@@@@@@@@@");
		InfoFolder infoFolder=new InfoFolder(oFileName);
		System.out.println("The name of the working directory is "+infoFolder.workingPathName);
		System.out.println("The name of the file is "+infoFolder.shortName);
	   
        //Creates the directory if it does not exist yet
		infoFolder.workingPath=new File(oFileName);
		if(!infoFolder.workingPath.exists()){
			infoFolder.workingPath.mkdirs();
		}
		//Renames the InfoFile information
		infoFolder.workingPathName=oFileName;
		//Created finally the new file
		String outputFileName=infoFolder.workingPathName+File.separator+infoFolder.shortName+"_flow.asc";
		//Renames the old file name if it exists;	
		File outputFile=new File(outputFileName);
		//The information in InfoFolder could be used here but we still need outputFile
		// for the rename operation
		if(outputFile.exists()){
			File oldFile=new File(outputFileName+".old");
			outputFile.renameTo(oldFile);
		}
		//Writes the header
		TextIO.writeFile(outputFileName);
	    TextIO.putf("NCOLS %d\n", cols);
	    TextIO.putf("NROWS %d\n",rows);
	    TextIO.putf("XLLCORNER %d\n", 0);
	    TextIO.putf("YLLCORNER %d\n", 0);
	    TextIO.putf("CELLSIZE %d\n", 1000);
	    TextIO.putf("NODATA_VALUE %d\n", NO_VALUE);
	    //Write the data
	    for(int i=0;i<rows;i++){
	    	for(int j=0; j<cols;j++){
			 TextIO.putf("%d ",this.flow[i][j]);
	    	}
	    	TextIO.putln();
	    }
		return infoFolder;
	}
	
	private  void printFirstRiverRows(int no){
		
		if(no > rows) no=rows;
		
		for(int i=0;i < no ;i++){
			for(int j=0;j< this.flow[0].length;j++){
				System.out.print(this.flow[i][j]+"\t");
			}
			System.out.println("\n");
		}
		
	}
	
	/**
	 * isDrained check if a point is a "source" or inherit drainage from other
	 * points.
	 * 
	 * @param point is the actual position where the condition of receiving water
	 * from up-slope is checked
	 * @return true if the point has at least one another point draining into it
	 * 
	 */
	
	public boolean isDrained(int[] point){
		
		for(int k=1; k< 4; k++){
		if(this.flow[point[0]+neighbors[k][0]][point[1]+neighbors[k][1]]==((k+4)%8)){
			return true;
		}else{
			//Do nothing
		}
		}
		//Notice below the use of the  braces to limit the scope of k;
		{ //k=4 is a special case
		int k=4;
		if(this.flow[point[0]+neighbors[k][0]][point[1]+neighbors[k][1]]==8){
			return true;
		}else{
			//Do nothing
		}
		}
		for(int k=5; k< neighbors.length; k++){
		if(this.flow[point[0]+neighbors[k][0]][point[1]+neighbors[k][1]]==((k+4)%8)){
			return true;
		}else{
			//Do nothing
		}
		}
		return false;
	}
	
/*	*//**
	 * isDrained check if a point is a "source" or inherit drainage from other
	 * points.
	 * 
	 * @param point is the actual position where the condition of receiving water
	 * from up-slope is checked
	 * @return true if the point has at least one another point draining into it
	 * 
	 */
	
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
			    //Necessary to reset the buffer at the right place
			    TextIO.readStandardInput();
			    return asciiEsri;
			}
			
			public static  Flow asciiRasterReader(String fileName) throws EsriAsciiHeaderException, EofException{
				
				
				
				//The file exists and now we read it:
				//Reads the data. Again two strategies: one is to read the file to the end of file, the other to read character by character the whole thing
				//I cannot avoid to instantiate a new variable for the header (unless I do it static: but it is meaningless)
				ReadEsriASCIIHeader  ar = new ReadEsriASCIIHeader(fileName);
				Flow asciiEsri=new Flow(ar.nRows,ar.nCols);
				//Now referencing the field inside the flow class
				asciiEsri.esriHeader = ar;
				
				asciiEsri.basinName = fileName;
			    for(int i=0;i<ar.nRows;i++){
			    	for(int j=0;j<ar.nCols;j++){
			    		asciiEsri.flow[i][j]=TextIO.getInt();
			    	}
			    }
			    TextIO.skipBlanks();
			    //Necessary to reset the buffer at the right place
			    TextIO.readStandardInput();
			    return asciiEsri;
			}
			
			public void halo(){
				for(int j=1;j<this.flow.length-1;j++){
					this.flow[j][0]=this.flow[j][this.flow.length-2];
				}
				for(int j=1;j<this.flow.length-1;j++){
					this.flow[j][this.flow.length-1]=this.flow[j][1];
				}
			}		
			public void reHalo(){
				for(int j=1;j<this.flow.length-1;j++){
					this.flow[j][0]=9;
				}
				for(int j=1;j<this.flow.length-1;j++){
					this.flow[j][this.flow.length-1]=9;
				}
			}
	//Main
	public static void main(String[] args) {
		System.out.println("This is Flow main()");
		
		//Build the object
		int rows=7; int cols=7;
		Flow rb=new Flow(rows,cols);
		Flow rb2 = null;
		//Print the object: this is an example of a non static call		
		System.out.println("Printing with the non-static method");
		rb.print();
		//If he method would have been declared static, then any reference to the instance 
		//could have been dropped. So the method could have been called as RiverBasinPrinter
		//However, in that case the method should have been called with the class as argument.
		System.out.println("Now with the static method");
		Flow.print(rb);	
	    //Here Printer(rb) should have been enough, since we are inside the class
		//Read the River From a File
		System.out.println("Reading a river netwok from an ESRI Ascii file");
		try {
			rb2=asciiRasterReader();
		} catch (EsriAsciiHeaderException e) {
			e.printStackTrace();
		} catch (EofException e){
			e.printStackTrace();
		}
		
		rb2.printFirstRiverRows(3);		
		
		System.out.println("This ends computation");
	}




}
