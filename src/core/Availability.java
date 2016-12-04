package core;

import java.util.ArrayList;
import java.util.Date;

public class Availability {
	
	public static ArrayList<Room> findAvailableRooms(Date checkIn, Date checkOut, RoomType roomType) {
		
		//TODO:get all rooms from DatabaseHandler with this roomType
		// availableRooms = 
		ArrayList<Room> availableRooms = new ArrayList<>();
		
		availableRooms = getAvailableRooms(availableRooms, checkIn, checkOut);
		
		return availableRooms;
	}
	
	private static ArrayList<Room> getAvailableRooms(ArrayList<Room> availableRooms, Date checkIn, Date checkOut) {
		for ( Room room : availableRooms ) {
			for ( Date date = checkIn; date.compareTo(checkOut) < 0; date = new Date(date.getTime() + (1000*60*60*24)) ) {
				if ( room.getBooked().contains(room) ) {
					availableRooms.remove(room);
					break;
				}
			}
		}
		return availableRooms;
	}
	
//
//	Hotel h;
//	boolean booked;
//
//	CheckAvailablity(Hotel h){
//		this.h = h;
//	}
//
//	List<Integer> checkAvailablity(Date Check_InDate,Date Check_OutDate){
//
//		List<Integer> i = new ArrayList<Integer>();
//
//
//
//		for(Room r: h.room){
//
//			booked = false;
//
//
//			for(Date d = Check_InDate; d.compareTo(Check_OutDate)<0; d = new Date(d.getTime() + (1000 * 60 * 60 * 24))){
//				if(!booked){
//					if(r.getBooked().contains(d)){
//						booked = true;
//
//					}
//				}else
//					break;
//
//			}
//			if(!booked)
//				i.add(r.getId());
//
//		}
//		return i;
//
//	}
//
//
//
//	boolean checkAvailablity(Room r,Date Check_InDate,Date Check_OutDate){
//
//		booked = false;
//
//		for(Date d = Check_InDate; d.compareTo(Check_OutDate)<0; d = new Date(d.getTime() + (1000 * 60 * 60 * 24))){
//			if(!booked){
//				if(r.getBooked().contains(d)){
//					booked = true;
//
//				}
//			}else
//				break;
//
//		}
//		if(!booked)
//			return true;
//		else
//			return false;
//
//	}

}
