package org.geoframe.arrays.util;

public class SimpleArrayFiller {
	
	public SimpleArrayFiller(int [][] mi){
		for(int i=1;i<mi.length;i++){
			for(int j=1; j<mi[0].length;j++){
				mi[i][j]=j;
			}
		}
	}

	public static void main(String[] args) {
		System.out.println("This is SimpleArrayFiller");
		int[][] trial=new int[8][7];
		new SimpleArrayFiller(trial);
        new SimpleArrayPrinter(trial);
		System.out.println("This ends computation");
		
	}

}
//A Drawback in doing this is that you do not use polymorphism. I case you can create an abstract 
//class and various subclasses. Besides these simple classes do not have arguments and methods. 
//Therefore is probably better to implement them as a method of a class method.