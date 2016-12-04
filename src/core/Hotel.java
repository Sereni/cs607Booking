package core;

import java.util.ArrayList;
import java.util.List;

public class Hotel {

	
	List<Room> room = new ArrayList<Room>();
	List<Room> roomInit;
	{
		roomInit = new ArrayList<Room>();
		roomInit.add(new Room(1,RoomType.Single));
		roomInit.add(new Room(2,RoomType.Single));
		roomInit.add(new Room(3,RoomType.Single));
		roomInit.add(new Room(4,RoomType.Double));
		roomInit.add(new Room(5,RoomType.Double));
		roomInit.add(new Room(6,RoomType.Double));
		roomInit.add(new Room(7,RoomType.Studio));
		roomInit.add(new Room(8,RoomType.Studio));
		roomInit.add(new Room(9,RoomType.Studio));
		roomInit.add(new Room(10,RoomType.Suite));
		roomInit.add(new Room(11,RoomType.Suite));
		roomInit.add(new Room(12,RoomType.Suite));
	}
	
		Hotel(){
			
			room = roomInit;
		}
		
		
	
	
}
