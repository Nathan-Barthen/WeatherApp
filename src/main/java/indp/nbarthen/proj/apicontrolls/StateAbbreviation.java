package indp.nbarthen.proj.apicontrolls;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StateAbbreviation {
	
	//Uses OpenWeathers Geocoding API to take users ZIP OR City+State to get lon lat values needed for future API calls.
	//cityStateZip is the text entered by the user (should be ZIP or City+State)
	public static String getStateAbriv(String state) {
		
		final Map<String, String> states;
		  {
		    states = new HashMap<>();
		    states.put("Alabama", "AL");
		    states.put("Alaska", "AK");
		    states.put("Arizona", "AZ");
		    states.put("Arkansas", "AR");
		    states.put("California", "CA");
		    states.put("Colorado", "CO");
		    states.put("Connecticut", "CT");
		    states.put("Delaware", "DE");
		    states.put("Florida", "FL");
		    states.put("Georgia", "GA");
		    states.put("Hawaii", "HI");
		    states.put("Idaho", "ID");
		    states.put("Illinois", "IL");
		    states.put("Indiana", "IN");
		    states.put("Iowa", "IA");
		    states.put("Kansas", "KS");
		    states.put("Kentucky", "KY");
		    states.put("Louisiana", "LA");
		    states.put("Maine", "ME");
		    states.put("Maryland", "MD");
		    states.put("Massachusetts", "MA");
		    states.put("Michigan", "MI");
		    states.put("Minnesota", "MN");
		    states.put("Mississippi", "MS");
		    states.put("Missouri", "MO");
		    states.put("Montana", "MT");
		    states.put("Nebraska", "NE");
		    states.put("Nevada", "NV");
		    states.put("New Hampshire", "NH");
		    states.put("New Jersey", "NJ");
		    states.put("New Mexico", "NM");
		    states.put("New York", "NY");
		    states.put("North Carolina", "NC");
		    states.put("North Dakota", "ND");
		    states.put("Ohio", "OH");
		    states.put("Oklahoma", "OK");
		    states.put("Oregon", "OR");
		    states.put("Pennsylvania", "PA");
		    states.put("Rhode Island", "RI");
		    states.put("South Carolina", "SC");
		    states.put("South Dakota", "SD");
		    states.put("Tennessee", "TN");
		    states.put("Texas", "TX");
		    states.put("Utah", "UT");
		    states.put("Vermont", "VT");
		    states.put("Virginia", "VA");
		    states.put("Washington", "WA");
		    states.put("West Virginia", "WV");
		    states.put("Wisconsin", "WI");
		    states.put("Wyoming", "WY");
		  }
		  
		  //If the string for State is 2 characters. It is already abbreviated
		  if(state.length() == 2) {
			  return state;
		  }
		  //Takes the state string passed in function call and returns abbreviation using hashMap
		  return states.get(state);
		
			
	}

	/*  If the user input is more than 2 words...
	 * 		Check if the the state is one word (e.g Maine) or two words (e.g West Virginia)
	 * 		Done by checking the last two strings in the user's input. 
	 */
	public static String getState(String[] cityState) {
		String stateFirstWord = cityState[cityState.length-2];
		String stateSecondWord = cityState[cityState.length-1];
		//If the first word is a valid state's first word.
		if(stateFirstWord.equals("New") || stateFirstWord.equals("North") || stateFirstWord.equals("Rhode") || stateFirstWord.equals("South") || stateFirstWord.equals("West")) {
			return stateFirstWord + " " + stateSecondWord;
		}
		//stateFirstWord is not the beginning of the state, return only stateSecondWord (state name)
		else {
			return stateSecondWord;
		}
		
		
		
	}
	
	/*  If the user input is more than 2 words...
	 * 		Check if the the state is one word (e.g Maine) or two words (e.g West Virginia)
	 * 		Done by checking the last two strings in the user's input. 
	 * 		If the second to last string is not part of a state's name, add it to city name.
	 */
	public static String getCity(String[] cityState) {
		String stateFirstWord = cityState[cityState.length-2];
		String stateSecondWord = cityState[cityState.length-1];
		//If the first word is a valid state's first word.
		if(stateFirstWord.equals("New") || stateFirstWord.equals("North") || stateFirstWord.equals("Rhode") || stateFirstWord.equals("South") || stateFirstWord.equals("West")) {
			//City = everything except last 2 indexes (last 2 indexes = state)
			String[] allButLast = Arrays.copyOfRange(cityState, 0, cityState.length - 2);
			return String.join(" ", allButLast); 
		}
		//stateFirstWord is not the beginning of the state, return only stateSecondWord (state name)
		else {
			//City = everything except last index (last indexes = state)
			String[] allButLast = Arrays.copyOfRange(cityState, 0, cityState.length - 1);
			return String.join(" ", allButLast); 
		}
		
		
		
	}



}
