package indp.nbarthen.proj.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.util.Vector;

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
	private String weatherMain;
	private String weatherDesc;
	private String weatherIconId;
	
	private double currTemp;
	private double currFeelsLike;
	private int currHumidity; 		// as a % (0-100)
	
	private String downfallType; 	//Optional: Ex. 'Rain' 'Snow;
	private double downfallAmount; 	//Optional: Ex. ' "1h": 3.16 ' Could be '1h' or '3h'
	
	private double windSpeed;
	private int cloudiness; 		// as a % (0-100)
	
	private int sunrise; 			// time, unix, UTC
	private int sunset; 			// time, unix, UTC
	
	private int time; //Time of data calculation, unix, UTC
	private int timezone; //Shift in seconds from UTC
	
	private int cityPopulation;
	
	private String apiError;
	
	
	
	
	public TodayReport(){
		
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
