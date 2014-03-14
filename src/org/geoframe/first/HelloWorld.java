//This class, one of the simplest, is contained in a package. (From Wikipedia: http://en.wikipedia.org/wiki/Java_package)
//A Java package is a mechanism for organizing Java classes into namespaces similar to the modules of Modula. Java packages can be stored in compressed files called JAR files, allowing classes to download faster as a group rather than one at a time. Programmers also typically use packages to organize classes belonging to the same category or providing similar functionality.
//A package provides a unique namespace for the types it contains.
//Classes in the same package can access each other's package-access members.
package org.geoframe.first;
//This is my first real java program. I read a few books on Java. Notably "Thinking in Java
//by Bruce Eckel, which is offered open source in its fourth edition. I also downloaded from 
// ItunesU the lectures by Prof. Ronchetti of University of Trento, that I found very illuminating, 
// especially for who, like me, as knowledge of C programming.package org.geoframe.first;

public class HelloWorld {
//A class (http://en.wikipedia.org/wiki/Class_(object-oriented_programming)) starts with 
//Capital letters and, in java, inherits the name of the file that contains it. A file
//can contain just one public class. Public means that the class is visible externally.
	
	/**
	 * @param args
	 */
// Being one of the simplest classes it just contains one method: the main(). it is 
// the first to be executed and organize the flow of the java program. However a main()
//is not mandatory in a java class. The main can be contained in another class of the
//same package. The main method is always "public static void"
	public static void main(String[] args) {
		// System.out.println is a method "println"
        System.out.println("Pietro ciao!");
        //Pietro is my son
	}

}
