package org.geoframe.scripts;

import java.io.File;

import org.geoframe.exceptions.EofException;
import org.geoframe.exceptions.EsriAsciiHeaderException;
import org.geoframe.io.TextIO;
import org.geoframe.ocn.Field;
import org.geoframe.ocn.TCA;

public class RealAreaExpII {
	//This is one of the scripts for the calculations of ARPaper2013
	//In particular, this reads alla the TCA and estimate the mean probability distribution of P[a]
	static final int L=16384+1;
    static long[] pArea=new long[L];
    static String workingDirectory="/Users/riccardo/Dropbox/A-Workspace/ARPaper2013-II";
    private static TCA tca;
    
	public static void main(String[] args) throws EsriAsciiHeaderException, EofException {
		
		System.out.println("This is RealAreaExpII main()");
		for(int i=1;i < 128;i++){
			//Creating the right name ... it could become a variable to read from a file
			String thisName=workingDirectory+File.separator+i+File.separator+i+"_itca.asc";
			//reading the file
			tca=TCA.asciiRasterReader(thisName);			
			//storing the data in the pAreavector
			for(int l=1;l<tca.tca.length-1;l++){
				for(int j=1;j<tca.tca[0].length-1;j++){
					//System.out.println("value: "+(int) Math.ceil(tca.field[l][j]));
					pArea[(int) tca.tca[l][j]]++;
				}
				
			}
		}
		
		//estimating the cumulant
		
		for(int l=0;l<L-1; l++){
			pArea[l+1]+=pArea[l];
		}
		
		//printing it to a File
		String pA=workingDirectory+File.separator+"I-AreaDistribution-II.txt";
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
