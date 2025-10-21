package com.hvn.sensex.model;

import com.hvn.sensex.model.Transaction.TransactionType;
import com.hvn.sensex.utils.Constants;

/**
 * Don't immediately buy or sell Stock.
 * Wait till the tide changes
 * @author hrishimacair
 *
 */
public class TransactionIntent {
	TransactionType type;

	Script script;
	double stopLossValue; // based on type, sale/Buy based on this value
	
	public TransactionIntent(Script script, TransactionType type, double value) {
		this.script = script;
		this.type = type;
		
		stopLossValue = type.equals(TransactionType.Buy) ? adjustStopLoss(value, Constants.BuyStoplossPercent, true) : adjustStopLoss(value, Constants.SaleStoplossPercent, false);
		// TODO Auto-generated constructor stub
	}

	private double adjustStopLoss(double value, double buystoplosspercent, boolean more) {
		double offset = value * buystoplosspercent/100;
		return more ? value + offset : value - offset;
	}

	/**
	 * Based on current Intent, either adjust intent, or give sale/buy signal
	 * @param scriptHighLow
	 * @return
	 */
	public TransactionType handleScriptValuation(ScriptHighLowCurrent scriptHighLow) {
		double value = scriptHighLow.current;
		
		if (type.equals(TransactionType.Buy) && value > stopLossValue)
			return TransactionType.Buy;

		if (type.equals(TransactionType.Sell) && value < stopLossValue)
			return TransactionType.Sell;
		
		// Adjust stoploss
		double newStopLossValue = type.equals(TransactionType.Buy) ? adjustStopLoss(value, Constants.BuyStoplossPercent, true) : adjustStopLoss(value, Constants.SaleStoplossPercent, false);

		// readjust stoploss value, if it is has moved in favorable directly
		stopLossValue = type.equals(TransactionType.Buy) ? Math.min(stopLossValue, newStopLossValue) : Math.max(stopLossValue, newStopLossValue);
			
		return TransactionType.None;
	}

}
