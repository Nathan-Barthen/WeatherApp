package indp.nbarthen.proj.repository;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibm.icu.util.TimeZone;

import java.util.Date;
import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibm.icu.text.SimpleDateFormat;

/*
 * Tomorrow:
 * 		Data will be calculated / attained using 5 day 3-Hourly weather forecast.
 */

@Entity
public class FutureDayReport {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String daysDate;
	
	private int avgWeatherId;			//Calculated / Chosen from 3-Hourly Values
	private String avgWeatherMain;		//Calculated / Chosen from 3-Hourly Values
	private String avgWeatherDesc;		//Calculated / Chosen from 3-Hourly Values
	private String avgWeatherIconId;	//Calculated / Chosen from 3-Hourly Values
	
	
	private double avgTemp;     		//Calculated from 3-Hourly Values
	private double lowTemp;     		//Calculated from 3-Hourly Values
	private double highTemp;     		//Calculated from 3-Hourly Values
	
	private double downfallProbability; 	//As a % - Calculated from 3-Hourly Values
	private double downfallRainAmount; 		//Optional: May be 0
	private double downfallSnowAmount; 		//Optional: May be 0			//Optional: May be 0
	
	
	private int sunrise; 			// time, unix, UTC - timezone
	private int sunset; 			// time, unix, UTC - timezone
	
	private int timezone; 			//Shift in seconds from UTC
	
	private int cityPopulation;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<TriHourlyReport> triHourlyReports;
	
	
	private String apiError;
	
	//Constructor
	public FutureDayReport(){
		triHourlyReports = new Vector<TriHourlyReport>();
		downfallRainAmount = 0;
		downfallSnowAmount = 0;
	}

	
	
	//Getters / Setters
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getDaysDate() {
		return daysDate;
	}
	
	@JsonIgnore
	public String getDaysDateForBottom() throws ParseException {
		//Date is in syntax mm/dd/yr (ex. 2/9/23)
		String dateString = daysDate;
		SimpleDateFormat inputFormat = new SimpleDateFormat("MMM dd, yyyy");
		SimpleDateFormat outputFormat = new SimpleDateFormat("M/d/yy");
		
		Date date = inputFormat.parse(dateString);
		date = new SimpleDateFormat("M/d/yy").parse(outputFormat.format(date));
		String formattedDate = new SimpleDateFormat("EEE, MMM d").format(date);
		
		return formattedDate;
	}
	@JsonIgnore
	public String getDaysDateNoYear() {
		return daysDate.substring(0, daysDate.indexOf(","));
	}


	public void setDaysDate(int daysAhead) {
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.add(Calendar.DAY_OF_YEAR, daysAhead);
		Date tomorrow = calendar.getTime();

		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
		String tomorrowStr = dateFormat.format(tomorrow);
		this.daysDate = tomorrowStr;
	}
	
	
	public int getAvgWeatherId() {
		return avgWeatherId;
	}


	public void setAvgWeatherId(int avgWeatherId) {
		this.avgWeatherId = avgWeatherId;
	}


	public String getAvgWeatherMain() {
		return avgWeatherMain;
	}


	public void setAvgWeatherMain(String avgWeatherMain) {
		this.avgWeatherMain = avgWeatherMain;
	}


	public String getAvgWeatherDesc() {
		return avgWeatherDesc;
	}


	public void setAvgWeatherDesc(String avgWeatherDesc) {
		this.avgWeatherDesc = avgWeatherDesc;
	}


	public String getAvgWeatherIconId() {
		return avgWeatherIconId;
	}


	public void setAvgWeatherIconId(String avgWeatherIconId) {
		this.avgWeatherIconId = avgWeatherIconId;
	}


	public int getAvgTemp() {
		//Rounded to a whole number
		return (int) Math.round(avgTemp);
	}


	public void setAvgTemp(double avgTemp) {
		this.avgTemp = avgTemp;
	}

	public int getLowTemp() {
		//Rounded to a whole number
		return (int) Math.round(lowTemp);
	}

	public void setLowTemp(double lowTemp) {
		this.lowTemp = lowTemp;
	}

	public int getHighTemp() {
		//Rounded to a whole number
		return (int) Math.round(highTemp);
	}

	public void setHighTemp(double highTemp) {
		this.highTemp = highTemp;
	}



	public int getDownfallProbability() {
		//Rounded to int / whole number.
		return (int) downfallProbability;
	}



	public void setDownfallProbability(double downfallProbability) {
		this.downfallProbability = downfallProbability;
	}



	public double getDownfallRainAmount() {
		return downfallRainAmount;
	}



	public void setDownfallRainAmount(double downfallRainAmount) {
		this.downfallRainAmount = downfallRainAmount;
	}
	
	@JsonIgnore
	public String getDownfallAmountRainMmAndInches() {
		double downfallInches = downfallRainAmount * 0.0393701; // conversion factor: 1 mm = 0.0393701 inches
		downfallInches = Math.round(downfallInches * 100.0) / 100.0; // round to 2 decimal places
		
		return Double.toString(downfallInches) + "in (" + Double.toString(downfallRainAmount) + "mm)";
		
	}

	@JsonIgnore
	public String getDownfallAmountRainInches() {
		double downfallInches = downfallRainAmount * 0.0393701; // conversion factor: 1 mm = 0.0393701 inches
		downfallInches = Math.round(downfallInches * 100.0) / 100.0; // round to 2 decimal places
		
		if(downfallInches == 0.00) {
			downfallInches = Math.round((downfallRainAmount * 0.0393701) * 1000.0) / 1000.0; // round to 3 decimal places
		}
		
		return Double.toString(downfallInches) + "in";
		
	}

	public double getDownfallSnowAmount() {
		return downfallSnowAmount;
	}



	public void setDownfallSnowAmount(double downfallSnowAmount) {
		this.downfallSnowAmount = downfallSnowAmount;
	}

	@JsonIgnore
	public String getDownfallAmountSnowMmAndInches() {
		double downfallInches = downfallSnowAmount * 0.0393701; // conversion factor: 1 mm = 0.0393701 inches
		downfallInches = Math.round(downfallInches * 100.0) / 100.0; // round to 2 decimal places
		
		return Double.toString(downfallInches) + "in (" + Double.toString(downfallSnowAmount) + "mm)";
		
	}
	
	@JsonIgnore
	public String getDownfallAmountSnowInches() {
		double downfallInches = downfallSnowAmount * 0.0393701; // conversion factor: 1 mm = 0.0393701 inches
		downfallInches = Math.round(downfallInches * 100.0) / 100.0; // round to 2 decimal places
		
		if(downfallInches == 0.00) {
			downfallInches = Math.round((downfallSnowAmount * 0.0393701) * 1000.0) / 1000.0; // round to 3 decimal places
		}
		
		return Double.toString(downfallInches) + "in";
		
	}
	

	@JsonIgnore
	public String getSunriseTime() {
		// Convert sunrise time to milliseconds
        long sunriseMillis = (sunrise+timezone) * 1000L;
        
        // Set the time zone to UTC
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        // Format the sunrise time to h:mm a format in UTC time zone
        return timeFormat.format(new Date(sunriseMillis)).toLowerCase();
	}

	public int getSunrise() {
		return sunrise;
	}


	public void setSunrise(int sunrise) {
		this.sunrise = sunrise;
	}


	@JsonIgnore
	public String getSunsetTime() {
		// Convert sunrise time to milliseconds
        long sunsetMillis = (sunset+timezone) * 1000L;
        
        // Set the time zone to UTC
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        // Format the sunrise time to h:mm a format in UTC time zone
        return timeFormat.format(new Date(sunsetMillis)).toLowerCase();
	}
	
	public int getSunset() {
		return sunset;
	}


	public void setSunset(int sunset) {
		this.sunset = sunset;
	}


	public int getTimezone() {
		return timezone;
	}


	public void setTimezone(int timezone) {
		this.timezone = timezone;
	}


	public int getCityPopulation() {
		return cityPopulation;
	}


	public void setCityPopulation(int cityPopulation) {
		this.cityPopulation = cityPopulation;
	}


	public List<TriHourlyReport> getTriHourlyReports() {
		return triHourlyReports;
	}


	public void setTriHourlyReports(List<TriHourlyReport> triHourlyReports) {
		this.triHourlyReports = triHourlyReports;
	}

	
	@JsonIgnore
	public String getSolarNoon() {
		// Calc solar noon time to seconds
		long solarNoon = (sunrise + sunset) / 2;
		//convert to miliseconds
        long solarNoonMillis = solarNoon * 1000L;
        
        // Set the time zone to UTC
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        // Format the sunrise time to h:mm a format in UTC time zone
        return timeFormat.format(new Date(solarNoonMillis)).toLowerCase();
	}
	
	@JsonIgnore
	public String getTotalSunlight() {
		// Calc total sunlight in the day
		long sunlight = sunset-sunrise;
		
		int hours = (int) (sunlight / 3600);
		int minutes = (int) ((sunlight % 3600) / 60);
		
		return hours + "hr " + minutes + "min";
        
	}
	
	
	

		public String getApiError() {
			return apiError;
		}


		public void setApiError(String apiError) {
			this.apiError = apiError;
		}

	    
}
