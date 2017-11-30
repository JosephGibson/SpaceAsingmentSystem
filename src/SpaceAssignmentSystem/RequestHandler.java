package SpaceAssignmentSystem;

import java.util.ArrayList;
import java.util.Iterator;

public class RequestHandler {
	private Scheduler schedule;
	private ArrayList<Request> pending  = new ArrayList<Request>();
	private ArrayList<Request> closed = new ArrayList<Request>();

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
			for(Request r : closed) {
					if(r.room.equals(request.room) && r.booking.overlap(request.booking)) { return false; }
			}
				
			return true;
	
	}
	
	public ArrayList<Request> getPending(){
		ArrayList<Request> temp = new ArrayList<Request>();
		temp.addAll(pending);
		return temp;
	}
	
	public ArrayList<Request> getClosed(){
		ArrayList<Request> temp = new ArrayList<Request>();
		temp.addAll(closed);
		return temp;
	}
	
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
	
	public void updateSchedule(Request r) throws SchedulerException {
		if( r.booking.owner.equals("open") ){
			Iterator<Booking> it = schedule.getRoom(r.room).bookings.iterator();
<<<<<<< HEAD
			while( it.hasNext() ) {
				Booking b= it.next();
				if(b.overlap(r.booking)) {
					it.remove();
				}
			}
			for (int k = 0; k <closed.size(); k++) {			
				if(r.booking.overlap(closed.get(k).booking)){
					closed.remove(closed.get(k));
				}
			}
			return;
		}
	
=======
			System.out.println(schedule.getRoom(r.room).bookings.size());
			while( it.hasNext() ) {
				Booking b= it.next();
				if(b.overlap(r.booking)) {
					it.remove();
				}
			}
			System.out.println(schedule.getRoom(r.room).bookings.size());
			return;
		}
>>>>>>> refs/remotes/origin/master
		else if( r.booking.owner.equals("closed") ) {
			closed.add(r);
			return;
		}
		else {
			throw new SchedulerException();
		}
	}
	
}
