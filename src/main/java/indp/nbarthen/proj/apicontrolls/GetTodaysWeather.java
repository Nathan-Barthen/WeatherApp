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
			
			String geocodingApiCallUrl = "https://api.openweathermap.org/data/2.5/weather?lat="+report.getLat()+"&lon="+report.getLon()+"&appid=" + apiKey;
			
			//Make API Call / Get JSON response
			RestTemplate  weatherReportRestTemplate = new RestTemplate();
			
			String weatherReportResponse = weatherReportRestTemplate.getForObject(geocodingApiCallUrl, String.class);
			
			ObjectMapper  weatherReportMapper = new ObjectMapper();
			
			JsonNode weatherReportRoot = weatherReportMapper.readTree(weatherReportResponse.toString());
			
			System.out.println(weatherReportRoot.toString());
			
			TodayReport todayReport = new TodayReport();
			report.setCity(weatherReportRoot.path("name").path("id").asText());
			todayReport.setWeatherId(weatherReportRoot.path("weather").path("id").asInt());
			todayReport.setWeatherMain(weatherReportRoot.path("weather").path("main").asText());
			todayReport.setWeatherDesc(weatherReportRoot.path("weather").path("description").asText());
			todayReport.setWeatherIconId(weatherReportRoot.path("weather").path("icon").asText());
			
			todayReport.setCurrTemp(weatherReportRoot.path("main").path("temp").asDouble());
			todayReport.setCurrFeelsLike(weatherReportRoot.path("main").path("feels_like").asDouble());
			todayReport.setCurrHumidity(weatherReportRoot.path("main").path("humidity").asInt());
			
			todayReport.setWindSpeed(weatherReportRoot.path("wind").path("speed").asDouble());
			
			todayReport.setCloudiness(weatherReportRoot.path("clouds").path("all").asInt());
			
			todayReport.setTime(weatherReportRoot.path("dt").asInt());
			todayReport.setCurrHumidity(weatherReportRoot.path("timezone").asInt());
			
			todayReport.setCurrHumidity(weatherReportRoot.path("sys").path("sunrise").asInt());
			todayReport.setCurrHumidity(weatherReportRoot.path("sys").path("sunset").asInt());
			
			if( !weatherReportRoot.path("rain").isNull() ){
				if(!weatherReportRoot.path("rain").path("1h").isNull()) {
					todayReport.setDownfallType("Rain (past hour)");
					todayReport.setDownfallAmount(weatherReportRoot.path("rain").path("1h").asDouble());
				}
				else {
					todayReport.setDownfallType("Rain (past hour)");
					todayReport.setDownfallAmount(weatherReportRoot.path("rain").path("3h").asDouble());
				}
			}
			else if( !weatherReportRoot.path("snow").isNull() ){
				if(!weatherReportRoot.path("snow").path("1h").isNull()) {
					todayReport.setDownfallType("Snow (past hour)");
					todayReport.setDownfallAmount(weatherReportRoot.path("snow").path("1h").asDouble());
				}
				else {
					todayReport.setDownfallType("Snow (past 3 hours)");
					todayReport.setDownfallAmount(weatherReportRoot.path("snow").path("3h").asDouble());
				}
			}
			else {
				todayReport.setDownfallType("No precipitation ");
				todayReport.setDownfallAmount(0);
			}
			
			return report;
			
		} catch (Exception e) {
			System.out.println(e);
			report.setApiError("Error reaching OpenWeather API.");
			return report;
		}
				
		
			
	}






}
