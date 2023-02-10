package indp.nbarthen.proj.controller;


import indp.nbarthen.proj.apicontrolls.*;
import indp.nbarthen.proj.repository.WeatherReport;
import indp.nbarthen.proj.repository.WeatherRepository;

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
		 
		 SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
		 	Date today = new Date();
		 	Date date = new Date();
		 	SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
		 	
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
		 	
		 	
		 	model.addAttribute("currentDate", dateFormat.format(today));
		 	model.addAttribute("currentTime", timeFormat.format(date).toLowerCase());
		 	
	        return "homePage";
	    }
	 
	 @RequestMapping({"weatherReport/getLocation/{userInput}"})
	    public String getInputLocation(@PathVariable("userInput") String userInput, Model model) throws JsonMappingException, JsonProcessingException {
		 	WeatherReport report = new WeatherReport();
		 	
		 	//Gets basic info using Geocoder API to make future API calls (lon, lat)
		 	report = GetLonLat.todaysWeatherReport(report, userInput);
		 	//Gets the current weather Info
		 	report = GetTodaysWeather.todaysWeatherReport(report);
		 	
		 	//If API call returned an error
		 	if(!report.getApiError().isEmpty()) {
		 		//Get API error message and return to homePage.html
		 		userSearchPopupError = report.getApiError();
		 		return "redirect:/";
		 	}
		 	
		 	weatherRepository.save(report);
		 	return "redirect:/weatherReport/" + report.getId() + "/" + report.getCity() + "/today";
	    }
	 
	 @RequestMapping({"weatherReport/{id}/{location}/today"})
	    public String getTodaysReport(@PathVariable("id") String id, @PathVariable("location") String location, Model model) throws JsonMappingException, JsonProcessingException {
		 	 WeatherReport report = weatherRepository.findById(id).get();
		 	 
		 	 
		 	 
		 	
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
