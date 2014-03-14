package org.geoframe.exec;

import org.geoframe.exceptions.EofException;
import org.geoframe.exceptions.EsriAsciiHeaderException;
import org.geoframe.io.TextIO;
import org.geoframe.ocn.Flow;
import org.geoframe.ocn.ReadEsriASCIIHeader;

/**
 * CheckSources tests the isDrained methed of Flow Class. It says where
 * there are river sources
 * 
 * @author Riccardo Rigon
 *
 */
public class CheckSources {
	//Both variable declared static: not a good solution, in general. Otherwise I have to provede a constructor
	//that allocates them
	
    static Flow rb2;
    public static  boolean[][] sources;
    //The method modifier and signature is affected by the choice of having made the field static
    public static void print(boolean s[][]){
    	for(int i=0;i<s.length;i++){
    		for(int j=0;j<s[0].length;j++){
    			System.out.print(s[i][j]+"\t");
    		}
    		System.out.print("\n");
    	}
    }
    
	public static void main(String[] args) {
		System.out.println("This is the CheckSource main()");
		//Read River from file
		System.out.println("Reading a river netwok from an ESRI Ascii file");
		
			try {
				rb2=Flow.asciiRasterReader();
			} catch (EofException e) {
				e.printStackTrace();
			} catch (EsriAsciiHeaderException e) {
			e.printStackTrace();
		    } 
		ReadEsriASCIIHeader.esriAsciiPrint(rb2.esriHeader);
		//TODO For any source marks it with a true
		sources=new boolean[rb2.flow.length][rb2.flow[0].length];
		int pos[] =new int[2];
		for(int i=1;i<rb2.flow.length-1;i++){
			for(int j=1;j< rb2.flow[0].length-1;j++){
				pos[0]=i;
				pos[1]=j;
				sources[i][j]=(!rb2.isDrained(pos));
				//System.out.println("("+i+","+j+")"+"->"+sources[i][j]);
			}
		}

		//Print a the raster with sources marked
		print(sources);
        System.out.println("This ends computation");
	}

}
