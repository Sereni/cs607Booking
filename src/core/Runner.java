package core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Runner {
	
	
	public static void main(String[] args) {
		
		
		Hotel h = new Hotel(); 
		CheckAvailablity av = new CheckAvailablity(h);
		Customer customer = new Customer();
		
		int choice = 0;
		Hotel_Event event = new BookingEvent();
		System.out.println("Welcome to the hotel");
		System.out.println("What is your name");
		
		Scanner scanner = new Scanner(System.in);
		String userInput = scanner.nextLine();
		
		customer.setName(userInput);
		
		System.out.println("What is your email-id");
		
		String userInput01 = scanner.nextLine();
		
		customer.setEmail_id(userInput01);
		
		
		System.out.println("say 1 -> Booking & 2-> Cancelation");
		
		choice = scanner.nextInt();
		
		
		if(choice == 1){
			 event = new BookingEvent();
		}else if(choice ==2){
			event = new CancelEvent();
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
		
		System.out.println("Enter your Check_in date in dd-M-yyyy format");
		
		Scanner scanner02 = new Scanner(System.in);
		String userInput02 = scanner02.nextLine();
		String in_dateInString =userInput02;
		
		Date in_date = null;
		
		try {
			in_date = sdf.parse(in_dateInString);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		System.out.println(in_date);
		
		customer.setCheck_in(in_date);
		
		
		System.out.println("Enter your Check_out date in dd-M-yyyy format");
		
		Scanner scanner03 = new Scanner(System.in);
		String userInput03 = scanner03.nextLine();
		String out_dateInString =userInput03;
		
		Date out_date = null;
		
		try {
			out_date = sdf.parse(out_dateInString);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		customer.setCheck_in(out_date);
		
		//////// Booking and Canceling event starts here!
		
		core.RoomType type = null;
		
		if(choice == 1){
		
		////////// booking
			
		System.out.println("What Type of room you need: say 1 <- Single 2 <- Double 3 <- Studio 4 <- Suite");	
		
		Scanner scanner05 = new Scanner(System.in);
		int userInput05 = scanner05.nextInt();
		
		if(userInput05 == 1)
			type = RoomType.Single;
		else if(userInput05 == 2)
			type = RoomType.Double;
		else if(userInput05 == 3)
			type = RoomType.Studio;
		else if(userInput05 == 4)
			type = RoomType.Suite;
		else 
			System.out.println("Invalid Input");
		
		System.out.println("Available Rooms are:");
		System.out.println(av.checkAvailablity(in_date, out_date, type));
		
		System.out.println("which room do you want");
		
		Scanner scanner06 = new Scanner(System.in);
		int userInput06 = scanner06.nextInt();
		
		event.eventcreating(h.room.get(userInput06),in_date,out_date);
		customer.addRoom(h.room.get(userInput06));
		
		
		//// testing whether the room is blocked 
		System.out.println(av.checkAvailablity(in_date, out_date));
		
		}else if(choice == 2){
		
		////////// canceling
		
		
		
		if(!av.checkAvailablity(h.room.get(2),in_date, out_date)){
			event.eventcreating(h.room.get(2), in_date, out_date);
			
		}else
			System.out.println("Wrong Info");
		
		System.out.println(av.checkAvailablity(in_date, out_date));
			
			
		}
		
		
	}
	
	
	
}
