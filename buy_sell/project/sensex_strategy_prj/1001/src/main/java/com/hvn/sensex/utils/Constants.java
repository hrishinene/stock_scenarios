package com.hvn.sensex.utils;

public class Constants {
	// Technical constants
	public static final String DBUrl = "jdbc:mysql://127.0.0.1:3306/DB_sensex";
	public static final String Username = "admin";
	public static final String Password = "password";
	
	// Simulation Period
	public static final String StartDay = "2013-04-01";
	public static final String EndDay = "2022-04-01";
	
	// Business Constants
	public static final int HighLowDaysRange = 15;
	public static final double BuyStoplossPercent = .5;
	public static final double SaleStoplossPercent = .5;
	public static final double DefaultAmountToInvest = 5000.0;
	public static final double MaxAmountToInvest = 50000.0;
	public static final int MinimumHoldingDays = 500;
	public static final double FractionOfCashToReinvest =  1; // between 0 to 1 
	public static final double MinimumStockPriceToBuy = 0; // don't buy cheap ones
	public static final double MaximumStockPriceToBuy = 10000; // don't buy cheap ones
	
	public static final boolean MultiHoldings = false;
}
