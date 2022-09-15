package com.hvn.sensex.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hvn.sensex.model.Transaction.TransactionType;
import com.hvn.sensex.process.MarketHelper;
import com.hvn.sensex.process.TradeActivityCallback;
import com.hvn.sensex.utils.Day;
import com.hvn.sensex.utils.Utils;

public class Portfolio {
	public static class InvestmentEvent {
		double amount;
		Day day;
		
		public InvestmentEvent(double amount, Day day) {
			this.amount = amount;
			this.day = day;
		}
		
		public double getCompoundValue(double interestRate, Day onDay) {
			int days = day.getDaysDifference(onDay);
			double years = days/365.0;
			
			return amount * Math.pow(interestRate, years);
		}
		
		public static double getCompoundValue(List<InvestmentEvent> investments, double interestRate, Day onDay) {
			double compoundValue = 0.0;
			for (InvestmentEvent investmentEvent : investments) {
				compoundValue += investmentEvent.getCompoundValue(interestRate, onDay);
			}
			
			return compoundValue;
		}
		
		public static double calculateIRR(List<InvestmentEvent> investments, Day onDay, double value, double minIRR, double maxIRR, double increment) {
			double irr = minIRR;
			while (irr < maxIRR) {
				double compoundValue = getCompoundValue(investments, irr, onDay);
				if (compoundValue > value)
					return (irr - increment/2.);
				
				irr += increment;
			}
			
			return irr;
		}
	}
	
	PortfolioConfig config;
	
	Map<Script, TransactionIntent> intentions = new HashMap<Script, TransactionIntent>();
	
	double cash = 0;
	double investment = 0;
	double earn = 0;
	int buyTransactionCount = 0;
	int sellTransactionCount = 0;
	Map<Script, List<Holding>> holdings = new HashMap<Script, List<Holding>>();
//	List<Transaction> transactions = new ArrayList<Transaction>();
	List<InvestmentEvent> investments = new ArrayList<InvestmentEvent>();
	
	public Portfolio(PortfolioConfig config) {
		this.config = config;
	}
	
	Map<Script, List<Holding>> getHoldings() {
		return holdings;
	}
	
	double getLiquidCash() {
		return cash;
	}

	double getAmountSpent() {
		return investment;
	}
	
	private List<Holding> collectHoldings() {
		List<Holding> retval = new ArrayList<Holding>();
		for (Script script : holdings.keySet()) {
			List<Holding> scriptHoldings = holdings.get(script);
			retval.addAll(scriptHoldings);
		}
		
		return retval;
	}
	
	double getValuation(Day day) {
		return getValuation(collectHoldings(), day) + cash;
	}

	private static double getValuation(Collection<Holding> holdings, Day day) {
		double value = 0;
		for (Holding holding : holdings) {
			value += holding.getValuation(day);
		}
		
		return value;
	}

	public String report() {
//		buff.append("Investment = " + this.investment).append("\n");
//		buff.append("Earnings = " + this.earn).append("\n");
//		buff.append("Cash in hand = " + this.cash).append("\n");
//		buff.append("Holdings Value = " + getValuation(holdings.values(), day)).append("\n");
		
		double totalInvestment = investment;
		double totalHolding = getValuation(collectHoldings(), getEndDay());
		double totalValuation = totalHolding + cash;
		double irr = InvestmentEvent.calculateIRR(investments, getEndDay(), totalValuation, 1.0, 2.0, 0.005);

		int indexCAGR = DayValue.calculateCAGR(MarketHelper.getIndexValue(Utils.parseDay(config.StartDay)), MarketHelper.getIndexValue(Utils.parseDay(config.EndDay)));
		
		StringBuffer buff = new StringBuffer("\n====\n");
		buff.append("Portfolio Report: " + config.name);
		buff.append("\n====\n");
		buff.append("Total Investment = ").append((int)totalInvestment).append("\n");
		buff.append("Total Cash = ").append((int) cash).append("\n");
		buff.append("Total Holding = ").append((int) totalHolding).append("\n");
		buff.append("Total Valuation = ").append((int) totalValuation).append("\n");
		buff.append("Portfolio CAGR = ").append((int)((irr - 1)*100)).append("%").append("\n");
		buff.append("Index CAGR = ").append(indexCAGR).append("%").append("\n");

		return buff.toString();
	}

	public void handleScriptValuation(Day today, ScriptHighLowCurrent scriptHighLow,
			TradeActivityCallback tradingActivityListener) {
		TransactionIntent intent = intentions.get(scriptHighLow.script);
		if (intent != null) {
			TransactionType txnType = intent.handleScriptValuation(scriptHighLow);
			if (txnType.equals(TransactionType.Buy))
				buy(today, scriptHighLow);
			else if(txnType.equals(TransactionType.Sell))
				sell(today, scriptHighLow);

			return;
		}
		
		// Set intent 
		TransactionIntent newIntent = analyseHighLow(today, scriptHighLow);
		if (newIntent != null)
			intentions.put(scriptHighLow.script, newIntent);
	}

	/**
	 * Check if today the script is at High or Low, if so, set intention
	 * @param today
	 * @param scriptHighLow
	 * @return
	 */
	private TransactionIntent analyseHighLow(Day today, ScriptHighLowCurrent scriptHighLow) {
		// to buy, previous holding should not exist
		List<Holding> scriptHoldings = holdings.get(scriptHighLow.script);
		
		if (scriptHighLow.low52 == scriptHighLow.current) {// do epsilon check later
			return (config.MultiHoldings || Utils.isNullOrEmpty(scriptHoldings))  ? new TransactionIntent(scriptHighLow.script, TransactionType.Buy, scriptHighLow.current) : null;
		}
		
		// To sell, matured holding must exist
		if (Utils.isNullOrEmpty(scriptHoldings))
			return null;
		
		// if holding, check the price
		if (scriptHighLow.high52 == scriptHighLow.current) // do epsilon check later
			return new TransactionIntent(scriptHighLow.script, TransactionType.Sell, scriptHighLow.current);
		
		// No intent
		return null;
	}

	/**
	 * Actually sell the script - dissolve holding and put the cash into portfolio
	 * Remove intent
	 * @param today
	 * @param scriptHighLow
	 */
	private void sell(Day today, ScriptHighLowCurrent scriptHighLow) {
		Script script = scriptHighLow.script;
		List<Holding> scriptHoldings = holdings.get(script);
		if (Utils.isNullOrEmpty(scriptHoldings)) {
			Utils.logWarn("No holding to sell - " + script);
			return;
		}
		
		List<Holding> toRemove = new ArrayList<Holding>();
		for (Holding holding : scriptHoldings) {
			if (!holding.isMatured(today))
				continue;

			this.earn += holding.getValuation(scriptHighLow.current);
			this.cash += holding.getValuation(scriptHighLow.current);
			this.sellTransactionCount++;
			
			toRemove.add(holding);
			Utils.logDebug(config.name + ",[SALE]," + script.companyName + "," + today.getSQLDate() + "," + (int) cash + "," + (int) scriptHighLow.current + "," + (int) holding.getValuation(scriptHighLow.current) + "," + (int) holding.numOfStocks + "," + Utils.round(holding.getIRR(today, scriptHighLow.current), 2));
		}
		
		scriptHoldings.removeAll(toRemove);
		if (Utils.isNullOrEmpty(scriptHoldings))
			holdings.remove(script);
		else
			holdings.put(script, scriptHoldings);
		
		intentions.remove(script);
	}

	/**
	 * Actually Buy the script, investment money, put it into Holdings and remove intent
	 * @param today
	 * @param scriptHighLow
	 */
	private void buy(Day today, ScriptHighLowCurrent scriptHighLow) {
		Script script = scriptHighLow.script;

		List<Holding> scriptHoldings = holdings.get(script);
		if (!Utils.isNullOrEmpty(scriptHoldings) && config.MultiHoldings == false) {
			Utils.logWarn("Already have a holding, can't buy - " + script);
			return;
		}
		
		// How much of cash to reinvest - more the better - but can't buy stocks in fraction
		double defaultAmount = config.DefaultAmountToInvest;
		double maxAmount = config.MaxAmountToInvest;
		double stockPrice = scriptHighLow.current;
		double fractionReinvest = Math.min(1.0, config.FractionOfCashToReinvest);
		double availableCash = cash * fractionReinvest;
		double minimumStockPrice = config.MinimumStockPriceToBuy;
		double maximumStockPrice = config.MaximumStockPriceToBuy;
		
		if (stockPrice < minimumStockPrice || stockPrice > maximumStockPrice)
			return;
		
		double amount = decideAmount(stockPrice, availableCash, defaultAmount, maxAmount);
		if (cash > amount) {
			cash -= amount;
		} else {
			investment += amount;
			// Add to Amount invested
			investments.add(new InvestmentEvent(amount, today));
		}
		
		Holding newHolding = new Holding(script, today, stockPrice, amount);
		if (scriptHoldings == null)
			scriptHoldings = new ArrayList<Holding>();
		
		scriptHoldings.add(newHolding);
		holdings.put(script, scriptHoldings);
		this.buyTransactionCount++;

		intentions.remove(script);
		Utils.logDebug(config.name + ",[BUY]," +  script.companyName + "," + today.getSQLDate() + "," +(int) cash + "," + (int)stockPrice +"," + (int) amount + "," + (int)(amount/stockPrice));
	}

	/**
	 * Decide Amount based on stock price.
	 * If stock price is less than cash in hand, invest from cash
	 * If stock price is greater than cash in hand, invest fresh
	 * @param stockPrice
	 * @param availableCash
	 * @param defaultAmount
	 * @return
	 */
	private double decideAmount(double stockPrice, double availableCash, double defaultAmount, double maxAmount) {
		// Don't invest more than default amount
		availableCash = Math.min(availableCash, maxAmount);
		
		// Enough cash to buy at least 1 stock
		int cashStocks = (int) (availableCash/stockPrice);
		if (cashStocks > 0)
			return stockPrice * cashStocks;
		
		// Buy as much as - if not at least 1 share.
		int investStocks = (int) (defaultAmount/stockPrice);
		return (investStocks > 0) ?	(stockPrice * investStocks)	: stockPrice;
	}

	public Day getStartDay() {
		// TODO Auto-generated method stub
		return Utils.parseDay(config.StartDay);
	}
	
	public Day getEndDay() {
		// TODO Auto-generated method stub
		return Utils.parseDay(config.EndDay);
	}

	public static String getHeaderRow() {
		StringBuffer buf = new StringBuffer("Name");

		// Config values
		buf.append(",StartDay");
		buf.append(",EndDay");

		buf.append(",HighLowDaysRange");

		buf.append(",BuyStoplossPercent");
		buf.append(",SaleStoplossPercent");

		buf.append(",DefaultAmountToInvest");
		buf.append(",MaxAmountToInvest");

		buf.append(",MinimumHoldingDays");

		buf.append(",FractionOfCashToReinvest");

		buf.append(",MinimumStockPriceToBuy");
		buf.append(",MaximumStockPriceToBuy");

		buf.append(",MultiHoldings");

		// processed Values
		buf.append(",Total Investment");
		buf.append(",Total Cash");
		buf.append(",Total Holding");
		buf.append(",Total Valuation");
		buf.append(",BuyTransactions");
		buf.append(",SaleTransactions");
		buf.append(",MoneyDrawdowns");
		buf.append(",Portfolio CAGR");
		buf.append(",Index CAGR");

		return buf.toString();
	}
	
	public String getReportRow() {
		StringBuffer buf = new StringBuffer(config.name);

		// Config values
		buf.append(",").append(config.StartDay);
		buf.append(",").append(config.EndDay);

		buf.append(",").append(config.HighLowDaysRange);

		buf.append(",").append(config.BuyStoplossPercent);
		buf.append(",").append(config.SaleStoplossPercent);

		buf.append(",").append(config.DefaultAmountToInvest);
		buf.append(",").append(config.MaxAmountToInvest);

		buf.append(",").append(config.MinimumHoldingDays);

		buf.append(",").append(config.FractionOfCashToReinvest);

		buf.append(",").append(config.MinimumStockPriceToBuy);
		buf.append(",").append(config.MaximumStockPriceToBuy);

		buf.append(",").append(config.MultiHoldings);
		
		
		double totalInvestment = investment;
		double totalHolding = getValuation(collectHoldings(), getEndDay());
		double totalValuation = totalHolding + cash;
		double irr = InvestmentEvent.calculateIRR(investments, getEndDay(), totalValuation, 1.0, 2.0, 0.005);
		int cagr = (int)((irr - 1)*100);
		int indexCAGR = DayValue.calculateCAGR(MarketHelper.getIndexValue(Utils.parseDay(config.StartDay)), MarketHelper.getIndexValue(Utils.parseDay(config.EndDay)));
		


		// processed Values
		buf.append(",").append((int)totalInvestment);
		buf.append(",").append((int)cash);
		buf.append(",").append((int)totalHolding);
		buf.append(",").append((int)totalValuation);
		buf.append(",").append((int)buyTransactionCount);
		buf.append(",").append((int)sellTransactionCount);
		buf.append(",").append((int)investments.size());
		buf.append(",").append(cagr);
		buf.append(",").append(indexCAGR);

		return buf.toString();
	}

	// This is 52 week minima or 60 days minima kind
	public Day getStartTimeRange(Day day) {
		return day.getDaysBefore(config.HighLowDaysRange);
	}

}
