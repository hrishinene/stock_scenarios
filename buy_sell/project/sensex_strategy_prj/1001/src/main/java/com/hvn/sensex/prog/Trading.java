package com.hvn.sensex.prog;

import com.hvn.sensex.model.Portfolio;
import com.hvn.sensex.model.PortfolioConfig;
import com.hvn.sensex.model.Script;
import com.hvn.sensex.model.Transaction;
import com.hvn.sensex.process.TradeActivityCallback;
import com.hvn.sensex.process.TradingEngine;
import com.hvn.sensex.utils.Day;
import com.hvn.sensex.utils.Utils;

public class Trading {
	
	static class TradingActivityListener implements TradeActivityCallback {

		public void onTransaction(Transaction txn) {
			// TODO Auto-generated method stub
			
		}

		public void onDayProcessed(Day day) {
			// TODO Auto-generated method stub
			
		}

		public void onYearlyHighLow(Script script) {
			// TODO Auto-generated method stub
			
		}
		
	}

	public static void main(String[] args) {
		Portfolio portfolio = new Portfolio(PortfolioConfig.create("Dummy"));
		
		TradingEngine engine = new TradingEngine(portfolio, Utils.parseDay("2014-04-01"), Utils.parseDay("2022-04-01"));
		
		engine.execute(new TradingActivityListener());
	}

}
