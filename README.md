# ScrollerCalendar
ScrollerCalendar provides straightforward wayt to see calendar for Android 4.0+.
![image](https://github.com/guanchao/ScrollerCalendar/raw/master/images/sample1.gif)

![image](https://github.com/guanchao/ScrollerCalendar/raw/master/images/sample2.gif)
## Usage
Declare a ScrollerCalendar inside your layout XML file:
```Java
<wgc.shuwoom.scrollercalendar.ScrollerCalendar
        xmlns:calendar="http://schemas.android.com/apk/res-auto"
        android:id="@+id/pickerView"
        android:overScrollMode="never"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#FFFFFF"
        calendar:showYearLunarLabel="true"/>
```
Next, you have to implement `ScrollerCalendarController` in your Activity. Your will have to set `onMonthOfYearSelected` which is called every time user selects a new date.
```Java
@Override
	public void onMonthOfYearSelected(int year, int month) 
	{
		Log.e("Month Selected",  year + "-" + month);
	}
```
### Customization
ScrollerCalendar is fully customizable:
> * calendar:dividerColor [color #FFDDDDDD]===> The color of the line under the year header label.
> * calendar:yearHeaderTextColor [color #FF000000]===> The color of year header label.
> * calendar:yearHeaderTextHeight [dimension 40dp]===> The height of the year header label.
> * calendar:yearHeaderTextSize [dimension 34sp]===> The size of year header text.
> * calendar:yearHeaderLunarTextColor [color #FFCCCCCC]===> The color of the lunar label besides of the year header label.
> * calendar:yearHeaderLunarTextSize [dimension 12sp]===> The size of the lunar label besides of the year header label.
> * calendar:yearHeaderDashColor [color #FFFF0000]===> The color of the dash line on the left of the lunar label.
> * calendar:monthLabelTextColor [color #FFFF0000]===> The color of the month label.
> * calendar:monthLabelTextSize [dimension 16sp]===> The size of the month label.
> * calendar:monthLabelTextHeight [dimension 35dp]===> The height of the month label.
> * calendar:dayLabelTextColor [color #FF000000]===> The color of the day label.
> * calendar:dayLabelTextTodayColor [color #FFFFFFFF]===> The color of the current day label.
> * calendar:dayLabelTextSize [dimension 10sp]===> The size of the day label.
> * calendar:dayLabelCircleBgColor [color #FFFF0000]===> The background color of the current day label.
> * calendar:dayLabelCircleRadius [dimension 8dp]===> The radius of the circle.
> * calendar:monthDayRowHeight [dimension 145dp]===> The height of every detailed month.
> * calendar:showYearLunarLabel [boolean true]===> Whether to show lunar label.

### Acknowledgements
Thanks to RobinChutaux for his [CalendarListview](https://github.com/traex/CalendarListview)

### MIT License
The MIT License (MIT)

    Copyright (c) 2015 guanchao wen

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.