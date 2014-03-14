package org.geoframe.scripts;

import java.io.File;

import org.geoframe.exceptions.EofException;
import org.geoframe.exceptions.EsriAsciiHeaderException;
import org.geoframe.io.TextIO;
import org.geoframe.ocn.Field;

public class PAreaReduced_III {
	//This is one of the scripts for the calculations of ARPaper2013
	//In particular, this reads all the TCA and estimate the mean probability distribution of P[a]
	static final int L=12866+1;
    static double[] pArea=new double[L];
    static String workingDirectory="/Users/riccardo/Dropbox/A-Workspace/ARPaper2013-III";
    private static Field tca;
    
	public static void main(String[] args) throws EsriAsciiHeaderException, EofException {
		
		System.out.println("This is PAreaReduced_III main()");
		for(int i=1;i < 129;i++){
			//Creating the right name ... it could become a variable to read from a file
			String thisName=workingDirectory+File.separator+i+File.separator+i+"_R_OCN_tca.asc";
			//reading the file
			tca=Field.asciiRasterReader(thisName);			
			//storing the data in the pAreavector
			for(int l=1;l<tca.field.length-1;l++){
				for(int j=1;j<tca.field[0].length-1;j++){
					//System.out.println("value: "+(int) Math.ceil(tca.field[l][j]));
					pArea[(int) Math.ceil(tca.field[l][j])]++;
				}
				
			}
		}
		
		//estimating the cumulant
		
		for(int l=0;l<L-1; l++){
			pArea[l+1]+=pArea[l];
		}
		
		//printing it to a File
		String pA=workingDirectory+File.separator+"R-AreaDistribution-III.txt";
		TextIO.writeFile(pA);
		//Writing the File
		TextIO.putln("! Area Distribution");
		double value;
		for(int l=0;l<L; l++){
			value=pArea[l];
			TextIO.putln(l+"\t"+value);
		}
		TextIO.writeStandardOutput();
		System.out.println("This ends computation");
	}
}
