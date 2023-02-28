package indp.nbarthen.proj.futuredata;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Vector;

import com.fasterxml.jackson.databind.JsonNode;

import indp.nbarthen.proj.repository.WeatherReport;

public class SplitUpFiveDayData {
	/*
	 * Takes the 5 day listArray of triHourlyReports and split it into separate days
	 * 		Each instance in List will contain the list of triHourlyReports for each day.
	 */
	public static List<List<JsonNode>> splitIntoDays(JsonNode listArray, WeatherReport report) {
			
			List<List<JsonNode>> eachDayList = new Vector<List<JsonNode>>();
			List<JsonNode> oneDay = new Vector<JsonNode>();
			
			// Calculate the timestamp for 12:00 am tomorrow
			LocalDateTime tomorrowMidnight = LocalDateTime.now(ZoneOffset.UTC).plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
			long tomorrowMidnightTimestamp = tomorrowMidnight.toInstant(ZoneOffset.UTC).toEpochMilli();
						
			
			int daysAhead = 2;
			for (JsonNode triHourlyData : listArray) {
					//Get the next days Date (timestamp for 12am for the next day)
					LocalDateTime oneDayAheadFromCurrInstance = LocalDateTime.now(ZoneOffset.UTC).plusDays(daysAhead).withHour(0).withMinute(0).withSecond(0).withNano(0);
					long oneDayAheadFromCurrInstanceTimestamp = oneDayAheadFromCurrInstance.toInstant(ZoneOffset.UTC).toEpochMilli();
							
					//Get local time for triHourlyData instance
					long localTimeMilli = (triHourlyData.path("dt").asInt() + report.getToday().getTimezone()) * 1000L;
					
					//If data is from today (skip/do nothing)
					if(localTimeMilli <= tomorrowMidnightTimestamp) {
						//Skip / Do nothing
					}
					//If data is from current Days instance( time >= instance at midnight AND time <= midnight 2 days from instance)
					else if(localTimeMilli >= tomorrowMidnightTimestamp && localTimeMilli <= oneDayAheadFromCurrInstanceTimestamp) {
						oneDay.add(triHourlyData);
					}
					//Current Day is over (triHourlyData iteration is onto the next day)
					else {
						//Save data to eachDayList
						eachDayList.add(oneDay);
						oneDay = new Vector<JsonNode>();
						//Add current instance to next day
						oneDay.add(triHourlyData);
						//Move to next day in 5 day list
						daysAhead++;
					}
			}
		
		//Add the last day
		eachDayList.add(oneDay);
		
		return eachDayList;
	}
}
