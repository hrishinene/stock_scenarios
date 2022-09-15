package com.hvn.sensex.model;

import com.hvn.sensex.utils.Constants;

public class PortfolioConfig {
	String name;

	// default values (from Constants)
	String StartDay = "2013-04-01";
	String EndDay = "2022-04-01";

	int HighLowDaysRange = Constants.HighLowDaysRange; // range of days to check for local maxima/minima
	
	double BuyStoplossPercent = Constants.BuyStoplossPercent; // when to buy after reaching the bottom
	double SaleStoplossPercent = Constants.SaleStoplossPercent; // when to buy after reaching the peak
	
	double DefaultAmountToInvest = Constants.DefaultAmountToInvest; // Amount to invest at a time from liquid
	double MaxAmountToInvest = Constants.MaxAmountToInvest; // Max amount to invest from the sold stock cash
	
	int MinimumHoldingDays = Constants.MinimumHoldingDays; // Do not sell before this many days
	
	double FractionOfCashToReinvest = Constants.FractionOfCashToReinvest; // between 0 to 1
	
	double MinimumStockPriceToBuy = Constants.MinimumStockPriceToBuy; // don't buy stocks cheaper than this
	double MaximumStockPriceToBuy = Constants.MaximumStockPriceToBuy; // don't buy stocks costlier than this
	
	boolean MultiHoldings = Constants.MultiHoldings; // Buy same stock multiple times before selling

	private PortfolioConfig(String name) {
		this.name = name;
	}

	public static PortfolioConfig create(String name) {
		return new PortfolioConfig(name);
	}

	
	public PortfolioConfig setStartDay(String startDay) {
		StartDay = startDay;
		return this;
	}

	public PortfolioConfig setEndDay(String endDay) {
		EndDay = endDay;
		return this;
	}

	public PortfolioConfig setHighLowDaysRange(int highLowDaysRange) {
		HighLowDaysRange = highLowDaysRange;
		return this;
	}

	public PortfolioConfig setBuyStoplossPercent(double buyStoplossPercent) {
		BuyStoplossPercent = buyStoplossPercent;
		return this;
	}

	public PortfolioConfig setSaleStoplossPercent(double saleStoplossPercent) {
		SaleStoplossPercent = saleStoplossPercent;
		return this;
	}

	public PortfolioConfig setDefaultAmountToInvest(double defaultAmountToInvest) {
		DefaultAmountToInvest = defaultAmountToInvest;
		return this;
	}

	public PortfolioConfig setMaxAmountToInvest(double maxAmountToInvest) {
		MaxAmountToInvest = maxAmountToInvest;
		return this;
	}

	public PortfolioConfig setMinimumHoldingDays(int minimumHoldingDays) {
		MinimumHoldingDays = minimumHoldingDays;
		return this;
	}

	public PortfolioConfig setFractionOfCashToReinvest(double fractionOfCashToReinvest) {
		FractionOfCashToReinvest = fractionOfCashToReinvest;
		return this;
	}

	public PortfolioConfig setMinimumStockPriceToBuy(double minimumStockPriceToBuy) {
		MinimumStockPriceToBuy = minimumStockPriceToBuy;
		return this;
	}

	public PortfolioConfig setMaximumStockPriceToBuy(double maximumStockPriceToBuy) {
		MaximumStockPriceToBuy = maximumStockPriceToBuy;
		return this;
	}

	public PortfolioConfig setMultiHoldings(boolean multiHoldings) {
		MultiHoldings = multiHoldings;
		return this;
	}

}
