package org.geoframe.util;

import java.io.File;

import org.geoframe.io.TextIO;


public class Folder {

	//The next is static because User Home is unique (is a property of the class not of its instances). It is public
		//to be visible everywhere. It is final because it cannot be changed.
		/**
		 * userHome contains the home user directory 
		 */
		public static final String userHome=System.getProperty("user.home");
	    /**
	     * contains the name (just the name or part of it, not the full name) of the directory or file on which the user works
	     */
		public  String shortName;
		/**
		 * contains the working Path for any use
		 * 
		 */
		public String workingPathName;
		/**
		 * contains the File object relative to the working path for any operation on it
		 */
		public File workingPath;

	//Set a directory given with relative reference to the user Home;
		/**
		 * explores a directory or a file name suitable to be used and return it
		 * Is a static method and can be used without actually allocating a Folder object
		 * 
		 * @return the file or directory name
		 */
		public static String getFileName(){
			
			String relativeWdPath,workingDirectory;
			String inputFileName;
			
			//Folder workingDirectory;
			//Because userHome is a public static field, it can be used without having initialized any instance of Folder:
			//This because the static variables have their own memory space and are initialized by their own;
			System.out.println(Folder.userHome);
			//Also the static method can be called without any instantiation of a Folder object.
			String ynf="No";
			do{
			System.out.println("Input the relative path of the working directory");	
			relativeWdPath=TextIO.getlnWord();
			workingDirectory=Folder.folderRequest(relativeWdPath);		
			new ListFiles(workingDirectory);
			System.out.println("Do you want to change directory ? [write 'Y' or the name of the file  you want to read or write]");
			ynf=TextIO.getlnWord();
			}while(ynf.equalsIgnoreCase("Yes") || ynf.equalsIgnoreCase("Y") || ynf.equalsIgnoreCase("Ye"));
	        if(ynf.equalsIgnoreCase("No")||ynf.equalsIgnoreCase("N")){
	        	//Here would be better to add some treatment of the inputs
	        	System.out.println("Enter the input file name");
	        	inputFileName=workingDirectory+File.separator+TextIO.getln();
	        	//System.out.println("*Input:"+inputFileName);
	        	File test=new File(inputFileName);
	        	while(!test.exists()){
	        		System.out.println("Enter the correct file name");
	        		inputFileName=workingDirectory+File.separator+TextIO.getlnWord();
	        		test=new File(inputFileName);
	        	}
	        }else{
	        	//The case where the file name is given as input for short  	
	        	inputFileName=workingDirectory+File.separator+ynf;
	        	File test=new File(inputFileName);
	        	while(!test.exists()){
	        		System.out.println("Enter the correct file name");
	        		inputFileName=workingDirectory+File.separator+TextIO.getlnWord();
	        		test=new File(inputFileName);
	        	}
	        }
	        
			return inputFileName;
			
		}
		
	public static String getDirectoryName(){
			
			String relativeWdPath,workingDirectory;
			String inputFileName;
			
			//Folder workingDirectory;
			//Because userHome is a public static field, it can be used without having initialized any instance of Folder:
			//This because the static variables have their own memory space and are initialized by their own;
			System.out.println(Folder.userHome);
			//Also the static method can be called without any instantiation of a Folder object.
			String ynf="No";
			do{
			System.out.println("Input the relative path of the working directory");	
			relativeWdPath=TextIO.getlnWord();
			workingDirectory=Folder.folderRequest(relativeWdPath);		
			new ListFiles(workingDirectory);
			System.out.println("Do you want to change directory ? [write 'Y' or the name of the file  you want to read or write]");
			ynf=TextIO.getlnWord();
			}while(ynf.equalsIgnoreCase("Yes") || ynf.equalsIgnoreCase("Y") || ynf.equalsIgnoreCase("Ye"));
	        if(ynf.equalsIgnoreCase("No")||ynf.equalsIgnoreCase("N")){
	        	//Here would be better to add some treatment of the inputs
	        	System.out.println("Enter the directory name");
	        	inputFileName=workingDirectory+File.separator+TextIO.getln();
	        	//System.out.println("*Input:"+inputFileName);
	        	/*	File test=new File(inputFileName);
	        	while(!test.exists()){
	        		System.out.println("Enter the correct file name");
	        		inputFileName=workingDirectory+File.separator+TextIO.getlnWord();
	        		test=new File(inputFileName);
	        	}*/
	        }else{
	        	//The case where the directory name is given as input for short  	
	        	inputFileName=workingDirectory+File.separator+ynf;
	        	/*File test=new File(inputFileName);
	        	while(!test.exists()){
	        		System.out.println("Enter the correct file name");
	        		inputFileName=workingDirectory+File.separator+TextIO.getlnWord();
	        		test=new File(inputFileName);
	        	} */
	        }
	        
			return inputFileName;
			
		}
/**
 * 		
 * @return the full path directory name
 */
		
		public static String getDirectory(){
			
			String relativeWdPath,workingDirectory;
			String inputFileName;
			
			//Folder workingDirectory;
			//Because userHome is a public static field, it can be used without having initialized any instance of Folder:
			//This because the static variables have their own memory space and are initialized by their own;
			System.out.println(Folder.userHome);
			//Also the static method can be called without any instantiation of a Folder object.
			String ynf="No";
			do{
			System.out.println("Input the relative path of the working directory");	
			relativeWdPath=TextIO.getlnWord();
			workingDirectory=Folder.folderRequest(relativeWdPath);		
			new ListFiles(workingDirectory);
			System.out.println("Do you want to change directory ? [write 'Y' or the name of the file  you want to read or write]");
			ynf=TextIO.getlnWord();
			}while(ynf.equalsIgnoreCase("Yes") || ynf.equalsIgnoreCase("Y") || ynf.equalsIgnoreCase("Ye"));
	    
	       inputFileName=workingDirectory;
	       File test=new File(inputFileName);
	       while((!test.exists()) || (!test.isDirectory())){
	        	System.out.println("Enter the correct directory name with full relative path");
	        	inputFileName=TextIO.getlnWord();
	        	test=new File(inputFileName);
	        }
	     
	        
	        
			return inputFileName;
			
		}	
		/**
		 * Ask for the file name in the case the working directory is already known
		 * @param workSpace is the info file containing information about the working directory
		 * @return the name of the file
		 */
		public static String getFileName(InfoFolder workSpace){
			
			String inputFileName;
			
			//Folder workingDirectory;
			//Because userHome is a public static field, it can be used without having initialized any instance of Folder:
			//This because the static variables have their own memory space and are initialized by their own;
			System.out.println(Folder.userHome);
			//Also the static method can be called without any instantiation of a Folder object.
	        System.out.println("Enter the input file name");
	        inputFileName=workSpace.workingPathName+File.separator+TextIO.getln();
	        System.out.println("*Input:"+inputFileName);
	
			return inputFileName;
			
		}	
	/**
	 * return a file or directory name after an interactive quest initiated to 
	 * see if it really can be instantiated. 
	 * @param flx "requires a candidate name"
	 * @return
	 */
		public static String folderRequest(String flx){
			
			String init=Folder.userHome;
			String total;
			//total=init+File.separator+flx;
			total=init+flx;
			File workingPath=new File(total);
			while(!workingPath.exists()){
				 System.out.println("Please re-enter the relative working path (directory does not exists)");
			     flx=TextIO.getlnWord();	
			     total=init+flx; ///Just cut File.separator
			     workingPath=new File(total);
			}
			return total;
		}
		
	//Implement the step-by-step way to create a directory	
	//To be revised since it gives an exception when calling the keyword F for FILE	
	/**
	 * a step by step way to create a file or directory by looking at the hard disk
	 * @param flx "is the candidate file name"
	 * @return the final choice for the name
	 * 
	 */
	public static String IteratedFolderRequest(String flx){
		File initialFldr=new File(flx);
		String fls;
		
		do{
		System.out.println("Directories present in the folder:");	
		File[] listOfFls = initialFldr.listFiles();
		   int j=0;
		 for (int i = 0; i < listOfFls.length; i++) 
		  {

		   if (listOfFls[i].isDirectory()) 
		   {
		   fls = listOfFls[i].getName();
		   System.out.println(fls);
		   j++;
		      }
		  }
		if(j==0) System.out.println("No directories are present in this folder");
		TextIO.putln("Input the Folder you want to choose or the name of a new Folder");
		flx+=File.separator+TextIO.getln();
		initialFldr=new File(flx);
		System.out.println("The current directory is: "+flx);	
		System.out.println("Is the current directory OK ? [Y,N, or F to see folder's files]");
		String ynf=TextIO.getlnWord();
		//How to compare two or more strings in Java
		if(ynf.equalsIgnoreCase("Yes") || ynf.equalsIgnoreCase("Y") || ynf.equalsIgnoreCase("Ye")){
			break;
		}else if(ynf.equalsIgnoreCase("File") || ynf.equalsIgnoreCase("F") || ynf.equalsIgnoreCase("Fi") || ynf.equalsIgnoreCase("Fil")  ){
			//List Files
			System.out.println("List Files");
			File[] lof = initialFldr.listFiles();
			   int jj=0;
			 for (int i = 0; i < lof.length; i++) 
			  {

			   if (lof[i].isFile()) 
			   {
			   fls = lof[i].getName();
			   System.out.println(fls);
			   jj++;
			      }
			  }
			 if(jj==0) System.out.println("No file exists in this directory");
			 //Now ask again if this folder is OK
			 System.out.println("Is the current directory OK ? [Y,N]");
			 ynf=TextIO.getlnWord();
			 //How to compare two or more strings in Java
				if(ynf.equalsIgnoreCase("Yes") || ynf.equalsIgnoreCase("Y") || ynf.equalsIgnoreCase("Ye")){
					break;
				}else{
					//do nothing
				}
		}else{
			// System.out.println("Do Nothing");
			// do nothing
		}
		}while(initialFldr.exists());	
		
	return flx;
	}

}
