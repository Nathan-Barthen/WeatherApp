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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class FiveDayReport {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<FutureDayReport> fiveDays;  //List 5 indexes long. Each index is one day in the 5 day report.
	
	private double avgTemp;     		//Calculated from FutureDayReport Values
	private double lowTemp;     		//Calculated from FutureDayReport Values
	private double highTemp;     		//Calculated from FutureDayReport Values
	
	private double fiveDayDownfallProb;
	private double fiveDayDownfallRainAmount;
	private double fiveDayDownfallSnowAmount;
	
	private String apiError;
	
	public FiveDayReport(){
		fiveDays = new Vector<FutureDayReport>();
		
		apiError = "";
	}


	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	

	public List<FutureDayReport> getFiveDays() {
		return fiveDays;
	}


	public void setFiveDays(List<FutureDayReport> fiveDays) {
		this.fiveDays = fiveDays;
	}


	public int getAvgTemp() {
		//Rounded to a whole number
		return (int) Math.round(avgTemp);
	}


	public void setAvgTemp(double avgTemp) {
		this.avgTemp = avgTemp;
	}

	public int getLowTemp() {
		//Rounded to a whole number
		return (int) Math.round(lowTemp);
	}

	public void setLowTemp(double lowTemp) {
		this.lowTemp = lowTemp;
	}

	public int getHighTemp() {
		//Rounded to a whole number
		return (int) Math.round(highTemp);
	}

	public void setHighTemp(double highTemp) {
		this.highTemp = highTemp;
	}

	public int getFiveDayDownfallProb() {
			//Rounded to int / whole number.
			return (int) fiveDayDownfallProb;
	}


	public void setFiveDayDownfallProb(double fiveDayDownfallProb) {
		this.fiveDayDownfallProb = fiveDayDownfallProb;
	}


	public double getFiveDayDownfallRainAmount() {
		return fiveDayDownfallRainAmount;
	}


	public void setFiveDayDownfallRainAmount(double fiveDayDownfallRainAmount) {
		this.fiveDayDownfallRainAmount = fiveDayDownfallRainAmount;
	}


	public double getFiveDayDownfallSnowAmount() {
		return fiveDayDownfallSnowAmount;
	}


	public void setFiveDayDownfallSnowAmount(double fiveDayDownfallSnowAmount) {
		this.fiveDayDownfallSnowAmount = fiveDayDownfallSnowAmount;
	}


	
	@JsonIgnore
	public String getDownfallAmountRainMmAndInches() {
		double downfallInches = fiveDayDownfallRainAmount * 0.0393701; // conversion factor: 1 mm = 0.0393701 inches
		downfallInches = Math.round(downfallInches * 100.0) / 100.0; // round to 2 decimal places
		
		if(downfallInches == 0.00) {
			downfallInches = Math.round((fiveDayDownfallRainAmount * 0.0393701) * 1000.0) / 1000.0; // round to 3 decimal places
		}
		
		return Double.toString(downfallInches) + "in (" + Double.toString(fiveDayDownfallRainAmount) + "mm)";
		
	}

	@JsonIgnore
	public String getDownfallAmountSnowMmAndInches() {
		double downfallInches = fiveDayDownfallSnowAmount * 0.0393701; // conversion factor: 1 mm = 0.0393701 inches
		downfallInches = Math.round(downfallInches * 100.0) / 100.0; // round to 2 decimal places
		
		if(downfallInches == 0.00) {
			downfallInches = Math.round((fiveDayDownfallSnowAmount * 0.0393701) * 1000.0) / 1000.0; // round to 3 decimal places
		}
		
		return Double.toString(downfallInches) + "in (" + Double.toString(fiveDayDownfallSnowAmount) + "mm)";
		
	}

	
	
	
		public String getApiError() {
			return apiError;
		}


		public void setApiError(String apiError) {
			this.apiError = apiError;
		}

	    
}
