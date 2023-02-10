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

public class GetLonLat {
	
	//Uses OpenWeathers Geocoding API to take users ZIP OR City+State to get lon lat values needed for future API calls.
	//cityStateZip is the text entered by the user (should be ZIP or City+State)
	public static WeatherReport todaysWeatherReport(WeatherReport report, String cityStateZip) {
		
		try {
			Dotenv dotenv = Dotenv.load();
			String apiKey = dotenv.get("API_KEY");		
			
			
			String countryCode = "US";
			String geocodingApiCallUrl = "";
			
			String zip = "";
			String city = "";
			String stateAbriv = "";
			
			
		  //Take user's input, calculate zip AND/OR city & state abbreviation
			
			//If string is not a ZIP (is not just numbers)
			if(!cityStateZip.matches("\\d+")) {
				String[] words = cityStateZip.split(" ");
				if (words.length == 2) {
					String state = words[1];
					stateAbriv = StateAbbreviation.getStateAbriv(state);
					city = words[0];
				}
				//If string is longer than 2 words. 
				else if( words.length > 2) {
					//Gets the state from user input
					String state = StateAbbreviation.getState(words);
					//Gets the state abbreviation from user input
					stateAbriv = StateAbbreviation.getStateAbriv(state);
					
					//Gets the city from user input
					city = StateAbbreviation.getCity(words);
				}
				//User passed one word (that is not a ZIP).
				else {
					//Return error
					report.setApiError("Error: Invalid input. Enter a ZIP or City followed by a State (e.g. 'Pittsburgh PA'");
					System.out.println(report.getApiError());
					return report;
				}
			}
			//String is only numbers (zip)
			else {
				zip = cityStateZip;
			}
			
			
		  //Get URL, Call API, get JSON response
			
			//User searched using ZIP
			if(!zip.equals("")) {
				geocodingApiCallUrl = "http://api.openweathermap.org/geo/1.0/zip?zip=" + zip + "," + countryCode + "&appid=" + apiKey;
			}
			//User searched using City+State 
			else {
				geocodingApiCallUrl = "http://api.openweathermap.org/geo/1.0/direct?q=" + city + "," + stateAbriv + "," + countryCode + "&limit=1" + "&appid=" + apiKey;
			}
			// http://api.openweathermap.org/geo/1.0/direct?q=Franklin,PA,US&limit=1&appid=ea8640d76f9d8fd822ec05c4b5bceaed
			
			//Make API Call / Get JSON response
			RestTemplate  weatherReportRestTemplate = new RestTemplate();
			
			String weatherReportResponse = weatherReportRestTemplate.getForObject(geocodingApiCallUrl, String.class);
			
			ObjectMapper  weatherReportMapper = new ObjectMapper();
			
			JsonNode weatherReportRoot = weatherReportMapper.readTree(weatherReportResponse.toString());
			
			
		  //Store information from JSON response
			if(weatherReportRoot.isEmpty()) {
				report.setApiError("Error: Invalid input. Enter a ZIP or City followed by a State (e.g. 'Pittsburgh PA')");
				return report;
			}
			
			//Zip response (response is not an array)
			if(!zip.isEmpty()) {
				report.setCity(weatherReportRoot.get("name").textValue());
				report.setLon( Double.toString(weatherReportRoot.get("lon").doubleValue()) );
				report.setLat( Double.toString(weatherReportRoot.get("lat").doubleValue()) );
				
				return report;
			}
			
			//City State response (returns array).
			report.setCity(weatherReportRoot.get(0).get("name").asText());
			report.setLon( Double.toString(weatherReportRoot.get(0).get("lon").asDouble()) );
			report.setLat( Double.toString(weatherReportRoot.get(0).get("lat").asDouble()) );
			
			return report;
			
		} catch (Exception e) {
			System.out.println(e);
			report.setApiError("Error reaching OpenWeather API.");
			return report;
		}
				
		
			
	}






}
