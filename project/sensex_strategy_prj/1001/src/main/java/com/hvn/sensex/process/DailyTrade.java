package com.hvn.sensex.process;

import java.util.List;

import com.hvn.sensex.model.Portfolio;
import com.hvn.sensex.model.ScriptHighLowCurrent;
import com.hvn.sensex.utils.Day;
import com.hvn.sensex.utils.Utils;

public class DailyTrade {
	Day day;
	Portfolio portfolio;
	
	public DailyTrade(Portfolio portfolio, Day day) {
		this.portfolio = portfolio;
		this.day = day;
	}
	
	void execute(TradeActivityCallback tradingActivityListener) {
		// Get all min-max
		List<ScriptHighLowCurrent> highlows = MarketHelper.getHighLowsCurrentList(portfolio.getStartTimeRange(day), day);
		
		if (highlows == null || highlows.isEmpty()) {
			Utils.logDebug("Market is closed on: " + day);
			return;
		}
		// For each high low, get today's price.
		for (ScriptHighLowCurrent scriptHighLow : highlows) {
				portfolio.handleScriptValuation(day, scriptHighLow, tradingActivityListener);
		}		
	}

}
