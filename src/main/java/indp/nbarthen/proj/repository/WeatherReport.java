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
	@OneToMany(cascade = CascadeType.ALL)
	private List<Location> locations;  //User searched by City+State (returns multiple locations, user chooses which is correct)
	
	
	@OneToOne(cascade = CascadeType.ALL)
	private TodayReport today;				 //Todays data
	
	@OneToOne(cascade = CascadeType.ALL)
	private FiveDayReport fiveDayReport;  	//5-Day Forecast data.
											//Tomorrow's Data is stored in fiveDayReport.fiveDays(1)
												//Since the 5-Day Report if the future report, the first index would be tomorrow.
	
	private String apiError;
	
	public WeatherReport(){
		units = "imperial"; 	//imerial (Fahrenheit)   Celsius = metric
		country = "US"; 		//Current default US
		locations = new Vector<Location>();
		today = new TodayReport();
		//tomorrow = new FutureDayReport();
		fiveDayReport = new FiveDayReport();
		
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


	
	
	public List<Location> getLocations() {
		return locations;
	}


	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}


	public TodayReport getToday() {
		return today;
	}
	public void setToday(TodayReport today) {
		this.today = today;
	}



	public FiveDayReport getFiveDayReport() {
		return fiveDayReport;
	}


	public void setFiveDayReport(FiveDayReport fiveDayReport) {
		this.fiveDayReport = fiveDayReport;
	}


		public String getApiError() {
			return apiError;
		}


		public void setApiError(String apiError) {
			this.apiError = apiError;
		}

	    
}
