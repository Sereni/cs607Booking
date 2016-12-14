package model;

import java.util.ArrayList;
import model.Room;
import view.commandline.HotelEventProcess;

/**
 * singelton 
 * subject for observer pattern
 * @author aida
 *
 */
public class BlockedRooms {

	private static BlockedRooms instance;
	private static ArrayList<HotelEventProcess> observers;
	private static ArrayList<Room> rooms;
	
	private BlockedRooms() {
		observers = new ArrayList<>();
		rooms = new ArrayList<>();
	}

	public static synchronized BlockedRooms getInstance() {
		if ( instance == null )
			return new BlockedRooms();
		return instance;
	}
	
	public void addObserver(HotelEventProcess event) {
		observers.add(event);
	}

	public void addRoom(Room room) {
		rooms.add(room);
		for( HotelEventProcess event: observers )
			event.notify();
	}
}