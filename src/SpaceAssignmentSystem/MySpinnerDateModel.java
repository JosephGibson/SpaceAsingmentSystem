package SpaceAssignmentSystem;

import java.util.*;
import javax.swing.*;

public class MySpinnerDateModel extends SpinnerDateModel
{
    private int increment = 1;
  

    public MySpinnerDateModel(java.util.Date value, Comparable start, Comparable end, int calendarField)
    {
        super(value, start, end, calendarField);
    }

    public MySpinnerDateModel()
    {
        this(new java.util.Date(), null, null, Calendar.DAY_OF_MONTH);
    }

    public void setIncrement(int increment)
    {
        this.increment = increment;
    }

    public int getIncrement()
    {
        return increment;
    }

    @Override
    public Object getNextValue()
    {
        Calendar cal = Calendar.getInstance();
	    java.util.Date value = (java.util.Date)getValue();	       
        cal.setTime(value);
        cal.add(Calendar.MINUTE, increment);
        java.util.Date next = cal.getTime();
        Comparable end = getEnd();
        return ((end == null) || (end.compareTo(next) >= 0)) ? next : null;
    }

    @Override
    public Object getPreviousValue()
    {
        Calendar cal = Calendar.getInstance();
        java.util.Date value = (java.util.Date)getValue();	
        cal.setTime(value);
        cal.add(Calendar.MINUTE, -increment);
        java.util.Date prev = cal.getTime();
        Comparable start = getStart();
        return ((start == null) || (start.compareTo(prev) <= 0)) ? prev : null;
    }
}