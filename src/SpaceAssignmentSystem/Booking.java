package SpaceAssignmentSystem;

public class Booking {
	public String semeseter;
	public int[] days;
	public int startTime;
	public int endTime;
	public String owner;	

	public Booking(String sm, int[] d, int s, int e, String o) {
		semeseter = sm;
		days = d;
		startTime = s;
		endTime = e;
		owner = o;
	}
	
	public boolean overlap(Booking b) {
		
		boolean sharedDay = false;
		for(int i : days) {
			for(int j : b.days) {
				if( i == j && i != -1) { sharedDay = true; }
			}
		}
		if(!sharedDay) {return false; }
		else if(startTime >= b.startTime && startTime < b.endTime && semeseter == b.semeseter) {return true; }
		else if(endTime > b.startTime && endTime <= b.endTime && semeseter == b.semeseter) {return true; }
		else{ return false; }
	}

}
