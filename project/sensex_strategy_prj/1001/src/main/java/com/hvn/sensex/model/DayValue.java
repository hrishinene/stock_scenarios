package com.hvn.sensex.model;

import com.hvn.sensex.utils.Day;

public class DayValue {
	public Day day;
	public Script script;
	public double value;
	public DayValue(Day day, String script, double value) {
		super();
		this.day = day;
		this.script = new Script(script);
		this.value = value;
	}
	@Override
	public String toString() {
		return "DayValue [day=" + day + ", script=" + script + ", value=" + value + "]";
	}
	
	public static int calculateCAGR(DayValue first, DayValue second) {
		int numOfDays = first.day.getDaysDifference(second.day);
		double years = numOfDays/365.;
		
		double valueGrowth = second.value/first.value;
		
		double yearlyCAGR = Math.pow(valueGrowth, 1./years);
		
		return (int) ((yearlyCAGR - 1) * 100);				
	}
	
	
}
