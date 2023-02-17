package indp.nbarthen.proj.repository;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.List;
import java.util.Vector;

/*
 * Tomorrow:
 * 		Data will be calculated / attained using 5 day 3-Hourly weather forecast.
 */

@Entity
public class TomorrowReport {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	
	private int avgWeatherId;			//Calculated / Chosen from 3-Hourly Values
	private String avgWeatherMain;		//Calculated / Chosen from 3-Hourly Values
	private String avgWeatherDesc;		//Calculated / Chosen from 3-Hourly Values
	private String avgWeatherIconId;	//Calculated / Chosen from 3-Hourly Values
	
	
	private double avgTemp;     	//Calculated from 3-Hourly Values
	private double highTemp;		//Calculated from 3-Hourly Values
	private double lowTemp;			//Calculated from 3-Hourly Values
	
	private String downfallRain; 			//Optional: Ex. 'Rain' 'Snow;  							//Calculated from 3-Hourly Values
	private double downfallRainAmount; 	//Optional: Ex. ' "1h": 3.16 ' Could be '1h' or '3h'	//Calculated from 3-Hourly Values
	private String downfallSnow; 			//Optional: Ex. 'Rain' 'Snow;  							//Calculated from 3-Hourly Values
	private double downfallSnowAmount; 	//Optional: Ex. ' "1h": 3.16 ' Could be '1h' or '3h'	//Calculated from 3-Hourly Values
	
	private int sunrise; 			// time, unix, UTC - timezone
	private int sunset; 			// time, unix, UTC - timezone
	
	private int timezone; 			//Shift in seconds from UTC
	
	private int cityPopulation;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<TriHourlyReport> triHourlyReports;
	
	
	private String apiError;
	
	//Constructor
	public TomorrowReport(){
		triHourlyReports = new Vector<TriHourlyReport>();
	}

	
	
	//Getters / Setters
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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


	public double getAvgTemp() {
		return avgTemp;
	}


	public void setAvgTemp(double avgTemp) {
		this.avgTemp = avgTemp;
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





	public String getDownfallRain() {
		return downfallRain;
	}



	public void setDownfallRain(String downfallRain) {
		this.downfallRain = downfallRain;
	}



	public double getDownfallRainAmount() {
		return downfallRainAmount;
	}



	public void setDownfallRainAmount(double downfallRainAmount) {
		this.downfallRainAmount = downfallRainAmount;
	}



	public String getDownfallSnow() {
		return downfallSnow;
	}



	public void setDownfallSnow(String downfallSnow) {
		this.downfallSnow = downfallSnow;
	}



	public double getDownfallSnowAmount() {
		return downfallSnowAmount;
	}



	public void setDownfallSnowAmount(double downfallSnowAmount) {
		this.downfallSnowAmount = downfallSnowAmount;
	}



	public int getSunrise() {
		return sunrise;
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


		public String getApiError() {
			return apiError;
		}


		public void setApiError(String apiError) {
			this.apiError = apiError;
		}

	    
}
