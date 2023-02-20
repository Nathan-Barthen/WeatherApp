package indp.nbarthen.proj.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonIgnore;

/*
 * Tomorrow:
 * 		Data will be calculated / attained using 5 day 3-Hourly weather forecast.
 */

@Entity
public class TriHourlyReport {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private double temp;
	private int humidity;  			//As a %
	private double feelsLikeTemp;
	
	private int weatherId;			//Calculated / Chosen from 3-Hourly Values
	private String weatherMain;		//Calculated / Chosen from 3-Hourly Values
	private String weatherDesc;		//Calculated / Chosen from 3-Hourly Values
	private String weatherIconId;	//Calculated / Chosen from 3-Hourly Values

	private double downfallProbability;		//As a %
	private String downfallType; 			//Optional: Ex. 'Rain' 'Snow;  							
	private double downfallTotalAmount; 	//Optional
	
	private long time;				//Time of data forecasted, unix (seconds), UTC - timezone (beginning of three hour window)
	
	public TriHourlyReport(){
		downfallType = "";
		downfallTotalAmount = 0;
	}

	
	
	//Getters / Setters
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}



	public int getTemp() {
		//Rounded to a whole number
		return (int) Math.round(temp);
	}

	public void setTemp(double temp) {
		this.temp = temp;
	}

	public int getHumidity () {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	

	public double getFeelsLikeTemp() {
		return feelsLikeTemp;
	}

	public void setFeelsLikeTemp(double feelsLikeTemp) {
		this.feelsLikeTemp = feelsLikeTemp;
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

	
	public double getDownfallProbability() {
		return downfallProbability;
	}
	public void setDownfallProbability(double downfallProbability) {
		this.downfallProbability = downfallProbability;
	}
	
	public String getDownfallType() {
		return downfallType;
	}

	public void setDownfallType(String downfallType) {
		this.downfallType = downfallType;
	}

	
	@JsonIgnore
	public String getDownfallAmountMmAndInches() {
		double downfallInches = downfallTotalAmount * 0.0393701; // conversion factor: 1 mm = 0.0393701 inches
		downfallInches = Math.round(downfallInches * 100.0) / 100.0; // round to 2 decimal places
		
		return Double.toString(downfallInches) + "in";
		
	}
	public double getDownfallTotalAmount() {
		return downfallTotalAmount;
	}

	public void setDownfallTotalAmount(double downfallTotalAmount) {
		this.downfallTotalAmount = downfallTotalAmount;
	}

	
	@JsonIgnore 
	public String getTimeWindow() {
		// Convert the localTimeMilli value to a LocalDateTime object
		Instant instant = Instant.ofEpochMilli(time);
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);

		// Format the localDateTime to a string in the desired format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ha");
		String startTime = localDateTime.format(formatter);
		String endTime = localDateTime.plusHours(3).format(formatter);

		String timeRange = startTime + "-" + endTime;
		return timeRange.toLowerCase(); // Example: timeRange = "12AM-3AM" if time represents 12:00 AM

	}
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	


	    
}
