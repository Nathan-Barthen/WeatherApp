/**
 * When user clicks 5-Day Forecast div, directs them to the url containing 5-Day Forecast information
 */
		
  document.getElementById("top-5DayDiv").addEventListener("click", function(){
	  var location = document.getElementById("location").value;
	  var id = document.getElementById("id").value;
	  var locationIndex = document.getElementById("locationIndex").value;
	  
      window.location.href = "/weatherReport/" + id + "/" + locationIndex + "/" + location + "/5-DayForecast";
  });

		
		
		
		
		