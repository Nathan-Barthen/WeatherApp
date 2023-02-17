/**
 * Used to control the advancedInfo popup at the weatherScreen.html
 * Adds/removed the hidden div and modifies the styles of advancedInfo.
 */
		// Get the advanced info div
		const advancedInfo = document.getElementById("advancedInfo");
		
		advancedInfo.addEventListener("click", function() {
		  // Check if the div has the class "expanded"
		  if (advancedInfo.classList.contains("expanded")) {
		    // If it has, remove the class "expanded" and set the height back to 6%
		    advancedInfo.classList.remove("expanded");
		    advancedInfo.style.height = "3%";
		    advancedInfo.style.top = "89.8%";
		    
		  } else {
		    // If it doesn't have the class, add the class "expanded" and set the height to 56%
		    advancedInfo.classList.add("expanded");
		    advancedInfo.style.height = "45%";
		    advancedInfo.style.top = "47.8%";
		    
		    //Call map function
		    loadMap();
		  }
		});