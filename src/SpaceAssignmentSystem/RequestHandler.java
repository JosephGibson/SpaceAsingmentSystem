package SpaceAssignmentSystem;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class RequestHandler {
	private Scheduler schedule;
	private ArrayList<Request> pending;
	private Queue<Request> denied;
	//
	
	public RequestHandler(Scheduler s) {
		schedule = s; 
		denied = new ArrayBlockingQueue<Request>(30);
	}
	
	public Booking[][] getWeek(String r) {
		ArrayList<Booking> sun = new ArrayList<Booking>();
		ArrayList<Booking> mon = new ArrayList<Booking>();
		ArrayList<Booking> tues = new ArrayList<Booking>();
		ArrayList<Booking> wednes = new ArrayList<Booking>();
		ArrayList<Booking> thrus = new ArrayList<Booking>();
		ArrayList<Booking> fri = new ArrayList<Booking>();
		ArrayList<Booking> sat = new ArrayList<Booking>();
		int[] counts = new int[7];
		for(Booking b : schedule.getRoom(r).bookings) {
			for(int i : b.days) {
				if(i == 0) { sun.add(b); counts[0]++; }
				else if(i == 1) { mon.add(b); counts[1]++; }
				else if(i == 2) { tues.add(b); counts[2]++; }
				else if(i == 3) { wednes.add(b); counts[3]++; }
				else if(i == 4) { thrus.add(b); counts[4]++; }
				else if(i == 5) { fri.add(b); counts[5]++; }
				else if(i == 6) { sat.add(b); counts[6]++; }
			}
		}
		Booking[][] week =  {(Booking[])sun.toArray(), (Booking[])mon.toArray(), (Booking[])tues.toArray(), (Booking[])wednes.toArray(), (Booking[])thrus.toArray(), (Booking[])fri.toArray(), (Booking[])sat.toArray()};
		return week;
	}
	
	public boolean validate(Request request){
		for(Booking b : schedule.getRoom(request.room).bookings ) {
			if(b.overlap(request.booking)) { return false; }
		}
		for(Request r : pending) {
			if(r.booking.overlap(request.booking) && r.priority > request.priority) {
				return false;
			}
		}
		for(Request r : schedule.closed) {
			if(r.room.equals(request.room) && r.booking.overlap(request.booking)) { return false; }
		}
		for(Request r : pending) {
			if(r.booking.overlap(request.booking)) {
				pending.remove(r);
				denied.add(r);
			}
		}
		return true;
	}
	
	public void sendRequest(Request r) {
		if(validate(r)) {
			pending.add(r);
		}
	}
	
	public void denyRequest(Request r) {
		pending.remove(r);
		denied.add(r);
	}
	
	public void approveRequest(Request r) throws SchedulerException {
		if(validate(r)) {
			schedule.approveRequest(r);
		}
		else {
			throw new SchedulerException();
		}
	}
	
	public void close(Request r) {
		schedule.close(r);
	}
	
}
