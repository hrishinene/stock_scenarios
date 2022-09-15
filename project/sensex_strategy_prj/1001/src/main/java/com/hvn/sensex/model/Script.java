package com.hvn.sensex.model;

import com.hvn.sensex.utils.Day;

public class Script {
	String companyName;
//	String companyCode;
	
	
	public Script(String companyName) {
		this.companyName = companyName;
	}
	
	public double getValueOn(Day day) {
		return 0.0;
	}
	
	public double get52WeekMax(Day day) {
		return 0.0;
	}
	

	public double get52WeekMin(Day day) {
		return 0.0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Script other = (Script) obj;
		if (companyName == null) {
			if (other.companyName != null)
				return false;
		} else if (!companyName.equals(other.companyName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Script [companyName=" + companyName + "]";
	}
}
