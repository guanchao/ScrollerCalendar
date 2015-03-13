package wgc.shuwoom.scrollercalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


 


public class YearAdapter extends RecyclerView.Adapter<YearAdapter.ViewHolder> implements YearView.OnMonthClickListener {
    protected static final int MONTHS_IN_YEAR = 12;
    private final TypedArray typedArray;
	private final Context context;
	private final ScrollerCalendarController scrollerCalendarController;
    private final Calendar calendar;
    private CalendarMonth selectedMonth;

    private YearView currentYearView;
    private final int yearRange;

	public YearAdapter(Context context, ScrollerCalendarController datePickerController, TypedArray typedArray) {
		yearRange = 200;
		this.typedArray = typedArray;
        this.context = context;
        calendar = Calendar.getInstance();
        selectedMonth = new CalendarMonth();
		scrollerCalendarController = datePickerController;
	}
	
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        final YearView yearView = new YearView(context, typedArray);
        return new ViewHolder(yearView, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position)
    {
        final YearView yearView = viewHolder.yearView;
        final HashMap<String, Integer> drawingParams = new HashMap<String, Integer>();
        int currentYear;
        currentYear = 2015 + (position - yearRange / 2);
        yearView.reuse();
        drawingParams.put(YearView.VIEW_PARAMS_YEAR_CURRENT, currentYear);
        drawingParams.put(YearView.VIEW_PARAMS_WEEK_START, calendar.getFirstDayOfWeek());
        yearView.setYearParams(drawingParams);
        yearView.invalidate();
        
        currentYearView = yearView;
    }

    public long getItemId(int position) {
		return position;
	}
    
    public YearView getYearView(){
    	return currentYearView;
    }

    @Override
    public int getItemCount()
    {
        return yearRange;
    }

	public void onMonthClick(YearView yearView, CalendarMonth calendarMonth) {
		if (calendarMonth != null) {
			onMonthTapped(calendarMonth);
        }
	}

	protected void onMonthTapped(CalendarMonth calendarMonth) {
		scrollerCalendarController.onMonthOfYearSelected(calendarMonth.year, calendarMonth.month);
		setSelectedMonth(calendarMonth);
		notifyDataSetChanged();
	}

	public void setSelectedMonth(CalendarMonth calendarMonth) {
		selectedMonth = calendarMonth;
		notifyDataSetChanged();
	}
	
	public CalendarMonth getSelectedMonths() {
		return selectedMonth;
	}
	
	public int getYearRange(){
		return yearRange;
	}
    
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        final YearView yearView;

        public ViewHolder(View itemView, YearView.OnMonthClickListener onMonthClickListener)
        {
            super(itemView);
            yearView = (YearView) itemView;
            yearView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            yearView.setClickable(true);
            yearView.setOnMonthClickListener(onMonthClickListener);
        }
    }




	public static class CalendarMonth implements Serializable
    {
        private static final long serialVersionUID = -5456695978688356202L;
        private Calendar calendar;

		int month;
		int year;
		

		public CalendarMonth() {
			setTime(System.currentTimeMillis());
		}

		public CalendarMonth(int year, int month) {
			setDay(year, month);
		}
		
		public CalendarMonth(int year, int month,MotionEvent event) {
			setDay(year, month);
		}

		public CalendarMonth(long timeInMillis) {
			setTime(timeInMillis);
		}

		public CalendarMonth(Calendar calendar) {
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
		}
		
		private void setTime(long timeInMillis) {
			if (calendar == null) {
				calendar = Calendar.getInstance();
            }
			calendar.setTimeInMillis(timeInMillis);
			month = this.calendar.get(Calendar.MONTH);
			year = this.calendar.get(Calendar.YEAR);
		}

		public void set(CalendarMonth calendarDay) {
		    year = calendarDay.year;
			month = calendarDay.month;
		}

		public void setDay(int year, int month) {
			this.year = year;
			this.month = month;
		}

        public Date getDate()
        {
            if (calendar == null) {
                calendar = Calendar.getInstance();
            }
            calendar.set(year, month);
            return calendar.getTime();
        }


        @Override
        public String toString()
        {
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{ year: ");
            stringBuilder.append(year);
            stringBuilder.append(", month: ");
            stringBuilder.append(month);
            stringBuilder.append(" }");

            return stringBuilder.toString();
        }
    }

   

}