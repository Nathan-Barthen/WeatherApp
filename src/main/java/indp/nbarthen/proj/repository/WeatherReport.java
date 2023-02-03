package indp.nbarthen.proj.repository;

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
	
	
	private String apiError;
	
	public WeatherReport(){
		
		
		
		apiError = "";
	}


	


		public String getApiError() {
			return apiError;
		}


		public void setApiError(String apiError) {
			this.apiError = apiError;
		}

	    
}
