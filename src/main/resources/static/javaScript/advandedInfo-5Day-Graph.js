/**
 * Used to control / create the temperature line graph / chart. 
 * It is a chart with 8 values. One for each 3-hour window in a day.
 * Also lists the high(s) and low(s) for those time windows as well.
 */
		


		var ctx = document.getElementById('temp-graph').getContext('2d');
		var tempGraph = new Chart(ctx, {
		  type: 'line',
		  data: {
		    labels: ['12am-3am', '3am-6am', '6am-9am', '9am-12pm', '12pm-3pm', '3pm-6pm', '6pm-9pm', '9pm-12am'],
		    datasets: [
		      {
		        label: 'Average Temp',
		        data: [20, 22, 24, 25, 26, 24, 22, 20],
		        backgroundColor: 'rgba(255, 99, 132, 0.2)',
		        borderColor: 'rgba(255, 204, 0, 1)',
		        borderWidth: 1
		      },
		      {
		        label: 'High Temp',
		        data: [22, 24, 26, 27, 28, 26, 24, 22],
		        backgroundColor: 'rgba(0, 0, 204, 0.2)',
		        borderColor: 'rgba(0, 0, 153, 1)',
		        borderWidth: 1,
		        fill: true,
		        order: 1
		      },
		      {
		        label: 'Low Temp',
		        data: [18, 20, 22, 24, 25, 23, 21, 19],
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
		
		
		
		