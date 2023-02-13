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
	
	
	public static WeatherReport todaysWeatherReport(WeatherReport report) {
		
		try {
			Dotenv dotenv = Dotenv.load();
			String apiKey = dotenv.get("API_KEY");		
			
			String geocodingApiCallUrl = "https://api.openweathermap.org/data/2.5/weather?lat="+report.getLat()+"&lon="+report.getLon()+"&appid=" + apiKey + "&units=" + report.getUnits();
			
			//Make API Call / Get JSON response
			RestTemplate  weatherReportRestTemplate = new RestTemplate();
			
			String weatherReportResponse = weatherReportRestTemplate.getForObject(geocodingApiCallUrl, String.class);
			
			ObjectMapper  weatherReportMapper = new ObjectMapper();
			
			JsonNode weatherReportRoot = weatherReportMapper.readTree(weatherReportResponse.toString());
			
			System.out.println(weatherReportRoot.toString());
			TodayReport todayReport = new TodayReport();
			
			
			//Save weather related Data.
			todayReport.setWeatherId(weatherReportRoot.path("weather").get(0).path("id").asInt());
			todayReport.setWeatherMain(weatherReportRoot.path("weather").get(0).path("main").asText());
			todayReport.setWeatherDesc(weatherReportRoot.path("weather").get(0).path("description").asText());
			todayReport.setWeatherIconId(weatherReportRoot.path("weather").get(0).path("icon").asText());
			
			//Save temperature related Data.
			todayReport.setCurrTemp(weatherReportRoot.path("main").path("temp").asDouble());
			todayReport.setCurrFeelsLike(weatherReportRoot.path("main").path("feels_like").asDouble());
			todayReport.setCurrHumidity(weatherReportRoot.path("main").path("humidity").asInt());
			
			//Save wind related Data.
			todayReport.setWindSpeed(weatherReportRoot.path("wind").path("speed").asDouble());
			
			//Save cloud related Data.
			todayReport.setCloudiness(weatherReportRoot.path("clouds").path("all").asInt());
			
			//Save time related Data.
			todayReport.setTime(weatherReportRoot.path("dt").asInt());
			todayReport.setTimezone(weatherReportRoot.path("timezone").asInt());
			
			////Save sunrise/sunset related Data.
			todayReport.setSunrise(weatherReportRoot.path("sys").path("sunrise").asInt());
			todayReport.setSunset(weatherReportRoot.path("sys").path("sunset").asInt());
			
			//Returned JSON contains Rain precipitation data
			if( !weatherReportRoot.path("rain").isEmpty() ){
				//Data is for past hour
				if(!weatherReportRoot.path("rain").path("1h").isEmpty()) {
					todayReport.setDownfallType("Rain (past hour)");
					todayReport.setDownfallAmount(weatherReportRoot.path("rain").path("1h").asDouble());
				}
				//Data is for past 3 hours
				else {
					todayReport.setDownfallType("Rain (past hour)");
					todayReport.setDownfallAmount(weatherReportRoot.path("rain").path("3h").asDouble());
				}
			}
			//Returned JSON contains Snow precipitation data
			else if( !weatherReportRoot.path("snow").isEmpty() ){
				//Data is for past hour
				if(!weatherReportRoot.path("snow").path("1h").isEmpty()) {
					todayReport.setDownfallType("Snow (past hour):");
					todayReport.setDownfallAmount(weatherReportRoot.path("snow").path("1h").asDouble());
				}
				//Data is for past 3 hours
				else {
					todayReport.setDownfallType("Snow (past 3 hours):");
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
