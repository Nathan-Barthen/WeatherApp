/**
 * Removed / Unused
 *    
 */
		
const toggleBtn = document.querySelector('#toggle-btn');
const bodyEl = document.querySelector('body');

//Toggle desktop and mobile view
toggleBtn.addEventListener('click', function() {
	
	//If button is clicked and view was on mobile
  if (bodyEl.classList.contains('mobile-view')) {
	//Change to desktop view
    bodyEl.classList.remove('mobile-view');
    bodyEl.classList.add('desktop-view');
    
    //Save cookie for view preference
    var view = bodyEl.classList.contains('desktop-view') ? 'desktop' : 'mobile';
	document.cookie = "view=" + view;
	
	//Update button text to "Desktop"
    toggleBtn.textContent = "Desktop";
  } 
  //View was on desktop
  else {
	//Change to mobile view
    bodyEl.classList.remove('desktop-view');
    bodyEl.classList.add('mobile-view');
    
    //Save cookie for view preference
    var view = bodyEl.classList.contains('desktop-view') ? 'desktop' : 'mobile';
	document.cookie = "view=" + view;
	
    //Update button text to "Mobile"
    toggleBtn.textContent = "Mobile";

  }
});




/* When a new page is loaded/selected... 
 *  Gets the users cookie (mobile or desktop preference)
 *  Sets the styles to desktop if it was preferred (by default it is mobile)
 */
window.addEventListener('load', function() {
	  //Get saved cookie. Will either be 'desktop' or undefined/'mobile'.
	  var view = getCookie('view');
	  //If it is 'desktop' change view to desktop.
	  if (view === 'desktop') {
	    bodyEl.classList.add('desktop-view');
	    
	    //Update button text to "Desktop"
	    toggleBtn.textContent = "Desktop";
	  }
	  //If it is undefined. It will stay as the default mobile view.
	});

	//Gets the cookie for view preference.
	function getCookie(name) {
	  var match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
	  if (match) {
	    return match[2];
	  }
	}

		
		
		
		