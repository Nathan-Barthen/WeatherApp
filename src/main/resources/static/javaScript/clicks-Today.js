/**
 * When user clicks Today div, directs them to the url containing today's information
 */
		
  document.getElementById("top-TodayDiv").addEventListener("click", function(){
	  var location = document.getElementById("location").value;
	  var id = document.getElementById("id").value;
	  var locationIndex = document.getElementById("locationIndex").value;
	  
      window.location.href = "/weatherReport/" + id + "/" + locationIndex + "/" + location + "/today";
  });

		
		
		
		
		