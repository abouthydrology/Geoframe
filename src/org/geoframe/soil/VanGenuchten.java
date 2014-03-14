package org.geoframe.soil;

import org.geoframe.io.TextIO;

/** This implements the van Genucthen parameterisation of soil characteristics
 * 
 * @author Riccardo Rigon
 * 
 * This software is distributed under the General Public License version 3
 *
 */
public class VanGenuchten {
//Variables:
	//Parameters: [All dimensionless, except pVGalpha and pVGPsi 
	// that have the reciprocal, pressure type of variable]
	private static double pVGalpha,pVGn,pVGm,pVGthetas,pVGthetar;
	//Variables Variables
//	private static double pTheta,pPsi;
	//Setup the parameters
	public void setupVanGenuchten (double pVGthetar,double pVGthetas,double pVGalpha, double pVGn,double pVGm){
	VanGenuchten.pVGalpha=pVGalpha;
	VanGenuchten.pVGm=pVGm;
	VanGenuchten.pVGn=pVGn;
	VanGenuchten.pVGthetar=pVGthetar;
	VanGenuchten.pVGthetas=pVGthetas;
	}
	//Call the function that returns a value of the soil water content for any suction
   public double getVGvalue(double pPsi){
	   if (pPsi >= 0){
	   return pVGthetar + (pVGthetas-pVGthetar)/Math.pow(1 + Math.pow(pVGalpha*pPsi,pVGn),pVGm);
	   }else{
		   return pVGthetas;
	   }
   }
   //The function at work
   public static void  main(String[] args){
	   
	   int i;
	   double psi; //Suction in hPa
	   double theta; //dimensionless water content [0,thetas]
	   
	   VanGenuchten VG=new VanGenuchten();
	   //Same parameters as in http://en.wikipedia.org/wiki/File:Wrc.gif
	   //psi is in hPa so pVGalpha is in hPa^{-1}
	   VG.setupVanGenuchten(0.0403,0.37068,0.08742,1.57535,0.36522042);
	   TextIO.putln("Parameters are:");
	   TextIO.put("theta_s<-"+VanGenuchten.pVGthetas+" theta_r<-"+VanGenuchten.pVGthetar+" ");
	   TextIO.putln("alpha<-"+VanGenuchten.pVGalpha+" n<-"+VanGenuchten.pVGn+" m<-"+VanGenuchten.pVGm+"\n");
	   TextIO.putln("[#]\tPsi\ttheta");
	   TextIO.putln("\t[hPa]\t[-]");
	   for(i=0;i<101;i++){
		   psi=i/10.;
		   theta=VG.getVGvalue(psi);	
		   TextIO.putln("["+i+"]\t"+psi+"\t"+theta);
	   }
	   
	   }


}//end of class VanGenuchten body
