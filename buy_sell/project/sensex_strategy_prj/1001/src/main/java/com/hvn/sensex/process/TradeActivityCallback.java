package com.hvn.sensex.process;

import com.hvn.sensex.model.Script;
import com.hvn.sensex.model.Transaction;
import com.hvn.sensex.utils.Day;

public interface TradeActivityCallback {
	public void onTransaction(Transaction txn);
	public void onDayProcessed(Day day);
	public void onYearlyHighLow(Script script);
}
