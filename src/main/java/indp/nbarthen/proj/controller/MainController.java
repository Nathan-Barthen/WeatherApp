package indp.nbarthen.proj.controller;


import indp.nbarthen.proj.apicontrolls.*;
import indp.nbarthen.proj.repository.WeatherReport;
import indp.nbarthen.proj.repository.WeatherRepository;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.util.*;


import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ibm.icu.text.SimpleDateFormat;



@Controller
public class MainController {
	private WeatherRepository weatherRepository;
	private String userSearchPopupError;
	
	public MainController(WeatherRepository  weatherRepository) {
		this.weatherRepository = weatherRepository;
		this.userSearchPopupError = "none";
	}
	
	
	
	
	
	 @RequestMapping({"/"})
	    public String homePage(Model model) {
		 
		 	//Get current time & date
		 	SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
		 	SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
		 	Date today = new Date();
		 	Date date = new Date();
		 	
		 	
		 	//User was redirected to home from API call error.
		 	if(!userSearchPopupError.equals("none")) {
		 		String popupError = userSearchPopupError;
		 		userSearchPopupError = "none";
		 		//Display error message
		 		model.addAttribute("popupError", popupError);
		 		model.addAttribute("currentDate", dateFormat.format(today));
			 	model.addAttribute("currentTime", timeFormat.format(date).toLowerCase());
		 		return "homePage";
		 	}
		 	
		 	//Default value for userSearchPopupError is 'none' (no error will popup)
		 	model.addAttribute("popupError", userSearchPopupError);
		 	model.addAttribute("currentTime", timeFormat.format(date).toLowerCase());
		 	model.addAttribute("currentDate", dateFormat.format(today));
		 	
		 	
	        return "homePage";
	    }
	 
	 @RequestMapping({"weatherReport/getLocation/{userInput}"})
	    public String getInputLocation(@PathVariable("userInput") String userInput, Model model) throws JsonMappingException, JsonProcessingException {
		 	WeatherReport report = new WeatherReport();
		 	
		 	//User passed a ZIP
		 	if(HandleUserInput.checkForZip(userInput)) {
		 		report = GetLonLat.todaysWeatherReportUsingZip(report, userInput);
			 	
			 	//If API call returned an error
			 	if(!report.getApiError().isEmpty()) {
			 		//Get API error message and return to homePage.html
			 		userSearchPopupError = report.getApiError();
			 		return "redirect:/";
			 	}
			 	
			 	weatherRepository.save(report);
			 	return "redirect:/weatherReport/" + report.getId() + "/" + ((report.getLocations().size())-1) + "/" + report.getCity() + "/today";
		 	}
		 	
		 	
		 	//City+State was entered
		 	else {
		 		//Gets basic info using Geocoder API to make future API calls (lon, lat)
			 	report = GetLonLat.todaysWeatherReportUsingCityState(report, userInput);
			 	
			 	//If API call returned an error
			 	if(!report.getApiError().isEmpty()) {
			 		//Get API error message and return to homePage.html
			 		userSearchPopupError = report.getApiError();
			 		return "redirect:/";
			 	}
			 	
			 	weatherRepository.save(report);
			 	
			 	return "redirect:/weatherReport/" + report.getId() + "/chooseLocation";
		 	}	 	
	    }
	 
	 
	 
	 @RequestMapping({"weatherReport/{id}/chooseLocation"})
	    public String chooseCorrectLocation(@PathVariable("id") String id, Model model) throws JsonMappingException, JsonProcessingException {
		 	 WeatherReport report = weatherRepository.findById(id).get();
			 model.addAttribute("report", report);
		 	
	        return "chooseCorrectLocation";
	    }
	 
	 @RequestMapping({"weatherReport/{id}/{locationIndex}/{location}/today"})
	    public String getTodaysReport(@PathVariable("id") String id, @PathVariable("locationIndex") String locationIndex, @PathVariable("location") String location, Model model) throws JsonMappingException, JsonProcessingException {
		 	 WeatherReport report = weatherRepository.findById(id).get();
		 	 
		 	 //Get apiKey
		 	Dotenv dotenv = Dotenv.load();
			String apiKey = dotenv.get("API_KEY");	
		 	//Get current time & date
			 SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
			 SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
			 Date today = new Date();
			 Date date = new Date();
			 
			//Gets the current weather Info
			 report = GetTodaysWeather.todaysWeatherReport(report, Integer.parseInt(locationIndex));
			 	
			 model.addAttribute("report", report);
			 model.addAttribute("locationIndex", locationIndex);
			 model.addAttribute("apiKey", apiKey);
			 model.addAttribute("today", report.getToday());
			 model.addAttribute("currentTime", timeFormat.format(date).toLowerCase());
			 model.addAttribute("currentDate", dateFormat.format(today));
		 	
	        return "weatherScreen-Today";
	    }

	 	
	 @RequestMapping({"weatherReport/location/tomorrow"})
	    public String getTomorrowsReport(Model model) throws JsonMappingException, JsonProcessingException {
		 	
		 	
	        return "weatherScreen-Tomorrow";
	    }
	 @RequestMapping({"weatherReport/location/5-DayForecast"})
	    public String get5DayReport(Model model) throws JsonMappingException, JsonProcessingException {
		 	
		 	
	        return "weatherScreen-5DayForecast";
	    }
	 
	 
	
	 
	 	
	 
}
