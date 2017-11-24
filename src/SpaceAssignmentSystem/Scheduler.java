package SpaceAssignmentSystem;

import java.io.*;
import java.util.Stack;
import java.util.Observable;

public class Scheduler extends Observable{
	private Room[] rooms;
	private String[] names = {"room1", "room2", "room3", "room4", "room5"};
	
	public Scheduler(){ 
		rooms = new Room[names.length];
		for (String s : names) {
			build(s);
		}
	}
	
	public Scheduler(String[] nms){ 
		names = nms;
		rooms = new Room[names.length];
		for (String s : names) {
			build(s);
		}
	}
	
	public RequestHandler createRequestHandler() {
		return new RequestHandler(this);
	}
	
	public Room getRoom(String s) { 
		return rooms[pick(s)];
	}
	
	public String[] roomNames() {
		return names;
	}
	
	public void approveRequest(Request[] requests) throws SchedulerException {
		Stack<Request> changes = new Stack<Request>();
		for(Request r : requests) {
			Room room = rooms[pick(r.room)];
			if(room.bookings.isEmpty()) {
				changes.push(r);
				room.bookings.add(r.booking);
			}
			for(Booking b : room.bookings) {
				if( b.start.after(r.booking.start) && b.start.before(r.booking.end) ) {
					throw new SchedulerException();
				}
				if( b.end.after(r.booking.start) && b.end.before(r.booking.end) ) {
					throw new SchedulerException();
				}
				else{
					changes.push(r);
					room.bookings.add(r.booking);
				}
			}
		}
		notifyObservers(changes.iterator());
	}	
	
	public void approveRequest(Request r) throws SchedulerException {
		Room room = rooms[pick(r.room)];
		if(room.bookings.isEmpty()) {
			room.bookings.add(r.booking) ;
			return;
		}
		for(Booking b : room.bookings) {
			if( b.start.after(r.booking.start) && b.start.before(r.booking.end) ) {
				throw new SchedulerException();
			}
			if( b.end.after(r.booking.start) && b.end.before(r.booking.end) ) {
				throw new SchedulerException();
			}
			else{
				room.bookings.add(r.booking);
			}
		}
		notifyObservers(r);
	}
	
	private void build(String s) {
		int i = pick(s);
		try {
			FileInputStream fileIn = new FileInputStream(s + ".ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			rooms[i] = (Room) in.readObject();
			fileIn.close();
			in.close();
		}
		catch(Exception e) {
			rooms[i] = new Room(s);
		}
	}
	
	private int pick(String s1) {
		int i = 0;
		for(String s2 : names) {
			if( s1.equals(s2) ) {
				return i;
			}
			else {
				i++;
			}
		}
		return -1;
	}

	public void notifyObservers(Object o) {
		setChanged();
		super.notifyObservers(o);
	}
	
}
