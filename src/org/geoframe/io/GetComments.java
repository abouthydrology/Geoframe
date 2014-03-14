package org.geoframe.io;
import java.io.File;

import org.geoframe.util.*;

public class GetComments {
	
	public GetComments(){
		
		char bang;
		String lineComment;
		
		bang=TextIO.peek();
		int h=0;
		while(bang=='!'){
			h++;
			lineComment=TextIO.getlnString().substring(1);
			System.out.println("["+h+"] "+lineComment);
			//Essential update:
			bang=TextIO.peek();
		}

	}
//Note to eliminate the bang you can use a String Builder Object
//http://stackoverflow.com/questions/13386107/how-to-remove-single-character-in-a-string-in-java	
	public static void main(String[] args) {
		
		//To make thing shorter, here I hard-coded the FileName
		String inputFileName=CreateFolder.userHome+File.separator+"WorkSpaces/Data/ParametersLinearEquation.txt";
		TextIO.readFile(inputFileName);
		new GetComments();

	}

}

// Make a GetAndStoreComment which use a linked list to store the data
