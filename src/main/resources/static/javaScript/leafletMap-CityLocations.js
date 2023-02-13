/**
 * Used to create the leaflet map to show city locations on chooseCorrectLocation.html
 * 	Used when user searches using City+State
 * 	The openWeather API returns a list of locations
 *   These locations will be displayed on a map where the user can select the correct location
 */
		
		var sumLat = 0;
		var sumLon = 0;
		var topLocations = document.querySelectorAll('#top-location');
		
		console.log("HELLO");
		for (var i = 0; i < topLocations.length; i++) {
			 var topLocation = topLocations[i];
			 var lat = parseFloat(topLocation.textContent.match(/Lat: ([-\d.]+)/)[1]);
			 var lon = parseFloat(topLocation.textContent.match(/Lon: ([-\d.]+)/)[1]);
			 sumLat += lat;
			 sumLon += lon;
		}

		var centerLat = sumLat / topLocations.length;
		var centerLon = sumLon / topLocations.length;

		var map = L.map('mapid').setView([centerLat, centerLon], 7); // initialize the map and set the view to the center of the state
	  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
	      attribution: 'Map data © <a href="https://openstreetmap.org">OpenStreetMap</a> contributors'
	  }).addTo(map); // add the tile layer
	  
	  
	  
	  //Open popups for each marker
	  for (var i = 0; i < topLocations.length; i++) {
		  var topLocation = topLocations[i];
		  var lat = parseFloat(topLocation.textContent.match(/Lat: ([-\d.]+)/)[1]);
		  var lon = parseFloat(topLocation.textContent.match(/Lon: ([-\d.]+)/)[1]);
		  var marker = L.marker([lat, lon], {title: "Location " + (i + 1)}).addTo(map);
		}