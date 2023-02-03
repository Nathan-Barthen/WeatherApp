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
	        return "weatherScreen";
	    }
	 
	 @RequestMapping({"weather-report/{location}"})
	    public String getSummoner(@PathVariable("location") String location, @RequestParam(value = "matchType", required = false) String loadMore, Model model) throws JsonMappingException, JsonProcessingException {
		 	
		 	
	        return "homePage";
	    }

	 	
	 	//Add summoner to local database (Stored-Summoners.json file)
		 @PostMapping({"/saveSummoner"})
		    public void addSummoner() {
		        //PlayerAcc summoner = getCurrSummoner();
		        //StoreSummoner.storeSummonerToFile(summoner);
		        
		}
	 
	 
	 
	
	 
	 	
	 
}
