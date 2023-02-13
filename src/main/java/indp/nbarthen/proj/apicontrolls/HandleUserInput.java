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

public class HandleUserInput {
	
	//Check to see if user's input was a zip
	public static Boolean checkForZip(String cityStateZip) {

			String zip;
			//If string is not a ZIP (is not just numbers)
			if(!cityStateZip.matches("\\d+")) {
				return false;
			}
			//String is only numbers (zip)
			else {
				return true;
			}
	
				
		
			
	}

	public static String[] getCityAndStateAbriv(String cityStateZip) {

		
		String city = "";
		String stateAbriv = "";
		
		//Array: index 1 = city. index 2 = stateAbriv
		String[] cityState = new String[2];
		
	  //Take user's input, calculate zip AND/OR city & state abbreviation
			String[] words = cityStateZip.split(" ");
			if (words.length == 2) {
				String state = words[1];
				stateAbriv = StateAbbreviation.getStateAbriv(state);
				city = words[0];
				cityState[0] = city;
				cityState[1] = stateAbriv;
				return cityState;
			}
			//If string is longer than 2 words. 
			else if( words.length > 2) {
				//Gets the state from user input
				String state = StateAbbreviation.getState(words);
				//Gets the state abbreviation from user input
				stateAbriv = StateAbbreviation.getStateAbriv(state);
				
				//Gets the city from user input
				city = StateAbbreviation.getCity(words);
				cityState[0] = city;
				cityState[1] = stateAbriv;
				return cityState;
			}
			//User passed one word (that is not a ZIP).
			else {
				//Return error
				cityState[0] = "Error: Invalid input. Enter a ZIP or City followed by a State (e.g. 'Pittsburgh PA')";
				cityState[1] = "";
				return cityState;
			}
		

			
	
		
}





}
