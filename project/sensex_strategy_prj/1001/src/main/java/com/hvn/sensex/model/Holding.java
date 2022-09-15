package com.hvn.sensex.model;

import com.hvn.sensex.process.MarketHelper;
import com.hvn.sensex.utils.Constants;
import com.hvn.sensex.utils.Day;

public class Holding {
	Script script;
	Day boughtOn;
	double numOfStocks; // can be fraction
	double costOfStocks; // whatever constant we have set - 100 rupees
	
	public Holding(Script script, Day day, double stockValue, double amount) {
		this.script = script;
		this.boughtOn = day;
		this.numOfStocks = amount/stockValue;
		this.costOfStocks = amount;
	}
	
	public double getValuation(Day day) {
		DayValue dayValue = MarketHelper.getValue(script.companyName, day);
		return getValuation(dayValue.value);
	}
	
	public double getValuation(double currentValue) {
		return numOfStocks * currentValue;
	}
	
	public double getIRR(Day today) {
		DayValue dayValue = MarketHelper.getValue(script.companyName, today);
		return getIRR(today, dayValue.value);
	}
	
	public double getIRR(Day today, double currentValue) {
		int numDays = boughtOn.getDaysDifference(today);
		double gain = getValuation(currentValue)/costOfStocks;
		
		double gainFraction = Math.pow(gain, 365.0/numDays);
		return (gainFraction  - 1.0)*100;
	}

	public boolean isMatured(Day onDay) {
		return boughtOn.getDaysDifference(onDay) > Constants.MinimumHoldingDays;
	}
}
