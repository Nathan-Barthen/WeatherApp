package indp.nbarthen.proj.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.util.Vector;

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
	private double highTemp;
	private double lowTemp;
	private double feelsLikeTemp;
	
	private int weatherId;			//Calculated / Chosen from 3-Hourly Values
	private String weatherMain;		//Calculated / Chosen from 3-Hourly Values
	private String weatherDesc;		//Calculated / Chosen from 3-Hourly Values
	private String weatherIconId;	//Calculated / Chosen from 3-Hourly Values

	private String downfallType; 			//Optional: Ex. 'Rain' 'Snow;  							
	private double downfallTotalAmount; 	//Optional: Ex. ' "1h": 3.16 ' Can only be'3h'
	
	private int time;				//Time of data forecasted, unix, UTC - timezone (beginning of three hour window)
	
	public TriHourlyReport(){
		
	}

	
	
	//Getters / Setters
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}



	public double getTemp() {
		return temp;
	}

	public void setTemp(double temp) {
		this.temp = temp;
	}

	public double getHighTemp() {
		return highTemp;
	}

	public void setHighTemp(double highTemp) {
		this.highTemp = highTemp;
	}

	public double getLowTemp() {
		return lowTemp;
	}

	public void setLowTemp(double lowTemp) {
		this.lowTemp = lowTemp;
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

	public String getDownfallType() {
		return downfallType;
	}

	public void setDownfallType(String downfallType) {
		this.downfallType = downfallType;
	}

	public double getDownfallTotalAmount() {
		return downfallTotalAmount;
	}

	public void setDownfallTotalAmount(double downfallTotalAmount) {
		this.downfallTotalAmount = downfallTotalAmount;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	


	    
}
