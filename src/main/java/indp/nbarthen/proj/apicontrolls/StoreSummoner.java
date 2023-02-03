package indp.nbarthen.proj.apicontrolls;

import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

import indp.nbarthen.proj.repository.WeatherReport;

public class StoreSummoner {
	//Reads the json provided to set the Summoner's Solo/Duo Rank information
	public static void storeSummonerToFile(WeatherReport summoner) {
		
		ObjectMapper mapper = new ObjectMapper();
        try {
            //Read the existing summoners from the file
            List<WeatherReport> existingSummoners = mapper.readValue(new File("Stored-Summoners.json"),
                    mapper.getTypeFactory().constructCollectionType(List.class, WeatherReport.class));
            //Add the new summoner to the list
            existingSummoners.add(summoner);
            //Write the updated list to the file
            mapper.writeValue(new File("Stored-Summoners.json"), existingSummoners);
        } catch (IOException e) {
            e.printStackTrace();
        }
				
		

	}






}
