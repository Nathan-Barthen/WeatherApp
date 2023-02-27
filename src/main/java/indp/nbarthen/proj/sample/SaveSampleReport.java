package indp.nbarthen.proj.sample;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import indp.nbarthen.proj.repository.WeatherReport;

public class SaveSampleReport {
	
	/*
	 * Used to save a sample WeatherReport.
	 * 		-Used if the user did not want to add their own API key but wanted to see result.
	 * 		-Creates a sample when adding ' SaveSampleReport.storeReportToFile(report); ' 
	 * 			inside of  ' @RequestMapping({"weatherReport/{id}/{locationIndex}/{location}/today"}) '  after the functions have been called to gather the data.
	 * 
	 */
	public static void storeReportToFile(WeatherReport report) {
		
		
		File file = new File("Stored-SampleReport.json");
		//If file is empty, make new list, add summoner, save.
		    if (file.length() == 0) {
		        // Create a new list with the new summoner and write it to the file
		        List<WeatherReport> reports = List.of(report);
		        ObjectMapper mapper = new ObjectMapper();
		        try {
		            mapper.writeValue(file, reports);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    } 
		 
				
		}

	}
