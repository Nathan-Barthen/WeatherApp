/**
 * Used to create the leaflet map to show city locations on chooseCorrectLocation.html
 * 	Used when user searches using City+State
 * 	The openWeather API returns a list of locations
 *   These locations will be displayed on a map where the user can select the correct location
 */
	function loadMap() {
		var lat = parseFloat(document.getElementById("lat").value);
		var lon = parseFloat(document.getElementById("lon").value);
		var apiKey = document.getElementById("apiKey").value;
		var city = document.getElementById("city").value;
		
		
	    var map = L.map("precipitationMap").setView([lat, lon], 11);

	    //Add background map to show city/states/counties
	    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
	    	  attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
	    	}).addTo(map);
	    
	    var marker = L.marker([lat, lon]).addTo(map);
		  marker.bindPopup(city);
		
		//State outline layer
	
		  
	    //Add openWeather precipitation map
		L.tileLayer("https://tile.openweathermap.org/map/precipitation_new/{z}/{x}/{y}.png?appid=" + apiKey, {
		  maxZoom: 18,
		  attribution: 'Map data © OpenWeatherMap'
		}).addTo(map);
		
		//Add openWeather clouds map
		L.tileLayer("https://tile.openweathermap.org/map/clouds_new/{z}/{x}/{y}.png?appid=" + apiKey, {
			  maxZoom: 18,
			  attribution: 'Map data © OpenWeatherMap'
		}).addTo(map);
		

	}
		