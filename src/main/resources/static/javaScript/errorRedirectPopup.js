/**
 * Used to show error popup if API returns an error and redirect user to homePage.html
 */

				window.onload = function() {
				    var popupElement = document.getElementById("popup");
				    if (popupElement.innerText.includes("Error")) {
				        alert(popupElement.innerText);
				    }
				  }