package indp.nbarthen.proj.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	
	private String downfallType; 			//Optional: Ex. 'Rain' 'Snow;  							//Calculated from 3-Hourly Values
	private double downfallTotalAmount; 	//Optional: Ex. ' "1h": 3.16 ' Could be '1h' or '3h'	//Calculated from 3-Hourly Values
	
	private int sunrise; 			// time, unix, UTC - timezone
	private int sunset; 			// time, unix, UTC - timezone
	
	private int timezone; 			//Shift in seconds from UTC
	
	private int cityPopulation;
	
	@OneToOne
	private List<TriHourlyReport> triHourlyReports;
	
	
	private String apiError;
	
	public TomorrowReport(){
		triHourlyReports = new Vector<TriHourlyReport>();
	}


	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	



		public String getApiError() {
			return apiError;
		}


		public void setApiError(String apiError) {
			this.apiError = apiError;
		}

	    
}
