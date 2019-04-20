function donate(siteID) {
	let donationButton = document.getElementById("donationButton");
	donationButton.classList.add("progress-bar");
	donationButton.classList.add("progress-bar-striped");
	donationButton.classList.add("progress-bar-animated");
	donationButton.classList.add("bg-warning");
	donationButton.classList.remove("btn-success");
	donationButton.classList.add("btn-warning");
	donationButton.innerHTML = "Submitting Donation...";
	if(siteID === undefined ||
	   siteID === null) {
		invalidDonation();
		return;
	}
	
	let parsedNumber = Number(document.getElementById('donationAmount').value);
	let parsedID = Number(siteID);
	if(parsedNumber === NaN ||
	   parsedNumber < 0.01 ||
	   parsedID === NaN) {
		invalidDonation();
		return;
	}
	let request = new XMLHttpRequest();
	let requestString = "/BlogHost/Donation?id=" + parsedID + "&amount=" + parsedNumber;
	
	request.open('GET', requestString);
	request.onreadystatechange = function(event) {
		if(request.readyState === 4) {
			switch(request.status) {
				case 200:
					validDonation();
					break;
				default:
					invalidDonation();
					break;
					
			}
		}
	}
	
	request.send();
}

function invalidDonation() {
	let donationButton = document.getElementById("donationButton");
	donationButton.classList.remove("progress-bar");
	donationButton.classList.remove("progress-bar-striped");
	donationButton.classList.remove("progress-bar-animated");
	donationButton.classList.remove("bg-warning");
	donationButton.classList.remove("btn-warning");
	donationButton.classList.add("btn-danger");
	donationButton.innerHTML = "Donation Failed to Submit";
	setTimeout(resetDonation, 3000);
}

function validDonation() {
	let donationButton = document.getElementById("donationButton");
	donationButton.classList.remove("progress-bar");
	donationButton.classList.remove("progress-bar-striped");
	donationButton.classList.remove("progress-bar-animated");
	donationButton.classList.remove("bg-warning");
	donationButton.classList.add("btn-success");
	donationButton.classList.remove("btn-warning");
	donationButton.innerHTML = "Donation Submitted!";
	setTimeout(resetDonation, 3000);
}

function resetDonation() {
	let donationButton = document.getElementById("donationButton");
	donationButton.classList.remove("btn-danger");
	donationButton.classList.add("btn-success");
	donationButton.innerHTML = donationText;
}

var donationText = document.getElementById("donationButton").innerHTML;