package indp.nbarthen.proj.futuredata;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.fasterxml.jackson.databind.JsonNode;

import indp.nbarthen.proj.repository.FutureDayReport;
import indp.nbarthen.proj.repository.TriHourlyReport;
import indp.nbarthen.proj.repository.WeatherReport;

public class GetFutureDaysCalculatedData {
	/*
	 * Takes the passed day (FutrueDayReport) and...
	 * 		Calculates / Saves: 
	 * 		     temp (avg, high, low), avg weather (id, main, desc, iconId), downfall totals (if they exist)
	*/
	
		public static FutureDayReport daysSummaryData(FutureDayReport futureDay) {
			FutureDayReport day = futureDay;
			
			double avgTemp = 0;     	
			double lowTemp = day.getTriHourlyReports().get(0).getTemp();
			double highTemp = day.getTriHourlyReports().get(0).getTemp();
			
			double totalRain = 0;
			double totalSnow = 0;
			
			double highestDownfallProb = 0;
			
			// Initialize a map to keep track of the count for each weatherMain value
			Map<String, Integer> weatherMainCount = new HashMap<>();

			// Initialize a map to keep track of the attributes for each weatherMain value
			Map<String, TriHourlyReport> weatherMainAttributes = new HashMap<>();
			
			for(TriHourlyReport triHourReport : day.getTriHourlyReports()) {
				
				//Save temp data
				avgTemp += triHourReport.getTemp();
				
				
				//Check for new high & low temp + save value
				if(triHourReport.getTemp() > highTemp) {
					highTemp = triHourReport.getTemp();
				}
				else if(triHourReport.getTemp() < lowTemp) {
					lowTemp = triHourReport.getTemp();
				}
				
				
				//Save weatherMain data (ex. 'Rain') to a hashmap to find most common.
				String weatherMain = triHourReport.getWeatherMain();

				// Update the count for the current weatherMain value
				int count = weatherMainCount.getOrDefault(weatherMain, 0);
				weatherMainCount.put(weatherMain, count + 1);

				// Update the attributes for the current weatherMain value
				if (!weatherMainAttributes.containsKey(weatherMain)) {
				    weatherMainAttributes.put(weatherMain, triHourReport);
				}
				
				//Save highest downfall probability			
				if( triHourReport.getDownfallProbability() > highestDownfallProb) {
					highestDownfallProb = triHourReport.getDownfallProbability();
				}
				//Save downfall amounts (if they exist)
				if(triHourReport.getDownfallType().contains("Rain")) {
					totalRain += triHourReport.getDownfallTotalAmount();
				}
				else if(triHourReport.getDownfallType().contains("Snow")) {
					totalSnow += triHourReport.getDownfallTotalAmount();
				}
				
			}
			
			
			
			// Find the most common weatherMain value + save additional information (desc, iconId, id)
			String mostCommonWeatherMain = null;
			int maxCount = 0;
			for (Map.Entry<String, Integer> entry : weatherMainCount.entrySet()) {
			    if (entry.getValue() > maxCount) {
			        maxCount = entry.getValue();
			        mostCommonWeatherMain = entry.getKey();
			    }
			}

			// Get the attributes for the most common weatherMain value
			TriHourlyReport mostCommonTriHourReport = weatherMainAttributes.get(mostCommonWeatherMain);
			day.setAvgWeatherId(mostCommonTriHourReport.getWeatherId());
			day.setAvgWeatherMain(mostCommonTriHourReport.getWeatherMain());
			day.setAvgWeatherDesc(mostCommonTriHourReport.getWeatherDesc());
			day.setAvgWeatherIconId(mostCommonTriHourReport.getWeatherIconId());
			
			
		   //Save calculated Data
			//Get mean of temps.
			avgTemp = avgTemp / day.getTriHourlyReports().size();
			//Save temps
			day.setAvgTemp(avgTemp);
			day.setLowTemp(lowTemp);
			day.setHighTemp(highTemp);
			
			//Save downfall probability (may be 0)
			day.setDownfallProbability(highestDownfallProb);
			//Save downfall (vales may be 0)
			day.setDownfallRainAmount(totalRain);
			day.setDownfallSnowAmount(totalSnow);
			
			
			
			
			return day;
		}
}
