package indp.nbarthen.proj.futuredata;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Vector;

import com.fasterxml.jackson.databind.JsonNode;

import indp.nbarthen.proj.repository.FutureDayReport;
import indp.nbarthen.proj.repository.TriHourlyReport;
import indp.nbarthen.proj.repository.WeatherReport;

public class GetTomorrowsReport {
	/*
	 * Gets the generic data from json (weatherReportRoot)
	 * Takes json and gets a list of tri-hourly data and stores tomorrows data from the 5 day forecast API json.
	 * 		For each 3-Hourly Data, it saves: 
	 * 			time, temp (temp, feels like, high, low), weather (id, main, desc, iconId), downfall (if available)
	*/
	public static WeatherReport tomorrowsTriHouryWeatherReport(WeatherReport report, JsonNode weatherReportRoot) {
		
		//Save generic data
		FutureDayReport tomorrow = report.getTomorrow();
		
		tomorrow.setSunrise(weatherReportRoot.path("city").get("sunrise").asInt());
		tomorrow.setSunset(weatherReportRoot.path("city").get("sunset").asInt());
		tomorrow.setCityPopulation(weatherReportRoot.path("city").get("population").asInt());
		tomorrow.setTimezone(weatherReportRoot.path("city").get("timezone").asInt());
		tomorrow.setDaysDate(1);
		
		//Array list of the tri-hourly data
		JsonNode listArray = weatherReportRoot.path("list");
		
		//Get the time for tomorrow at 12:00am and 2 days from now at 12:00 am

			// Calculate the timestamp for 12:00 am tomorrow
			LocalDateTime tomorrowMidnight = LocalDateTime.now(ZoneOffset.UTC).plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
			long tomorrowMidnightTimestamp = tomorrowMidnight.toInstant(ZoneOffset.UTC).toEpochMilli();
	
			// Calculate the timestamp for 12am 2 days from now
			LocalDateTime twoDaysFromNow12Am = LocalDateTime.now(ZoneOffset.UTC).plusDays(2).withHour(0).withMinute(0).withSecond(0).withNano(0);
			long twoDaysFromNowMidNightTimestamp = twoDaysFromNow12Am.toInstant(ZoneOffset.UTC).toEpochMilli();

		
		//List of tri-hourly report (to be stored in TomorrowReport.triHourlyReports
		 List<TriHourlyReport> triHourlyReports = new Vector<TriHourlyReport>();
		
		for (JsonNode triHourlyData : listArray) {
			//Get local time for triHourlyData instance
			long localTimeMilli = (triHourlyData.path("dt").asInt() + report.getTomorrow().getTimezone()) * 1000L;
			//If data is tomorrows data ( time >= tomorrow at midnight AND time <= midnight 2 days from now)
			if(localTimeMilli >= tomorrowMidnightTimestamp && localTimeMilli <= twoDaysFromNowMidNightTimestamp) {
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
		}
		
		
		//Save triHourlyData to TomorrowReport
		tomorrow.setTriHourlyReports(triHourlyReports);
		
		//Save TomorrowReport to report.TomorrowReport
		report.setTomorrow(tomorrow);
		
		return report;
	}
}
