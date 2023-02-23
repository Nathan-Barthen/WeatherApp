/**
 * Used to control / create the temperature line graph / chart. 
 * It is a chart with 8 values. One for each 3-hour window in a day.
 */
		
		var timeWindows = [];
		var temps = [];
		
		// Loop over the Java object and push timeWindow and temp values into arrays
		document.querySelectorAll('#bottom-3hourlyWeather-time').forEach(function(element, index) {
			  timeWindows.push(element.innerText);
		});

		document.querySelectorAll('#bottom-3hourlyWeather-temp').forEach(function(element, index) {
			  var tempWithoutSymbol = element.innerText.replace(/[^\d.-]/g, ''); // removes all non-numeric and non-decimal characters
			  temps.push(tempWithoutSymbol);
		});


		var ctx = document.getElementById('temp-graph').getContext('2d');
		var tempGraph = new Chart(ctx, {
		  type: 'line',
		  data: {
		    labels: timeWindows,
		    datasets: [
		      {
		        label: 'Temp',
		        data: temps,
		        backgroundColor: 'rgba(0, 0, 204, 0.2)',
		        borderColor: 'rgba(0, 0, 153, 1)',
		        borderWidth: 1
		      }
		    ]
		  },
		  options: {
			    responsive: true,
			    scales: {
			      y: {
			  		
			        ticks: { 
			  			color: 'rgb(0, 0, 102)', 
			  			padding: 5,
			  			beginAtZero: true,
			  			font: {
	                    	size: 20,
	                	},
					    callback: function(value) {
				          return value + '°';
				        }
			  		}
			      },
			      x: {
			        ticks: { 
			    	  color: 'rgb(153, 122, 0)', 
			    	  padding: 5,
			    	  beginAtZero: true, 
			    	  font: {
		               	  size: 18,
		              }
			      	
			    	}
			      }
			    }
			  }
		});
		
		
		
		