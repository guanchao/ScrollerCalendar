package wgc.shuwoom.scrollercalendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import wgc.shuwoom.scrollercalendar.R;
import wgc.shuwoom.scrollercalendar.YearAdapter.CalendarMonth;

public class ScrollerCalendar extends RecyclerView
{
	private YearAdapter yearAdapter;
    private TypedArray typedArray;
    private OnScrollListener onScrollListener;
    private ScrollerCalendarController scrollerCalendarController;

    public ScrollerCalendar(Context context)
    {
        this(context, null);
    }

    public ScrollerCalendar(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    @SuppressLint("Recycle")
	public ScrollerCalendar(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        if (!isInEditMode())
        {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScrollerCalendar);
            setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            init(context);
        }
    }
    
	public void init(Context paramContext) {
        setLayoutManager(new LinearLayoutManager(paramContext));
		setUpListView();

        onScrollListener = new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
            	
                super.onScrolled(recyclerView, dx, dy);
                final YearView child = (YearView) recyclerView.getChildAt(0);
                if (child == null) {
                    return;
                }
            }
        };
       
	}
	
    public void setController(ScrollerCalendarController scrollerCalendarController)
    {
        this.scrollerCalendarController = scrollerCalendarController;
        setUpAdapter();
        setAdapter(yearAdapter);
    }

    public CalendarMonth getSelectedMonths()
    {
        return yearAdapter.getSelectedMonths();
    }

    public ScrollerCalendarController getController()
    {
        return scrollerCalendarController;
    }

    public TypedArray getTypedArray()
    {
        return typedArray;
    }
    
	private void setUpAdapter() {
		if (yearAdapter == null) {
			yearAdapter = new YearAdapter(getContext(), scrollerCalendarController, typedArray);
        }
		scrollToPosition(yearAdapter.getYearRange() / 2);
		yearAdapter.notifyDataSetChanged();
	}

	private void setUpListView() {
		setVerticalScrollBarEnabled(false);
		setOnScrollListener(onScrollListener);
		setFadingEdgeLength(0);
	}
}