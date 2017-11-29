package SpaceAssignmentSystem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;



public class RequestHandler {
	private Scheduler schedule;

	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	private ArrayList<Request> pending  = new <Request>ArrayList();;

	
	public RequestHandler(Scheduler s) {
		schedule = s; 
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
		Booking[][] week =  {castBooking(sun), castBooking(mon), castBooking(tues), castBooking(wednes), castBooking(thrus), castBooking(fri), castBooking(sat)};
		return week;
	}
	
	public boolean validate(Request request) { 				
			
			for(Booking b : schedule.getRoom(request.room).bookings ) {
				if(b.overlap(request.booking) && b.owner != "Open") {return false; }
			}	
			
			for(Request r : pending) {
				if(r.booking.overlap(request.booking) && r.priority >= request.priority) {
					return false;
				}
			}			

			while (pending.iterator().hasNext()) {
				Request r = pending.iterator().next();
				if(r.booking.overlap(request.booking)) {
					pending.remove(r);
					return false;
				}
				else if (!r.booking.overlap(request.booking)) {
					return true;
				}
			}
				
			return true;
	
	}
	public ArrayList<Request> getPending(){
		return pending;
	};
	public void sendRequest(Request r) {		
		if(validate(r)) {		
			pending.add(r);
		}
	}
	

	
	public void removeRequest(Request r) {
		pending.remove(r);
	}
	
	public void approveRequest(Request r) throws SchedulerException {
		try {
			schedule.approveRequest(r);
		}
		catch (SchedulerException e1) {
			throw new SchedulerException();
		}
	}

	public Booking[] castBooking (ArrayList<Booking> booking) {
		Booking [] temp = new Booking[booking.size()];
		for(int i = 0; i < booking.size(); i++) {
			temp[i] = booking.get(i);
		}
		return temp;
	};
	
}
