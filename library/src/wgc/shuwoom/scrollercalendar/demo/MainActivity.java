package wgc.shuwoom.scrollercalendar.demo;


import wgc.shuwoom.scrollercalendar.ScrollerCalendar;
import wgc.shuwoom.scrollercalendar.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity  implements
wgc.shuwoom.scrollercalendar.ScrollerCalendarController{
	
	private ScrollerCalendar monthPickerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		monthPickerView = (ScrollerCalendar) findViewById(R.id.pickerView);
		monthPickerView.setController(this);
		
	}

	@Override
	public void onMonthOfYearSelected(int year, int month) {
		Toast.makeText(getApplicationContext(), year+"-"+month, Toast.LENGTH_SHORT).show();
		
	}



}
