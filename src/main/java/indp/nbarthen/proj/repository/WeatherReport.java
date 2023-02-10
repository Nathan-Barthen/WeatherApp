package indp.nbarthen.proj.repository;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.util.Vector;

@Entity
public class WeatherReport {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String lon;
	private String lat;
	private String units;
	private String city;
	private String country;  	//Currently only works for US.
	private String state;
	
	@OneToOne(cascade = CascadeType.ALL)
	private TodayReport today;
	@OneToOne(cascade = CascadeType.ALL)
	private TomorrowReport tomorrow;
	
	private String apiError;
	
	public WeatherReport(){
		units = "imperial"; 	//Default (Fahrenheit)   Celsius = metric
		country = "US"; 		//Current default US
		today = new TodayReport();
		tomorrow = new TomorrowReport();
		
		apiError = "";
	}


	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}

	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}


	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}



	public TodayReport getToday() {
		return today;
	}
	public void setToday(TodayReport today) {
		this.today = today;
	}


	public TomorrowReport getTomorrow() {
		return tomorrow;
	}
	public void setTomorrow(TomorrowReport tomorrow) {
		this.tomorrow = tomorrow;
	}


		public String getApiError() {
			return apiError;
		}


		public void setApiError(String apiError) {
			this.apiError = apiError;
		}

	    
}
