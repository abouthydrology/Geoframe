package org.geoframe.first;
import org.geoframe.io.TextIO;

public class LinearEquationSolver {
	
	//Declaring static the quantities below, we make them to be a property of the class, not of the single objects.
	//Therefore when you instatiate a new LinearEquationSolver, no space is allocated for a,b, and sol, and any of them
	//share the same parameters. Therefore it would have sense to declare the class itself static ? And if we have more
	//linear equations to be solved in parallel threads ?
	
	//private double a,b,sol=Double.NaN;
	private double sol=Double.NaN;
	
	public LinearEquationSolver(double a, double b){
	
		if(a !=0){
			sol = -b/a;
		} else if(b !=0) {
			throw new RuntimeException("\nA solution does not exists because a =0\n");
		}else {
			throw new RuntimeException("\nThis is the trivial identity 0=0\n");
		}
				
	}
	
	public double getSolution(){
		if(Double.isNaN(sol)) {
			throw new RuntimeException("\nLinearEquationSolver not initialized\n");
			}else{
				return sol;
			}
	}
	
	public static void main(String[] args){
		
		double a =3,b=4,sol;
		System.out.println("\nThis is the LinearEquationSolver main( )\n");
//      Try ... catch not used here because we know that the solution exists
		
		LinearEquationSolver ll = new LinearEquationSolver(a,b);
		sol=ll.getSolution();
		System.out.println("The solution of "+a+" x + "+b+" == 0 is: x = "+sol);

//      Now to test the try-catch 
		System.out.println("\nNow testing the try ... catch ");
	a=0;
		try{	
		LinearEquationSolver ll1 = new LinearEquationSolver(a,b);
		sol=ll1.getSolution();
		System.out.println("The solution of "+a+" x + "+b+" == 0 is: x = "+sol);
		}
		
		catch(RuntimeException e){
			TextIO.put(e);
			}
		System.out.println("\nNow testing the try ... catch with the last option\n");		
	b=0;
		try{	
		LinearEquationSolver ll2 = new LinearEquationSolver(a,b);
		sol=ll2.getSolution();
		System.out.println("The solution of "+a+" x + "+b+" == 0 is: x = "+sol);
		}
		catch(RuntimeException e){
			TextIO.put(e);
			}
//Please note that the code between the try and catch is simply repeated for any case, so it would be helpful to do a class
// that embeds it and save a lot of code writing.
		
		System.out.println("\nEnd of Computation\n");
		
	}
//What happens if we make the class a static one ?	

}
