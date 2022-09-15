package com.hvn.sensex.utils;

/**
 * @author hrishi
 *
 */
public class Pair<FIRST, SECOND> {
	FIRST first = null;
	SECOND second = null;
	/**
	 * @param first
	 * @param second
	 */
	public Pair() {}
	
	public Pair(FIRST first, SECOND second) {
		this.first = first;
		this.second = second;
	}
	public FIRST getFirst() {
		return first;
	}
	public SECOND getSecond() {
		return second;
	}

	public void setFirst(FIRST first) {
		this.first = first;
	}

	public void setSecond(SECOND second) {
		this.second = second;
	}
	
	
}
