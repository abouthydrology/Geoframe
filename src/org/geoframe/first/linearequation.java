//  The algorithm I implement
// is trivial: it solves the linear equation $ a*x + b = 0$, whose solution is known from high 
// school to be $x =  -b/a$ if a is different from zero.  However, the first example is not even
// as complicated like that, since the values of the parameters are given as fixed variable. 
// Still there are several things to notice for a novice, (like me, indeed).
// 1: the file name containing the class has the same name than the class, this is mandatory, 
//and all is lowercase: this is a convention used by most.
// 2: the class (a program chunk, not necessarily self-contained to produce an executable, 
// well, a JVM executable) is contained in a "package", a set of classes (small programs) devoted to a 
// common task. Default are provided by Java, but here the name org.geoframe.first is chosen.
// This allows a worldwide unique identification of the classes contained in a such a package [see ....]
// Actually this also means that the source code , the linearequation.java file, and the executable file, 
// linearequation.class file will be contained, under an appropriate subdirectory PATH/or/geoframe/first/
// of the file system, where PATH is the root directory for any Java program.
// Being in the same package guarantees to the classes to share their data and method [ ].
package org.geoframe.first;
// 3: It is often said that in Java everything is an object. Programming is about data and algorithms. This means that both data and 
// algorithms, and they are objects. Usually a program is made up of data objects interacting with algorithms objects. 
// When one executes a class, two main actions are performed. Space in memory is allocated
// for data and algorithms are allocated in the memory of the computer (which is intended as subdivided in a stack, and
// heap). Then a certain sequence of actions is executed. The schedule of these actions is contained in a "main" method".
// However some actions are done even before than executing what the main(\ ) schedules [ ] 
public class linearequation {
// Solve a linear equation x - 2 = 0 
// Here below, a javadoc comment. Please refers to [ ] for more information.
	/**
	 * @param args
	 * @author Riccardo Rigon, 2011
	 * 
	 */
// Please notice that this class is public, meaning that, once compiled it will be visible from anyone,
// For alternative see [ ] 
    static double a=1;
    static double b=-2;

	public static void main(String[] args) {
		 // Solving equation
		System.out.println( - b/a);
	}

}
