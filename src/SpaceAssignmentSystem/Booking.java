package SpaceAssignmentSystem;

public class Booking {
	public DateSA start;
	public DateSA end;
	public String owner;

	public Booking(DateSA start, DateSA end, String s) {
		this.start = start;
		this.end = end;
		owner = s;
	}
	
	public String toString() {
		return String.format("Start date: %s\nEnd date: %s\nOwner: %s", start.toString(), end.toString(), owner);
	}
}
