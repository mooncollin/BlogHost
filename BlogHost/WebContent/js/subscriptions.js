function subscribe(siteID) {
	if(siteID === undefined || siteID === null) {
		invalidSubscriber();
		return;
	}
	
	let parsedID = Number(siteID)
	if(parsedID === NaN) {
		invalidSubscriber();
		return;
	}
	
	let request = new XMLHttpRequest();
	
	let requestString = "/BlogHost/Subscribe?id=" + parsedID;
	
	
	let button = document.getElementById("subscribeButton");
	if(button.classList.contains("btn-info")) {
		button.innerHTML = "Subscribing...";
	}
	else {
		button.innerHTML = "Unsubscribing..";
	}
	button.classList.add("progress-bar");
	button.classList.add("progress-bar-striped");
	button.classList.add("bg-info");
	button.classList.add("progress-bar-animated");
	
	request.open('GET', requestString);
	request.onreadystatechange = function(event) {
		if(request.readyState === 4) {
			switch(request.status) {
				case 201:
					validSubscriber("on");
					break;
				case 200:
					validSubscriber("off");
					break;
				default:
					invalidSubscriber();
			}
		}
	}
	
	request.send();
}

function validSubscriber(result) {
	resetSubscriber();
	let button = document.getElementById("subscribeButton");
	if(result === "on") {
		
		button.classList.remove("btn-info");
		button.classList.add("btn-outline-secondary");
		button.innerHTML = "Subscribed";
	}
	else {
		button.classList.remove("btn-outline-secondary")
		button.classList.add("btn-info");
		button.innerHTML = "Subscribe";
	}
}

function invalidSubscriber() {
	resetSubscriber();
	let button = document.getElementById("subscribeButton");
	button.classList.remove("btn-info");
	button.classList.add("btn-danger");
	button.innerHTML = "Error Subscribing";
	setTimeout(function() {
		resetSubscriber();
	}, 2500);
}

function resetSubscriber() {
	let button = document.getElementById("subscribeButton");
	button.innerHTML = originalSubscriberText;
	button.className = "";
	button.classList.add(...originalSubscriberClasses);
}

let button = document.getElementById("subscribeButton");
var originalSubscriberText = button.innerHTML;
var originalSubscriberClasses = [...button.classList];