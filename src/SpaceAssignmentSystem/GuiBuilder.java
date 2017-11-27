package SpaceAssignmentSystem;

import javax.swing.*;
import java.text.SimpleDateFormat;
import SpaceAssignmentSystem.DateSA;
import javax.swing.table.DefaultTableCellRenderer;
import org.jdatepicker.impl.*;
import SpaceAssignmentSystem.GuiBuilder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

// Main GUI class, handles building SWING elements for rendering.
public class GuiBuilder extends JPanel implements Observer{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public GuiBuilder(RequestHandler) throws SchedulerException {
		// Load Dummy data for rapid-prototyping
		System.out.println();
		
		DateSA d = new DateSA(12, 1);
		
		
	
		
		String[] roomList = { "Class Room 1", "Class Room 2", "Class Room 3", "Gym", "Libary", "Music Room", "Theater" };
		String[] calColumnNames = { "Monday", "Tuesday", "Wendsday", "Thursday", "Friday", "Saturday", "Sunday" };
		Object[][] calData = buildDay();
	
		String[] batchList = {"Daily", "Weekly", "Monthly", "Yearly"};
		String[] statusList =  {"Open", "Booked", "Blacked Out"};
		String[] userList = {"John Doe", "Mr. Smith", "Mrs. Smith", "Principle Skinner", "Alen Turing", "Java John"};
		String[] requestColumnNames = {"Name", "Room", "Start Time", "End Time", "Status"};
		String[][] requestData = {{"John Doe","Room 1", "Start Time", "End Time", "Approved"}, 
							{"Jane Doe","Room 2", "Start Time", "End Time", "Pending"},
							{"Fake Name", "Room 3", "Start Time", "End Time", "Denied"}};

		
		// Create the table for displaying rooms:		
		JTable calTable = new JTable(calData, calColumnNames);
		JTable requestTable = new JTable(requestData, requestColumnNames);
		JTable conflictTable = new JTable(requestData, requestColumnNames);
		JTable noConflictTable = new JTable(requestData, requestColumnNames);
		
		// Set the cell rendering to center for all cells.
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() ;
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		 for(int x=0;x<calTable.getColumnCount();x++){
	         calTable.getColumnModel().getColumn(x).setCellRenderer(centerRenderer);
	        }
		calTable.setEnabled(false);
		JTable rowTable = new RowNumberTable(calTable);
		rowTable.setEnabled(false);
		
		// Create all other swing elements.
		JLabel startL = new JLabel("Start Time:");
		JLabel endL = new JLabel("End Time: ");
		
		// Create main / main wrapper panes 
		JPanel mainPane = new JPanel(new  BorderLayout());
		JTabbedPane  tabWrapperPane = new JTabbedPane();
		JPanel calenderWrapperPane = new JPanel(new BorderLayout());
		JPanel calenderToolBarPane = new JPanel(new BorderLayout());
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
		
		JPanel conflictWrapperPane = new JPanel(new BorderLayout());
		JScrollPane conflictTablePane = new JScrollPane(conflictTable);
		JPanel conflictButtonPane = new JPanel();
			
		
		
		

		
		// Set up parameters and objects for JDatePicker swing elements 		
		Date now = Calendar.getInstance().getTime();
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM d, yyy");
		String today = DATE_FORMAT.format(now);
		Properties pS = new Properties();
		pS.put("text.today", "Today");
		pS.put("text.day", "Day");
		pS.put("text.month", "Month");
		pS.put("text.year", "Year");			
		UtilDateModel modelR = new UtilDateModel();	
		UtilDateModel model = new UtilDateModel();	
		JDatePanelImpl calenderDatePanel = new JDatePanelImpl(model, pS);
		JDatePickerImpl calenderDatePicker = new JDatePickerImpl(calenderDatePanel, new DateComponentFormatter());
		calenderDatePicker.getJFormattedTextField().setText(today);	
		
		
		Properties pR = new Properties();
		pR.put("text.today", "Today");
		pR.put("text.day", "Day");
		pR.put("text.month", "Month");
		pR.put("text.year", "Year");	
		JDatePanelImpl requestDatePanel = new JDatePanelImpl(modelR, pR);
		JDatePickerImpl requestDatePicker = new JDatePickerImpl(requestDatePanel, new DateComponentFormatter());
		requestDatePicker.getJFormattedTextField().setText(today);		
		
		
		// Build the JSpinner to enter in time of day for booking, both start and end for both scheduler and request.
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY,12);
		cal.set(Calendar.MINUTE,00);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		Date requestStartDate = cal.getTime();
		Date requestEndDate = cal.getTime();
		MySpinnerDateModel requestStartSpinnerModel = new MySpinnerDateModel(requestStartDate, null, null, Calendar.MINUTE);
		requestStartSpinnerModel.setIncrement(15);
		MySpinnerDateModel  requestEndSpinnerModel = new MySpinnerDateModel (requestEndDate, null, null, Calendar.MINUTE);
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
		MySpinnerDateModel scheduleStartSpinnerModel = new MySpinnerDateModel(scheduleStartDate, null, null, Calendar.MINUTE);
		scheduleStartSpinnerModel.setIncrement(15);
		MySpinnerDateModel  scheduleEndSpinnerModel = new MySpinnerDateModel (scheduleEndDate, null, null, Calendar.MINUTE);
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
		
		
		 JCheckBox requestBoxM = new JCheckBox("Monday");  
		 requestBoxM.setBounds(50, 50, 30,30);  
		 JCheckBox requestBoxT = new JCheckBox("Tuesday");  
		 requestBoxT.setBounds(50, 50, 30,30);  	 
		 JCheckBox requestBoxW = new JCheckBox("Wendsday");  
		 requestBoxW.setBounds(50, 50, 30,30);   
		 JCheckBox requestBoxR = new JCheckBox("Thursday");  
		 requestBoxR.setBounds(50, 50, 30,30);   		 
		 JCheckBox requestBoxF = new JCheckBox("Friday");  
		 requestBoxF.setBounds(50, 50, 30,30);  	 
		 JCheckBox requestBoxS = new JCheckBox("Saturday");  
		 requestBoxS.setBounds(50, 50, 30,30);   		 
		 JCheckBox requestBoxG = new JCheckBox("Sunday");  
		 requestBoxG.setBounds(50, 50, 30,30);  
		 JCheckBox scheduleBoxM = new JCheckBox("Monday");  
		 scheduleBoxM.setBounds(50, 50, 30,30);  
		 JCheckBox scheduleBoxT = new JCheckBox("Tuesday");  
		 scheduleBoxT.setBounds(50, 50, 30,30);		 
		 JCheckBox scheduleBoxW = new JCheckBox("Wendsday");  
		 scheduleBoxW.setBounds(50, 50, 30,30);
		 JCheckBox scheduleBoxR = new JCheckBox("Thursday");  
		 scheduleBoxR.setBounds(50, 50, 30,30);	 
		 JCheckBox scheduleBoxF = new JCheckBox("Friday");  
		 scheduleBoxF.setBounds(50, 50, 30,30);		 
		 JCheckBox scheduleBoxS = new JCheckBox("Saturday");  
		 scheduleBoxS.setBounds(50, 50, 30,30);		 
		 JCheckBox scheduleBoxG = new JCheckBox("Sunday");  
		 scheduleBoxG.setBounds(50, 50, 30,30);
		 
		 
		 
        
        
        
        
		
		
        //Build all buttons with listeners.    
		JButton requestSubmit = new JButton("Submit Request");
		requestSubmit.addActionListener( new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e)  {
		    	SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("hh:mm a");
		    	String startString = DATE_FORMAT.format((requestStartSpinner.getValue()));
		    	String endString = DATE_FORMAT.format((requestEndSpinner.getValue()));
		    	JOptionPane.showMessageDialog(null, "Booked, Starting: " + startString + " Ending: " + endString + " In " + requestRoomBox.getSelectedItem() , "TO-DO message box", JOptionPane.INFORMATION_MESSAGE);
		    }
		});
		
		JButton requestClear = new JButton("clear");
		requestClear.addActionListener( new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e)  {
		    	JOptionPane.showMessageDialog(null, "To Do");
		    }
		});
		
		JButton requestAdd = new JButton("add Requests");
		requestClear.addActionListener( new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e)  {
		    	JOptionPane.showMessageDialog(null, "To Do");
		    }
		});
			
		

		JButton noConflictApproveRequest = new JButton("Approve Request");
		noConflictApproveRequest.addActionListener( new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e)  {
		    	JOptionPane.showMessageDialog(null, "To Do");
		    }
		});
		
		JButton noConflictRejectRequest = new JButton("Reject Request");
		noConflictRejectRequest.addActionListener( new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e)  {
		    	JOptionPane.showMessageDialog(null, "To Do");
		    }
		});
		
		

		JButton conflictApproveRequest = new JButton("Approve Request");
		conflictApproveRequest.addActionListener( new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e)  {
		    	JOptionPane.showMessageDialog(null, "To Do");
		    }
		});
		
		JButton conflictRejectRequest = new JButton("Auto Resolve Conflicts");
		conflictRejectRequest.addActionListener( new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e)  {
		    	JOptionPane.showMessageDialog(null, "To Do");
		    }
		});
		
		JButton conflictResolve = new JButton("Reject Request");
		conflictResolve.addActionListener( new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e)  {
		    	JOptionPane.showMessageDialog(null, "To Do");
		    }
		});

		
		JButton scheduleSubmit = new JButton("Update Schedule");
		scheduleSubmit.addActionListener( new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e)  {
		    	JOptionPane.showMessageDialog(null, "To Do");
		    }
		});

		tabWrapperPane.setPreferredSize(new Dimension(350, 550));
		
		
		// Set all the layouts and add all the elements to the appropriate frames.
		calenderPane.setPreferredSize(new Dimension(800, 410));
		calenderPane.setRowHeaderView(rowTable);
		calenderPane.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, rowTable.getTableHeader());
		calenderToolBarPane.add(calenderDatePicker, BorderLayout.LINE_START);
		calenderWrapperPane.add(calenderToolBarPane, BorderLayout.NORTH);
		calenderWrapperPane.add(calenderPane, BorderLayout.LINE_START);	
		
		//Add elements to booking request pane.
		requestCheckPane.add(requestBoxM);
		requestCheckPane.add(requestBoxT);
		requestCheckPane.add(requestBoxW);
		requestCheckPane.add(requestBoxR);
		requestCheckPane.add(requestBoxF);
		requestCheckPane.add(requestBoxS);
		requestCheckPane.add(requestBoxG);		

		requestCheckPane.setPreferredSize(new Dimension(350, 50));
		requestButtonPane.add(startL);
		requestButtonPane.add(requestStartSpinner);
		requestButtonPane.add(endL);
		requestButtonPane.add(requestEndSpinner);
		requestButtonPane.add(requestBatchBox);
		requestButtonPane.add(requestRoomBox);
		requestButtonPane.add(requestUsersBox);
		requestButtonPane.add(requestDatePicker);

		requestButtonPane.add(requestAdd);
		requestButtonPane.add(requestClear);
		requestButtonPane.add(requestSubmit);

		requestButtonPane.setPreferredSize(new Dimension(350, 200));
		requestButtonWrapperPane.add(requestCheckPane, BorderLayout.NORTH);
		requestButtonWrapperPane.add(requestButtonPane, BorderLayout.SOUTH);	
		requestButtonWrapperPane.setPreferredSize(new Dimension(350, 200));
		requestTablePane.setPreferredSize(new Dimension(350, 300));
		requestWrapperPane.add(requestTablePane, BorderLayout.NORTH);
		requestWrapperPane.add(requestButtonWrapperPane, BorderLayout.SOUTH);
		
		
		
		
		
		
		scheduleCheckPane.add(scheduleBoxM);
		scheduleCheckPane.add(scheduleBoxT);
		scheduleCheckPane.add(scheduleBoxW);
		scheduleCheckPane.add(scheduleBoxR);
		scheduleCheckPane.add(scheduleBoxF);
		scheduleCheckPane.add(scheduleBoxS);
		scheduleCheckPane.add(scheduleBoxG);
		scheduleCheckPane.setPreferredSize(new Dimension(350, 150));
		scheduleButtonPane.add(scheduleBatchBox);
		scheduleButtonPane.add(scheduleStatusBox);
		scheduleButtonPane.add(scheduleRoomBox);
		scheduleButtonPane.add(startL);
		scheduleButtonPane.add(scheduleStartSpinner);
		scheduleButtonPane.add(endL);
		scheduleButtonPane.add(scheduleEndSpinner);
		scheduleButtonPane.add(scheduleSubmit);
		scheduleButtonPane.setPreferredSize(new Dimension(350, 300));
		scheduleWrapperPane.add(scheduleCheckPane, BorderLayout.NORTH);
		scheduleWrapperPane.add(scheduleButtonPane, BorderLayout.SOUTH);

		
		
		//Add elements to no conflict request pane.
		noConflictButtonPane.add(noConflictRejectRequest);
		noConflictButtonPane.add(noConflictApproveRequest);
		noConflictButtonPane.setPreferredSize(new Dimension(350, 100));
		noConflictTablePane.setPreferredSize(new Dimension(350, 400));
		noConflictWrapperPane.add(noConflictButtonPane,  BorderLayout.SOUTH);
		noConflictWrapperPane.add(noConflictTablePane,  BorderLayout.NORTH);
		
		//Add elements to conflict request frame.
		conflictButtonPane.add(conflictResolve);
		conflictButtonPane.add(conflictRejectRequest);
		conflictButtonPane.add(conflictApproveRequest);		
		conflictButtonPane.setPreferredSize(new Dimension(350, 100));
		conflictTablePane.setPreferredSize(new Dimension(350, 400));
		conflictWrapperPane.add(conflictButtonPane,  BorderLayout.SOUTH);
		conflictWrapperPane.add(conflictTablePane,  BorderLayout.NORTH);

				
		// Add elements to table wrapper frame.
		tabWrapperPane.add(requestWrapperPane, "Request Booking");
		tabWrapperPane.add(noConflictWrapperPane, "Apporve Bookings");
		tabWrapperPane.add(conflictWrapperPane, "Conflicted Bookings");
		tabWrapperPane.add(scheduleWrapperPane, "Scheduler");

		
		// Add wrapper to the main pane. 
		mainPane.add(calenderWrapperPane, BorderLayout.LINE_START);
		mainPane.add(tabWrapperPane, BorderLayout.LINE_END);	
		add(mainPane);
	};

	public static void renderGUI() throws SchedulerException {
		//Create the frame for the scheduler
		JFrame frame = new JFrame("School Scheduler");
		// Create the schedule table pane and attach it to the frame.
		GuiBuilder newContentPane = new GuiBuilder();
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
	public Object[][] buildDay() {
		Object[][] data = new Object[96][7];
		for (int j = 0; j < 96; j++) {
			for (int i = 0; i < 7; i++) {
				data[j][i] = "Blank";
			}
		}
		return data;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}