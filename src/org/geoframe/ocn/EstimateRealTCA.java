/*
 * This file is part of Geoframe 
 * (C) AboutHydrology - http://aboutHydrology.blogspot.com
 * 
 * Geoframe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geoframe.ocn;

import org.geoframe.exceptions.EofException;
import org.geoframe.exceptions.EsriAsciiHeaderException;

/**
 * Calculates the contributing area of a river. This treats Flow and TCA symmetrically
 * 
 * @author Riccardo Rigon 2013
 * @version 1/8
 *
 */
public class EstimateRealTCA {
  /**
   * Stores the reference to a Flow object a.k.a the  drainage directions of a river basin
   */
	public Flow basin=null;
/**
 * Stores the reference to a TCA object, i.e.an object that can contain the contributing area values	
 */
    public Field area=null;
    public Field weights=null;
    
    //Static variable not to be allocated any time inside a loop   
 /**
  *    the sum of the contributing areas upstream of a point
  */
    private static double localAreaSum=0;
    //private static boolean[] isDraining=new boolean[10];
    @SuppressWarnings("unused")
	private String workingDir;
    
 //Constructors !

/**
 * Estimates the TCA of the river contained in the File given as input
 * 
 * @param fileName
 */
    EstimateRealTCA(String fileName){
        //Open and read the file	
		System.out.println("Reading a river netwok from an ESRI Ascii file");
		try {
				this.basin=Flow.asciiRasterReader();
			} catch (EofException e) {
				e.printStackTrace();
		    } catch (EsriAsciiHeaderException e) {
		    	e.printStackTrace();
		}
		System.out.println("Allocating  and initializing the TCA object");		
       //Allocate and initialize the TCA object 
	   this.area = new Field(this.basin.rows,this.basin.cols);
	   try {
		   this.weights=Field.asciiRasterReader();
	   		} catch (EsriAsciiHeaderException e) {
	   			e.printStackTrace();
	   		} catch (EofException e) {
	   			e.printStackTrace();
	   		}
	   //this.weights.print();
	   //this.trivialTCA();
	   this.tca();
    }

    
    /**
     * Estimates the the TCA of the river contained in the Flow object given as input   
     * @param rvr
     */
       public EstimateRealTCA(Flow rvr,Field weights){
       //TODO	
       	this.basin=rvr;
           //Allocate and initialize the TCA object 
    	   this.area = new Field(rvr.rows,rvr.cols);
    	   this.weights=weights;
    	   //In alternative: this.trivialTCA();
    	   this.tca();
       }   
/**
 * is the core calculator of the tca
 * @version 1/16 
 */
private void tca(){
	//for any point check if it is a source
	int[] point=new int[2];
	for(int i=1;i<this.basin.flow.length-1;i++){
		for(int j=1;j<this.basin.flow[0].length-1;j++){
			point[0]=i;
			point[1]=j;
			if(!this.basin.isDrained(point)){//Is a source	
				this.area.field[point[0]][point[1]]+=this.weights.field[point[0]][point[1]];
				point=this.basin.goDownstream(point);	
				while(isAssigned(point) && (this.basin.flow[point[0]][point[1]]!=10)){
					//sumLocalAreas(point);
					this.area.field[point[0]][point[1]]=localAreaSum;
					//System.out.println(localAreaSum-this.area.tca[point[0]][point[1]]);
					point=this.basin.goDownstream(point);	
				}
				if(isAssigned(point) && this.basin.flow[point[0]][point[1]]==10){
					//sumLocalAreas(point);
					this.area.field[point[0]][point[1]]=localAreaSum;
				}
				
			}
		}
	}
		
	}


//At this point it should not be difficult to simplify it and
//substitute directly the area calculation inside isAssgnes to
// the true statements, which should save calculation time.
/**
 * sees if all the points draining into 'point' have a contributing area assigned
 * and at the same time update the value of the total upstream contributing area
 * @param point
 * @return true if all the point which drain into 'point' have a contributing area assigned
 */
private boolean isAssigned(int[] point){
//		System.out.println("Point: "+point[0]+" "+point[1]);
	    localAreaSum=0;
	    for(int k=1; k< 5; k++){
		if(basin.flow[point[0]+IRiverBasin.neighbors[k][0]][point[1]+IRiverBasin.neighbors[k][1]]==(k+4)){
			if(area.field[point[0]+IRiverBasin.neighbors[k][0]][point[1]+IRiverBasin.neighbors[k][1]]==0){
	            //isDraining[k]=false;
				return false;
			}else{
				//isDraining[k]=true;
				localAreaSum+=area.field[point[0]+IRiverBasin.neighbors[k][0]][point[1]+IRiverBasin.neighbors[k][1]];
			}
		}//else{
			//isDraining[k]=false;
		//}
		
		}
		
		for(int k=5; k< 9; k++){
			if(basin.flow[point[0]+IRiverBasin.neighbors[k][0]][point[1]+IRiverBasin.neighbors[k][1]]==((k+4)%8)){
				if(area.field[point[0]+IRiverBasin.neighbors[k][0]][point[1]+IRiverBasin.neighbors[k][1]]==0){
					//isDraining[k]=false;
					return false;
				}else{
					//System.out.println(k+" "+area.field[point[0]+IRiverBasin.neighbors[k][0]][point[1]+IRiverBasin.neighbors[k][1]]);
					//isDraining[k]=true;
					localAreaSum+=area.field[point[0]+IRiverBasin.neighbors[k][0]][point[1]+IRiverBasin.neighbors[k][1]];

				}
			}//else{
				//isDraining[k]=false;
			//}

		}
		//Added when sumLocalAreas has been commented
		localAreaSum+=this.weights.field[point[0]][point[1]];
		return true;
	}
/**
 * Estimates the TCA of the river contained in the File given as input with 
 * the simplest algorithm
 * 
 */
private void trivialTCA(){
	int[] pos=new int[2];
	for(int i=1;i<this.basin.flow.length-1;i++){
		for(int j=1;j<this.basin.flow[0].length-1;j++){
			pos[0]=i;
			pos[1]=j;
			while(this.basin.flow[pos[0]][pos[1]]!=10){
				this.area.field[pos[0]][pos[1]]+=this.weights.field[pos[0]][pos[1]];
				//this.area.field[pos[0]][pos[1]]+=1.0; 
				pos=this.basin.goDownstream(pos);	
			}
				this.area.field[pos[0]][pos[1]]+=this.weights.field[pos[0]][pos[1]];
			    //  this.area.field[pos[0]][pos[1]]+=1.0;
		}
	}
	
}
/**
 * estimates the energy expenditure of a river network
 * @param exp 
 * @return the energy expenditure variable
 * @reference "Rodriguez-Iturbe, I. and Rinaldo, A., Channel River Networks: chance and self-organization, C.U.P.", 1997
 *
 *//*
public double energyExpenditure(double exp){
	
	double pEnergyExpenditure=0;
	
	for(int i=1;i<this.area.field.length-1;i++){
		for(int j=1;j<this.area.field[0].length-1;j++){
			pEnergyExpenditure+=Math.pow(this.area.field[i][j],exp);
			
		}
		
	}
	return pEnergyExpenditure;
}
*/	public static void main(String[] args) {
		
		System.out.println("This is EstimateRealTCA main()");
        double ee;
		//Reads the Flow matrix from a file	
		String edn= "Nomefile"; //The name of the parameter requested ?
		EstimateRealTCA bTCA=new EstimateRealTCA(edn);
		//write TCA on a file (not necessarily the same directory)
		//bTCA.area.print();
		//Actually a little boring to give the directory again and again
		//Write on Esrii Asci file
		bTCA.area.asciiRasterWriter();
		ee=bTCA.area.energyExpenditure(0.5);
		System.out.println("Energy Expenditure = "+ee);
		System.out.println("This is ends calculations");	

	}

}
