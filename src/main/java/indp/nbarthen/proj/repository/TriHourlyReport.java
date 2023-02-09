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


	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	


	    
}
