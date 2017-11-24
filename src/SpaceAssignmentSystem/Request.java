package SpaceAssignmentSystem;

public class Request {
	public String room;
	public Booking booking;
	
	public Request(DateSA start, DateSA end, String owner, String r) {
		booking = new Booking(start, end, owner);
		room = r;
	}
	
}
