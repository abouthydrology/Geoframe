package org.geoframe.util;
import java.io.File;


public class ListFiles {
	
	public static  File[] listOfFiles;
	public String wd;
	public File thisFolder;
	
	public ListFiles(String wd){
		
		 thisFolder=new File(wd);
		 listOfFiles= thisFolder.listFiles();
		 int jj=0,ii=0;
		 System.out.println("Directories");		 
		 for (int i = 0; i < listOfFiles.length; i++) 
		  {
		   if (listOfFiles[i].isDirectory()) 
		   {
		   jj++;
		   System.out.println("["+jj+"] "+ listOfFiles[i].getName());
		      }
		  }
		 ii=jj;
		 if(jj==0) System.out.println(".");
		 System.out.println("Files");
		 for (int i = 0; i < listOfFiles.length; i++) 
		  {
		   if (listOfFiles[i].isFile()) 
		   {
		  jj++;
		   System.out.println("["+jj+"] "+ listOfFiles[i].getName());
		      }
		   if(ii==jj) System.out.println(".");
		  }
		
	}
	
	public static void main(String[] args){
		
		System.out.println("This is ListFiles main()");
		
		new ListFiles(CreateFolder.userHome);
		
		System.out.println("This ends computation");
	}
	

}
