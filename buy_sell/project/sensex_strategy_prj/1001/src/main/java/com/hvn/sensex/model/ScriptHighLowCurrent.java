package com.hvn.sensex.model;

import com.hvn.sensex.utils.Day;

/**
 * This is more like a struct. Values fetched in batch from DB
 * @author hrishimacair
 *
 */
public class ScriptHighLowCurrent {
	public Day onDay;
	public Script script;
	public double high52;
//	public Day high52On;
	public double low52;
//	public Day low52On;
	public double current;

	public ScriptHighLowCurrent() {}
	
	public ScriptHighLowCurrent(Script scr, Day day, double current2, double high522, double low522) {
		this.script = scr;
		this.onDay = day;
		this.current = current2;
		this.high52 = high522;
		this.low52 = low522;
	}

	@Override
	public String toString() {
		return "ScriptHighLowCurrent [onDay=" + onDay + ", script=" + script + ", high52=" + high52 + ", low52=" + low52
				+ ", current=" + current + "]";
	}
	
	
}
