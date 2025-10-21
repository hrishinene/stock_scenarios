package com.hvn.sensex.process;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hvn.sensex.db.DBConnection;
import com.hvn.sensex.model.DayValue;
import com.hvn.sensex.model.Script;
import com.hvn.sensex.model.ScriptHighLowCurrent;
import com.hvn.sensex.utils.Constants;
import com.hvn.sensex.utils.Day;
import com.hvn.sensex.utils.Pair;

public class MarketHelper {
	public static List<Script> getScripts() {
		return null; // fetch from DB
	}
	
	
	public static Map<Script, ScriptHighLowCurrent> getHighLowsCurrentMap(Day fromDay, Day today) {

		Map<Script, ScriptHighLowCurrent> dayMap = populateDayMap(today);
		if (dayMap == null || dayMap.isEmpty()) {
//			System.out.println("Market is closed on: " + day.getSQLDate());
			return null;
		}
		
		Map<Script, ScriptHighLowCurrent> yearMap =  populateYearlyHighLowMap(fromDay, today);
		if (yearMap == null || yearMap.isEmpty()) {
			System.out.println("[ERROR!] Unable to fetch yearly values on: " + today.getSQLDate());
			return null;
		}
		
		return combineMaps(dayMap, yearMap);
	
	}
	
	private static Map<Script, ScriptHighLowCurrent> combineMaps(Map<Script, ScriptHighLowCurrent> dayMap,
			Map<Script, ScriptHighLowCurrent> yearMap) {
		
		Map<Script, ScriptHighLowCurrent> retval = new HashMap<Script, ScriptHighLowCurrent>();
		for (Script scr : dayMap.keySet()) {
			ScriptHighLowCurrent dayVal = dayMap.get(scr);
			ScriptHighLowCurrent yearVal = yearMap.get(scr);
			
			retval.put(scr, new ScriptHighLowCurrent(scr, dayVal.onDay, dayVal.current, yearVal.high52, yearVal.low52));
			
		}
		
		return retval;
	}


	private static Map<Script, ScriptHighLowCurrent> populateYearlyHighLowMap(Day fromDay, Day today) {
//		Day prevDay = day.getDaysBefore(Constants.HighLowDaysRange);
//		String yearQuery1 = "SELECT Company_Name ,  MIN( BDP_Close_double), MAX(BDP_Close_double) FROM sensex_closing WHERE BDP_Date between '" + day.get52WeeksBefore().getSQLDate() + "' and '" + day.getSQLDate() + "' group by Company_Name order by  Company_Name ASC" ;;
		String yearQuery = "SELECT Company_Name ,  MIN( BDP_Close_double), MAX(BDP_Close_double) FROM sensex_closing WHERE BDP_Date between '" + fromDay.getSQLDate() + "' and '" + today.getSQLDate() + "' group by Company_Name order by  Company_Name ASC" ;;

//		System.out.println("52 week- " + yearQuery1);
//		System.out.println("365 days- " + yearQuery);
		Map<Script, ScriptHighLowCurrent> retval = new HashMap<Script, ScriptHighLowCurrent>();
		try (DBConnection connection = new DBConnection()) {
			ResultSet set = connection.executeQuery(yearQuery);
			while (set.next()) {
				ScriptHighLowCurrent highlow = new ScriptHighLowCurrent();
				int indx = 1; // 1 based index
				String scriptName = set.getString(indx++);
				if (scriptName == null || scriptName.isBlank())
					continue;
				
				highlow.script = new Script(scriptName);
				highlow.onDay = today;
				highlow.low52 = set.getDouble(indx++);
				highlow.high52 = set.getDouble(indx++);
				
				retval.put(highlow.script, highlow);
			}
			
			return retval;
		} catch (Exception e) {
			System.out.println("[ERROR!] Failed to execute SQL: " + yearQuery);
			return null;
		}
	}


	private static Map<Script, ScriptHighLowCurrent> populateDayMap(Day day) {
		String dayQuery = "SELECT Company_Name, BDP_Close_double FROM sensex_closing WHERE BDP_Date = '"+ day.getSQLDate() +"'";

		Map<Script, ScriptHighLowCurrent> retval = new HashMap<Script, ScriptHighLowCurrent>();
		try (DBConnection connection = new DBConnection()) {
			ResultSet set = connection.executeQuery(dayQuery);
			while (set.next()) {
				ScriptHighLowCurrent highlow = new ScriptHighLowCurrent();
				int indx = 1; // 1 based index
				String scriptName = set.getString(indx++);
				if (scriptName == null || scriptName.isBlank())
					continue;
				
				highlow.script = new Script(scriptName);
				highlow.current = set.getDouble(indx++);
				highlow.onDay = day;

				retval.put(highlow.script, highlow);
			}
			
			return retval;
		} catch (Exception e) {
			System.out.println("[ERROR!] Failed to execute SQL: " + dayQuery);
			return null;
		}	
	}


	public static List<ScriptHighLowCurrent> getHighLowsCurrentList(Day fromDay, Day today) {
		Map<Script, ScriptHighLowCurrent> highLowsCurrentMap =  getHighLowsCurrentMap(fromDay, today);
		
		// If market is closed, it can be null
		return (highLowsCurrentMap == null || highLowsCurrentMap.isEmpty()) ? null : new ArrayList<ScriptHighLowCurrent>(highLowsCurrentMap.values());
	}


	public static DayValue getValue(String companyName, Day day) {
		String dayQuery = "SELECT BDP_Close_double FROM sensex_closing WHERE Company_Name = '" + companyName + "' AND BDP_Date <= '"+ day.getSQLDate() +"' order by BDP_Date DESC limit 1";

		try (DBConnection connection = new DBConnection()) {
			ResultSet set = connection.executeQuery(dayQuery);
			while (set.next()) {
				double val = set.getDouble(1); // only 1 value
				return new DayValue(day, companyName, val);
			}
			
		} catch (Exception e) {
			System.out.println("[ERROR!] Failed to execute SQL: " + dayQuery);
			throw new RuntimeException(e);
		}	
		
		return new DayValue(day, companyName, -100.);
	}
	

	public static DayValue getIndexValue(Day day) {
		String query = "SELECT Close_double FROM sensex_index WHERE On_date <= '" + day.getSQLDate()
				+ "' order by On_date DESC limit 1";

		try (DBConnection connection = new DBConnection()) {
			ResultSet set = connection.executeQuery(query);
			set.next();
			double val = set.getDouble(1); // only 1 value
			return new DayValue(day, "Index", val);

		} catch (Exception e) {
			System.out.println("[ERROR!] Failed to execute SQL: " + query);
			throw new RuntimeException(e);
		}
		
	}
	
	public static Pair<DayValue, DayValue> getIndexValues(Day startDay, Day endDay) {
		return new Pair<DayValue, DayValue>(getIndexValue(startDay), getIndexValue(endDay));
	}
}
