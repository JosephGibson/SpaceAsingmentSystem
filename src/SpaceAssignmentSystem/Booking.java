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
				System.out.println("J = " + j);
				System.out.println("i =" + i);
				if( i == j ) { System.out.println("shared day");
								sharedDay = true; }
			}
		}
		if(!sharedDay) {System.out.println("e1"); return false; }
		else if(startTime >= b.startTime && startTime <= b.endTime && semeseter == b.semeseter) {System.out.println("a1" + b.owner); return true; }
		else if(endTime >= b.startTime && endTime <= b.endTime && semeseter == b.semeseter) {System.out.println("a2"); return true; }
		else{System.out.println("e2"); return false; }
	}

	public String getSemeseter() { 
		return semeseter;
	}

	public void setSemeseter(String semeseter) {
		this.semeseter = semeseter;
	}

	public int[] getDays() {
		return days;
	}

	public int getStartTime() {
		return startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public String getOwner() {
		return owner;
	}
}
