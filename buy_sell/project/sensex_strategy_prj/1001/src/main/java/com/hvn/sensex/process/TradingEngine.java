package com.hvn.sensex.process;

import java.util.List;
import java.util.concurrent.Callable;

import com.hvn.sensex.model.Portfolio;
import com.hvn.sensex.utils.Day;

public class TradingEngine implements Callable<String>{
	Day startDay, endDay;
	Portfolio portfolio;

	public TradingEngine(Portfolio portfolio, Day startDay, Day endDay) {
		super();
		this.portfolio = portfolio;
		this.startDay = startDay;
		this.endDay = endDay;
	}
	
	public TradingEngine(Portfolio portfolio) {
		this(portfolio, portfolio.getStartDay(), portfolio.getEndDay());
	}

	public void execute(TradeActivityCallback tradingActivityListener) {
		List<Day> days = startDay.getDaysTill(endDay);
		for (Day day : days) {
			DailyTrade trade = new DailyTrade(portfolio, day);
			trade.execute(tradingActivityListener);
		}		
	}

	@Override
	public String call() throws Exception {
		execute(null);
		return portfolio.getReportRow();
	}
}
