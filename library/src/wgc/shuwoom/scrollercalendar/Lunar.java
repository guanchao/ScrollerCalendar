package wgc.shuwoom.scrollercalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Lunar {
    private int year;
    private int month;
    private int day;
    private boolean leap;
    final static String chineseNumber[] = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};
    static SimpleDateFormat chineseDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
    final static long[] lunarInfo = new long[]
    {0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
     0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977,
     0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,
     0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950,
     0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557,
     0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950, 0x06aa0,
     0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0,
     0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6,
     0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570,
     0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0,
     0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5,
     0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930,
     0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530,
     0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45,
     0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0};

    public Lunar(){
    	
    }
    
    public Lunar(int year){
    	this.year = year;
    }
    
    //====== 传回农历 y年的总天数
    final private static int yearDays(int y) {
        int i, sum = 348;
        for (i = 0x8000; i > 0x8; i >>= 1) {
            if ((lunarInfo[y - 1900] & i) != 0) sum += 1;
        }
        return (sum + leapDays(y));
    }

    //====== 传回农历 y年闰月的天数
    final private static int leapDays(int y) {
        if (leapMonth(y) != 0) {
            if ((lunarInfo[y - 1900] & 0x10000) != 0)
                return 30;
            else
                return 29;
        } else
            return 0;
    }

    //====== 传回农历 y年闰哪个月 1-12 , 没闰传回 0
    final private static int leapMonth(int y) {
        return (int) (lunarInfo[y - 1900] & 0xf);
    }

    //====== 传回农历 y年m月的总天数
    final private static int monthDays(int y, int m) {
        if ((lunarInfo[y - 1900] & (0x10000 >> m)) == 0)
            return 29;
        else
            return 30;
    }

    //====== 传回农历 y年的生肖
    final public String animalsYear() {
        final String[] Animals = new String[]{"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
        return Animals[(year - 4) % 12];
    }

    //====== 传入 月日的offset 传回干支, 0=甲子
    final private static String cyclicalm(int num) {
        final String[] Gan = new String[]{"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
        final String[] Zhi = new String[]{"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
        return (Gan[num % 10] + Zhi[num % 12]);
    }

    //====== 传入 offset 传回干支, 0=甲子
    final public String cyclical() {
        int num = year - 1900 + 36;
        return (cyclicalm(num));
    }
    
    
    
    public static String getChinaDayString(int day) {
        String chineseTen[] = {"初", "十", "廿", "三"};
        int n = day % 10 == 0 ? 9 : day % 10 - 1;
        if (day > 30)
            return "";
        if (day == 10)
            return "初十";
        else
            return chineseTen[day / 10] + chineseNumber[n];
    }

    /**
     * 判断是不是农历初一
     * @param year_log
     * @param month_log
     * @param day_log
     * @return 是返回true，否则为false
     */
    public boolean isFirstDayOfMonthInLunar(int year_log, int month_log, int day_log){
    	//@SuppressWarnings("unused") 
    	int yearCyl, monCyl, dayCyl;
        int leapMonth = 0;
        String nowadays;
        Date baseDate = null;
        Date nowaday=null;
        try {
            baseDate = chineseDateFormat.parse("1900年1月31日");
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
        
        nowadays=year_log+"年"+month_log+"月"+day_log+"日";
        try {
        	nowaday = chineseDateFormat.parse(nowadays);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }

        //求出和1900年1月31日相差的天数
        int offset = (int) ((nowaday.getTime() - baseDate.getTime()) / 86400000L);
        dayCyl = offset + 40;
        monCyl = 14;

        //用offset减去每农历年的天数
        // 计算当天是农历第几天
        //i最终结果是农历的年份
        //offset是当年的第几天
        int iYear, daysOfYear = 0;
        for (iYear = 1900; iYear < 10000 && offset > 0; iYear++) {
            daysOfYear = yearDays(iYear);
            offset -= daysOfYear;
            monCyl += 12;
        }
        if (offset < 0) {
            offset += daysOfYear;
            iYear--;
            monCyl -= 12;
        }
        //农历年份
        year = iYear;

        yearCyl = iYear - 1864;
        leapMonth = leapMonth(iYear); //闰哪个月,1-12
        leap = false;

        //用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
        int iMonth, daysOfMonth = 0;
        for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
            //闰月
            if (leapMonth > 0 && iMonth == (leapMonth + 1) && !leap) {
                --iMonth;
                leap = true;
                daysOfMonth = leapDays(year);
            } else
                daysOfMonth = monthDays(year, iMonth);

            offset -= daysOfMonth;
            //解除闰月
            if (leap && iMonth == (leapMonth + 1)) leap = false;
            if (!leap) monCyl++;
        }
        //offset为0时，并且刚才计算的月份是闰月，要校正
        if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
            if (leap) {
                leap = false;
            } else {
                leap = true;
                --iMonth;
                --monCyl;
            }
        }
        //offset小于0时，也要校正
        if (offset < 0) {
            offset += daysOfMonth;
            --iMonth;
            --monCyl;
        }
        month = iMonth;
        day = offset + 1;
        
        if(day == 1){
        	return true;
        }else{
        	return false;
        }
    }
    
    /** *//**
     * 传出y年m月d日对应的农历.
     * yearCyl3:农历年与1864的相差数              ?
     * monCyl4:从1900年1月31日以来,闰月数
     * dayCyl5:与1900年1月31日相差的天数,再加40      ?
     * @param cal 
     * @return 
     */
    public  String  getLunarDate(int year_log, int month_log, int day_log) {
        //@SuppressWarnings("unused") 
    	int yearCyl, monCyl, dayCyl;
        int leapMonth = 0;
        String nowadays;
        Date baseDate = null;
        Date nowaday=null;
        try {
            baseDate = chineseDateFormat.parse("1900年1月31日");
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
        
        nowadays=year_log+"年"+month_log+"月"+day_log+"日";
        try {
        	nowaday = chineseDateFormat.parse(nowadays);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }

        //求出和1900年1月31日相差的天数
        int offset = (int) ((nowaday.getTime() - baseDate.getTime()) / 86400000L);
        dayCyl = offset + 40;
        monCyl = 14;

        //用offset减去每农历年的天数
        // 计算当天是农历第几天
        //i最终结果是农历的年份
        //offset是当年的第几天
        int iYear, daysOfYear = 0;
        for (iYear = 1900; iYear < 10000 && offset > 0; iYear++) {
            daysOfYear = yearDays(iYear);
            offset -= daysOfYear;
            monCyl += 12;
        }
        if (offset < 0) {
            offset += daysOfYear;
            iYear--;
            monCyl -= 12;
        }
        //农历年份
        year = iYear;

        yearCyl = iYear - 1864;
        leapMonth = leapMonth(iYear); //闰哪个月,1-12
        leap = false;

        //用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
        int iMonth, daysOfMonth = 0;
        for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
            //闰月
            if (leapMonth > 0 && iMonth == (leapMonth + 1) && !leap) {
                --iMonth;
                leap = true;
                daysOfMonth = leapDays(year);
            } else
                daysOfMonth = monthDays(year, iMonth);

            offset -= daysOfMonth;
            //解除闰月
            if (leap && iMonth == (leapMonth + 1)) leap = false;
            if (!leap) monCyl++;
        }
        //offset为0时，并且刚才计算的月份是闰月，要校正
        if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
            if (leap) {
                leap = false;
            } else {
                leap = true;
                --iMonth;
                --monCyl;
            }
        }
        //offset小于0时，也要校正
        if (offset < 0) {
            offset += daysOfMonth;
            --iMonth;
            --monCyl;
        }
        month = iMonth;
        day = offset + 1;
        
        //阳历
        if((month_log==1)&&(day_log==1) )
        	return "元旦";
        if((month_log==2)&&(day_log==14) )
        	return "情人";
        if((month_log==3)&&(day_log==8) )
        	return "妇女";
        if((month_log==4)&&(day_log==5) )
        	return "清明";
        if((month_log==5)&&(day_log==1) )
        	return "劳动";
        if((month_log==5)&&(day_log==10) )
        	return "母亲";
        if((month_log==6)&&(day_log==1) )
        	return "儿童";
        if((month_log==6)&&(day_log==21) )
        	return "父亲";
        if((month_log==7)&&(day_log==1) )
        	return "建党";
        if((month_log==8)&&(day_log==1) )
        	return "建军";
        if((month_log==9)&&(day_log==10) )
        	return "教师";
        if((month_log==10)&&(day_log==1) )
        	return "国庆";
        
        //农历
        if(((month)==1)&&day==1)
        	return "春节";
        else if(((month)==1)&&day==15)
        	return "元宵";
        else if(((month)==4)&&day==5)
        	return "清明";
        else if(((month)==5)&&day==5)
        	return "端午";
//        else if(((month)==5)&&day==22)
//        	return "小暑";
//        else if(((month)==6)&&day==8)
//        	return "大暑";
//        else if(((month)==6)&&day==24)
//        	return "立秋";
        else if(((month)==7)&&day==7)
        	return "七夕";
//        else if(((month)==7)&&day==10)
//        	return "处暑";
//        else if(((month)==7)&&day==15)
//        	return "中元";
//        else if(((month)==8)&&day==11)
//        	return "秋分";
        else if(((month)==8)&&day==15)
        	return "中秋";
//        else if(((month)==8)&&day==26)
//        	return "寒露";
        else if(((month)==9)&&day==9)
        	return "重阳";
//        else if(((month)==9)&&day==27)
//        	return "立冬";
//        else if(((month)==10)&&day==11)
//        	return "小雪";
//        else if(((month)==10)&&day==26)
//        	return "大雪";
//        else if(((month)==11)&&day==1)
//        	return "冬至";
//        else if(((month)==11)&&day==27)
//        	return "小寒";
        else if(((month)==12)&&day==8)
        	return "腊八";
//        else if(((month)==12)&&day==11)
//        	return "大寒";
        else if(((month)==12)&&day==26)
        	return "立春";
        else if(((month)==12)&&day==30)
        	return "除夕";
        //else if(day==1)
    	//	return chineseNumber[month - 1] + "月";
    	else 
    		return getChinaDayString(day);
    
    
    }

   

    public String toString() {
    	if(chineseNumber[month - 1]=="一"&&getChinaDayString(day)=="初一")
    		return "农历"+year + "年";
    	else if(getChinaDayString(day)=="初一")
    		return chineseNumber[month - 1] + "月";
    	else 
    		return getChinaDayString(day);
        //return year + "年" + (leap ? "闰" : "") + chineseNumber[month - 1] + "月" + getChinaDayString(day);
    }
    
    public static final void main(String[] args){
    	Lunar lunar = new Lunar();
    	System.out.println(lunar.getLunarDate(2015, 1, 9));
    }
    
}