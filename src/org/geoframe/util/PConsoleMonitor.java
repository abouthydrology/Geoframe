package org.geoframe.util;

/**
 * PConsoleMonitor is used to control the progress of a calculation. It takes as reference the total number of "steps"
 * to be performed and an indicator of where the calculation has arrived. If a certain condition is obtained a message is
 * printed to the console.
 * 
 * @author Riccardo Rigon
 *
 */
public class PConsoleMonitor {
	//The total number of steps
	//The progress made
	private long tNumber;
	private long status;
	private long previousStatus;
	//Status getter (or setter, it depends from the point of view)	
	public void progress(long status){
		this.status = status;
		//If status/tNumber is greater or equal to 0.1, 0.2 ... 0.9 print 10%, 20% done
		//with a check
		processStatus();
	}
	//Process the status
	private void processStatus(){
		
		long percentage = (10*status/tNumber);
		
		//System.out.println("Percentage"+percentage);
		if(percentage > previousStatus){
			previousStatus=percentage;
			System.out.println(10*previousStatus+"% of work done \r");
		}else{
			//Do nothing
		}
		
	}

	
	
    //Constructor
	public PConsoleMonitor(long tNumber){
		this.tNumber=tNumber;
	}
	//main
	public static void main(String[] args) {
		System.out.println("This is PConsoleMonitor main()");
		
		int rows=70,cols=130;
		int area;
		
		PConsoleMonitor thisMonitor=new PConsoleMonitor(rows*cols);
		
		for(int i=0; i < rows ;i++){
			for(int j=0; j <cols; j++){
				area=i*j;
			}
			//System.out.println(i*cols);
			thisMonitor.progress(i*(cols));
		}
		System.out.println("This ends computation");

	}

}
