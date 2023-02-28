package indp.nbarthen.proj.sample;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import com.fasterxml.jackson.databind.ObjectMapper;

import indp.nbarthen.proj.repository.WeatherReport;

public class GetSampleInfo {
	//Reads the json provided to get the sample information for one location.
	public static Vector<WeatherReport> getAllSamples() {
		Vector<WeatherReport> allStoredReports = new Vector<WeatherReport>();
		
		File file = new File("Stored-SampleReport.json");
		if (file.length() != 0) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				allStoredReports = mapper.readValue(file,
						mapper.getTypeFactory().constructCollectionType(Vector.class, WeatherReport.class));
			    
			} catch (IOException e) {
			    e.printStackTrace();
			}
		}
		//Should only be one report.
		return allStoredReports;

	}






}

