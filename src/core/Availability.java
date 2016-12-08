package core;

import java.util.ArrayList;
import java.util.Date;

import persistence.DatabaseHandler;

/**
 * This class is for checking availability and find available rooms
 * 
 * @author Aida
 */
public class Availability {
	
	/**
	 * Give us an ArrayList of rooms with type of roomType (e.g. SINGLE) that are available between check in and check out dates
	 * First we get all rooms with specified roomType from database
	 * Second we check availability of each room
	 * @param checkIn check in date
	 * @param checkOut check out date
	 * @param roomType type of room 
	 * @return list of available rooms based on arguments
	 */
	public static ArrayList<Room> findAvailableRooms(Date checkIn, Date checkOut, RoomType roomType) {
		
		// read all rooms with specified roomType
		ArrayList<Room> availableRooms;
		try {
			availableRooms = new DatabaseHandler().getRooms(roomType.name());
		} catch (Exception e) {
			// TODO handle exception better
			availableRooms = new ArrayList<Room>();
			e.printStackTrace();
		}
		
		// check availability of each room based on dates
		for ( Room room : availableRooms ) {
			if ( !isRoomAvailable(room, checkIn, checkOut) ) //if a room is not available we remove it from the list
				availableRooms.remove(room);
		}
		
		//TODO: check with array list of blocked room in Runner
		return availableRooms;
	}
	
	/**
	 * check availability of a room between check in and check out dates
	 * a room is available if for each date from check in to checkout does not booked
	 * @param room the room that we want to check the availability
	 * @param checkIn check in date
	 * @param checkOut check out date
	 * @return true if room is available between specified dates and return false if it's not available
	 */
	public static boolean isRoomAvailable( Room room, Date checkIn, Date checkOut) {
		//for each date between checkIn and checkOut inclusive checkIn and checkOut
		for ( Date date = checkIn; date.compareTo(checkOut) < 0; date = new Date(date.getTime() + (1000*60*60*24)) ) {
			if ( room.getBooked().contains(room) ) {
				return false;
			}
		}
		return true;
	}
}
