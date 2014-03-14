package org.geoframe.exec;
import org.geoframe.io.*;
import org.geoframe.first.*;

/**
 * It solves a linear equation using two double parameters getting them fron StdIO.
 * It uses TextIO by DJE.  Two different methods are used to get the parameter. One just 
 * to get two parameters. The other to get any number of named parameters.
 * 
 * @author Riccardo Rigon 2013
 *
 */
public class LinearEquationSolverWithVariableInput {
	static double a;
	static double b,sol;
	
	public static void main(String[] args) {

		System.out.println("This is the LinearEquationWithVariableInputs main()");
		
		//This solves the linear equation by using the GetTwoDoubleParametersClass

		//Declaring and getting the parameters
		System.out.println("Solving a lineat equation using GetTwoDoubleParameters");
		a=GetTwoDoubleParameters.setParameter("a");
		b=GetTwoDoubleParameters.setParameter("b");
		//Printing them
		System.out.println("a = "+a);
		System.out.println("b = "+b);
		//Solving the linear equation
		try{
		LinearEquationSolver ll = new LinearEquationSolver(a,b);
		sol=ll.getSolution();
		System.out.println("The solution of "+a+" x + "+b+" == 0 is: x = "+sol);
		}catch(RuntimeException e){
			TextIO.put(e);
		}
		//Then it solves the linear equation by using GetDoubleParameters
		System.out.println("Solving a linear equation using GetDoubleParameters");
		//Declaring the parameters
		String[] pa={"a","b"};
		//Using the class that makes the input job
		GetDoubleParameters prmtrs=new GetDoubleParameters(pa);  
		//Prints the parameters and their names
		prmtrs.print(pa);
		//Solving the linear equation. The same as above (but with different parameters)
		a=prmtrs.getParameters()[0];
		b=prmtrs.getParameters()[1];
		try{
		LinearEquationSolver ll1 = new LinearEquationSolver(a,b);	
		sol=ll1.getSolution();
		System.out.println("The solution of "+a+" x + "+b+" == 0 is: x = "+sol);
		}catch(RuntimeException e){
		  TextIO.put(e);
		}
		//Solving the linear equation. But reading the variables from a file
		System.out.println("Solving a lineat equation using GetDoubleParametersFromFile");
		GetDoubleParametersFromFile aDataFrame=new GetDoubleParametersFromFile();
		for(int i=0;i<aDataFrame.rows;i++){
		a=aDataFrame.vls[i][0];
		b=aDataFrame.vls[i][1];
		try{
			LinearEquationSolver ll1 = new LinearEquationSolver(a,b);	
			sol=ll1.getSolution();
			System.out.println("The solution of "+a+" x + "+b+" == 0 is: x = "+sol);
			}catch(RuntimeException e){
			  TextIO.put(e);
			}		
		}
		System.out.println("This ends the computation");

	}

}
