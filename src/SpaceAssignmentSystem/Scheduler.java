package SpaceAssignmentSystem;

import java.io.*;
import java.util.ArrayList;
import java.util.Observable;

public class Scheduler extends Observable{
	private Room[] rooms;
	private String[] names = { "Class Room 1", "Class Room 2", "Class Room 3", "Gym", "Libary", "Music Room", "Theater" };
	public ArrayList<Request> closed = new <Request> ArrayList();
	
	public Scheduler(){ 
		rooms = new Room[names.length];
		for (String s : names) {
			build(s);
		}
		closed = new ArrayList<Request>();
	}
	
	public Scheduler(String[] nms){ 
		names = nms;
		rooms = new Room[names.length];
		for (String s : names) {
			build(s);
		}
		closed = new ArrayList<Request>();
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
	
	public void close(Request r) {
		closed.add(r);
	}
	
	public void approveRequest(Request r) throws SchedulerException {
		int i = pick(r.room);
		rooms[i].bookings.add(r.booking);
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
