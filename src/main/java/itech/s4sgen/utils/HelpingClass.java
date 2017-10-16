package itech.s4sgen.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class HelpingClass {
	
	private static MessageDigest md;

	private static SimpleDateFormat slashFormat =  new SimpleDateFormat("dd/MM/yyyy");
	private static SimpleDateFormat dashFormat =  new SimpleDateFormat("dd-MM-yyyy");
	
	public static String cryptWithMD5(String pass){
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] passBytes = pass.getBytes();
			md.reset();
			byte[] digested = md.digest(passBytes);
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<digested.length;i++){
				sb.append(Integer.toHexString(0xff & digested[i]));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException ex) {
			System.out.println("Error In Encrypting Password !!!");
		}
		
		return null;
		
		
	}
	
	public static Date stringToDate(String date) throws ParseException {
		if(date.contains("/"))
			return slashFormat.parse(date);
		else
			return dashFormat.parse(date);
	}
	
	public static List<String> getStudyProgramsList(){
		return Arrays.asList("Bachelors in Computer Science"
				,"Bachelors in Information Technology"
				,"Bachelors in Software Engineering"
				,"Bachelors in Business Administration");
	}
	
	public static List<String> getSemestersList(){
		return Arrays.asList("1st Semester",
				"2nd Semester",
				"3rd Semester",
				"4th Semester",
				"5th Semester",
				"6th Semester",
				"8th Semester");
	}
	
	public static List<String> getStudyCoursesList(){
		return Arrays.asList("CS101",
				"STA101",
				"MTH101",
				"ENG101",
				"IT101");
	}
	
	public static List<String> getMonthsList(){
		return Arrays.asList("January",
				"February",
				"March",
				"April",
				"May",
				"June",
				"July",
				"August",
				"September",
				"October",
				"November",
				"December");
	}

	public static List<String> getDutiesList(){
		return Arrays.asList("School Cleaning",
				"School Maintenance",
				"School Security",
				"School Transport",
				"Other");
	}
	
	public static List<String> getRoutesList(){
		return Arrays.asList("Route 1",
				"Route 2",
				"Route 3",
				"Route 4",
				"Route 5");
	}
	
	public static List<String> getServicesList(){
		return Arrays.asList("Electricity",
				"Telephone Services",
				"Internet Services",
				"Water Services",
				"Other");
	}
	
	public static List<String> getDoctorsSpecialitiesList(){
		return Arrays.asList("Heart's Specialist",
		"Ear,Nose and Neck Specialist",
		"Stomach, liver Specialist",
		"Brain & Nerves Specialist",
		"Kidney & Bledder Specialist");
	}
	
	public static List<String> getMemberShipTypeList(){
		return Arrays.asList("Single Full Membership",
		"Associate Full Membership",
		"Junior Full Membership",
		"Social Membership",
		"Daily Dinner Membership");
	}
	
	public static List<String> getRestaurantJobsList(){
		return Arrays.asList("Food & Dishes Prepairing",
				"Order Taker and Dish Delivery",
				"Restaurant Security",
				"Restaurant Cleaning",
				"Orders Delivery Boy");
	}
}
