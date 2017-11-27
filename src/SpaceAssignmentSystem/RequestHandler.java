package SpaceAssignmentSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
	
	public Map<String, Booking[]> getFullDay(DateSA d) {
		Map<String, Booking[]> m = new HashMap<String, Booking[]>();
		for(String s : schedule.roomNames()) {
			ArrayList<Booking> bookings = new ArrayList<Booking>();
			for(Booking b : schedule.getRoom(s).bookings) {
				if(b.start.equals(d)) {
					bookings.add(b);
				}
			}
			Booking[] bookingsArray = new Booking[bookings.size()];
			m.put(s, bookings.toArray(bookingsArray));
		}
		return m;
	}
	
	public Map<String, Booking[]> getNoConflict(DateSA d) {
		Map<String, Booking[]> m = getFullDay(d);
		ArrayList<Booking> noConflict = new ArrayList<Booking>();
		for(String s : m.keySet() ) {
			Booking[] conflict = m.get(s);
			noConflict.clear();
			for(Booking b1 : conflict) {
				for(Booking b2 : noConflict) {
					if( b1.start.after(b2.start) && b1.start.before(b2.end) ) {
						noConflict.remove(b2);
					}
					if( b1.end.after(b2.start) && b1.end.before(b2.end) ) {
						noConflict.remove(b2);
					}
					else {
						noConflict.add(b1);
					}
				}
			}
			m.put(s, (Booking[]) noConflict.toArray());
		}
		return m;
	}
	
	public Map<String, Booking[]> getConflict(DateSA d){
		Map<String, Booking[]> m = getFullDay(d);
		ArrayList<Booking> noConflict = new ArrayList<Booking>();
		for(String s : m.keySet() ) {
			ArrayList<Booking> conflict = new ArrayList<Booking>();
			for(Booking b : m.get(s)) {
				conflict.add(b);
			}
			noConflict.clear();
			for(Booking b1 : conflict) {
				for(Booking b2 : noConflict) {
					if( b1.start.after(b2.start) && b1.start.before(b2.end) ) {
						noConflict.remove(b2);
					}
					if( b1.end.after(b2.start) && b1.end.before(b2.end) ) {
						noConflict.remove(b2);
					}
					else {
						noConflict.add(b1);
					}
				}
			}
			conflict.removeAll(noConflict);
			m.put(s, (Booking[]) conflict.toArray());
		}
		return m;	
	}
	// Send Resut => TODO ; Delete requests via priorty, check valid request.
	
	public void newRequest(Request r) {
		pending.add(r);
	}
	
	public void denyRequest(Request r) {
		pending.remove(r);
		denied.add(r);
	}
	
	public void approveRequest(Request[] requests) throws SchedulerException {
		schedule.approveRequest(requests);
	}
	
	public void approveRequest(Request r) throws SchedulerException {
		schedule.approveRequest(r);
	}
	
}
