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
import java.util.Map;
import java.util.Properties;

// Main GUI class, handles building SWING elements for rendering.
public class GuiBuilder extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public GuiBuilder() throws SchedulerException {
		// Load Dummy data for rapid-prototyping
		System.out.println();
		
		DateSA d = new DateSA(12, 1);
		
//		Map<String, Booking[]> today = getFullDay(d);
	
		
		String[] calColumnNames = { "Room 1", "Room 2", "Room 3", "Room 4", "Room 5", "Room 6", "Room 7" };
		String[] userList = {"John Doe", "Jim Doe", "Jane Doe"};
		Object[][] calData = buildDay();
		String[] requestColumnNames = {"Name", "Room", "Start Time", "End Time", "Status"};
		String[] batchList = {"Daily", "Weekly", "Monthly", "By Semester", "Annually"};

		String[][] requestData = {{"John Doe","Room 1", "Start Time", "End Time", "Approved"}, 
							{"Jane Doe","Room 2", "Start Time", "End Time", "Pending"},
							{"Fake Name", "Room 3", "Start Time", "End Time", "Denied"}};

		
		// Create the table for displaying rooms:		
		JTable calTable = new JTable(calData, calColumnNames);
		JTable requestTable = new JTable(requestData, requestColumnNames);
		JComboBox<String> roomBox = new JComboBox<String>(calColumnNames);
		JComboBox<String> userBox = new JComboBox<String>(userList);
		JComboBox<String> batchBox = new JComboBox<String>(batchList);
		
		// Set the cell rendering to center for all cells.
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() ;
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		 for(int x=0;x<calTable.getColumnCount();x++){
	         calTable.getColumnModel().getColumn(x).setCellRenderer(centerRenderer);
	        }
		calTable.setEnabled(false);
		JTable rowTable = new RowNumberTable(calTable);
		
		// Create all other swing elements.
		JLabel startL = new JLabel("Start:");
		JLabel endL = new JLabel("End: ");
		
		// create all the swing panes and tables
		JPanel mainPane = new JPanel(new  BorderLayout());
		JPanel calenderToolBarPane = new JPanel(new BorderLayout());
		JPanel requestWrapperPane = new JPanel(new BorderLayout());
		JPanel scheduleWrapperPane = new JPanel(new BorderLayout());
		JPanel approvalWrapperPane = new JPanel(new BorderLayout());
		JPanel approvalToolBarPane = new JPanel(new BorderLayout());
		JScrollPane calenderPane = new JScrollPane(calTable);			
		JTabbedPane  tabWrapperPane = new JTabbedPane();
		JScrollPane requestPane = new JScrollPane(requestTable);
		JPanel requestToolBarPane = new JPanel(new BorderLayout());
		JPanel requestButtonPane = new JPanel();
		JPanel adminButtonPane = new JPanel();

		JPanel calenderWrapperPane = new JPanel(new BorderLayout());
		
		
		// Set up parameters and objects for JDatePicker swing elements 		
		Date now = Calendar.getInstance().getTime();
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM d, yyy");
		String today = DATE_FORMAT.format(now);
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.day", "Day");
		p.put("text.month", "Month");
		p.put("text.year", "Year");		
		Properties pR = new Properties();
		pR.put("text.today", "Today");
		pR.put("text.day", "Day");
		pR.put("text.month", "Month");
		pR.put("text.year", "Year");		
		UtilDateModel modelR = new UtilDateModel();	
		UtilDateModel model = new UtilDateModel();	
		JDatePanelImpl calenderDatePanel = new JDatePanelImpl(model, p);
		JDatePickerImpl calenderDatePicker = new JDatePickerImpl(calenderDatePanel, new DateComponentFormatter());
		calenderDatePicker.getJFormattedTextField().setText(today);		
		JDatePanelImpl requestDatePanel = new JDatePanelImpl(modelR, pR);
		JDatePickerImpl requestDatePicker = new JDatePickerImpl(requestDatePanel, new DateComponentFormatter());
		requestDatePicker.getJFormattedTextField().setText(today);		
		
		
		// Build the JSpinner to enter in time of day for booking, both start and end.	
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY,12);
		cal.set(Calendar.MINUTE,00);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		Date startDate = cal.getTime();
		Date endDate = cal.getTime();
		MySpinnerDateModel startSpinnerModel = new MySpinnerDateModel(startDate, null, null, Calendar.MINUTE);
		startSpinnerModel.setIncrement(15);
		MySpinnerDateModel  endSpinnerModel = new MySpinnerDateModel (endDate, null, null, Calendar.MINUTE);
		endSpinnerModel.setIncrement(15);
        JSpinner startSpinner = new JSpinner(startSpinnerModel);
        JSpinner endSpinner = new JSpinner(endSpinnerModel);
        JSpinner.DateEditor se = new JSpinner.DateEditor(startSpinner, "hh:mm a");

        se.getComponent(0).setFocusable(false);
        JSpinner.DateEditor ee = new JSpinner.DateEditor(endSpinner, "hh:mm a");
        ee.getComponent(0).setFocusable(false);
        startSpinner.setEditor(se);
        endSpinner.setEditor(ee);
        
        //Build all buttons with listeners.    
		JButton submit = new JButton("Submit Request");
		submit.addActionListener( new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e)  {
		    	SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("hh:mm a");
		    	String startString = DATE_FORMAT.format((startSpinner.getValue()));
		    	String endString = DATE_FORMAT.format((endSpinner.getValue()));
		    	String today = requestDatePicker.getJFormattedTextField().getText();
		    	JOptionPane.showMessageDialog(null, "Booked, Starting: " + startString + " Ending: " + endString + " In " + roomBox.getSelectedItem() , "TO-DO message box", JOptionPane.INFORMATION_MESSAGE);
		    }
		});
	
		JButton cancle = new JButton("Cancle Request");
		cancle.addActionListener( new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e)  {
		    	JOptionPane.showMessageDialog(null, "To Do");
		    }
		});
		
		JButton batch = new JButton("Batch Request");
		batch.addActionListener( new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e)  {
		    	JOptionPane.showMessageDialog(null, "To Do");
		    }
		});
		
		
		JButton blackOutButton = new JButton("Black Out");
		blackOutButton.addActionListener( new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e)  {
		    	JOptionPane.showMessageDialog(null, "To Do");
		    }
		});
		
		JButton approveRequest = new JButton("Approve Request");
		approveRequest.addActionListener( new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e)  {
		    	JOptionPane.showMessageDialog(null, "To Do");
		    }
		});
		
		JButton rejectRequest = new JButton("Reject Request");
		rejectRequest.addActionListener( new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e)  {
		    	JOptionPane.showMessageDialog(null, "To Do");
		    }
		});
		
		JButton changeScheduleButton = new JButton("Batch Approve");
		changeScheduleButton.addActionListener( new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e)  {
		    	JOptionPane.showMessageDialog(null, "To Do");
		    }
		});
		
		
		tabWrapperPane.add(approvalWrapperPane, "Send Request");
		tabWrapperPane.add(requestWrapperPane, "Approve Requests");
		tabWrapperPane.add(scheduleWrapperPane, "Scheduler");


		
		//Build Request and Admin button pane's 
		requestButtonPane.add(startL);
		requestButtonPane.add(startSpinner);
		requestButtonPane.add(endL);
		requestButtonPane.add(endSpinner);
		requestButtonPane.add(roomBox);	
		requestButtonPane.add(cancle);
		requestButtonPane.add(submit);		
		requestButtonPane.add(batchBox);
		requestButtonPane.add(userBox);
		requestButtonPane.add(requestDatePicker);		
		adminButtonPane.add(rejectRequest);	
		adminButtonPane.add(approveRequest);
		adminButtonPane.add(blackOutButton);
		adminButtonPane.add(changeScheduleButton);
	    approvalToolBarPane.add(requestButtonPane, BorderLayout.SOUTH);
		
	
	
		// Set all the layouts and add all the elements to the appropriate frames.
		calenderPane.setPreferredSize(new Dimension(600, 410));
		calenderPane.setRowHeaderView(rowTable);
		calenderPane.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, rowTable.getTableHeader());
		calenderToolBarPane.add(calenderDatePicker, BorderLayout.LINE_START);
		tabWrapperPane.setPreferredSize(new Dimension(350, 500));
		requestButtonPane.setPreferredSize(new Dimension(350, 125));
		adminButtonPane.setPreferredSize(new Dimension(350, 75));
		requestWrapperPane.add(requestPane, BorderLayout.NORTH);
		requestWrapperPane.add(requestToolBarPane, BorderLayout.SOUTH);
		calenderWrapperPane.add(calenderToolBarPane, BorderLayout.NORTH);
		calenderWrapperPane.add(calenderPane, BorderLayout.LINE_START);
		
		
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

}