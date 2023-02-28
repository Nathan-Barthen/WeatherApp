package indp.nbarthen.proj.futuredata;

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
	
	/*Gets the future information from the API
	 * 	Information is stored in report and returned by the function
	 * 	locationIndex is used to access the the index inside of report.getLocations()
	 * 		-Since the city+state query returns 5 locations, 
	 *        the index is remembered/passed so we use the location selected by the user.
	 */
	
	//Makes the API call to OpenWeather to get the 5-Day forecast information used for Tomorrow and 5Day page.
	
	/*
	 * Calls additional functions to calculate and store data for tomorrow and 5Day-Forecast.
	 * Calls:
	 * 		
	 * 		GetFutureDaysCalculatedData.daysSummaryData - gets the calculated data for a FutureDayReport.
	 * 			-Used for tomorrows report and the 5 days in the 5-Day Forecast (FiveDayReport.fiveDays).
	 * 			-First index in FiveDayReport.fiveDays will be tomorrow's data.
	 */
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

			

						
			//Gets data for the 5 Day Report (first index will be Tomorrows Information)
			report = GetFiveDayReport.fiveDayReport(report, weatherReportRoot);
			
			
			return report;
			
		} catch (Exception e) {
			System.out.println(e);
			report.setApiError("Error reaching OpenWeather API.");
			return report;
		}
	
	}
	
}
	
	
	
