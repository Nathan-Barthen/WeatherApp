package indp.nbarthen.proj.controller;


import indp.nbarthen.proj.apicontrolls.*;
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



@Controller
public class MainController {
	private WeatherRepository weatherRepository;
	
	public MainController(WeatherRepository  weatherRepository) {
		this.weatherRepository = weatherRepository;
	}
	
	 @RequestMapping({"/"})
	    public String homePage(Model model) throws JsonMappingException, JsonProcessingException {
		 	
		 	
		//change back to "homePage"
	        return "weatherScreen-Today";
	    }
	 
	 @RequestMapping({"weatherReport/{location}/today"})
	    public String getTodaysReport(@PathVariable("location") String location, @RequestParam(value = "matchType", required = false) String loadMore, Model model) throws JsonMappingException, JsonProcessingException {
		 	
		 	
	        return "homePage";
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
