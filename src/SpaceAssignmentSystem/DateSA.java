package SpaceAssignmentSystem;

public class DateSA implements Comparable<DateSA> {
	public int month;
	public int day;
	public int hour;
	public int min;
	
	public DateSA(int m, int d) throws SchedulerException {
		if(m > 0 && m < 13) {
			month = m;
		}
		else {
			throw new SchedulerException();
		}
		if(d > 0 && d< 32) {
			day = d;
		}
		else {
			throw new SchedulerException();
		}
		hour = -1;
	}
	
	public DateSA(int m, int d, int h, int mi) throws SchedulerException {
		if(m > 0 && m < 13) {
			month = m;
		}
		else {
			throw new SchedulerException();
		}
		if(d > 0 && d< 32) {
			day = d;
		}
		else {
			throw new SchedulerException();
		}
		if(h > 6 && h < 23) { //rooms can only be booked between 6am and 11pm
			hour = h;
		}
		else {
			throw new SchedulerException();
		}
		if( mi%15 == 0 && mi >= 0 && mi < 60 ) {
			min = mi;
		}
		else {
			throw new SchedulerException();
		}
	}

	@Override
	public int compareTo(DateSA d) {
		if(month > d.month) {
			return 1;
		}
		if(month < d.month) {
			return -1;
		}
		if(day > d.day) {
			return 1;
		}
		if(day < d.day) {
			return -1;
		}
		if(d.hour == -1 || hour == -1) {
			return 0;
		}
		if(hour > d.hour) {
			return 1;
		}
		if(hour < d.hour) {
			return  -1;
		}
		if(min > d.min) {
			return 1;
		}
		if(min < d.min) {
			return  -1;
		}
		else {
			return 0;
		}
	}

	public boolean after(DateSA d) {
		if(this.compareTo(d) == 1) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean before(DateSA d) {
		if(this.compareTo(d) == -1) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean equals(DateSA d) {
		if(this.compareTo(d) == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String toString() {
		return month + "/" + day + "/" + hour + "/" + min;
	}
	
}
