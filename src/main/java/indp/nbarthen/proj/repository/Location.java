package indp.nbarthen.proj.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonIgnore;

/*
 * Location:
 * 	   If user searched using City+State.
 * 		OpenWeather will return a list of locations
 * 			This is contained here so it can be shown on a map where the user will pick correct location.
 */

@Entity
public class Location {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String lon;
	private String lat;
	private String city;
	private String stateAbriv;
	
	public Location(){
		
	}

	
	
	//Getters / Setters
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}


	
	public String getLon() {
		return lon;
	}
	@JsonIgnore
	public double getDoubleLon() {
		//Used in chooseCorrectLocation.html 
		//Convert to double, round to 3 decimal places
		double doubleLon = Double.parseDouble(lon);
		return Math.round(doubleLon * 1000.0) / 1000.0;
	}
	
	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}
	
	@JsonIgnore
	public double getDoubleLat() {
		//Used in chooseCorrectLocation.html 
		//Convert to double, round to 3 decimal places
		double doubleLat = Double.parseDouble(lat);
		return Math.round(doubleLat * 1000.0) / 1000.0;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStateAbriv() {
		return stateAbriv;
	}

	public void setStateAbriv(String stateAbriv) {
		this.stateAbriv = stateAbriv;
	}




	


	    
}
