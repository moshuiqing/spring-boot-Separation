package com.home.liuhao.util.systemutil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;





/**  
 * 概要说明 : 日期转换工具类  <br>
 * 详细说明 : 日期转换工具类  <br>
 * 创建时间 : 2018年7月23日 上午11:13:00 <br>
 * @author  by liuhao  
 */
public class DateUtils
{
	
	/**
	 * 返回带有毫秒级的字符串
	 * @param d
	 * @return
	 */
	public static String backDate(Date d) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",Locale.ENGLISH);
		
		return sdf.format(d);
	}
	

    /**  
     * 概要说明 :  根据年月查询当月天数<br>
     * 详细说明 : 根据年月查询当月天数<br>
     *
     * @param yearMonth
     * @return
     * @throws ParseException  Integer 类型返回值说明
     * @see  com.jinge.portal.utils.DateUtils#getDaysOfMonth()
     * @author  by liuhao @ 2018年7月23日, 上午11:12:54 
     */
    public static Integer getDaysOfMonth(String yearMonth) throws ParseException
    {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date date = format.parse(yearMonth);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        int days = calendar.get(Calendar.DATE);
        return days;

    }

    /**  
     * 概要说明 : 日期格式化 <br>
     * 详细说明 : 日期格式化 <br>
     *
     * @param date
     * @return  String 类型返回值说明
     * @see  com.jinge.portal.utils.DateUtils#getStrDate()
     * @author  by liuhao @ 2018年7月23日, 上午11:14:02 
     */
    public static String getStrDate(Date date)
    {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String str = dateFormat.format(date);
        return str;

    }
    /**  
     * 概要说明 : 日期格式化 <br>
     * 详细说明 : 日期格式化 <br>
     *
     * @param date
     * @return  String 类型返回值说明
     * @see  com.jinge.portal.utils.DateUtils#getStrDate()
     * @author  by liuhao @ 2018年7月23日, 上午11:14:02 
     */
    public static String getStrDateTime(Date date)
    {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String str = dateFormat.format(date);
        return str;

    }

    /**  
     * 概要说明 : 获取当天时间字符串 <br>
     * 详细说明 : 获取当天时间字符串 <br>
     *
     * @return  String 类型返回值说明
     * @see  com.jinge.portal.utils.DateUtils#getNow()
     * @author  by liuhao @ 2018年9月2日, 下午7:36:09 
     */
    public static String getNow()
    {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        return dateFormat.format(date);
    }

    /**  
     * 概要说明 : 日期转换 <br>
     * 详细说明 : 日期转换 <br>
     *
     * @param date
     * @return  String 类型返回值说明
     * @see  com.jinge.portal.utils.DateUtils#getStrDate2()
     * @author  by liuhao @ 2018年7月23日, 上午11:15:07 
     */
    public static String getStrDate2(Date date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        String str = dateFormat.format(date);
        return str;
    }

    /**
     * [概要说明:]N天前的开始日期<br>
     * [详细说明:]<br>
     * @param today
     * @param num
     * @return
     * @see com.jinge.icai.common.DateUtils#daysBeforeStart()
     * @author XB
     */
    @SuppressWarnings("deprecation")
    public static String daysBeforeStart(Date today, Integer num)
    {
        today.setHours(23);
        today.setMinutes(59);
        today.setSeconds(59);
        return absolute(today, -(num));
    }

    /**
     * [概要说明:]N天前的结束日期<br>
     * [详细说明:]<br>
     * @param today
     * @return
     * @see com.jinge.icai.common.DateUtils#sevendaysBeforeStart()
     * @author XB
     */
    @SuppressWarnings("deprecation")
    public static String daysBeforeEnd(Date today, Integer num)
    {
        today.setHours(23);
        today.setMinutes(59);
        today.setSeconds(59);
        return absolute(today, -(num - 1));
    }

    @SuppressWarnings("deprecation")
    public static String daysAfterStart(Date today, Integer num)
    {
        today.setHours(00);
        today.setMinutes(00);
        today.setSeconds(00);
        return absolute(today, num);
    }

    /**
     * [概要说明:]今天的开始日期<br>
     * [详细说明:]<br>
     * @param today
     * @return
     * @see com.jinge.icai.common.DateUtils#todayStart()
     * @author LiuYongchao(11)
     */
    @SuppressWarnings("deprecation")
    public static String todayStart(Date today)
    {
        today.setHours(23);
        today.setMinutes(59);
        today.setSeconds(59);
        return absolute(today, -1);
    }

    /**
     * [概要说明:]定位日期<br>
     * [详细说明:]<br>
     * @param date
     * @param amount
     * @return
     * @see com.jinge.icai.common.DateUtils#absolute()
     * @author LiuYongchao(11)
     */
    public static String absolute(Date date, int amount)
    {
        int year = Integer.valueOf(formateDate(date, "yyyy"));
        int month = Integer.valueOf(formateDate(date, "MM"));
        int day = Integer.valueOf(formateDate(date, "dd"));
        int hour = Integer.valueOf(formateDate(date, "HH"));
        int minute = Integer.valueOf(formateDate(date, "mm"));
        int second = Integer.valueOf(formateDate(date, "ss"));

        Calendar calendar = new GregorianCalendar(year, month - 1, day, hour, minute, second);
        calendar.add(Calendar.DAY_OF_MONTH, amount);
        return formateDate(new Date(calendar.getTime().getTime()), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * [概要说明:]今天结束日期<br>
     * [详细说明:]<br>
     * @param today
     * @return
     * @see com.jinge.icai.common.DateUtils#todayEnd()
     * @author LiuYongchao(11)
     */
    @SuppressWarnings("deprecation")
    public static String todayEnd(Date today)
    {
        today.setHours(23);
        today.setMinutes(59);
        today.setSeconds(59);
        return absolute(today, 0);
    }

    /**
     * [概要说明:]日期格式化<br>
     * [详细说明:]<br>
     * @param date 日期
     * @param pattern 格式
     * @return
     * @see com.jinge.icai.common.DateUtils#formateDate()
     * @author LiuYongchao(11)
     */
    public static String formateDate(Date date, String pattern)
    {
        if (null == date)
        {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * [概要说明:]<br>
     * [详细说明:]<br>
     * @param date
     * @param pattern
     * @return
     * @see com.jinge.icai.common.DateUtils#formateDate()
     * @author LiuYongchao(11)
     */
    public static Date formateDate(String date, String pattern)
    {
        if (null == date || date.equals(""))
        {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date datetime = null;
        try
        {
            datetime = format.parse(date);
        }
        catch (ParseException e)
        {
            datetime = null;
        }
        return datetime;
    }

    /**
     * [概要说明:]<br>
     * [详细说明:]<br>
     * @param hour
     * @return
     * @see com.jinge.icai.common.DateUtils#beforeHours()
     * @author LiuYongchao(11)
     */
    public static Date beforeHours(long hour)
    {
        long currentTimestamp = System.currentTimeMillis();

        long interval = hour * 60 * 60 * 1000;
        return new Date(currentTimestamp - interval);
    }

    /**
     * [概要说明:]获取几个月之前的日期，一个月按30天计算<br>
     * [详细说明:]<br>
     * @param MonthNum 月数量
     * @return
     * @see com.jinge.icai.common.DateUtils#getDate()
     * @author hyy
     */
    public static Date getDate(String MonthNum)
    {
        long dtlong = System.currentTimeMillis();
        long intSiTime = Long.parseLong(MonthNum);
        long dd = intSiTime * 30 * 24;
        long dd1 = dd * 60 * 60;
        long dd2 = dd1 * 1000;
        long longSitime = dtlong - dd2;
        Date dt2 = new Date(longSitime);
        return dt2;
    }

    /**
     * [概要说明:]获取此时几天前的日期时间<br>
     * [详细说明:]<br>
     * @param dayNum
     * @return
     * @see com.jinge.icai.common.DateUtils#getDateByDay()
     * @author hyy
     */
    public static Date getDateByDay(String dayNum)
    {
        long dtlong = System.currentTimeMillis();
        long intSiTime = Long.parseLong(dayNum);
        long dd = intSiTime * 24;
        long dd1 = dd * 60 * 60;
        long dd2 = dd1 * 1000;
        long longSitime = dtlong - dd2;
        Date dt2 = new Date(longSitime);
        return dt2;
    }

    /**
     * [概要说明:]获取此时几小时前的日期时间<br>
     * [详细说明:]<br>
     * @param dayNum
     * @return
     * @see com.jinge.icai.common.DateUtils#getDateByDay()
     * @author hyy
     */
    public static Date getDateByHours(String HoursNum)
    {
        long dtlong = System.currentTimeMillis();
        long intSiTime = Long.parseLong(HoursNum);
        long dd = intSiTime * 60;
        long dd1 = dd * 60;
        long dd2 = dd1 * 1000;
        long longSitime = dtlong - dd2;
        Date dt2 = new Date(longSitime);
        return dt2;
    }

    /**
     * 获取指定日期是星期几，传null获取当前日期是星期几
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date)
    {
        String[] weekOfDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar calendar = Calendar.getInstance();
        if (date != null)
        {
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
        {
            w = 0;
        }
        return weekOfDays[w];
    }

    /**
     * 返回当前日期n天后的日期
     * @param n
     * @return
     */
    public static Date afterDay(int n)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, n);
        Date date = c.getTime();
        return date;
    }

    /**
     * 返回当前日期n天后的日期
     * @param n
     * @return
     */
    public static String afterDayString(int n)
    {
        Calendar c = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        c.setTime(new Date());
        c.add(Calendar.DATE, n);
        Date date = c.getTime();
        return df.format(date);
    }

    /**
     * 根据月份获取天数
     * @param year
     * @param month
     * @return
     */
    public static int getMaxDayByMonth(int year, int month)
    {
        int maxDay = 0;
        int day = 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        maxDay = calendar.getActualMaximum(Calendar.DATE);
        return maxDay;
    }

    /**
     * 
     * 概要说明 : 字符串加上特定时间后成新的字符串<br>
     * 详细说明 : 20时12分+20=20时32分<br>
     *
     * @param date
     * @param workTime
     * @return String 类型返回值说明
     * @see 类名.方法名
     * @author   by lixu @ Feb 2, 20175:36:19 PM
     */
    public static String passDateTime(String date, Integer workTime)
    {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date date2 = null;
        int x = workTime;
        try
        {
            date2 = df.parse(date);
            date2.setTime(date2.getTime() + x * 60 * 1000);
        }
        catch (ParseException e)
        {

            e.printStackTrace();

        }
        return df.format(date2);
    }

    /**  
     * 概要说明 : 返回当前年月 <br>
     * 详细说明 : 返回当前年月 <br>
     *
     * @return  String 类型返回值说明
     * @see  com.jinge.portal.utils.DateUtils#backYearMonth()
     * @author  by liuhao @ 2018年7月26日, 上午8:42:34 
     */
    public static String backYearMonth()
    {

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);

    }


    /**  
     * 概要说明 : 返回yyyy-MM-dd的格式日期 <br>
     * 详细说明 : 返回yyyy-MM-dd的格式日期 <br>
     *
     * @param date 日期
     * @return
     * @throws ParseException  Date 类型返回值说明
     * @see  com.jinge.portal.utils.DateUtils#backDate()
     * @author  by liuhao @ 2018年8月3日, 上午9:15:03 
     */
    public static Date backDate(String date) throws ParseException
    {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.parse(date);

    }

    /**  
     * 概要说明 : 返回yyyy-MM样式的日期 <br>
     * 详细说明 : 返回yyyy-MM样式的日期<br>
     *
     * @param date
     * @return
     * @throws ParseException  Date 类型返回值说明
     * @see  com.jinge.portal.utils.DateUtils#backDate2()
     * @author  by liuhao @ 2018年8月3日, 上午9:15:36 
     */
    public static Date backDate2(String date) throws ParseException
    {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        return sdf.parse(date);

    }

    /**  
     * 概要说明 : 生成编号 <br>
     * 详细说明 : 生成编号 <br>
     *
     * @param strartStr
     * @return  String 类型返回值说明
     * @see  com.jinge.portal.utils.DateUtils#backNum()
     * @author  by liuhao @ 2018年8月18日, 下午2:06:30 
     */
    public static String backNum(String strartStr)
    {
        Date date = new Date();
        int match = (int) ((Math.random() * 9 + 1) * 10);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String num = dateFormat.format(date);
        return strartStr + num + match;

    }

    /*public static void main(String[] args)
    {
        System.out.println(DateUtils.backNum(""));

    }*/

}
