package com.hvn.sensex.test;


import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

import com.hvn.sensex.model.Portfolio;
import com.hvn.sensex.model.PortfolioConfig;
import com.hvn.sensex.model.ScenarioBuilder;
import com.hvn.sensex.model.Script;
import com.hvn.sensex.model.Transaction;
import com.hvn.sensex.process.TradeActivityCallback;
import com.hvn.sensex.process.TradingEngine;
import com.hvn.sensex.utils.Day;

public class TradingTest {

	
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

	
	@Test
	public void testTrading() {
		String fileName = "/tmp/scenario.csv";

		PrintWriter printWriter = null;
		FileWriter fileWriter = null;
		
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		List<Future<String>> futures = new ArrayList<Future<String>>();

		List<PortfolioConfig> configs = ScenarioBuilder.getConfigs();

		
		try {
			fileWriter = new FileWriter(fileName);
			printWriter = new PrintWriter(fileWriter);

			System.out.println(Portfolio.getHeaderRow());
			printWriter.write(Portfolio.getHeaderRow() + "\n");

			for (PortfolioConfig config : configs) {
				Portfolio portfolio = new Portfolio(config);

				TradingEngine engine = new TradingEngine(portfolio);

				futures.add(executorService.submit(engine));

//			engine.execute(new TradingActivityListener());

//			System.out.println(portfolio.getReportRow()); // later put that in file

			}

			for (Future<String> future : futures) {
				try {
					String message = future.get();
					System.out.println(message);
					printWriter.write(message + "\n");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			executorService.shutdown();

		    if (printWriter !=null)
		    	printWriter.close();
		}
	}
}
