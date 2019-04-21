let topBar = document.getElementById("topBar");
let sideBar = document.getElementById("sideBar");
var originalHeight = topBar.offsetHeight;
sideBar.style.top = originalHeight + "px";

var navDown = false;
document.getElementById("navButton").onclick = function() {
	if(navDown) {
		sideBar.style.top = originalHeight + "px";
		navDown = false;
	}
	else {
		sideBar.style.top = (topBar.offsetHeight * 1.6) + "px";
		navDown = true;
	}
	
}

window.onscroll = function(event) {
	if(window.scrollY >= topBar.offsetHeight) {
		sideBar.style.top = "0px";
	}
	else {
		sideBar.style.top = topBar.offsetHeight + "px";
	}
}