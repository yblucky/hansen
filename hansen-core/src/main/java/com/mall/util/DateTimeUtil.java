package com.mall.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateTimeUtil {
	
	public final static String PATTERN_LONG = "yyyy-MM-dd HH:mm:ss";

	public final static String PATTERN_A = "yyyyMM";
	
	public final static String PATTERN_B = "yyyyMMdd";
	
	public final static String PATTERN_C = "yyyy-MM-dd";
	
	/**
	 * 格式化日期
	 * @param date 需要格式化的 日期对象
	 * @param pattern 日期样式
	 */
	public static String formatDate( Date date, String pattern ) {
		SimpleDateFormat format = new SimpleDateFormat( pattern );
		return format.format( date );
	}

	/**
	 * 格式化日期
	 * @param day 相对当前时间的日期偏移,-1:日期向前偏移一天/1:日期向后偏移一天
	 * @param pattern 日期样式
	 */
	public static String formatDate( int day, String pattern ) {
		Calendar c = Calendar.getInstance();
		c.add( Calendar.DAY_OF_MONTH, day );
		return formatDate( c.getTime(), pattern );
	}

	/**
	 * 计算相对偏移日期午夜的秒数
	 * @param day 相对当前时间的日期偏移,-1:日期向前偏移一天/1:日期向后偏移一天
	 */
	public static int getExpiration( int day ) {
		Calendar c = Calendar.getInstance();
		long midnight = c.getTimeInMillis();
		c.set( Calendar.HOUR_OF_DAY, 0 );
		c.set( Calendar.MINUTE, 0 );
		c.set( Calendar.SECOND, 0 );
		c.set( Calendar.MILLISECOND, 0 );
		c.add( Calendar.DAY_OF_MONTH, day );
		return ( int )( ( c.getTimeInMillis() - midnight ) / 1000 );
	}
	
	/**
	 * 返回日期年月日整型，如20140318
	 */
	public static int getYearMonthDay( Calendar calendar ){
		return calendar.get( Calendar.YEAR ) * 10000 
				+ (calendar.get( Calendar.MONTH ) + 1) * 100 
				+ calendar.get( Calendar.DATE );
	}
	
	/**
	 * 获取日期的年期，如201401， addPeriodCount月份偏移量
	 */
	public static int getYearPeriod( Date date, int addPeriodCount ) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime( date );
		calendar.add( Calendar.MONTH, addPeriodCount );
		int y = calendar.get( Calendar.YEAR );
		int m = calendar.get( Calendar.MONTH ) + 1;
		return y * 100 + m;
	}
	
	
}
