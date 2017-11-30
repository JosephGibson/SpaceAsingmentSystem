package SpaceAssignmentSystem;

import javax.swing.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableCellRenderer;
import SpaceAssignmentSystem.GuiBuilder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;


// Main GUI class, handles building SWING elements for rendering.
public class GuiBuilder extends JPanel implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int priority = 0;
	ArrayList<Object> requestToBeSubmitted = new ArrayList<Object>();
	SimpleDateFormat DATE_FORMAT_DISPLAY = new SimpleDateFormat("hh:mm a");
	SimpleDateFormat DATE_FORMAT_YEAR = new SimpleDateFormat("MMM d, yyy '| Week:' w");
	SimpleDateFormat DATE_FORMAT_TIME_ENCODED = new SimpleDateFormat("HHmm");
	SimpleDateFormat DATE_FORMAT_WEEK = new SimpleDateFormat("w");
	String[] userList = { "John Doe", "Mr. Smith", "Mrs. Smith", "Principle Skinner", "Alen Turing", "Java John" };
	static String[] roomList = Scheduler.roomNames();
	String[] batchList = { "Fall Semester", "Winter Semester", "Summer Semester" };
	Object[][] noConflictData = buildNoConflictData();
	Object[][] requestData = buildBlankRequest();
	Object[][] calData = buildDay();
	String[] statusList = { "Open", "Closed" };
	String[] weekList = { "Week 1", "Week 2",  "Week 3", "Week 4", "Week 5", "Week 6", "Week 7", "Week 8", "Week 9", "Week 10",  "Week 11", "Week 12", "Week 13", "Week 14", "Week 15", "Week 16", "Week 17" };

	public GuiBuilder(RequestHandler RH) throws SchedulerException {

		String[] calColumnNames = {  "Sunday", "Monday", "Tuesday", "Wendsday", "Thursday", "Friday", "Saturday" };
		String[] requestColumnNames = { "Priority", "Name", "Room", "Time" };
		String[] noConflictColumnNames = {"ID", "Name", "Room", "Time"};



		// Create the table for displaying rooms:
		JTable calTable = new JTable(calData, calColumnNames);
		JTable requestTable = new JTable(requestData, requestColumnNames);
		JTable noConflictTable = new JTable(noConflictData, noConflictColumnNames); 

		// Set the cell rendering to center for all cells.
		DefaultTableCellRenderer centerRenderer = new CustomRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		for (int x = 0; x < calTable.getColumnCount(); x++) {
			calTable.getColumnModel().getColumn(x).setCellRenderer(centerRenderer);
		}
		calTable.setEnabled(false);
		JTable rowTable = new RowNumberTable(calTable);
		rowTable.setEnabled(false);

		requestTable.getColumnModel().getColumn(0).setPreferredWidth(40);
		requestTable.getColumnModel().getColumn(1).setPreferredWidth(60);
		requestTable.getColumnModel().getColumn(2).setPreferredWidth(80);
		requestTable.getColumnModel().getColumn(3).setPreferredWidth(270);
		
		
		noConflictTable.getColumnModel().getColumn(0).setPreferredWidth(25);
		noConflictTable.getColumnModel().getColumn(1).setPreferredWidth(70);
		noConflictTable.getColumnModel().getColumn(2).setPreferredWidth(85);
		noConflictTable.getColumnModel().getColumn(3).setPreferredWidth(270);

		

		// Create all other swing elements.
		JLabel startL = new JLabel("Start Time:");
		JLabel endL = new JLabel("End Time: ");
		JLabel requestStartL = new JLabel("Start Time:");
		JLabel requestEndL = new JLabel("End Time: ");
		// Create main / main wrapper panes
		JPanel mainPane = new JPanel(new BorderLayout());
		JTabbedPane tabWrapperPane = new JTabbedPane();
		JPanel calenderWrapperPane = new JPanel(new BorderLayout());
		JPanel calenderToolBarPane = new JPanel(new FlowLayout());
		JScrollPane calenderPane = new JScrollPane(calTable);

		JPanel requestWrapperPane = new JPanel(new BorderLayout());
		JScrollPane requestTablePane = new JScrollPane(requestTable);
		JPanel requestButtonWrapperPane = new JPanel();
		JPanel requestCheckPane = new JPanel(new GridLayout(3, 3));
		JPanel requestButtonPane = new JPanel(new FlowLayout());

		JPanel scheduleWrapperPane = new JPanel(new BorderLayout());
		JPanel scheduleCheckPane = new JPanel(new GridLayout(3, 3));
		JPanel scheduleButtonPane = new JPanel();

		JPanel noConflictWrapperPane = new JPanel(new BorderLayout());
		JScrollPane noConflictTablePane = new JScrollPane(noConflictTable);
		JPanel noConflictButtonPane = new JPanel();



		// Build the JSpinner to enter in time of day for booking, both start and end
		// for both scheduler and request.
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 00);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date requestStartDate = cal.getTime();
		Date requestEndDate = cal.getTime();
		MySpinnerDateModel requestStartSpinnerModel = new MySpinnerDateModel(requestStartDate, null, null,
				Calendar.MINUTE);
		requestStartSpinnerModel.setIncrement(15);
		MySpinnerDateModel requestEndSpinnerModel = new MySpinnerDateModel(requestEndDate, null, null, Calendar.MINUTE);
		requestEndSpinnerModel.setIncrement(15);
		JSpinner requestStartSpinner = new JSpinner(requestEndSpinnerModel);
		JSpinner requestEndSpinner = new JSpinner(requestStartSpinnerModel);
		JSpinner.DateEditor rse = new JSpinner.DateEditor(requestStartSpinner, "hh:mm a");
		rse.getComponent(0).setFocusable(false);
		JSpinner.DateEditor ree = new JSpinner.DateEditor(requestEndSpinner, "hh:mm a");
		ree.getComponent(0).setFocusable(false);
		requestStartSpinner.setEditor(rse);
		requestEndSpinner.setEditor(ree);

		Date scheduleStartDate = cal.getTime();
		Date scheduleEndDate = cal.getTime();
		MySpinnerDateModel scheduleStartSpinnerModel = new MySpinnerDateModel(scheduleStartDate, null, null,
				Calendar.MINUTE);
		scheduleStartSpinnerModel.setIncrement(15);
		MySpinnerDateModel scheduleEndSpinnerModel = new MySpinnerDateModel(scheduleEndDate, null, null,
				Calendar.MINUTE);
		scheduleEndSpinnerModel.setIncrement(15);
		JSpinner scheduleStartSpinner = new JSpinner(scheduleStartSpinnerModel);
		JSpinner scheduleEndSpinner = new JSpinner(scheduleEndSpinnerModel);
		JSpinner.DateEditor sse = new JSpinner.DateEditor(scheduleStartSpinner, "hh:mm a");
		sse.getComponent(0).setFocusable(false);
		JSpinner.DateEditor see = new JSpinner.DateEditor(scheduleEndSpinner, "hh:mm a");
		see.getComponent(0).setFocusable(false);
		scheduleStartSpinner.setEditor(sse);
		scheduleEndSpinner.setEditor(see);

		JComboBox<String> requestRoomBox = new JComboBox<String>(roomList);
		JComboBox<String> requestBatchBox = new JComboBox<String>(batchList);
		JComboBox<String> requestUsersBox = new JComboBox<String>(userList);
		JComboBox<String> scheduleRoomBox = new JComboBox<String>(roomList);
		JComboBox<String> scheduleBatchBox = new JComboBox<String>(batchList);
		JComboBox<String> scheduleStatusBox = new JComboBox<String>(statusList);
		JComboBox<String> calenderRoomBox = new JComboBox<String>(roomList);
		JComboBox<String> calenderBatchBox = new JComboBox<String>(batchList);
		JComboBox<String> calenderWeekBox = new JComboBox<String>(weekList);


		JCheckBox requestBoxM = new JCheckBox("Monday");
		requestBoxM.setBounds(50, 50, 30, 30);
		JCheckBox requestBoxT = new JCheckBox("Tuesday");
		requestBoxT.setBounds(50, 50, 30, 30);
		JCheckBox requestBoxW = new JCheckBox("Wendsday");
		requestBoxW.setBounds(50, 50, 30, 30);
		JCheckBox requestBoxR = new JCheckBox("Thursday");
		requestBoxR.setBounds(50, 50, 30, 30);
		JCheckBox requestBoxF = new JCheckBox("Friday");
		requestBoxF.setBounds(50, 50, 30, 30);
		JCheckBox requestBoxS = new JCheckBox("Saturday");
		requestBoxS.setBounds(50, 50, 30, 30);
		JCheckBox requestBoxG = new JCheckBox("Sunday");
		requestBoxG.setBounds(50, 50, 30, 30);
		JCheckBox scheduleBoxM = new JCheckBox("Monday");
		scheduleBoxM.setBounds(50, 50, 30, 30);
		JCheckBox scheduleBoxT = new JCheckBox("Tuesday");
		scheduleBoxT.setBounds(50, 50, 30, 30);
		JCheckBox scheduleBoxW = new JCheckBox("Wendsday");
		scheduleBoxW.setBounds(50, 50, 30, 30);
		JCheckBox scheduleBoxR = new JCheckBox("Thursday");
		scheduleBoxR.setBounds(50, 50, 30, 30);
		JCheckBox scheduleBoxF = new JCheckBox("Friday");
		scheduleBoxF.setBounds(50, 50, 30, 30);
		JCheckBox scheduleBoxS = new JCheckBox("Saturday");
		scheduleBoxS.setBounds(50, 50, 30, 30);
		JCheckBox scheduleBoxG = new JCheckBox("Sunday");
		scheduleBoxG.setBounds(50, 50, 30, 30);
		
		
		calenderRoomBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				 String semesester = (String) calenderBatchBox.getSelectedItem();
				 String room = (String) calenderRoomBox.getSelectedItem();
				 buildCalenderData(RH, semesester, room);
				 calTable.repaint();
				
			}
		});
		
		
		calenderBatchBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				 String semesester = (String) calenderBatchBox.getSelectedItem();
				 String room = (String) calenderRoomBox.getSelectedItem();
				 buildCalenderData(RH, semesester, room);
				 calTable.repaint();
				
			
			}
		});
		

		// Build all buttons with listeners.
		JButton requestSubmit = new JButton("Submit Request");
		requestSubmit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (requestToBeSubmitted.size() > 0) {
					for (int i=0; i < requestToBeSubmitted.size(); i=i+7) {
						String name = requestToBeSubmitted.get(i).toString();
						String room = requestToBeSubmitted.get(i+1).toString();
						int start = Integer.parseInt(requestToBeSubmitted.get(i+2).toString());
						int end = Integer.parseInt(requestToBeSubmitted.get(i+3).toString());
						String[] temp = (requestToBeSubmitted.get(i+4).toString().replace("[", "").replace("]", "").replace(",", "").split(" "));
						int[] days = new int[temp.length];
						for(int j=0; j < temp.length; j++) {
							days[j] = Integer.parseInt(temp[j]);
						};						
						String semester = requestToBeSubmitted.get(i+5).toString();
						int priority = Integer.parseInt(requestToBeSubmitted.get(i+6).toString());
						Request newRequest = new Request(room, semester, days, start, end, name, priority);
						RH.sendRequest(newRequest);			

					}									
				
					for (int j = 0; j < 5; j++) {
						for (int i = 0; i < 4; i++) {
							requestData[j][i] = "";
						}
					}
					requestTable.repaint();
					priority = 0;
					requestToBeSubmitted.clear();
					try {
						getPendingData(RH);
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					noConflictTable.repaint();

				} else {
					JOptionPane.showMessageDialog(null, "No request to submit");
				}
			}
		});

		JButton requestClear = new JButton("clear");
		requestClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int j = 0; j < 5; j++) {
					for (int i = 0; i < 4; i++) {
						requestData[j][i] = "";
					}
				}
				requestTable.repaint();
				priority = 0;
				requestToBeSubmitted.clear();
			}
		});

		JButton requestAdd = new JButton("add Requests");
		requestAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				int startTimeEncoded = 	Integer.parseInt(DATE_FORMAT_TIME_ENCODED.format((requestStartSpinner.getValue())));
				int endTimeEncoded =  Integer.parseInt(DATE_FORMAT_TIME_ENCODED.format((requestEndSpinner.getValue()))); 
				if (priority < 5 && endTimeEncoded > startTimeEncoded) {							
					String startString = DATE_FORMAT_DISPLAY.format((requestStartSpinner.getValue()));
					String endString = DATE_FORMAT_DISPLAY.format((requestEndSpinner.getValue()));
					String room = (String) requestRoomBox.getSelectedItem();
					String semester = (String) requestBatchBox.getSelectedItem();
					String name = (String) requestUsersBox.getSelectedItem();
					ArrayList<Integer> days = new ArrayList<Integer>();
					ArrayList<String> dayChar = new ArrayList<String>();
					if (requestBoxG.isSelected()) {
						days.add(0);
						dayChar.add("U");

					}
					if (requestBoxM.isSelected()) {
						days.add(1);
						dayChar.add("M");

					}
					if (requestBoxT.isSelected()) {
						days.add(2);
						dayChar.add("T");

					}
					if (requestBoxW.isSelected()) {
						days.add(3);
						dayChar.add("W");

					}
					if (requestBoxR.isSelected()) {
						days.add(4);
						dayChar.add("R");

					}
					if (requestBoxF.isSelected()) {
						days.add(5);
						dayChar.add("F");
					}
					if (requestBoxS.isSelected()) {
						days.add(6);
						dayChar.add("S");
					}

					if (days.size() > 0) {
						requestData[priority][0] = priority + 1;
						requestData[priority][1] = name;
						requestData[priority][2] = room;
						requestData[priority][3] = semester.replace(" Semester", "") + ", " + dayChar + " "
								+ startString + "-" + endString;
						requestTable.repaint();
													
						requestToBeSubmitted.add(name);
						requestToBeSubmitted.add(room);
						requestToBeSubmitted.add(startTimeEncoded);
						requestToBeSubmitted.add(endTimeEncoded);
						requestToBeSubmitted.add(days);
						requestToBeSubmitted.add(semester);
						requestToBeSubmitted.add(5 - priority);
						priority++;
					
					}
					else {
						JOptionPane.showMessageDialog(null, "Choose at least one day for requests");
					}

				} else {
					if( endTimeEncoded <= startTimeEncoded) {
						JOptionPane.showMessageDialog(null, "Bad time slot.");
					}
					else {
						JOptionPane.showMessageDialog(null, "To many requests.");
					}
					
				}
			}
		});

		JButton noConflictApproveRequest = new JButton("Approve Request");
		noConflictApproveRequest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Request> pending = RH.getPending();
				if (pending.size() > 0) {
					Request temp = pending.get(0);
					
					try {
						RH.approveRequest(temp);
						RH.removeRequest(temp);
					} catch (SchedulerException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					for (int j = 0; j < 99; j++) {
						for (int i = 0; i < 5; i++) {
							noConflictData[j][i] = "";
						}
					}
					try {
						getPendingData(RH);
						noConflictTable.repaint();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					 String semesester = (String) calenderBatchBox.getSelectedItem();
					 String roomBox = (String) calenderRoomBox.getSelectedItem();
					 buildCalenderData(RH, semesester, roomBox);
					 calTable.repaint();
					
				}
				else {
					JOptionPane.showMessageDialog(null, "No requests to Approve");
					
				}				

			}
		});

		JButton noConflictRejectRequest = new JButton("Reject Request");
		noConflictRejectRequest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				ArrayList<Request> pending = RH.getPending();
				if (pending.size() > 0) {
					Request temp = pending.get(0);					
					RH.removeRequest(temp);			
					for (int j = 0; j < 99; j++) {
						for (int i = 0; i < 5; i++) {
							noConflictData[j][i] = "";
						}
					}
					try {
						getPendingData(RH);
						noConflictTable.repaint();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
				}
				else {
					JOptionPane.showMessageDialog(null, "No requests to Remove");
					
				}				

			
			}
		});

		JButton scheduleSubmit = new JButton("Update Schedule");
		scheduleSubmit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int startTime = Integer.parseInt(DATE_FORMAT_TIME_ENCODED.format((scheduleStartSpinner.getValue())));
				int endTime = Integer.parseInt(DATE_FORMAT_TIME_ENCODED.format((scheduleEndSpinner.getValue())));
				if (startTime < endTime) {
					int[] days = {-1, -1, -1, -1, -1, -1, -1};
					boolean atLeastOneday = false;
					String semesester = (String) scheduleBatchBox.getSelectedItem();
					String room = (String) scheduleRoomBox.getSelectedItem();
					String status = (String) scheduleStatusBox.getSelectedItem();
				
					
					if (scheduleBoxG.isSelected()) {
						days[0] = 0;
						atLeastOneday = true;

					}
					if (scheduleBoxM.isSelected()) {
						days[1] = 1;
						atLeastOneday = true;

					}
					if (scheduleBoxT.isSelected()) {
						days[2] = 2;
						atLeastOneday = true;

					}
					if (scheduleBoxW.isSelected()) {
						days[3] = 3;
						atLeastOneday = true;

					}
					if (scheduleBoxR.isSelected()) {
						days[4] = 4;
						atLeastOneday = true;

					}
					if (scheduleBoxF.isSelected()) {
						days[5] = 5;
						atLeastOneday = true;
					}
					if (scheduleBoxS.isSelected()) {
						days[6] = 6;
						atLeastOneday = true;
					}
					if (atLeastOneday) {
						if (status == "Closed") {
							Request r = new Request(room,semesester, days, startTime, endTime, "closed", 99);
							try {
								RH.updateSchedule(r);
							} catch (SchedulerException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						else if (status == "Open") {
							Request r = new Request(room,semesester, days, startTime, endTime, "open", -1);
							try {
								RH.updateSchedule(r);
							} catch (SchedulerException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						String semesesterBox = (String) calenderBatchBox.getSelectedItem();
						String roomBox = (String) calenderRoomBox.getSelectedItem();
						buildCalenderData(RH, semesesterBox, roomBox);
						calTable.repaint();					
					}
					else {
						JOptionPane.showMessageDialog(null, "Please Select one day");
					}


				}
				else {
					JOptionPane.showMessageDialog(null, "Bad Time slot.");
				}
				
				
			
			}
		});

		tabWrapperPane.setPreferredSize(new Dimension(500, 550));

		// Set all the layouts and add all the elements to the appropriate frames.
		calenderPane.setPreferredSize(new Dimension(800, 410));
		calenderPane.setRowHeaderView(rowTable);
		calenderPane.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, rowTable.getTableHeader());
		calenderToolBarPane.add(calenderRoomBox);
		calenderToolBarPane.add(calenderBatchBox);
		calenderToolBarPane.add(calenderWeekBox);
		calenderWrapperPane.add(calenderToolBarPane, BorderLayout.NORTH);
		calenderWrapperPane.add(calenderPane, BorderLayout.LINE_START);

		// Add elements to booking request pane.
		requestCheckPane.add(requestBoxM);
		requestCheckPane.add(requestBoxT);
		requestCheckPane.add(requestBoxW);
		requestCheckPane.add(requestBoxR);
		requestCheckPane.add(requestBoxF);
		requestCheckPane.add(requestBoxS);
		requestCheckPane.add(requestBoxG);

		requestCheckPane.setPreferredSize(new Dimension(500, 50));
		requestButtonPane.add(requestStartL);
		requestButtonPane.add(requestStartSpinner);
		requestButtonPane.add(requestEndL);
		requestButtonPane.add(requestEndSpinner);
		requestButtonPane.add(requestBatchBox);
		requestButtonPane.add(requestRoomBox);
		requestButtonPane.add(requestUsersBox);

		requestButtonPane.add(requestAdd);
		requestButtonPane.add(requestClear);
		requestButtonPane.add(requestSubmit);

		requestButtonPane.setPreferredSize(new Dimension(500, 200));
		requestButtonWrapperPane.add(requestCheckPane, BorderLayout.NORTH);
		requestButtonWrapperPane.add(requestButtonPane, BorderLayout.SOUTH);
		requestButtonWrapperPane.setPreferredSize(new Dimension(500, 200));
		requestTablePane.setPreferredSize(new Dimension(500, 300));
		requestWrapperPane.add(requestTablePane, BorderLayout.NORTH);
		requestWrapperPane.add(requestButtonWrapperPane, BorderLayout.SOUTH);

		// Add elements to schedule Pane
		scheduleCheckPane.add(scheduleBoxM);
		scheduleCheckPane.add(scheduleBoxT);
		scheduleCheckPane.add(scheduleBoxW);
		scheduleCheckPane.add(scheduleBoxR);
		scheduleCheckPane.add(scheduleBoxF);
		scheduleCheckPane.add(scheduleBoxS);
		scheduleCheckPane.add(scheduleBoxG);
		scheduleCheckPane.setPreferredSize(new Dimension(500, 150));
		scheduleButtonPane.add(scheduleBatchBox);
		scheduleButtonPane.add(scheduleStatusBox);
		scheduleButtonPane.add(scheduleRoomBox);
		scheduleButtonPane.add(startL);
		scheduleButtonPane.add(scheduleStartSpinner);
		scheduleButtonPane.add(endL);
		scheduleButtonPane.add(scheduleEndSpinner);
		scheduleButtonPane.add(scheduleSubmit);
		scheduleButtonPane.setPreferredSize(new Dimension(500, 300));
		scheduleWrapperPane.add(scheduleCheckPane, BorderLayout.NORTH);
		scheduleWrapperPane.add(scheduleButtonPane, BorderLayout.SOUTH);

		// Add elements to no conflict request pane.
		noConflictButtonPane.add(noConflictRejectRequest);
		noConflictButtonPane.add(noConflictApproveRequest);
		noConflictButtonPane.setPreferredSize(new Dimension(500, 100));
		noConflictTablePane.setPreferredSize(new Dimension(500, 400));
		noConflictWrapperPane.add(noConflictButtonPane, BorderLayout.SOUTH);
		noConflictWrapperPane.add(noConflictTablePane, BorderLayout.NORTH);

		// Add elements to table wrapper frame.
		tabWrapperPane.add(requestWrapperPane, "Request Booking");
		tabWrapperPane.add(noConflictWrapperPane, "Pending Requests");
		tabWrapperPane.add(scheduleWrapperPane, "Scheduler");

		// Add wrapper to the main pane.
		mainPane.add(calenderWrapperPane, BorderLayout.LINE_START);
		mainPane.add(tabWrapperPane, BorderLayout.LINE_END);
		add(mainPane);
	};

	public static void renderGUI(RequestHandler RH) throws SchedulerException {
		// Create the frame for the scheduler

		JFrame frame = new JFrame("School Scheduler");
		// Create the schedule table pane and attach it to the frame.
		GuiBuilder newContentPane = new GuiBuilder(RH);
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);
		// Set the frame to exit operations on close
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Set the label.
		frame.pack();
		// Display the window.
		frame.setVisible(true);
	
	}

	// A simple function to build the day delimiter, not final implementation.
	public Object[][] buildBlankRequest() {
		Object[][] request = new Object[5][4];
		for (int j = 0; j < 5; j++) {
			for (int i = 0; i < 4; i++) {
				request[j][i] = "";
			}
		}
		return request;
	}

	public Object[][] buildDay() {
		Object[][] data = new Object[95][7];
		for (int j = 0; j < 95; j++) {
			for (int i = 0; i < 7; i++) {
				data[j][i] = "";
			}
		}
		return data;
	}
	public Object[][] buildNoConflictData(){
		Object[][] data = new Object[99][5];
		for (int j = 0; j < 99; j++) {
			for (int i = 0; i < 5; i++) {
				data[j][i] = "";
			}
		}
		return data;	
	
	}
	public  Object[][] getPendingData(RequestHandler RH) throws ParseException {				
		ArrayList<Request> pending = RH.getPending();	
		
		for (int i=0; i<pending.size();i++) {
			Request temp = pending.get(i);
			Booking b = temp.booking;		
			ArrayList<String> dayString = new ArrayList<String>();
			for(int j =0; j < b.days.length; j++) {
				int tempVal = b.days[j];
				if(tempVal==0) {dayString.add("U");}
				else if (tempVal==1) {dayString.add("M");}
				else if (tempVal==2) {dayString.add("T");}
				else if (tempVal==3) {dayString.add("W");}
				else if (tempVal==4) {dayString.add("R");}
				else if (tempVal==5) {dayString.add("F");}
				else if (tempVal==6) {dayString.add("S");}			
					
			}
			noConflictData[i][0] = i + 1; 
			noConflictData[i][1] = b.owner; 
			noConflictData[i][2] = temp.room; 
			noConflictData[i][3] =  b.semeseter.replace(" Semester", "") + ": " + dayString + " " + DATE_FORMAT_DISPLAY.format((DATE_FORMAT_TIME_ENCODED.parse(String.valueOf(b.startTime)))) +  "-" + DATE_FORMAT_DISPLAY.format((DATE_FORMAT_TIME_ENCODED.parse(String.valueOf(b.endTime))));	
		}
		return noConflictData;
	
	}
	public int getTimeColum(int i) {
		int x1 =0, x2 =0 ,x3 =0 ,x4 =0;
		while (i > 0) {
		    x1 = i % 10;
		    i = i / 10;
		    x2 = i % 10;
		    i = i / 10;
		    x3 = i % 10;
		    i = i / 10;
		    x4 = i % 10;
		    i = i / 10;		    
		}
		int result = ((x4*10)+x3)*60 + x1 + 10*x2;
		return result / 15;
	}

	public  void buildCalenderData(RequestHandler RH, String semeseter, String room){	
		for (int j = 0; j < 95; j++) {
			for (int i = 0; i < 7; i++) {
				calData[j][i] = "";
			}
		}	
		ArrayList<Request> closed = RH.getClosed();
		Booking[][] week = RH.getWeek(room);
		for (int i = 0; i < week.length; i++) {
			Booking[] day = week[i];
			for(int j=0; j<day.length; j++) {
				if (day[j].semeseter == semeseter){
					int startCell = getTimeColum(day[j].startTime); 
					int endCell = getTimeColum(day[j].endTime);
					for (int k = startCell; k<endCell;k++) {
						calData[k][i] =  day[j].owner; 
					}
				}
				
			}
		}
		for (int x = 0; x<closed.size();x++) {
			if (closed.get(x).room == room) {
				Booking temp = closed.get(x).booking;
				int startCell = getTimeColum(temp.startTime); 
				int endCell = getTimeColum(temp.endTime);
				for(int z =0; z < temp.days.length; z++) {
					if(temp.semeseter == semeseter && temp.days[z] != -1){
						for (int m = startCell; m<endCell;m++) {
							calData[m][z] = "closed";
						}
			
					}
				}
				
			}
			
		}
	}
	
	
	class CustomRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 6703872492730589499L;
	   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){

	       Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	        if(!table.getValueAt(row, column).equals("") && !table.getValueAt(row, column).equals("closed")){
	            cellComponent.setBackground(Color.GRAY);
	        } else if(table.getValueAt(row, column).equals("closed")) {
	           cellComponent.setBackground(Color.RED);
	        } else {
	        	cellComponent.setBackground(Color.GREEN);
	        }
	        return cellComponent;
	    }
	}


	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

}