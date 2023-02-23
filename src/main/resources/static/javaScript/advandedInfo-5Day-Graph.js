/**
 * Used to control / create the temperature line graph / chart. 
 * It is a chart with 5 values. One for each day in the 5-Day Report.
 * Also lists the high(s) and low(s) for those time windows as well.
 */
		
		var dates = [];
		var avgTemps = [];
		var highTemps = [];
		var lowTemps = [];
		
		// Get all the div elements that are inside of hiddenContent-tempValuesDiv div
		var divs = document.querySelectorAll("#hiddenContent-tempValuesDiv > div");


		// Loop through each div element and extract the values
		for (var i = 0; i < divs.length; i++) {
		  var div = divs[i];
		  var avgTemp = div.querySelector("#hiddenAvgTemp").textContent;
		  var highTemp = div.querySelector("#hiddenHighTemp").value;
		  var lowTemp = div.querySelector("#hiddenLowTemp").value;
		  var date = div.querySelector("#hiddenDate").value;
		  
		  // Push the values to the arrays
		  dates.push(date);
		  avgTemps.push(avgTemp);
		  highTemps.push(highTemp);
		  lowTemps.push(lowTemp);
		}
		
		

		var ctx = document.getElementById('temp-graph').getContext('2d');
		var tempGraph = new Chart(ctx, {
		  type: 'line',
		  data: {
			labels: dates,
		    datasets: [
		      {
		        label: 'Average Temp',
		        data: avgTemps,
		        backgroundColor: 'rgba(255, 99, 132, 0.2)',
		        borderColor: 'rgba(255, 204, 0, 1)',
		        borderWidth: 1
		      },
		      {
		        label: 'High Temp',
		        data: highTemps,
		        backgroundColor: 'rgba(0, 0, 204, 0.2)',
		        borderColor: 'rgba(0, 0, 153, 1)',
		        borderWidth: 1,
		        fill: true,
		        order: 1
		      },
		      {
		        label: 'Low Temp',
		        data: lowTemps,
		        backgroundColor: 'rgba(153, 204, 255, 1)',
		        borderColor: 'rgba(0, 0, 153, 1)',
		        borderWidth: 1,
		        fill: true,
		        order: 0
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
	                    	size: 23,
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
		               	  size: 22,
		              }
			      	
			    	}
			      }
			    }
			  }
		});
		
		
		
		