package SpaceAssignmentSystem;

public class Request {
	public String room;
	public Booking booking;
	public int priority;
	
	
	public Request(String r, String se, int[] days,  int s, int e, String o, int p) {
		room = r;
		booking = new Booking(se, days, s, e, o);
		priority = p;
	}


	public String getRoom() {
		return room;
	}


	public Booking getBooking() {
		return booking;
	}


	public int getPriority() {
		return priority;
	}
	
}
