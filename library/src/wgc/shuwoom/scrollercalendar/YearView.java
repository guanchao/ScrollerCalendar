/***********************************************************************************
 * The MIT License (MIT)

 * Copyright (c) 2014 Robin Chutaux

 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ***********************************************************************************/
package wgc.shuwoom.scrollercalendar;

import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import wgc.shuwoom.scrollercalendar.R;
import wgc.shuwoom.scrollercalendar.CommonUtils;
import wgc.shuwoom.scrollercalendar.Lunar;


public class YearView extends View {
	public static final String VIEW_PARAMS_YEAR_CURRENT = "current";
	public static final String VIEW_PARAMS_WEEK_START = "week_start";

	protected static int DAY_LABEL_CIRCLE_RADIUS;
	protected static int MONTH_HEADER_HEIGHT;
	protected static int YEAR_HEADER_TEXT_HEIGHT;
	
	
	protected static int YEAR_HEADER_TEXT_SIZE;
	protected static int YEAR_HEADER_LUNAR_TEXT_SIZE;
	protected static int MONTH_LABEL_TEXT_SIZE;
	protected static int DAY_LABEL_TEXT_SIZE;

	protected int padding = 0;
	protected int lineSpacingBetweenYearAndMonth = 0;
	protected int lineSpacingBetweenDayAndDay = 0;

	protected Paint yearHeaderTextPaint;
	protected Paint dividerPaint;
	protected Paint yearHeaderLunarTextPaint;
	protected Paint yearHeaderDashPaint;
	protected Paint monthLabelTextPaint;
	protected Paint dayLabelTextPaint;
	protected Paint dayLabelCircleBgPaint;
	
	protected int yearHeaderTextColor;
	protected int dividerColor;
	protected int yearHeaderLunarTextColor;
	protected int yearHeaderDashColor;
	protected int monthTextColor;
	protected int dayLabelTextColor;
	protected int dayLabelTextTodayColor;
	protected int dayLabelCircleBgColor;

	protected int today = -1;
	protected int weekStart = 1;
	protected int numDays = 7;
	protected int numCells = 0;
	private int dayOfWeekStart = 0;
	protected int month;
	protected int year;

	protected int rowMonthHeight;
	protected int mWidth;
	
	protected final Time currentTime;

	private int width;

	private Context mContext;
	private final Calendar calendar;
	private boolean showYearLunarLabel;
	
	private OnMonthClickListener mOnMonthClickListener;
	

	public YearView(Context context, TypedArray typedArray) {
		super(context);
		mContext = context;
		Resources resources = context.getResources();
		calendar = Calendar.getInstance();
		currentTime = new Time(Time.getCurrentTimezone());
		currentTime.setToNow();
		
		dividerColor = typedArray.getColor(
				R.styleable.ScrollerCalendar_dividerColor, 
				resources.getColor(R.color.divider_color));
		yearHeaderTextColor = typedArray.getColor(
				R.styleable.ScrollerCalendar_yearHeaderTextColor,
				resources.getColor(R.color.year_header_text_color));
		yearHeaderLunarTextColor = typedArray.getColor(
				R.styleable.ScrollerCalendar_yearHeaderLunarTextColor,
				resources.getColor(R.color.year_header_lunar_text_color));
		yearHeaderDashColor = typedArray.getColor(
				R.styleable.ScrollerCalendar_yearHeaderDashColor,
				resources.getColor(R.color.year_header_dash_color));
		monthTextColor = typedArray.getColor(
				R.styleable.ScrollerCalendar_monthLabelTextColor,
				resources.getColor(R.color.month_labe_text_color));
		dayLabelTextColor = typedArray.getColor(
				R.styleable.ScrollerCalendar_dayLabelTextColor,
				resources.getColor(R.color.day_label_text_color));
		dayLabelTextTodayColor = typedArray.getColor(
				R.styleable.ScrollerCalendar_dayLabelTextTodayColor,
				resources.getColor(R.color.day_label_text_today_color));
		dayLabelCircleBgColor = typedArray.getColor(
				R.styleable.ScrollerCalendar_dayLabelCircleBgColor,
				resources.getColor(R.color.day_label_circle_bg_color));

		YEAR_HEADER_TEXT_SIZE = typedArray.getDimensionPixelSize(
				R.styleable.ScrollerCalendar_yearHeaderTextSize,
				resources.getDimensionPixelSize(R.dimen.year_header_text_size));
		YEAR_HEADER_LUNAR_TEXT_SIZE = typedArray.getDimensionPixelSize(
				R.styleable.ScrollerCalendar_yearHeaderLunarTextSize,
				resources.getDimensionPixelSize(R.dimen.year_header_lunar_text_size));
		DAY_LABEL_TEXT_SIZE = typedArray.getDimensionPixelSize(
				R.styleable.ScrollerCalendar_dayLabelTextSize,
				resources.getDimensionPixelSize(R.dimen.day_label_text_size));
		MONTH_LABEL_TEXT_SIZE = typedArray.getDimensionPixelSize(
				R.styleable.ScrollerCalendar_monthLabelTextSize,
				resources.getDimensionPixelSize(R.dimen.month_label_text_size));

		YEAR_HEADER_TEXT_HEIGHT = typedArray.getDimensionPixelSize(
				R.styleable.ScrollerCalendar_yearHeaderTextHeight, 
				resources.getDimensionPixelOffset(R.dimen.year_header_text_height));
		MONTH_HEADER_HEIGHT = typedArray.getDimensionPixelOffset(
				R.styleable.ScrollerCalendar_monthLabelTextHeight,
				resources.getDimensionPixelSize(R.dimen.month_label_text_height));
		DAY_LABEL_CIRCLE_RADIUS = typedArray.getDimensionPixelSize(
				R.styleable.ScrollerCalendar_dayLabelCircleRadius,
				resources.getDimensionPixelSize(R.dimen.day_label_circle_radius));
		
		rowMonthHeight = typedArray.getDimensionPixelSize(
				R.styleable.ScrollerCalendar_monthDayRowHeight,
				resources.getDimensionPixelSize(R.dimen.month_day_row_height));
		lineSpacingBetweenDayAndDay = 
				resources.getDimensionPixelSize(R.dimen.padding_between_day_and_day);
		showYearLunarLabel = typedArray.getBoolean(
				R.styleable.ScrollerCalendar_showYearLunarLabel,false);
		padding = CommonUtils.dp2px(mContext, 5);
		lineSpacingBetweenYearAndMonth = CommonUtils.dp2px(mContext, 10);
		initView();

	}

	public int getMonth() {
		return month;
	}

	public int calculateNumRows() {
		int offset = findDayOffset();
		int dividend = (offset + numCells) / numDays;
		int remainder = (offset + numCells) % numDays;
		return (dividend + (remainder > 0 ? 1 : 0));
	}

	private void drawYearHeaderLabels(Canvas canvas) {
		int y = (4 * YEAR_HEADER_TEXT_HEIGHT) / 5;
		Lunar lunar = new Lunar(year);

		canvas.drawText(year + "年", 2 * padding, y, yearHeaderTextPaint);

		if(showYearLunarLabel){
			yearHeaderDashPaint.setStrokeWidth((float) 4.0);
			canvas.drawLine(width - 5 * YEAR_HEADER_LUNAR_TEXT_SIZE - 2
					* padding, (3 * y) / 8, width - 5
					* YEAR_HEADER_LUNAR_TEXT_SIZE, (3 * y) / 8, yearHeaderDashPaint);
			canvas.drawText(lunar.cyclical() + lunar.animalsYear() + "年", width - 2
					* padding, y / 2, yearHeaderLunarTextPaint);
			
			yearHeaderDashPaint.setStrokeWidth((float) 2.0);
			canvas.drawLine(width - 5 * YEAR_HEADER_LUNAR_TEXT_SIZE - 2
					* padding, (7 * y) / 8, width - 5
					* YEAR_HEADER_LUNAR_TEXT_SIZE, (7 * y) / 8, yearHeaderDashPaint);
			canvas.drawText("农历初一", width - 2 * padding, y,
					yearHeaderLunarTextPaint);
		}
		
		canvas.drawLine(2 * padding, YEAR_HEADER_TEXT_HEIGHT, width,
				YEAR_HEADER_TEXT_HEIGHT, dividerPaint);
	}

	private void drawMonthTitle(Canvas canvas) {
		int paddingDay = (mWidth - 2 * padding) / (2 * numDays);

		int x = padding;
		int y = 0;

		for (int i = 1; i <= 12; i++) {

			switch (i) {
			case 1:
			case 2:
			case 3:
				x = paddingDay + (width / 3) * (i - 1);
				y = (MONTH_HEADER_HEIGHT) / 2 + YEAR_HEADER_TEXT_HEIGHT
						+ lineSpacingBetweenYearAndMonth;
				break;
			case 4:
			case 5:
			case 6:
				x = paddingDay + (width / 3) * (i - 4);
				y = (MONTH_HEADER_HEIGHT) / 2 + YEAR_HEADER_TEXT_HEIGHT
						+ lineSpacingBetweenYearAndMonth + rowMonthHeight * 1;
				break;
			case 7:
			case 8:
			case 9:
				x = paddingDay + (width / 3) * (i - 7);
				y = (MONTH_HEADER_HEIGHT) / 2 + YEAR_HEADER_TEXT_HEIGHT
						+ lineSpacingBetweenYearAndMonth + rowMonthHeight * 2;
				break;
			case 10:
			case 11:
			case 12:
				x = paddingDay + (width / 3) * (i - 10);
				y = (MONTH_HEADER_HEIGHT) / 2 + YEAR_HEADER_TEXT_HEIGHT
						+ lineSpacingBetweenYearAndMonth + rowMonthHeight * 3;
				break;
			}

			canvas.drawText(i + "月", x, y, monthLabelTextPaint);
		}
	}

	private void drawAllMonthNums(Canvas canvas) {
		int y = MONTH_HEADER_HEIGHT + YEAR_HEADER_TEXT_HEIGHT + lineSpacingBetweenYearAndMonth;
		int paddingDay = (mWidth / 3 - 2 * padding) / (2 * numDays);
		int day = 1;

		for (int i = 0; i < 12; i++) {

			switch (i) {
			case 0:
			case 1:
			case 2:
				y = MONTH_HEADER_HEIGHT + YEAR_HEADER_TEXT_HEIGHT
						+ lineSpacingBetweenYearAndMonth;
				break;
			case 3:
			case 4:
			case 5:
				y = MONTH_HEADER_HEIGHT + YEAR_HEADER_TEXT_HEIGHT
						+ lineSpacingBetweenYearAndMonth + rowMonthHeight * 1;
				break;
			case 6:
			case 7:
			case 8:
				y = MONTH_HEADER_HEIGHT + YEAR_HEADER_TEXT_HEIGHT
						+ lineSpacingBetweenYearAndMonth + rowMonthHeight * 2;
				break;
			case 9:
			case 10:
			case 11:
				y = MONTH_HEADER_HEIGHT + YEAR_HEADER_TEXT_HEIGHT
						+ lineSpacingBetweenYearAndMonth + rowMonthHeight * 3;
				break;
			}

			setYearParams(year, i);
			int dayOffset = findDayOffset();
			day = 1;

			while (day <= numCells) {
				int column = 0;
				switch (i) {
				case 0:
				case 3:
				case 6:
				case 9:
					column = 0;
					break;
				case 1:
				case 4:
				case 7:
				case 10:
					column = 1;
					break;
				case 2:
				case 5:
				case 8:
				case 11:
					column = 2;
					break;
				}

				int x = paddingDay * (1 + dayOffset * 2) + (column * width) / 3
						+ paddingDay;

				canvas.drawText(String.format("%d", day), x, y, dayLabelTextPaint);

				if ( today == day) {
					canvas.drawCircle(x+CommonUtils.dp2px(mContext, 1), y - CommonUtils.dp2px(mContext, 4),
							DAY_LABEL_CIRCLE_RADIUS, dayLabelCircleBgPaint);

					dayLabelTextPaint.setColor(dayLabelTextTodayColor);
					canvas.drawText(String.format("%d", day), x, y,
							dayLabelTextPaint);
					dayLabelTextPaint.setColor(dayLabelTextColor);
				}

				dayOffset++;

				if (dayOffset == numDays) {
					dayOffset = 0;
					y += lineSpacingBetweenDayAndDay;
				}
				day++;
			}// end of while

		}// end of for
	}
	
	private int findDayOffset() {
		return (dayOfWeekStart < weekStart ? (dayOfWeekStart + numDays)
				: dayOfWeekStart) - weekStart;
	}

	private void onDayClick(YearAdapter.CalendarMonth calendarMonth) {
		if (mOnMonthClickListener != null) {
			mOnMonthClickListener.onMonthClick(this, calendarMonth);
		}
	}

	private boolean isToday(int monthDay, Time time) {
		return (year == time.year) && (month == time.month)
				&& (monthDay == time.monthDay);
	}

	

	

	public YearAdapter.CalendarMonth getMonthFromLocation(
			MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		int month = 0;
		int columnWidth = width / 3;
		 
		if (y < YEAR_HEADER_TEXT_HEIGHT) {
			return null;
		}

		if ((y <= YEAR_HEADER_TEXT_HEIGHT + rowMonthHeight)) {
			if (x <= columnWidth) {
				month = 0;
			}
			if ((x < 2 * columnWidth) && (x > columnWidth)) {
				month = 1;
			}
			if ((x <= 3 * columnWidth) && (x >= 2 * columnWidth)) {
				month = 2;
			}
		}
		if ((y <= YEAR_HEADER_TEXT_HEIGHT + 2 * rowMonthHeight)
				&& (y > YEAR_HEADER_TEXT_HEIGHT + rowMonthHeight)) {
			if (x <= columnWidth) {
				month = 3;
			}
			if ((x < 2 * columnWidth) && (x > columnWidth)) {
				month = 4;
			}
			if ((x <= 3 * columnWidth) && (x >= 2 * columnWidth)) {
				month = 5;
			}
		}

		if ((y <= YEAR_HEADER_TEXT_HEIGHT + 3 * rowMonthHeight)
				&& (y > YEAR_HEADER_TEXT_HEIGHT + 2 * rowMonthHeight)) {
			if (x <= columnWidth) {
				month = 6;
			}
			if ((x < 2 * columnWidth) && (x > columnWidth)) {
				month = 7;
			}
			if ((x <= 3 * columnWidth) && (x >= 2 * columnWidth)) {
				month = 8;
			}
		}

		if ((y <= YEAR_HEADER_TEXT_HEIGHT + 4 * rowMonthHeight)
				&& (y >= YEAR_HEADER_TEXT_HEIGHT + 3 * rowMonthHeight)) {
			if (x <= columnWidth) {
				month = 9;
			}
			if ((x < 2 * columnWidth) && (x > columnWidth)) {
				month = 10;
			}
			if ((x <= 3 * columnWidth) && (x >= 2 * columnWidth)) {
				month = 11;
			}
		}

		return new YearAdapter.CalendarMonth(year, month, event);
	}

	protected void initView() {
		monthLabelTextPaint = new Paint();
		monthLabelTextPaint.setFakeBoldText(true);
		monthLabelTextPaint.setAntiAlias(true);
		monthLabelTextPaint.setTextSize(MONTH_LABEL_TEXT_SIZE);
		monthLabelTextPaint.setColor(monthTextColor);
		monthLabelTextPaint.setTextAlign(Align.CENTER);
		monthLabelTextPaint.setStyle(Style.FILL);

		dayLabelCircleBgPaint = new Paint();
		dayLabelCircleBgPaint.setFakeBoldText(true);
		dayLabelCircleBgPaint.setAntiAlias(true);
		dayLabelCircleBgPaint.setColor(dayLabelCircleBgColor);
		dayLabelCircleBgPaint.setTextAlign(Align.CENTER);
		dayLabelCircleBgPaint.setStyle(Style.FILL);

		yearHeaderTextPaint = new Paint();
		yearHeaderTextPaint.setAntiAlias(true);
		yearHeaderTextPaint.setTextAlign(Align.LEFT);
		yearHeaderTextPaint.setTextSize(YEAR_HEADER_TEXT_SIZE);
		yearHeaderTextPaint.setColor(yearHeaderTextColor);
		yearHeaderTextPaint.setStyle(Style.FILL);
		yearHeaderTextPaint.setFakeBoldText(false);

		yearHeaderLunarTextPaint = new Paint();
		yearHeaderLunarTextPaint.setAntiAlias(true);
		yearHeaderLunarTextPaint.setTextAlign(Align.RIGHT);
		yearHeaderLunarTextPaint.setTextSize(YEAR_HEADER_LUNAR_TEXT_SIZE);
		yearHeaderLunarTextPaint.setColor(yearHeaderLunarTextColor);
		yearHeaderLunarTextPaint.setStyle(Style.FILL);
		yearHeaderLunarTextPaint.setFakeBoldText(false);

		dayLabelTextPaint = new Paint();
		dayLabelTextPaint.setAntiAlias(true);
		dayLabelTextPaint.setTextSize(DAY_LABEL_TEXT_SIZE);
		dayLabelTextPaint.setColor(dayLabelTextColor);
		dayLabelTextPaint.setStyle(Style.FILL);
		dayLabelTextPaint.setTextAlign(Align.CENTER);
		dayLabelTextPaint.setFakeBoldText(false);

		dayLabelCircleBgPaint = new Paint();
		dayLabelCircleBgPaint.setAntiAlias(true);
		dayLabelCircleBgPaint.setTextSize(DAY_LABEL_TEXT_SIZE);
		dayLabelCircleBgPaint.setColor(dayLabelCircleBgColor);
		dayLabelCircleBgPaint.setStyle(Style.FILL);
		dayLabelCircleBgPaint.setTextAlign(Align.CENTER);
		dayLabelCircleBgPaint.setFakeBoldText(false);

		dividerPaint = new Paint();
		dividerPaint.setAntiAlias(true);
		dividerPaint.setColor(dividerColor);
		dividerPaint.setStyle(Style.FILL);
		dividerPaint.setTextAlign(Align.CENTER);
		dividerPaint.setFakeBoldText(false);
		
		yearHeaderDashPaint = new Paint();
		yearHeaderDashPaint.setAntiAlias(true);
		yearHeaderDashPaint.setColor(yearHeaderDashColor);
	}

	protected void onDraw(Canvas canvas) {

		drawYearHeaderLabels(canvas);
		drawMonthTitle(canvas);
		drawAllMonthNums(canvas);

	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		setMeasuredDimension(width, rowMonthHeight* 4 + YEAR_HEADER_TEXT_HEIGHT
				+lineSpacingBetweenYearAndMonth);
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mWidth = w;
	}

	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			YearAdapter.CalendarMonth calendarDay = getMonthFromLocation(event);
			if (calendarDay != null) {
				onDayClick(calendarDay);
			}
		}

		return true;
	}

	public void reuse() {
		requestLayout();
	}

	public void setYearParams(int year, int month) {
		this.month = month;
		this.year = year;

		today = -1;

		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		dayOfWeekStart = calendar.get(Calendar.DAY_OF_WEEK);

		weekStart = calendar.getFirstDayOfWeek();
		numCells = CalendarUtils.getDaysInMonth(month, year);

		for (int i = 0; i < numCells; i++) {
			final int day = i + 1;
			if (isToday(day, currentTime)) {
				today = day;
			}
		}
	}



	@SuppressWarnings("deprecation")
	public void setYearParams(HashMap<String, Integer> params) {
		if (!params.containsKey(VIEW_PARAMS_YEAR_CURRENT)) {
			throw new InvalidParameterException(
					"You must specify current_year for this view");
		}
		setTag(params);

		year = params.get(VIEW_PARAMS_YEAR_CURRENT);

		today = -1;

		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		dayOfWeekStart = calendar.get(Calendar.DAY_OF_WEEK);

		if (params.containsKey(VIEW_PARAMS_WEEK_START)) {
			weekStart = params.get(VIEW_PARAMS_WEEK_START);
		} else {
			weekStart = calendar.getFirstDayOfWeek();
		}

		numCells = CalendarUtils.getDaysInMonth(month, year);

		for (int i = 0; i < numCells; i++) {
			final int day = i + 1;
			if (isToday(day, currentTime)) {
				today = day;
			}

		}

		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth();

	}

	public void setOnMonthClickListener(
			OnMonthClickListener onMonthClickListener) {
		mOnMonthClickListener = onMonthClickListener;
	}

	public static abstract interface OnMonthClickListener {
		public abstract void onMonthClick(YearView simpleMonthView,
				YearAdapter.CalendarMonth calendarDay);
	}
}