package org.geoframe.ocn;

import java.io.File;
import java.util.Random;

import org.geoframe.exceptions.EofException;
import org.geoframe.exceptions.EsriAsciiHeaderException;
import org.geoframe.io.TextIO;
import org.geoframe.util.CreateFolder;
import org.geoframe.util.DateAndTime2String;
import org.geoframe.util.Folder;
import org.geoframe.util.InfoFolder;
import org.geoframe.util.PConsoleMonitor;

public class ROCN {
	
	public Flow basin=null;
	public Field area=null;
	public double pEnergyExpenditureExponent;
	public long pIterations;
	public String mode="normal";
	//TODO to be eventually moved to the RunOCN class
	private static Random randomGenerator=new Random();	
	private static int[] newDir=new int[3];
    //TODO to be eventually moved to the InitializeOCN class
	public static String outputFilesName;
	//Constructors

	
	public ROCN(Flow basin,Field area){
		// Energy Expenditure Exponent, number of iterations and mode must be set outside
		this.basin=basin;
		this.area=area;
	}
	//Letting to read just the names leaves you the freedom to get the names from StdIO or 
	//a file
	public ROCN(String basinName,String areaName){
		// Energy Expenditure Exponent, number of iterations and mode must be set outside
	
		try {
			this.basin = Flow.asciiRasterReader(basinName);
		} catch (EsriAsciiHeaderException e) {
			e.printStackTrace();
		} catch (EofException e) {
			e.printStackTrace();
		}
		try {
			this.area = Field.asciiRasterReader(areaName);
		} catch (EsriAsciiHeaderException e) {
			e.printStackTrace();
		} catch (EofException e) {
			e.printStackTrace();
		}
	}
	
	//private void getWorking(){
		
	//}
/**
 * initializes a OCN by reading the flow of a river and its tca from files
 * which are indicated via the stdIO
 * 
 * @return a populated OCN type
 * 
 */
// Possibly it would be interesting to extend this main structure everywhere
// with initialize() - run() - print() 	methods
// This becomes interesting when the program is long and the main should become itself very long and complex	
	
	public static ROCN initialize(){
	
	//Gets the file names for inputs	
	String name=Folder.getDirectory();
	InfoFolder trial=new InfoFolder(name);
	
	System.out.println("The working directory is:"+trial.workingPathName);
	trial.shortName=trial.workingPath.getName();
	System.out.println("The file is:"+trial.shortName);
	File test=new File(trial.workingPath+File.separator+trial.shortName+"_flow.asc");
	while(!test.exists()){
		System.out.println("Re-enter the files prefix");
		trial.shortName=TextIO.getlnWord();
		test=new File(trial.workingPath+File.separator+trial.shortName+"_flow.asc");			
	}
	//System.out.print("The files to open are:\n"+trial.workingPath+File.separator+trial.shortName+"_flow.asc\n");
	//System.out.println(trial.workingPath+File.separator+trial.shortName+"_tca.asc\n");
    //Gets the file names for outputs - It uses the same Folder and creates a sub-folder with the
	//the outputs and a log file
	new DateAndTime2String();
	String outputDirName = trial.workingPathName+File.separator+DateAndTime2String.dateTime;
	//System.out.println(outputDirName);
	/*String outputFlowName = outputDirName+File.separator+trial.shortName+"_flow.asc";
	String outputTCAName=outputDirName+File.separator+trial.shortName+"_tca.asc";
	System.out.println(outputFlowName);
	System.out.println(outputTCAName);*/
	//Creates the directory
	new CreateFolder(outputDirName);
	//TODO Implementing the simulation log file
	//Allocate OCN
	String basinName=trial.workingPath+File.separator+trial.shortName+"_flow.asc";
	String tcaName=trial.workingPath+File.separator+trial.shortName+"_tca.asc";
	ROCN ocn=new ROCN(basinName,tcaName);
	//Set the name for the outputs
	ROCN.outputFilesName=outputDirName+File.separator+trial.shortName;
	//Read the flow
	try {
		Flow.asciiRasterReader(basinName);
	} catch (EsriAsciiHeaderException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (EofException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//Read the TCA
	try {
		Field.asciiRasterReader(tcaName);
	} catch (EsriAsciiHeaderException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (EofException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//Set the simulation's parameters
	System.out.println("Enter the number of iterations");
	ocn.pIterations=TextIO.getlnLong();
	System.out.println("Enter the exponent for energy expenditure calculation");
	ocn.pEnergyExpenditureExponent=TextIO.getDouble();	
	return ocn;
	}
	/**
	 * A standard print method
	 */
	public void print(){
		//TODO To be really improved
        this.basin.print();
        this.area.print();
	}
	/**
	 * executes a change 
	 */
    public void run(){
    	//for the number of times requested
    	int[] randomInt=new int[2];
    	int[] newPoint=new int[2];
    	long[][] palette =new long[this.basin.rows][this.basin.cols];
     	long counter=0;
     	PConsoleMonitor thisMonitor=new PConsoleMonitor(this.pIterations);
     	
    	this.area.energyExpenditure=this.area.energyExpenditure(this.pEnergyExpenditureExponent);
    	
    	for(int i=1;i<this.pIterations+1;i++){
    	  //Select a point at random
    		//System.out.println("****->"+i);  		
    	  do{ 
    		  randomInt[0] = randomGenerator.nextInt(this.basin.rows-2)+1;
    	      randomInt[1] = randomGenerator.nextInt(this.basin.cols-2)+1;
    	   
    	  }while(this.basin.flow[randomInt[0]][randomInt[1]]==10);
          //Mark the palette to the outlet
    	 
    	  counter++;
    	  setPalette(randomInt,palette, counter);
        //printPalette(palette);
    	//Maybe it is more economical to select one direction and
    	//check if it is valid instead of scanning them all.  
    	//Select a random direction while the flow can change
    	 while(!this.isAPossibleChange(randomInt,palette,counter)){
    		do{ 
       	  		randomInt[0] = randomGenerator.nextInt(this.basin.rows-2)+1;
       	  		randomInt[1] = randomGenerator.nextInt(this.basin.cols-2)+1;

       	  		//System.out.println("->"+randomInt[0]+" "+randomInt[1]);
    		}while(this.basin.flow[randomInt[0]][randomInt[1]]==10);
   	  	    counter++;
   	  		setPalette(randomInt,palette, counter);
    	    //System.out.println("XXXX-Changed point");
    	 }
    	 //System.out.println("NewDir->"+newDir[0]+" "+newDir[1]);
         //Estimate Energy Expenditure Variation
    	 //printPalette(palette);
    	 double deltaE=deltaEE(randomInt,this.pEnergyExpenditureExponent,palette,counter);
		 //System.out.println("DeltaE : "+deltaE);
    	 //Perform the changes to be made if some condition is met
		 if(deltaE< 0){
			 reSetOCN(randomInt, palette,counter);
			 this.area.energyExpenditure+=deltaE;
		 }
           double eE=this.area.energyExpenditure(this.pEnergyExpenditureExponent);
           if(Math.abs(eE-this.area.energyExpenditure)> 0.000005){
        	   //System.out.println("NewEnergyExpenditure: "+eE+" or "+this.area.energyExpenditure);
        	   //System.out.println("*****Something wrong here******");
        	   //TextIO.getAnyChar();
        	   this.area.energyExpenditure=eE;
           } else{
        	   //System.out.println("NewEnergyExpenditure at iteration "+i+": "+eE);
           }
           thisMonitor.progress(i);
    	}//end for
    	  
	}
    
  private void  setPalette(int[] randomInt,long[][] palette, long i){
	      int[] newPoint=new int[2];
     	  newPoint[0]=randomInt[0];
    	  newPoint[1]=randomInt[1];
    	  //System.out.println("NewPoint: "+newPoint[0]+" "+newPoint[1]+"->"+this.basin.flow[newPoint[0]][newPoint[1]]);
    	  palette[newPoint[0]][newPoint[1]]=i;
          while(this.basin.flow[newPoint[0]][newPoint[1]]!=10){
        	  newPoint=this.basin.goDownstream(newPoint);
        	  //System.out.println("->"+newPoint[0]+" "+newPoint[1]);
        	  palette[newPoint[0]][newPoint[1]]=i;
          } 
    }
     
    private void printPalette(long[][] palette){
    	System.out.print("\n");
    	for(int i=0;i<palette.length;i++){
    		for(int j=0; j<palette[0].length;j++){
    			System.out.print(palette[i][j]+"\t");
    		}
    		System.out.print("\n");
    	}
    	System.out.print("\n");
    }
    
    /**
     * see for the point chosen which are the possible new flow directions
     * 
     * @param randomPoint
     */
    
	protected boolean isAPossibleChange(int[] randomPoint,long[][] palette,long l) {
		
		//System.out.println("**"+randomPoint[0]+" "+randomPoint[1]+"-->"+this.basin.flow[randomPoint[0]][randomPoint[1]]);
		int i,j;
		    int no=0;
		    //First scan the odd directions in neighbors
			for(int k=1;k<9;k+=2){
				if(this.basin.flow[randomPoint[0]][randomPoint[1]]!=k){
					i=randomPoint[0]+Flow.neighbors[k][0];
					j=randomPoint[1]+Flow.neighbors[k][1];
					
					if( this.basin.flow[i][j] < 9 && this.basin.flow[i][j]!=(k+4)%8){
						Flow.nextPossibilities[no][0]=i;
						Flow.nextPossibilities[no][1]=j;
						Flow.nextPossibilities[no][2]=k;
						no++;
					}else if(this.basin.flow[i][j]==10){
						Flow.nextPossibilities[no][0]=i;
						Flow.nextPossibilities[no][1]=j;
						Flow.nextPossibilities[no][2]=k;
						no++;
					}
				}
	
			}
			//The even directions in neighbors must be checked for crossings

			int k;
			   
			    k=2;
			    if(this.basin.flow[randomPoint[0]][randomPoint[1]]!=k){
				i=randomPoint[0]+Flow.neighbors[k][0];
				j=randomPoint[1]+Flow.neighbors[k][1];

				if(this.basin.flow[i][j] < 9 && this.basin.flow[i][j]!=k+4){
					if(!(this.basin.flow[randomPoint[0]+Flow.neighbors[3][0]][randomPoint[1]+Flow.neighbors[3][1]] == 8 || this.basin.flow[randomPoint[0]+Flow.neighbors[1][0]][randomPoint[1]+Flow.neighbors[1][1]]==4)){					
						Flow.nextPossibilities[no][0]=i;
						Flow.nextPossibilities[no][1]=j;
						Flow.nextPossibilities[no][2]=k;
						no++;
					}
				}else if(this.basin.flow[i][j]==10){
					//This could be wrong without crossing checks
					Flow.nextPossibilities[no][0]=i;
					Flow.nextPossibilities[no][1]=j;
					Flow.nextPossibilities[no][2]=k;
					no++;
				}
			    }
				k=4;
				if(this.basin.flow[randomPoint[0]][randomPoint[1]]!=k){
				i=randomPoint[0]+Flow.neighbors[k][0];
				j=randomPoint[1]+Flow.neighbors[k][1];

				if(this.basin.flow[i][j] < 9 && this.basin.flow[i][j]!=k+4){
					if(!(this.basin.flow[randomPoint[0]+Flow.neighbors[3][0]][randomPoint[1]+Flow.neighbors[3][1]] == 6 || this.basin.flow[randomPoint[0]+Flow.neighbors[5][0]][randomPoint[1]+Flow.neighbors[5][1]]==2)){
						Flow.nextPossibilities[no][0]=i;
						Flow.nextPossibilities[no][1]=j;
						Flow.nextPossibilities[no][2]=k;
						no++;
					}
				}else if(this.basin.flow[i][j]==10){
					Flow.nextPossibilities[no][0]=i;
					Flow.nextPossibilities[no][1]=j;
					Flow.nextPossibilities[no][2]=k;
					no++;
				}
				}
				k=6;
				if(this.basin.flow[randomPoint[0]][randomPoint[1]]!=k){
				i=randomPoint[0]+Flow.neighbors[k][0];
				j=randomPoint[1]+Flow.neighbors[k][1];

				if(this.basin.flow[i][j] < 9 && this.basin.flow[i][j]!=(k+4)%8){
					if(!(this.basin.flow[randomPoint[0]+Flow.neighbors[5][0]][randomPoint[1]+Flow.neighbors[5][1]] == 8 || this.basin.flow[randomPoint[0]+Flow.neighbors[7][0]][randomPoint[1]+Flow.neighbors[7][1]]==4)){
						Flow.nextPossibilities[no][0]=i;
						Flow.nextPossibilities[no][1]=j;
						Flow.nextPossibilities[no][2]=k;
						no++;
					}
				}else if(this.basin.flow[i][j]==10){
					Flow.nextPossibilities[no][0]=i;
					Flow.nextPossibilities[no][1]=j;
					Flow.nextPossibilities[no][2]=k;
					no++;
				}
				}
				k=8;
				if(this.basin.flow[randomPoint[0]][randomPoint[1]]!=k){
				i=randomPoint[0]+Flow.neighbors[k][0];
				j=randomPoint[1]+Flow.neighbors[k][1];

				if(this.basin.flow[i][j] < 9 && this.basin.flow[i][j]!=(k+4)%8){
					if(!(this.basin.flow[randomPoint[0]+Flow.neighbors[7][0]][randomPoint[1]+Flow.neighbors[7][1]] == 2 || this.basin.flow[randomPoint[0]+Flow.neighbors[1][0]][randomPoint[1]+Flow.neighbors[1][1]]==6)){
						Flow.nextPossibilities[no][0]=i;
						Flow.nextPossibilities[no][1]=j;
						Flow.nextPossibilities[no][2]=k;
						no++;
					}
				}else if(this.basin.flow[i][j]==10){
					Flow.nextPossibilities[no][0]=i;
					Flow.nextPossibilities[no][1]=j;
					Flow.nextPossibilities[no][2]=k;
					no++;
				}					
				}
		   if(no==0){
		            	//It is not a feasible change
		            	//System.out.println("*");
		            	return false;
		     }
			//no--;
            if(no==1){
            	//It is not a loop but anyway it does not implies changes
            	//System.out.println("**");
            	      
            } else{
            	//use Fisher-Yates shuffling algorithm to randomize the available directions
            	shuffleArray(Flow.nextPossibilities,no-1);
            }
			//boolean loopIndicator = true;
			for(int h=0; h<no;h++){
				if(!isALoop(randomPoint,Flow.nextPossibilities[h],palette,l)){
					//System.out.println("Is OK\n");					
					ROCN.newDir[0]=Flow.nextPossibilities[h][0];
					ROCN.newDir[1]=Flow.nextPossibilities[h][1];
					ROCN.newDir[2]=Flow.nextPossibilities[h][2];
					//System.out.println("Accepted change ->"+OCN.newDir[0]+" "+OCN.newDir[1]+"Flow index: "+OCN.newDir[2]);
					//this.basin.print();
					return true;
				}
			}
			return false;
	
	}
	
	private boolean isALoop(int[] nextPoint,int[] nextPossibility,long[][] palette,long l){
		
		int[] newPoint=new int[2];
		//This look exactly to the same values
        newPoint=nextPossibility;
        //System.out.println(nextPoint[0]+"-^-"+nextPoint[1]);
        //printPalette(palette);
		while(palette[newPoint[0]][newPoint[1]]!=l && this.basin.flow[newPoint[0]][newPoint[1]]!=10){
		
			newPoint=this.basin.goDownstream(newPoint);
			//System.out.println(newPoint[0]+"--"+newPoint[1]);
			if(newPoint[0]==nextPoint[0] && newPoint[1]==nextPoint[1]){
				
				//System.out.println("+++");
				return true;
			}
		}
		palette[newPoint[0]][newPoint[1]]=-l;
		//printPalette(palette);
		return false;
	}
	

	
	 private double deltaEE(int[] randomInt,double pEEE,long[][] palette,long itr){
		 
		 double pEDelta=0;
		 double pLocalArea;
		 int[] newPoint=new int[2];
		 
		 
		 //Estimates the Energy Expenditure Variation in the old direction;
		 newPoint=randomInt;
		 pLocalArea=this.area.field[newPoint[0]][newPoint[1]];
		 //System.out.println("Local Area "+pLocalArea);
		 newPoint=this.basin.goDownstream(newPoint);
		 while(palette[newPoint[0]][newPoint[1]] > - itr){
			 pEDelta+=-Math.pow(this.area.field[newPoint[0]][newPoint[1]],pEEE)+Math.pow(this.area.field[newPoint[0]][newPoint[1]]-pLocalArea,pEEE);
			 //System.out.println(newPoint[0]+" "+newPoint[1]+"->"+pEDelta);
			 newPoint=this.basin.goDownstream(newPoint);
			 //TextIO.getAnyChar();
		 }
			 //Estimates the Energy Expenditure Variation in the new direction;	 
		 newPoint[0]=newDir[0];
		 newPoint[1]=newDir[1];
		 while(palette[newPoint[0]][newPoint[1]] > - itr){
			 pEDelta+=-Math.pow(this.area.field[newPoint[0]][newPoint[1]],pEEE)+Math.pow(this.area.field[newPoint[0]][newPoint[1]]+pLocalArea,pEEE);
			 //System.out.println(newPoint[0]+" "+newPoint[1]+"-->"+pEDelta);
			 newPoint=this.basin.goDownstream(newPoint);
			 //TextIO.getAnyChar();
		 }
		 return pEDelta;
	 }
	 
	  private void reSetOCN(int[] randomInt,long[][] palette,long itr){
		  double pLocalArea;
	      int[] newPoint=new int[2];
		//N.B these operations which follows could go in parallel  
		//Varies the tca in the in the old direction;
	      newPoint=randomInt;
		  pLocalArea=this.area.field[newPoint[0]][newPoint[1]];
		 //System.out.println("Local Area "+pLocalArea);
		  newPoint=this.basin.goDownstream(newPoint);
		  while(palette[newPoint[0]][newPoint[1]] > - itr){
			     this.area.field[newPoint[0]][newPoint[1]]-=pLocalArea;
				 newPoint=this.basin.goDownstream(newPoint);
				 //TextIO.getAnyChar();
			 }
		//The new direction is contained in OCN.newDir[2];
		this.basin.flow[randomInt[0]][randomInt[1]]=ROCN.newDir[2];
		//Varies the tca in the new direction;
		 newPoint[0]=ROCN.newDir[0];
		 newPoint[1]=ROCN.newDir[1];
		 while(palette[newPoint[0]][newPoint[1]] > - itr){
			 this.area.field[newPoint[0]][newPoint[1]]+=pLocalArea;
			 newPoint=this.basin.goDownstream(newPoint);
			 //TextIO.getAnyChar();
		 }
	    }
	    
	    
	 // Implementing FisherÐYates shuffle http://en.wikipedia.org/wiki/Fisher-Yates_shuffle
	//  http://stackoverflow.com/questions/1519736/random-shuffling-of-an-array 
	static void shuffleArray(int[][] ar,int no)
	  {
	    int[] a;
	    for (int i = no; i > 0; i--)
	    {
	      int index = randomGenerator.nextInt(i + 1);
	      // Simple swap
	      a = ar[index];
	      ar[index] = ar[i];
	      ar[i] = a;
	    }
	  }
	
	
  //Save and Write the new network (the OCN)
  private void ocnDeploy(){
	  
	  //Write OCN on disk
	  String outputFlowName=ROCN.outputFilesName+"_flow.asc";
	  String outputTCAName=ROCN.outputFilesName+"_tca.asc";
	  //TextIO.writeFile(outputFlowName);
	  this.basin.asciiRasterWriter(outputFlowName);
	  //TextIO.readStandardInput();
      //TextIO.writeFile(outputTCAName);
	  this.area.asciiRasterWriter(outputTCAName);
	  //TextIO.readStandardInput();
	  
	  SimulationInfo sI=new SimulationInfo(this);
	  String logFileName=ROCN.outputFilesName+"_log.txt";	
	  TextIO.writeFile(logFileName);
	  sI.print();
	  OCNInfo ocnInfo=new OCNInfo.Builder(this.pEnergyExpenditureExponent,this.pIterations).workingDirectoryName(ROCN.outputFilesName).inputFlowFileName("").inputTCAFileName("").outputDirectoryName(outputFlowName).build();		
	  ocnInfo.print();
	  TextIO.writeStandardOutput();

	  
  }
	
	public static void main(String[] args) throws EsriAsciiHeaderException, EofException {
		System.out.println("This is ROCN main");
        ROCN ocn=ROCN.initialize();
        //ocn.print();
        ocn.run();
        ocn.ocnDeploy();
		System.out.println("This ends computation");
	}
}
