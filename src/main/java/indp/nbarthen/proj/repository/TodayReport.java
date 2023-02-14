package indp.nbarthen.proj.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.util.Date;
import java.util.Vector;

import com.ibm.icu.text.SimpleDateFormat;

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
	
	private String downfallType; 	//Optional: Ex. 'Rain' 'Snow;
	private double downfallAmount; 	//Optional: Ex. ' "1h": 3.16 ' Could be '1h' or '3h'
	
	private double windSpeed;		// m/s (meters per second)
	private int cloudiness; 		// as a % (0-100)
	
	private int sunrise; 			// time, unix, UTC
	private int sunset; 			// time, unix, UTC
	
	private int time; 		//Time of data calculation, unix, UTC
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


	public String getDownfallType() {
		return downfallType;
	}


	public void setDownfallType(String downfallType) {
		this.downfallType = downfallType;
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


	public String getSunrise() {
		//Converts Sunrise to local time (ex. 6:21am)
		SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
		 
		return timeFormat.format(sunrise).toLowerCase();
	}


	public void setSunrise(int sunrise) {
		this.sunrise = sunrise;
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


		public String getApiError() {
			return apiError;
		}


		public void setApiError(String apiError) {
			this.apiError = apiError;
		}

	    
}
