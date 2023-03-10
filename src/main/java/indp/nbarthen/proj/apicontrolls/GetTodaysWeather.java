package indp.nbarthen.proj.apicontrolls;

import java.io.IOException;
import java.util.List;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

import indp.nbarthen.proj.repository.TodayReport;
import indp.nbarthen.proj.repository.WeatherReport;
import io.github.cdimascio.dotenv.Dotenv;

public class GetTodaysWeather {
	
	/*Gets the today's information from the API
	 * 	Information is stored in report and returned by the function
	 * 	locationIndex is used to access the the index inside of report.getLocations()
	 * 		-Since the city+state query returns 5 locations, 
	 *       the index is remember so we use the location selected by the user.
	 */
	public static WeatherReport todaysWeatherReport(WeatherReport report, int locationIndex) {
		
		try {
			Dotenv dotenv = Dotenv.load();
			String apiKey = dotenv.get("API_KEY");		
			
			//If OpenWeather response returned multiple locations. Access lon lat location selected by the user.
			String lon = report.getLocations().get(locationIndex).getLon();
			String lat = report.getLocations().get(locationIndex).getLat();
			String geocodingApiCallUrl = "https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid=" + apiKey + "&units=" + report.getUnits();
			
			//Make API Call / Get JSON response
			RestTemplate  weatherReportRestTemplate = new RestTemplate();
			
			String weatherReportResponse = weatherReportRestTemplate.getForObject(geocodingApiCallUrl, String.class);
			
			ObjectMapper  weatherReportMapper = new ObjectMapper();
			
			JsonNode weatherReportRoot = weatherReportMapper.readTree(weatherReportResponse.toString());
			
			TodayReport todayReport = new TodayReport();
			
			//Save Lon, Lat to report
			report.setLon(report.getLocations().get(locationIndex).getLon());
			report.setLat(report.getLocations().get(locationIndex).getLat());
			//Save weather related Data.
			todayReport.setWeatherId(weatherReportRoot.path("weather").get(0).path("id").asInt());
			todayReport.setWeatherMain(weatherReportRoot.path("weather").get(0).path("main").asText());
			todayReport.setWeatherDesc(weatherReportRoot.path("weather").get(0).path("description").asText());
			todayReport.setWeatherIconId(weatherReportRoot.path("weather").get(0).path("icon").asText());
			
			//Save temperature related Data.
			todayReport.setCurrTemp(weatherReportRoot.path("main").path("temp").asDouble());
			todayReport.setCurrFeelsLike(weatherReportRoot.path("main").path("feels_like").asDouble());
			todayReport.setCurrHumidity(weatherReportRoot.path("main").path("humidity").asInt());
			
			//Save visibility
			todayReport.setVisibility(weatherReportRoot.path("visibility").asInt());
			
			//Save wind related Data.
			todayReport.setWindSpeed(weatherReportRoot.path("wind").path("speed").asDouble());
			
			//Save cloud related Data.
			todayReport.setCloudiness(weatherReportRoot.path("clouds").path("all").asInt());
			
			//Save time related Data.
			todayReport.setTime(weatherReportRoot.path("dt").asInt());
			todayReport.setTimezone(weatherReportRoot.path("timezone").asInt());
			
			//Save sunrise/sunset related Data.
			todayReport.setSunrise(weatherReportRoot.path("sys").path("sunrise").asInt());
			todayReport.setSunset(weatherReportRoot.path("sys").path("sunset").asInt());
			//Save City
			report.setCity(weatherReportRoot.path("name").asText());
			
			
			//Returned JSON contains Rain precipitation data
			if ( weatherReportRoot.has("rain") ){
				//Data is for past hour
				if( weatherReportRoot.path("rain").has("1h") ) {
					todayReport.setDownfallType("Rain (past 1 hr):");
					todayReport.setDownfallAmount(weatherReportRoot.path("rain").path("1h").asDouble());
				}
				//Data is for past 3 hours
				else {
					todayReport.setDownfallType("Rain (past 3 hr):");
					todayReport.setDownfallAmount(weatherReportRoot.path("rain").path("3h").asDouble());
				}
			}
			//Returned JSON contains Snow precipitation data
			else if( weatherReportRoot.has("snow") ){
				//Data is for past hour
				if( weatherReportRoot.path("snow").has("1h") ) {
					todayReport.setDownfallType("Snow (past 1 hr):");
					todayReport.setDownfallAmount(weatherReportRoot.path("snow").path("1h").asDouble());
				}
				//Data is for past 3 hours
				else {
					todayReport.setDownfallType("Snow (past 3 hr):");
					todayReport.setDownfallAmount(weatherReportRoot.path("snow").path("3h").asDouble());
				}
			}
			//Returned JSON contains NO precipitation data
			else {
				todayReport.setDownfallType("No precipitation");
				todayReport.setDownfallAmount(0);
			}
			
			
			report.setToday(todayReport);
			return report;
			
		} catch (Exception e) {
			System.out.println(e);
			report.setApiError("Error reaching OpenWeather API.");
			return report;
		}
				
		
			
	}






}
