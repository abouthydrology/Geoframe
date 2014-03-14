package org.geoframe.annotations;

public @interface howTo {
	//TODO 
	//This would help with associating a class with a HowTo 
	//To make clear for what a class is used
	//For istance to GetfileName should be associated to 
	//How to get a file Name from Console
	//Processing HowTos should give the html list with all the HowTos
	String howTo() default "";
}
