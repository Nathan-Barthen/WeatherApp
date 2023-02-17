/**
 * When user clicks Tomorrow div, directs them to the url containing tomorrow information
 */
		
  document.getElementById("top-TomorrowDiv").addEventListener("click", function(){
	  var location = document.getElementById("location").value;
	  var id = document.getElementById("id").value;
	  var locationIndex = document.getElementById("locationIndex").value;
	  
      window.location.href = "/weatherReport/" + id + "/" + locationIndex + "/" + location + "/tomorrow";
  });

		
		
		
		
		