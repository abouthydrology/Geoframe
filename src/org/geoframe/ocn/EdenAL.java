package org.geoframe.ocn;

import java.util.ArrayList;
import java.util.Random;
//import org.geoframe.io.TextIO;
import org.geoframe.util.PConsoleMonitor;

//Entia non sunt multiplicanda praeter necessitatem
/**
 * 
 * EdenAL builds a river network using an Eden growth algorithm (CIT). The algorithm implemented starts from
 * the outlets which are given and identify the boundary around them. Subsequently it randomly selects one point of the boundary 
 * A new boundary is built around the new point, and eventually the point is made to randomly drain into one of the outlets 
 * (or one of the other points for which the drainage has already been established in a previous iteration). 
 * The process is iterated till all the point in the network have a drainage.
 * The postfix AL reminds that an ArrayList is used to store the list of boundary points, as well as, the list of outlet.
 * The initial condition is given by a matrix where the points to be excluded from the process are signed by the "no value" number 9.
 * Outlets are marked by a "10". Points belonging to the dynamical boundary are marked by a 11. All drainage directions
 * are denoted by a number between 1 and 8, according to the so-called D8 algorithms (CIT).
 * 
 * @author Riccardo Rigon, 2013
 * 
 * @version 1/16
 * 
 * @References  ReferencesList ={{ Rigon et al., The Horton Machine, 2006}};
 *
 */
public class EdenAL extends Flow{

	//I decided to use and ArrayList but probably using a LinkedList or a Tree should be more efficient
	private ArrayList<int[]> routlet=new ArrayList<int[]>();
	
	private static ArrayList<int[]>  rBoundary=new ArrayList<int[]>();
	//Add One random flow direction. It uses here the normal Java random generator. It is possible to do better
	//"Random numbers" are too much important to let them be generated casually ;-)"
    private static Random randomGenerator=new Random();
   // private static int[][] nextPossibilities=new int[9][2];
    //the next to avoid useless loops for searching the next possibilities;
   // private static int no;
    private static int[] nextFlow;
    private static int[] newFD= new int[2];
    private static int[] relativeFlowCoord=new int[2];
    private static int flowCode;
    //This should not be here private static EdenAL two;
    public static ArrayList<int []> outlets;
    
  /** newFlow()Select a new point to enlarge the basin. The point is chosen randomly among the boundary points. 
   *  The method uses selectPossibleFlows() to get the point s where the newflow point can drain. Once the point is chosen,
   *  it is removed from the boundary.
   *  
   *  @author Riccardo Rigon
   */
		public void newFlow(){
			int count=rBoundary.size();
			//int[][] pf;
	
			//Generate a random number between 1 and count-1 (the first index is 0)
//There is a bug in the following two lines
			int randomInt = randomGenerator.nextInt(count);
//			System.out.println("count:"+count+" pos: "+randomInt);
			nextFlow=rBoundary.get(randomInt);
			//for(int h=1;h<100;h++) System.out.println(randomGenerator.nextInt(count)+1);
//            System.out.println("Position: "+randomInt+" New point: ("+nextFlow[0]+","+nextFlow[1]+")");
			//Select the possible new directions;
			
            selectPossibleFlow(nextFlow);
//            System.out.println("Selection done");
			int randomInt1=randomGenerator.nextInt(no+1);
//			System.out.println("no: "+no+" pos 1: "+randomInt1);
			newFD[0]=nextPossibilities[randomInt1][0];
			newFD[1]=nextPossibilities[randomInt1][1];
			relativeFlowCoord[0]=newFD[0]-nextFlow[0];
			relativeFlowCoord[1]=newFD[1]-nextFlow[1];
			flowCode=flowCodes[relativeFlowCoord[0]+1][relativeFlowCoord[1]+1];
			//Put the next in a "try ... catch" 
			rBoundary.remove(randomInt);
		}
		
	/*     *//** 
	      * Select the possible directions of flow for a given basin point. The method scan the next neighbors
	      * according to D8 (CIT) and populates an ArrayList with values of the directions that are allowed.
	      * @param nextFlow
	      *//*

		//Warning: the next method is strongly dependent on neighbors. It the neighbors matrix is changed, everything
		//is screwed up. Probably not a good example of design.
		private void selectPossibleFlow(int[] nextFlow) {
			
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
			}*/
	/**
	 * setBoundary	updates the boundary of the growing river network by adding the neighbors of newpoint
	 * 
	 * @param newpoint
	 */
		
	public void setBoundary(int[] newpoint){
		for(int k =1; k< 9; k++){

			if (this.flow[neighbors[k][0]+newpoint[0]][neighbors[k][1]+newpoint[1]] ==0){
				this.flow[neighbors[k][0]+newpoint[0]][neighbors[k][1]+newpoint[1]] = 11;
			    rBoundary.add(new int[] {neighbors[k][0]+newpoint[0],neighbors[k][1]+newpoint[1]});
			    
			}
		}
		

	}
	/** setOutlets selects the outlet and marks it with the value '10'. 
	*It works just for one outlet. If it is used multiple times, close-by outlets are cancelled during
	*the growth of the river basin. For multiple outlets use setMultipleOutlets.
	*
	* @deprecated Substituted by selectMultipleOutlets()
	* 
	* @param row
	* @param col
	*/
	public void setOutlet(int row, int col){
		
	    int outlet[]={row,col};
        if(rBoundary.size() > 1){
        	//TODO Error throw
        	System.out.println("This method allocate just one outlet");
        } else {
		if(row<2 || row >this.flow.length-1 || col<2 || col> this.flow[0].length-1){
			//Exception OutOfBoundExceptions = new Exception;
			//throw OutOfBoundExceptions ;
			System.out.println("This provisionally substitutes error catching. Outlet ("+row+","+col+") is not correct.");
		}else{
			this.flow[row][col] = 10;
			routlet.add(outlet);
			setBoundary(outlet);
		}
		}
	}
/**
 * setMultipleOutlet( ) selects the outlet and marks it with the value '10'. 
 * 
 * @param outlets
 */
	
	public void setMultipleOutlet(ArrayList<int[]> outlets){
		
		ArrayList<Integer> wrongOutlets=new ArrayList<Integer>();
		
        for(int[] item : outlets){
		if(item[0]<1 || item[0] >this.flow.length-2 || item[1]<1 || item[1]> this.flow[0].length-2){
			//Exception OutOfBoundExceptions = new Exception;
			//throw OutOfBoundExceptions ;
			System.out.println("This provisionally substitutes error catching. Outlet ("+item[0]+","+item[1]+") is not correct.");
		    //The next line breaks the iterator, so I just collects where in the ArrayList the wron outlets are.
			//outlets.remove(item);
			wrongOutlets.add(outlets.indexOf(item));
		}else{
			this.flow[item[0]][item[1]] = 10;
		}
        }
	    
        for(int index : wrongOutlets){
        	outlets.remove(index);
        }
		for(int[] item : outlets){	
			System.out.println("Outlet no "+item.length+" : ("+item[0]+","+item[1]+")");
			setBoundary(item);
		}
		
	}
	
	/**
	 * populateRiverBasin() uses the methods above to perform an Eden growth of a basin.
	 */
	public void populateRiverBasin(){
		
		//Is the effective method
		int cc=0,tIterations=0;
		
				
		if(outlets==null){
			System.out.println("Error stub: No appropriate outlet chosen");
		} else if(rBoundary==null){
			System.out.println("Error stub: Null boundary");
		}
		if(this.flow==null){
			System.out.println("Error stub: River not Allocated");
		} else {
		tIterations=this.rows*this.cols-outlets.size();
		PConsoleMonitor thisMonitor=new PConsoleMonitor(tIterations);
		this.setMultipleOutlet(outlets);
	    //print(two);
		while(rBoundary.size()> 0){
			this.newFlow();
		    this.flow[nextFlow[0]][nextFlow[1]]=flowCode; 
			this.setBoundary(nextFlow);	
			cc++;
			thisMonitor.progress(cc);
            //System.out.println("Iteration no: "+cc);		
			//print(two);
			//TextIO.getAnyChar();
		}
		} 
	}

	/**
	 * Inherits the constructor from the superclass RiverBasin
	 * @param rows
	 * @param cols
	 */
	public EdenAL(int rows, int cols) {
		super(rows, cols);
		// TODO Auto-generated constructor stub
		
	}

	public static void main(String[] args) {
		
		//int cc=0;
		EdenAL two;
		System.out.println("This is Eden main()");
//The next lines test that Eden's filling works with one outlet. But the method with multiple outlets is more general		
		
		//Now testing multiple outlets
		two = new EdenAL(11,17);
		//*two = new EdenAL(214,328);
		//There is probably a better construct to initialize the outlets
		//*int[][] input={{212,155},{1,1},{107,326}};
		int[][] input={{7,13},{1,1}};
		outlets = new ArrayList<int[]>();
		for(int t=0; t < input.length;t++ ){
			outlets.add(input[t]);
		}
		System.out.println("Outlets:");
		for(int[] item : outlets){
			System.out.println(item[0]+" "+item[1]);
		}

		//In the next command you should check the bounding error
		try{
		two.populateRiverBasin();
		//RasterWriterGF ask information for writing both a folder and a file inside it with the same name
		//of the folder
		two.asciiRasterWriterGF();
		}catch(Error ee){
			//TODO fix it
		}
        System.out.println("This ends computation");
	}



}
