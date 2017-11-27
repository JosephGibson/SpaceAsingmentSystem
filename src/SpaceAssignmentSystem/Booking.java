package SpaceAssignmentSystem;

public class Booking {
	public char semeseter;
	public int[] dayOfWeek;
	public int startTime;
	public int endTime;
	public String owner;
	// Boolean system request everythings closed until its open.
	
	

	public Booking(DateSA start, DateSA end, String s) {
		this.start = start;
		this.end = end;
		owner = s;
	}
	
	public String toString() {
		return String.format("Start date: %s\nEnd date: %s\nOwner: %s", start.toString(), end.toString(), owner);
	}
}
