package indp.nbarthen.proj.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.util.Date;
import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.TimeZone;

/*
 * Today:
 * 		Data is returned from Open Weather's Current weather data
 */

@Entity
public class TodayReport {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	
	private int weatherId;
	private String weatherMain;  	//Main weather Desc (ex. 'Sunny')
	private String weatherDesc;		//Longer Weather Desc (ex. 'Expect Sun')
	private String weatherIconId;
	
	private double currTemp;
	private double currFeelsLike;
	private int currHumidity; 		// as a % (0-100)
	private int visibility;			//meters (max is 10km)
	
	private String downfallType; 	//Optional: Ex. 'Rain' 'Snow;
	private double downfallAmount; 	//Optional: Ex. ' "1h": 3.16 ' Could be '1h' or '3h' (in mm)
	
	private double windSpeed;		// m/s (meters per second)
	private int cloudiness; 		// as a % (0-100)
	
	private int sunrise; 			// time, unix (seconds), UTC 
	private int sunset; 			// time, unix (seconds), UTC
	
	private int time; 		//Time of data calculation, unix (seconds), UTC 
	private int timezone; 	//Shift in seconds from UTC
	
	private int cityPopulation;
	
	private String apiError;
	
	
	//Constructor
	public TodayReport(){
		
	}

	
	
	//Getters / Setters
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public int getWeatherId() {
		return weatherId;
	}


	public void setWeatherId(int weatherId) {
		this.weatherId = weatherId;
	}


	public String getWeatherMain() {
		return weatherMain;
	}


	public void setWeatherMain(String weatherMain) {
		this.weatherMain = weatherMain;
	}


	public String getWeatherDesc() {
		return weatherDesc;
	}


	public void setWeatherDesc(String weatherDesc) {
		this.weatherDesc = weatherDesc;
	}


	public String getWeatherIconId() {
		return weatherIconId;
	}


	public void setWeatherIconId(String weatherIconId) {
		this.weatherIconId = weatherIconId;
	}


	public int getCurrTemp() {
		//Rounded to a whole number
		return (int) Math.round(currTemp);
	}


	public void setCurrTemp(double currTemp) {
		this.currTemp = currTemp;
	}


	public int getCurrFeelsLike() {
		//Rounded to a whole number
		return (int) Math.round(currFeelsLike);
	}


	public void setCurrFeelsLike(double currFeelsLike) {
		this.currFeelsLike = currFeelsLike;
	}


	public int getCurrHumidity() {
		return currHumidity;
	}


	public void setCurrHumidity(int currHumidity) {
		this.currHumidity = currHumidity;
	}

	@JsonIgnore
	public String getVisibilityMiles() {
		//Returns visibility in miles (rounded to 1 decimal place)
		double visibilityInMiles = visibility * 0.000621;
		visibilityInMiles = Math.round(visibilityInMiles * 10.0) / 10.0;
		return String.format("%.1f", visibilityInMiles);
	}
	public int getVisibility() {
		return visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

	public String getDownfallType() {
		return downfallType;
	}


	public void setDownfallType(String downfallType) {
		this.downfallType = downfallType;
	}


	@JsonIgnore
	public String getDownfallAmountMmAndInches() {
		double downfallInches = downfallAmount / 0.0393701; // conversion factor: 1 mm = 0.0393701 inches
		downfallInches = Math.round(downfallInches * 100.0) / 100.0; // round to 2 decimal places
		
		return Double.toString(downfallInches) + "in (" + Double.toString(downfallAmount) + "mm)";
		
	}
	public double getDownfallAmount() {
		return downfallAmount;
	}


	public void setDownfallAmount(double downfallAmount) {
		this.downfallAmount = downfallAmount;
	}


	public double getWindSpeed() {
		//Converted to m/s to mph (rounded to first decimal)
		double mphWindSpeed = windSpeed * 2.237;
		return Math.round(mphWindSpeed * 10) / 10.0;
	}


	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}


	public int getCloudiness() {
		return cloudiness;
	}


	public void setCloudiness(int cloudiness) {
		this.cloudiness = cloudiness;
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


	public int getTime() {
		return time;
	}


	public void setTime(int time) {
		this.time = time;
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
	
	@JsonIgnore
	public String getRemainingSunlight() {
		long timezoneMilli = timezone * 1000L;
		//Get current time in users timezone (milliseconds)
		long currentUtcTime = ( System.currentTimeMillis() );
		// Get sunset time (milliseconds)
		long sunsetMillis = (sunset) * 1000L;
		
		//Gets sunlightLeft adding in users timezone shift
		long sunlightLeft = sunsetMillis - currentUtcTime;
		
		long sunlightLeftSeconds = sunlightLeft / 1000;
		int hours = (int) (sunlightLeftSeconds / 3600);
		int minutes = (int) ((sunlightLeftSeconds % 3600) / 60);
		
		if(sunlightLeft <= 0) {
			return "None";
		}
		
		return hours + "hr " + minutes + "min";
        
	}
	
	
		public String getApiError() {
			return apiError;
		}


		public void setApiError(String apiError) {
			this.apiError = apiError;
		}

	    
}
