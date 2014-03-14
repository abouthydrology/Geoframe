package org.geoframe.io;

/** 
 * GetTwoDoubleParameters reads two double parameters from stIO. A very simple
 * class that uses a static method.
 * 
 * @author Riccardo Rigon
 *
 */
public class GetTwoDoubleParameters {
	
	public double a;
	public double  b;
	
//What if we make them private ?	
	/**
	 * set Parameter is used to get a parameter from StdIO. It uses the class TextIO
	 * @param name
	 * @return
	 */
	static public double setParameter(String name){
		double c;		
			TextIO.put("Please insert the '"+name+"' parameter: ");
			c = TextIO.getDouble();
		return c;
	}
	
	public static void main(String[] args) {
		// Now we test the functioning of the class
		double a,b;
		
		System.out.println("This is the GetTwoDoubleParameters main( )");
		
		a=setParameter("a");
		b=setParameter("b");
		System.out.println("a = "+a);
		System.out.println("b = "+b);
		System.out.println("This ends computation");		

	}

}
