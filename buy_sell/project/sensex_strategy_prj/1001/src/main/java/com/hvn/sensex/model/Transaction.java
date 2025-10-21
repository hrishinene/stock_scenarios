package com.hvn.sensex.model;

import com.hvn.sensex.utils.Day;

public abstract class Transaction {
	public enum TransactionType {
		Buy,
		Sell,
		None
	};
	
	Script script;
	Day itsDay;
	TransactionType type;

}
