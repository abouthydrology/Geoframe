package org.geoframe.exec;

import java.util.Random;

import org.geoframe.exceptions.EofException;
import org.geoframe.exceptions.EsriAsciiHeaderException;
import org.geoframe.ocn.ReadEsriASCIIHeader;
import org.geoframe.ocn.Flow;
import org.geoframe.ocn.IRiverBasin;
import org.geoframe.util.GetFileName;

/**
 * This is to test the goDownStream method in RiverBasinClass. It call
 * the random number generator for choosing some points in the basin and
 * subsequently goes to the outlet following the drainage directions.
 * The main invoke 50 of these points, and prints the paths on the console video.
 * 
 * @author Riccardo Rigon
 *
 */
public class GoDownstreamExec {
	
	IRiverBasin rb;
	static GetFileName af;
	static Flow rb2 = null;
	
	public static void main(String[] args) throws EofException {
		System.out.println("This is GoDownstream main");
		//All of them below are part of the construction of asciiRasterReader() to check after revision
		//read the data from a file
		//get the file name
		//af = new GetFileName();
		//get the header
		//ReadEsriASCIIHeader ar = new ReadEsriASCIIHeader(af.fileName);
		System.out.println("Reading a river netwok from an ESRI Ascii file");
		try {
			rb2=Flow.asciiRasterReader();
		} catch (EsriAsciiHeaderException e) {
			e.printStackTrace();
		}
		//Select a point at random and then Go-Down-Stream
		ReadEsriASCIIHeader.esriAsciiPrint(rb2.esriHeader);
		System.out.println("Selecting a point at random and go downstream till the outlet");
		Random randomC = new Random();
		for(int k=0;k< 50;k++){
	        int i=randomC.nextInt(rb2.flow.length-2)+1;
	        int j=randomC.nextInt(rb2.flow[0].length-2)+1;
			System.out.println("\nPoint "+"("+i+","+j+") -> ");
			int[] pos=new int[2];
			pos[0]=i;
			pos[1]=j;
			int [] newpos=new int[2];
			while(rb2.flow[pos[0]][pos[1]]!=10){
				newpos=rb2.goDownstream(pos);
				pos[0]=newpos[0];
				pos[1]=newpos[1];
				//Maybe to be moved to pos=rb2.goDownstream(pos);
				System.out.print("("+pos[0]+","+pos[1]+") -> ");
			}
			System.out.print("("+pos[0]+","+pos[1]+")\n");
		}
       
		System.out.println("This ends computation");
	}

}
