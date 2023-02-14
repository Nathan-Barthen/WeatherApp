/**
 * Used to create the leaflet map to show city locations on chooseCorrectLocation.html
 * 	Used when user searches using City+State
 * 	The openWeather API returns a list of locations
 *   These locations will be displayed on a map where the user can select the correct location
 */
		
	//Changes the color of the iconImg on the at the top of the webpage (not on the map)
		const colors = ["blue", "yellow", "green", "orange", "violet"];
		const divs = document.querySelectorAll("#top-locationContainer");
		
		for (let i = 0; i < divs.length; i++) {
		  const div = divs[i];
		  const iconImg = div.querySelector("#iconImgDiv img");
		  const colorIndex = i % colors.length;
		  const colorVar = colors[colorIndex];
		  iconImg.src = "https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-" + colorVar + ".png";
		}


	//Gets the middle of the screen by getting the mean of the lon and lat values
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

  //Creates the leaflet map
		var map = L.map('mapid').setView([centerLat, centerLon], 7); // initialize the map and set the view to the center of the state
	  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
	      attribution: 'Map data © <a href="https://openstreetmap.org">OpenStreetMap</a> contributors'
	  }).addTo(map); // add the tile layer
	  
	  
 //Adds each marker for each location.  
	  //Open popups for each marker
	  for (var i = 0; i < topLocations.length; i++) {
		  var cityElement = document.querySelector('#city');
		  var city = cityElement.textContent;
		  var topLocation = topLocations[i];
		  var lat = parseFloat(topLocation.textContent.match(/Lat: ([-\d.]+)/)[1]);
		  var lon = parseFloat(topLocation.textContent.match(/Lon: ([-\d.]+)/)[1]);
		  var icon = new L.Icon({
			  iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-' +  colors[i] +'.png',
			  shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
			  iconSize: [25, 41],
			  iconAnchor: [12, 41],
			  popupAnchor: [1, -34],
			  shadowSize: [41, 41]
			});
		  
		  var marker = L.marker([lat, lon], {icon: icon} ).addTo(map);
		  marker.bindPopup(city);
		}