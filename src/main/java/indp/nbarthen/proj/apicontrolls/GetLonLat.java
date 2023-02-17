package indp.nbarthen.proj.apicontrolls;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

import indp.nbarthen.proj.repository.Location;
import indp.nbarthen.proj.repository.TodayReport;
import indp.nbarthen.proj.repository.WeatherReport;
import io.github.cdimascio.dotenv.Dotenv;

public class GetLonLat {
	
	//Uses OpenWeathers Geocoding API to take users ZIP to get lon lat values needed for future API calls.
	//cityStateZip is the text entered by the user (ZIP)
	public static WeatherReport todaysWeatherReportUsingZip(WeatherReport report, String cityStateZip) {
		
		try {
			Dotenv dotenv = Dotenv.load();
			String apiKey = dotenv.get("API_KEY");		
			
			
			String countryCode = "US";
			String geocodingApiCallUrl = "";
			
			String zip = cityStateZip;
			
		
		  //Get URL, Call API, get JSON response
			
			//User searched using ZIP
			geocodingApiCallUrl = "http://api.openweathermap.org/geo/1.0/zip?zip=" + zip + "," + countryCode + "&appid=" + apiKey;
	
			//Make API Call / Get JSON response
			RestTemplate  weatherReportRestTemplate = new RestTemplate();
			
			String weatherReportResponse = weatherReportRestTemplate.getForObject(geocodingApiCallUrl, String.class);
			
			ObjectMapper  weatherReportMapper = new ObjectMapper();
			
			JsonNode weatherReportRoot = weatherReportMapper.readTree(weatherReportResponse.toString());
			
			
		   //Store information from JSON response
			//Incorrect / invalid zip. Return error
			if(weatherReportRoot.isEmpty()) {
				report.setApiError("Error: Invalid input. Enter a valid ZIP or City followed by a State (e.g. 'Pittsburgh PA')");
				return report;
			}
			
			//Zip response (response is not an array) (Exact location)
			report.setCity(weatherReportRoot.get("name").textValue());
			report.setLon( Double.toString(weatherReportRoot.get("lon").doubleValue()) );
			report.setLat( Double.toString(weatherReportRoot.get("lat").doubleValue()) );
				
			//Also store data in Locations vector
			List<Location> locations = new Vector<Location>();
			Location location = new Location();
			location.setCity(weatherReportRoot.get("name").textValue());
			location.setLon( Double.toString(weatherReportRoot.get("lon").doubleValue()) );
			location.setLat( Double.toString(weatherReportRoot.get("lat").doubleValue()) );
			
			locations.add(location);
			report.setLocations(locations);
			
			return report;

			
		} catch (Exception e) {
			System.out.println(e);
			report.setApiError("Error reaching OpenWeather API.");
			return report;
		}
				
		
			
	}

public static WeatherReport todaysWeatherReportUsingCityState(WeatherReport report, String cityStateZip) {
		
		try {
			Dotenv dotenv = Dotenv.load();
			String apiKey = dotenv.get("API_KEY");		
			
			
			String countryCode = "US";
			String geocodingApiCallUrl = "";
			

			 //Take user's input, calculate city & state abbreviation
			//getCityAndStateAbriv will return an array with two values. index 1 = city. index 2 = stateAbriv.
			String[] cityState = HandleUserInput.getCityAndStateAbriv(cityStateZip);
			String city = cityState[0];
			String stateAbriv = cityState[1];
			
			
			//User passed one word (that is not a ZIP).
			if (city.contains("Error")) {
				//Return error (city contains error message)
				report.setApiError(city);
				System.out.println(report.getApiError());
				return report;
			}
			
			
		  //Get URL, Call API, get JSON response
			geocodingApiCallUrl = "http://api.openweathermap.org/geo/1.0/direct?q=" + city + "," + stateAbriv + "," + countryCode + "&limit=5" + "&appid=" + apiKey;
		
			
			
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

			
			List<Location> locations = new Vector<Location>();
			//City State response (returns array).
			for(int i=0; i<weatherReportRoot.size(); i++) {
				//If the current index in the returned json is in the correct state
				if( StateAbbreviation.getStateAbriv(weatherReportRoot.get(i).get("state").asText()).equals(stateAbriv) ) {
					Location location = new Location();
					location.setCity(weatherReportRoot.get(i).get("name").asText());
					location.setLon( Double.toString(weatherReportRoot.get(i).get("lon").asDouble()) );
					location.setLat( Double.toString(weatherReportRoot.get(i).get("lat").asDouble()) );
					location.setStateAbriv(stateAbriv);
					
					locations.add(location);
				}
			}
			report.setLocations(locations);
			return report;
			
		} catch (Exception e) {
			System.out.println(e);
			report.setApiError("Error reaching OpenWeather API.");
			return report;
		}
				
		
			
	}




}
