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
import indp.nbarthen.proj.repository.TomorrowReport;
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
			TomorrowReport tomorrow = report.getTomorrow();
			tomorrow.setSunrise(weatherReportRoot.path("city").get("sunrise").asInt());
			tomorrow.setSunset(weatherReportRoot.path("city").get("sunset").asInt());
			tomorrow.setCityPopulation(weatherReportRoot.path("city").get("population").asInt());
			tomorrow.setTimezone(weatherReportRoot.path("city").get("timezone").asInt());
			tomorrow.setTomorrowsDate();
			
			//Array list of the tri-hourly data
			JsonNode listArray = weatherReportRoot.path("list");

			//Get and save tomorrow's triHourlyReport data
			report = tomorrowsTriHouryWeatherReport(report, listArray);
			//Save/Calc tomorrow's summary data
			report = tomorrowsSummaryData(report);
			
			
			//Save weather related Data.
			//todayReport.setWeatherId(weatherReportRoot.path("weather").get(0).path("id").asInt());
			
			
			
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
		
		//Get the time for tomorrow at 12:00am and 3:00 am
		// Get the current UTC timestamp in milliseconds
		long currentTimestamp = System.currentTimeMillis();

		// Calculate the timestamp for 12:00 am tomorrow
		LocalDateTime tomorrowMidnight = LocalDateTime.now(ZoneOffset.UTC).plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
		long tomorrowMidnightTimestamp = tomorrowMidnight.toInstant(ZoneOffset.UTC).toEpochMilli();

		// Calculate the timestamp for 2am 2 days from now
			//Since the api returns 3 hour windows. The tomorrow data may go over to 2am the following day
		LocalDateTime twoDaysFromNow2Am = LocalDateTime.now(ZoneOffset.UTC).plusDays(2).withHour(2).withMinute(0).withSecond(0).withNano(0);
		long twoDaysFromNow2AmTimestamp = twoDaysFromNow2Am.toInstant(ZoneOffset.UTC).toEpochMilli();

		
		//List of tri-hourly report (to be stored in TomorrowReport.triHourlyReports
		 List<TriHourlyReport> triHourlyReports = new Vector<TriHourlyReport>();
		
		for (JsonNode triHourlyData : listArray) {
			//Get local time for triHourlyData instance
			long localTimeMilli = (triHourlyData.path("dt").asInt() + report.getTomorrow().getTimezone()) * 1000L;
			//If data is tomorrows data ( time > tomorrow at midnight AND time < 2am the following day )
			if(localTimeMilli >= tomorrowMidnightTimestamp && localTimeMilli <= twoDaysFromNow2AmTimestamp) {
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
		TomorrowReport tomorrow = report.getTomorrow();
		tomorrow.setTriHourlyReports(triHourlyReports);
		
		//Save TomorrowReport to report.TomorrowReport
		report.setTomorrow(tomorrow);
		
		return report;
	}

	
	
	
	
	//Calculates / Saves: temp (avg, high, low), avg weather (id, main, desc, iconId), downfall totals (if they exist)
	public static WeatherReport tomorrowsSummaryData(WeatherReport report) {
		TomorrowReport tomorrow = report.getTomorrow();
		
		double avgTemp = 0;     	//Calculated from 3-Hourly Values
		
		double totalRain = 0;
		double totalSnow = 0;
		
		double highestDownfallProb = 0;
		
		// Initialize a map to keep track of the count for each weatherMain value
		Map<String, Integer> weatherMainCount = new HashMap<>();

		// Initialize a map to keep track of the attributes for each weatherMain value
		Map<String, TriHourlyReport> weatherMainAttributes = new HashMap<>();
		
		for(TriHourlyReport triHourReport : tomorrow.getTriHourlyReports()) {
			
			//Save temp data
			avgTemp += triHourReport.getTemp();
			
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
		tomorrow.setAvgWeatherId(mostCommonTriHourReport.getWeatherId());
		tomorrow.setAvgWeatherMain(mostCommonTriHourReport.getWeatherMain());
		tomorrow.setAvgWeatherDesc(mostCommonTriHourReport.getWeatherDesc());
		tomorrow.setAvgWeatherIconId(mostCommonTriHourReport.getWeatherIconId());
		
		
	   //Save calculated Data
		//Get mean of temps.
		avgTemp = avgTemp / tomorrow.getTriHourlyReports().size();
		//Save temps
		tomorrow.setAvgTemp(avgTemp);
		
		//Save downfall probability (may be 0)
		tomorrow.setDownfallProbability(highestDownfallProb);
		//Save downfall (vales may be 0)
		tomorrow.setDownfallRainAmount(totalRain);
		tomorrow.setDownfallSnowAmount(totalSnow);
		
		
		//Save tomorrow report.
		report.setTomorrow(tomorrow);
		
		return report;
	}



}
