package indp.nbarthen.proj.apicontrolls;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

import indp.nbarthen.proj.repository.TodayReport;
import indp.nbarthen.proj.repository.FiveDayReport;
import indp.nbarthen.proj.repository.FutureDayReport;
import indp.nbarthen.proj.repository.TriHourlyReport;
import indp.nbarthen.proj.repository.WeatherReport;
import io.github.cdimascio.dotenv.Dotenv;

public class GetFutureWeather {
	
	/*Gets the today's information from the API
	 * 	Information is stored in report and returned by the function
	 * 	locationIndex is used to access the the index inside if report.getLocations()
	 * 		-Since the city+state query returns 5 locations, 
	 *       the index is remember so we use the location selected by the user.
	 */
	
	//Saves generic data for tomorrow: (sunrise, sunset, population, timezone, date)
	//Calls tomorrowsTriHouryWeatherReport function and tomorrowsSummaryData function.
	public static WeatherReport futureWeatherReport(WeatherReport report, int locationIndex) {
		
		try {
			Dotenv dotenv = Dotenv.load();
			String apiKey = dotenv.get("API_KEY");		
			
			//If OpenWeather response returned multiple locations. Access lon lat location selected by the user.
			String lon = report.getLocations().get(locationIndex).getLon();
			String lat = report.getLocations().get(locationIndex).getLat();
			String geocodingApiCallUrl = "https://api.openweathermap.org/data/2.5/forecast?lat="+lat+"&lon="+lon+"&appid=" + apiKey + "&units=" + report.getUnits();
			
			//Make API Call / Get JSON response
			RestTemplate  weatherReportRestTemplate = new RestTemplate();
			
			String weatherReportResponse = weatherReportRestTemplate.getForObject(geocodingApiCallUrl, String.class);
			
			ObjectMapper  weatherReportMapper = new ObjectMapper();
			
			JsonNode weatherReportRoot = weatherReportMapper.readTree(weatherReportResponse.toString());

			
			//Save generic data
			FutureDayReport tomorrow = report.getTomorrow();
			tomorrow.setSunrise(weatherReportRoot.path("city").get("sunrise").asInt());
			tomorrow.setSunset(weatherReportRoot.path("city").get("sunset").asInt());
			tomorrow.setCityPopulation(weatherReportRoot.path("city").get("population").asInt());
			tomorrow.setTimezone(weatherReportRoot.path("city").get("timezone").asInt());
			tomorrow.setDaysDate(1);
			
			//Array list of the tri-hourly data
			JsonNode listArray = weatherReportRoot.path("list");

			//Get and save tomorrow's triHourlyReport data
			report = tomorrowsTriHouryWeatherReport(report, listArray);
			//Save/Calc tomorrow's summary data
			FutureDayReport tmrw = report.getTomorrow();
			tmrw = daysSummaryData(tmrw);
			report.setTomorrow(tmrw);
			
			//Gets data for the 5 Day Report
			report = fiveDayReport(report, listArray);
			
			
			//Save weather related Data.
			
			
			
			
			return report;
			
		} catch (Exception e) {
			System.out.println(e);
			report.setApiError("Error reaching OpenWeather API.");
			return report;
		}
				
		
			
	}


	
	//Takes the list of tri-hourly data and stores tomorrows data from the 5 day forecast API json.
	//For each 3-Hourly Data, it saves: time, temp (temp, feels like, high, low), weather (id, main, desc, iconId), downfall (if available)
	public static WeatherReport tomorrowsTriHouryWeatherReport(WeatherReport report, JsonNode listArray) {
		
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
		FutureDayReport tomorrow = report.getTomorrow();
		tomorrow.setTriHourlyReports(triHourlyReports);
		
		//Save TomorrowReport to report.TomorrowReport
		report.setTomorrow(tomorrow);
		
		return report;
	}

	
	
	
	
	//Calculates / Saves: temp (avg, high, low), avg weather (id, main, desc, iconId), downfall totals (if they exist)
	public static FutureDayReport daysSummaryData(FutureDayReport futureDay) {
		FutureDayReport day = futureDay;
		
		double avgTemp = 0;     	
		double lowTemp = day.getTriHourlyReports().get(0).getTemp();
		double highTemp = day.getTriHourlyReports().get(0).getTemp();
		
		double totalRain = 0;
		double totalSnow = 0;
		
		double highestDownfallProb = 0;
		
		// Initialize a map to keep track of the count for each weatherMain value
		Map<String, Integer> weatherMainCount = new HashMap<>();

		// Initialize a map to keep track of the attributes for each weatherMain value
		Map<String, TriHourlyReport> weatherMainAttributes = new HashMap<>();
		
		for(TriHourlyReport triHourReport : day.getTriHourlyReports()) {
			
			//Save temp data
			avgTemp += triHourReport.getTemp();
			
			
			//Check for new high & low temp + save value
			if(triHourReport.getTemp() > highTemp) {
				highTemp = triHourReport.getTemp();
			}
			else if(triHourReport.getTemp() < lowTemp) {
				lowTemp = triHourReport.getTemp();
			}
			
			
			//Save weatherMain data (ex. 'Rain') to a hashmap to find most common.
			String weatherMain = triHourReport.getWeatherMain();

			// Update the count for the current weatherMain value
			int count = weatherMainCount.getOrDefault(weatherMain, 0);
			weatherMainCount.put(weatherMain, count + 1);

			// Update the attributes for the current weatherMain value
			if (!weatherMainAttributes.containsKey(weatherMain)) {
			    weatherMainAttributes.put(weatherMain, triHourReport);
			}
			
			//Save highest downfall probability
			if( triHourReport.getDownfallProbability() > highestDownfallProb) {
				highestDownfallProb = triHourReport.getDownfallProbability();
			}
			//Save downfall amounts (if they exist)
			if(triHourReport.getDownfallType().contains("Rain")) {
				totalRain += triHourReport.getDownfallTotalAmount();
			}
			else if(triHourReport.getDownfallType().contains("Snow")) {
				totalSnow += triHourReport.getDownfallTotalAmount();
			}
			
		}
		
		
		
		// Find the most common weatherMain value + save additional information (desc, iconId, id)
		String mostCommonWeatherMain = null;
		int maxCount = 0;
		for (Map.Entry<String, Integer> entry : weatherMainCount.entrySet()) {
		    if (entry.getValue() > maxCount) {
		        maxCount = entry.getValue();
		        mostCommonWeatherMain = entry.getKey();
		    }
		}

		// Get the attributes for the most common weatherMain value
		TriHourlyReport mostCommonTriHourReport = weatherMainAttributes.get(mostCommonWeatherMain);
		day.setAvgWeatherId(mostCommonTriHourReport.getWeatherId());
		day.setAvgWeatherMain(mostCommonTriHourReport.getWeatherMain());
		day.setAvgWeatherDesc(mostCommonTriHourReport.getWeatherDesc());
		day.setAvgWeatherIconId(mostCommonTriHourReport.getWeatherIconId());
		
		
	   //Save calculated Data
		//Get mean of temps.
		avgTemp = avgTemp / day.getTriHourlyReports().size();
		//Save temps
		day.setAvgTemp(avgTemp);
		day.setLowTemp(lowTemp);
		day.setHighTemp(highTemp);
		
		//Save downfall probability (may be 0)
		day.setDownfallProbability(highestDownfallProb);
		//Save downfall (vales may be 0)
		day.setDownfallRainAmount(totalRain);
		day.setDownfallSnowAmount(totalSnow);
		
		
		
		
		return day;
	}

	
	
	
	
	/* 
	 * Gets the 5 day report for the from listArray (3-Hourly Reports from OpenWeather API)
	 * 		To be stored in WeatherReport.fiveDays list 
	 * 			Each instance in list will be a day (5 days total)
	 * 				In each instance there will be generic data and triHourlyReports
	 */
	public static WeatherReport fiveDayReport(WeatherReport report, JsonNode listArray) {
				
			//Each instance in List will contain the list of triHourlyReports for that day.
			List<List<JsonNode>> eachDayList = splitIntoDays(listArray, report);
			
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
				oneDay.setDaysDate(daysAhead);
				
				//Save/Calc the day's summary data
				oneDay = daysSummaryData(oneDay);
				//Add that day to the list of 5-Days
				fiveDays.add(oneDay);
				
				daysAhead++;
			}
			
			//Create FiveDayReport to be saved to report.FiveDatReport
			FiveDayReport fiveDayReport = new FiveDayReport();
			fiveDayReport.setFiveDays(fiveDays);
			fiveDayReport = calc5DayGenerics(fiveDayReport);
			
			//Save the 5-DayReport to report.FiveDays
			report.setFiveDayReport(fiveDayReport);
		
			return report;
		
	}

	
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
					long localTimeMilli = (triHourlyData.path("dt").asInt() + report.getTomorrow().getTimezone()) * 1000L;
					
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
						System.out.println("---Else-NewDay----");
						oneDay.add(triHourlyData);
						//Move to next day in 5 day list
						daysAhead++;
					}
					TriHourlyReport rep = new TriHourlyReport();
					rep.setTime(localTimeMilli);
					System.out.println(rep.getTimeWindow());
			}
		
		//Add the last day
		eachDayList.add(oneDay);
		
		return eachDayList;
	}
	
	
	
	/*
	 *  Calculated the generic values for the FiveDayReport (avg, high, low temp). 
	 */
	public static FiveDayReport calc5DayGenerics(FiveDayReport fiveDaysReport) {
		double avgTemp = 0;     		
		double lowTemp = fiveDaysReport.getFiveDays().get(0).getLowTemp();     		
		double highTemp = fiveDaysReport.getFiveDays().get(0).getHighTemp();     		    
		
		for(FutureDayReport day : fiveDaysReport.getFiveDays()) {
			avgTemp += day.getAvgTemp();
			if(day.getLowTemp() < lowTemp) {
				lowTemp = day.getLowTemp();
			}
			if(day.getHighTemp() > highTemp) {
				highTemp = day.getHighTemp();
			}
		}
		avgTemp = avgTemp / fiveDaysReport.getFiveDays().size();
		
		fiveDaysReport.setAvgTemp(avgTemp);
		fiveDaysReport.setLowTemp(lowTemp);
		fiveDaysReport.setHighTemp(highTemp);
		
		
		return fiveDaysReport;
	}
}
