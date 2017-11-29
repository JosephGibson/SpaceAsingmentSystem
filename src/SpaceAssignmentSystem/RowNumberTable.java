package SpaceAssignmentSystem;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

// ALL CODE ATTRIBUTED TO Rob Camick.
// Allows for Row headers to be inserted to tables. 

public class RowNumberTable extends JTable implements ChangeListener, PropertyChangeListener, TableModelListener {
	private JTable main;

	public RowNumberTable(JTable table) {
		main = table;
		main.addPropertyChangeListener(this);
		main.getModel().addTableModelListener(this);

		setFocusable(false);
		setAutoCreateColumnsFromModel(false);
		setSelectionModel(main.getSelectionModel());

		TableColumn column = new TableColumn();
		column.setHeaderValue("Hours :");
		addColumn(column);
		column.setCellRenderer(new RowNumberRenderer());

		getColumnModel().getColumn(0).setPreferredWidth(125);
		setPreferredScrollableViewportSize(getPreferredSize());
	}
	@Override
	public void addNotify() {
		super.addNotify();

		Component c = getParent();

		// Keep scrolling of the row table in sync with the main table.

		if (c instanceof JViewport) {
			JViewport viewport = (JViewport) c;
			viewport.addChangeListener(this);
		}
	}

	/*
	 * Delegate method to main table
	 */
	@Override
	public int getRowCount() {
		return main.getRowCount();
	}

	@Override
	public int getRowHeight(int row) {
		int rowHeight = main.getRowHeight(row);

		if (rowHeight != super.getRowHeight(row)) {
			super.setRowHeight(row, rowHeight);
		}

		return rowHeight;
	}

	/*
	 * No model is being used for this table so just use the row number as the value
	 * of the cell.
	 */
	@Override
	public Object getValueAt(int row, int column) {
		return Integer.toString(row + 1);
	}

	/*
	 * Don't edit data in the main TableModel by mistake
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	/*
	 * Do nothing since the table ignores the model
	 */
	@Override
	public void setValueAt(Object value, int row, int column) {
	}

	//
	// Implement the ChangeListener
	//
	public void stateChanged(ChangeEvent e) {
		// Keep the scrolling of the row table in sync with main table

		JViewport viewport = (JViewport) e.getSource();
		JScrollPane scrollPane = (JScrollPane) viewport.getParent();
		scrollPane.getVerticalScrollBar().setValue(viewport.getViewPosition().y);
	}

	//
	// Implement the PropertyChangeListener
	//
	public void propertyChange(PropertyChangeEvent e) {
		// Keep the row table in sync with the main table

		if ("selectionModel".equals(e.getPropertyName())) {
			setSelectionModel(main.getSelectionModel());
		}

		if ("rowHeight".equals(e.getPropertyName())) {
			repaint();
		}

		if ("model".equals(e.getPropertyName())) {
			main.getModel().addTableModelListener(this);
			revalidate();
		}
	}

	// Implement the TableModelListener
	@Override
	public void tableChanged(TableModelEvent e) {
		revalidate();
	}

	/*
	 * Attempt to mimic the table header renderer
	 */
	private static class RowNumberRenderer extends DefaultTableCellRenderer {
		public RowNumberRenderer() {
			setHorizontalAlignment(JLabel.CENTER);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (table != null) {
				JTableHeader header = table.getTableHeader();

				if (header != null) {
					setForeground(header.getForeground());
					setBackground(header.getBackground());
					setFont(header.getFont());
				}
			}

			if (isSelected) {
				setFont(getFont().deriveFont(Font.BOLD));
			}

			setText((value == null) ? "" : setTimes(value.toString()));
			setBorder(UIManager.getBorder("TableHeader.cellBorder"));

			return this;
		}
		public String setTimes(Object value) {
			int i = Integer.valueOf((String) value) - 1;
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY,00);
			cal.set(Calendar.MINUTE,00);
			cal.set(Calendar.SECOND,0);
			cal.set(Calendar.MILLISECOND,0);
			cal.add(Calendar.MINUTE, 15 * i);
			Calendar cal2 = Calendar.getInstance();
			cal2.set(Calendar.HOUR_OF_DAY,00);
			cal2.set(Calendar.MINUTE,00);
			cal2.set(Calendar.SECOND,0);
			cal2.set(Calendar.MILLISECOND,0);
			cal2.add(Calendar.MINUTE, 15 * (i+1));
			SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("hh:mm a");
			
			String temp =  DATE_FORMAT.format((cal.getTime())) + "-" + DATE_FORMAT.format((cal2.getTime()));
			return temp;
			
		}
	}
}
