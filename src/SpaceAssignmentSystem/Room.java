package SpaceAssignmentSystem;

import java.util.ArrayList;

public class Room {
	public String name;
	public ArrayList<Booking> bookings;
	
	public Room(String s) {
		name = s;
		bookings = new ArrayList<Booking>();
	}

}
