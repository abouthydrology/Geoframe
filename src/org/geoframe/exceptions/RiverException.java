package org.geoframe.exceptions;
/**
 * This class is used to throw exception when dealing with river networks types. Nothing special, 
 * so far. 
 * 
 * @author Riccardo Rigon
 *
 */
//Essentially and exception is a class (like another) that, however, extends Exception 
//(or one of its subclasses)

public class RiverException extends Exception {
	
	/**
	 * What is a serial version UID and a serializable class ? 
	 * http://stackoverflow.com/questions/285793/what-is-a-serialversionuid-and-why-should-i-use-it
	 * 
	 */
	private static final long serialVersionUID = 3271782343460598787L;

	RiverException(String exception){
		System.out.println(exception);
	}

	public static void main(String[] args) {
		System.out.println("This is RiverException main()");
		try{
			
			throw new RiverException("This is my exception");
			
		}catch(RiverException e){
			
			System.out.println("Exception caught. This simplifies how eceptions work");
		}
		System.out.println("This ends computation");

	}

}
