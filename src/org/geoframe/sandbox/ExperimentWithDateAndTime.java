package org.geoframe.sandbox;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//From http://www.mkyong.com/java/java-how-to-get-current-date-time-date-and-calender/
public class ExperimentWithDateAndTime {

	public static void main(String[] args) {
		  System.out.println("This is ExperimentWithDateAndTime main");
		   DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		   //get current date time with Date()
		   Date date = new Date();
		   System.out.println(dateFormat.format(date));
	 
		   //get current date time with Calendar()
		   Calendar cal = Calendar.getInstance();
		   System.out.println(dateFormat.format(cal.getTime()));
		   System.out.println("This ends computation");

	}

}
