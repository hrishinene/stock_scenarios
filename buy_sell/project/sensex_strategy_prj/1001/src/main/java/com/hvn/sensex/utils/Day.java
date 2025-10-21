package com.hvn.sensex.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Transaction day
 * @author hrishimacair
 *
 */
public class Day {
	public static final long DayMiliseconds = (24 * 3600 * 1000);
	public static final long YearMiliseconds = (365L * DayMiliseconds);
	
	Date date;
	
	public Day(String yyyy_mm_dd) {
		Utils.parseDate(yyyy_mm_dd);
	}
	
	public Day(Date dt) {
		this.date = dt;
	}
	
	public Day getNextDay() {
		return new Day(new Date(date.getTime() + DayMiliseconds));
	}
	
	public Day getPreviousDay() {
		return new Day(new Date(date.getTime() - DayMiliseconds));
	}
	
	public Day get52WeeksBefore() {
		return new Day(new Date(date.getTime() - YearMiliseconds));
	}	
	
	public Day get52WeeksAfter() {
		return new Day(new Date(date.getTime() + YearMiliseconds));
	}
	
	public boolean isAfter(Day other) {
		return date.after(other.date);
		
	}
	
	public boolean isBefore(Day other) {
		return date.before(other.date);
	}
	
	public boolean isSame(Day other) {
		return date.equals(other.date);
	}
	
	
	public List<Day> getDaysTill(Day endDay) {
		List<Day> retval = new ArrayList<Day>();
		
		Day day = this;
		while (!day.isAfter(endDay)) {
			retval.add(day);
			day = day.getNextDay();
		}

		return retval;
	}

	public String getSQLDate() {
		return Utils.getYYYY_MM_DD(date);
	}

	@Override
	public String toString() {
		return "Day [date=" + getSQLDate() + "]";
	}

	public int getDaysDifference(Day onDay) {
		return (int) (Math.abs(onDay.date.getTime() - date.getTime())/DayMiliseconds);
	}

	public Day getDaysBefore(int numOfDays) {
//		return get52WeeksBefore();
		return new Day(new Date(date.getTime() - (numOfDays*DayMiliseconds)));
	}
}
