package org.geoframe.ocn;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.geoframe.io.TextIO;

public class SimulationInfo {
	
	String program;
	String version;
	String author;
	String computer;
	String operatingSystem;
	String operatingSystemVersion;
	
	String date;
    
    public SimulationInfo(Object object){
    	this.program=object.getClass().toString();
    	//TODO Clearly to be changed to be get interactively
    	this.version="1/8";
    	//TODO Clearly to be changed
    	this.author="Riccardo Rigon";    	
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	    //get current date time with Date()
		Date date = new Date();
		this.date=dateFormat.format(date);
		this.computer="MACBook Pro 13inches ";
		this.operatingSystem=System.getProperty("os.name");
		this.operatingSystemVersion=System.getProperty("os.version");
		
    }
    
    public  void print(){
    	TextIO.putln("These outputs were generated by "+this.program);
    	TextIO.putln("This program was run by: "+this.author);
    	TextIO.putln("at : "+this.date);
    	TextIO.putln("using a "+this.computer);
    	TextIO.putln("operating System : "+this.operatingSystem+" version "+this.operatingSystemVersion);
    	TextIO.putln();
    }
    
	public static void main(String[] args) {
		System.out.println("This is SimulationInfo main");
		ROCN ocn=ROCN.initialize();
		SimulationInfo ocnInfo=new SimulationInfo(ocn);
		ocnInfo.print();
		System.out.println("This ends computation");

	}

}
