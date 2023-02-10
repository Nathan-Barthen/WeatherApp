/**
 * When user enters data (zip or city+state) to search box
 */
		
	document.getElementById("top-search").addEventListener("submit", function(event) {
	    event.preventDefault();
	    var userInput = document.getElementById("userInput").value;
	    window.location = "/weatherReport/getLocation/" + encodeURIComponent(userInput);
	  });

		
		
		
		
		