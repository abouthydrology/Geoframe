package org.geoframe.util;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateAndTime2String {
     
	public static String dateTime;
	
	public DateAndTime2String(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		   //get current date time with Date()
		   Date date = new Date();
		   DateAndTime2String.dateTime=dateFormat.format(date);
	}
	
	public static void main(String[] args) {
		 System.out.println("This is DateAndTime2String main");
		 new DateAndTime2String();
		 System.out.println(DateAndTime2String.dateTime);   	  
		 System.out.println("This ends computation");

	}


}
