package indp.nbarthen.proj.futuredata;

import indp.nbarthen.proj.repository.FiveDayReport;
import indp.nbarthen.proj.repository.FutureDayReport;

public class GetFiveDaysCalculatedData {
	/*
	 *  Calculates the generic values for the FiveDayReport (avg, high, low temp), (downfallProb, snow/rain downfall).
	 */
	public static FiveDayReport calc5DayGenerics(FiveDayReport fiveDaysReport) {
		double avgTemp = 0;     		
		double lowTemp = fiveDaysReport.getFiveDays().get(0).getLowTemp();     		
		double highTemp = fiveDaysReport.getFiveDays().get(0).getHighTemp();    
		
		double fiveDayDownfallProb = 0;
		double fiveDayDownfallRainAmount = 0;
		double fiveDayDownfallSnowAmount = 0;
		
		
		//Loop through days. Save temp data and downfall data
		for(FutureDayReport day : fiveDaysReport.getFiveDays()) {
			avgTemp += day.getAvgTemp();
			if(day.getLowTemp() < lowTemp) {
				lowTemp = day.getLowTemp();
			}
			if(day.getHighTemp() > highTemp) {
				highTemp = day.getHighTemp();
			}
			
			//Save highest downfall probability
			if( day.getDownfallProbability() > fiveDayDownfallProb) {
				fiveDayDownfallProb = day.getDownfallProbability();
			}
			//Save downfall amounts (may be 0)
			fiveDayDownfallRainAmount += day.getDownfallRainAmount();
			fiveDayDownfallSnowAmount += day.getDownfallSnowAmount();
		}
		
		
		avgTemp = avgTemp / fiveDaysReport.getFiveDays().size();
		
		fiveDaysReport.setAvgTemp(avgTemp);
		fiveDaysReport.setLowTemp(lowTemp);
		fiveDaysReport.setHighTemp(highTemp);
		fiveDaysReport.setFiveDayDownfallProb(fiveDayDownfallProb);
		fiveDaysReport.setFiveDayDownfallRainAmount(fiveDayDownfallRainAmount);
		fiveDaysReport.setFiveDayDownfallSnowAmount(fiveDayDownfallSnowAmount);
		
		
		return fiveDaysReport;
	}
}
