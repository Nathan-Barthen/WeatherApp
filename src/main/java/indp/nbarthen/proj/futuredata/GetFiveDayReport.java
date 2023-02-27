package indp.nbarthen.proj.futuredata;

import java.util.List;
import java.util.Vector;

import com.fasterxml.jackson.databind.JsonNode;

import indp.nbarthen.proj.repository.FiveDayReport;
import indp.nbarthen.proj.repository.FutureDayReport;
import indp.nbarthen.proj.repository.TriHourlyReport;
import indp.nbarthen.proj.repository.WeatherReport;

public class GetFiveDayReport {
	/* 
	 * Gets the 5 day report for the from listArray (3-Hourly Reports from OpenWeather API)
	 * 		To be stored in WeatherReport.fiveDays list 
	 * 			Each instance in list will be a day (5 days total)
	 * 				In each instance there will be generic data and triHourlyReports
	 * 
	 * 		Calls: 
	 * 
	 * 			SplitUpFiveDayData.splitIntoDays 
	 * 				-Split the list of triHourlyReports into separate days
	 * 				-listArray contains 3-hourly data for all five days. 
	 * 				-SplitUpFiveDayData.splitIntoDays breaks this data up into its own list.
	 * 			
	 * 			GetFutureDaysCalculatedData.daysSummaryData 
	 * 				-gets the calculated data for a FutureDayReport for all 5 days in the listArray.
	 * 
	 * 			GetFiveDaysCalculatedData.calc5DayGenerics
	 * 				-calculates the generic values for the FiveDayReport:
	 * 					-(avg, high, low temp), (downfallProb, snow/rain downfall).
	 */	
	public static WeatherReport fiveDayReport(WeatherReport report, JsonNode weatherReportRoot) {
			
			//Array list of the tri-hourly data
			JsonNode listArray = weatherReportRoot.path("list");
			
			//Each instance in List will contain the list of triHourlyReports for that day by calling SplitUpFiveDayData.splitIntoDays
			List<List<JsonNode>> eachDayList = SplitUpFiveDayData.splitIntoDays(listArray, report);
			
			//FutureDayReport List to be saved to report.FiveDays list
			List<FutureDayReport> fiveDays = new Vector<FutureDayReport>();
			
			int daysAhead = 1;
			//Loop through each day in eachDayList
			for (List<JsonNode> day : eachDayList) {
				//Loop through each triHourlyData in day
				FutureDayReport oneDay = new FutureDayReport();
				//List of tri-hourly report (to be stored in TomorrowReport.triHourlyReports
				List<TriHourlyReport> triHourlyReports = new Vector<TriHourlyReport>();
				
				for(JsonNode triHourlyData : day) {
				
					long localTimeMilli = (triHourlyData.path("dt").asInt() + report.getTomorrow().getTimezone()) * 1000L;
					//If data is tomorrows data ( time >= tomorrow at midnight AND time <= midnight 2 days from now)
					
					TriHourlyReport triReport = new TriHourlyReport();
					//Save 3-hourly data.
					triReport.setTime(localTimeMilli);
					//Save temp data
					triReport.setTemp(triHourlyData.path("main").get("temp").asDouble());
					triReport.setFeelsLikeTemp(triHourlyData.path("main").get("feels_like").asDouble());
					triReport.setHumidity(triHourlyData.path("main").get("humidity").asInt());
					//Save weather data.
					triReport.setWeatherId(triHourlyData.path("weather").get(0).get("id").asInt());
					triReport.setWeatherMain(triHourlyData.path("weather").get(0).get("main").asText());
					triReport.setWeatherDesc(triHourlyData.path("weather").get(0).get("description").asText());
					triReport.setWeatherIconId(triHourlyData.path("weather").get(0).get("icon").asText());
					//Check for precipitation data and save data if it exists
					triReport.setDownfallProbability(triHourlyData.get("pop").asDouble()*100);
					//Returned JSON contains Rain precipitation data
					if ( triHourlyData.has("rain") ){
						//Data is for past hour
						if( triHourlyData.path("rain").has("1h") ) {
							triReport.setDownfallType("Rain (past 1 hr):");
							triReport.setDownfallTotalAmount(triHourlyData.path("rain").path("1h").asDouble());
						}
						//Data is for past 3 hours
						else {
							triReport.setDownfallType("Rain (past 3 hr):");
							triReport.setDownfallTotalAmount(triHourlyData.path("rain").path("3h").asDouble());
						}
					}
					//Returned JSON contains Snow precipitation data
					else if( triHourlyData.has("snow") ){
						//Data is for past hour
						if( triHourlyData.path("snow").has("1h") ) {
							triReport.setDownfallType("Snow (past 1 hr):");
							triReport.setDownfallTotalAmount(triHourlyData.path("snow").path("1h").asDouble());
						}
						//Data is for past 3 hours
						else {
							triReport.setDownfallType("Snow (past 3 hr):");
							triReport.setDownfallTotalAmount(triHourlyData.path("snow").path("3h").asDouble());
						}
					}
					//Returned JSON contains NO precipitation data
					else {
						triReport.setDownfallType("No precipitation");
						triReport.setDownfallTotalAmount(0);
					}
					
					triHourlyReports.add(triReport);
						
				}	
				//Add the list of triHourlyReports to the oneDays list
				oneDay.setTriHourlyReports(triHourlyReports);
				//Add Generic data to oneDay
				oneDay.setTimezone(report.getTomorrow().getTimezone());
				oneDay.setCityPopulation(report.getTomorrow().getCityPopulation());
				oneDay.setDaysDate(daysAhead);
				
				//Save/Calc the day's summary data by calling GetFutureDaysCalculatedData.daysSummaryData
				oneDay = GetFutureDaysCalculatedData.daysSummaryData(oneDay);
				//Add that day to the list of 5-Days
				fiveDays.add(oneDay);
				
				daysAhead++;
			}
			
			
			//Create FiveDayReport to be saved to report.FiveDatReport
			FiveDayReport fiveDayReport = new FiveDayReport();
			fiveDayReport.setFiveDays(fiveDays);
			fiveDayReport = GetFiveDaysCalculatedData.calc5DayGenerics(fiveDayReport);
			//Save the 5-DayReport to report.FiveDays
			report.setFiveDayReport(fiveDayReport);
		
			return report;
		
	}
}
