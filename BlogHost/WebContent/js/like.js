function like(reader, post, action, baseUri) {

	
  var XHR = new XMLHttpRequest();

  
  // Turn the data object into an array of URL-encoded key/value pairs.
 
  String : str = "reader="+reader+"&post="+post+"&action="+action;

  // Define what happens on successful data submission
//  XHR.addEventListener('load', function(event) {
//    alert('Yeah! Data sent and response loaded.');
//  });
//  // Define what happens in case of error
//  XHR.addEventListener('error', function(event) {
//	  alert('Oops! Something goes wrong.');
//  });
  XHR.open('Get', baseUri.concat('', "/Like?" +str));
  XHR.onreadystatechange = function (aEvt) {
	  if (XHR.readyState == 4) {
		     if(XHR.status == 200) {
		    	 var value = (action == 1) ? "Unlike": "Like";
		    	 var fctCall = (action == 1) ? 
		    			 "like("+reader+","+
		    			 post+","+"0"+",'"+baseUri+"');": 
		    				 "like("+reader+","+
			    			 post+","+"1"+",'"+baseUri+"');";
		    	 var button = document.getElementById('likeButton'+post);
		    	 button.setAttribute("value", value);
		    	 button.setAttribute("onclick", fctCall);
		    	 
		    	 var count = document.getElementsByName('likeCount'+post);
		    	 var countPrev = count[0].innerHTML;
		    	 countPrev = parseInt(countPrev.split(" ")[1]);
		    	 var countAft = (action == 1) ? countPrev+1:countPrev-1;
		    	 for (var i = 0; i < count.length;i++){	
		    		 count[i].innerHTML = "Likes: " + countAft;
		    	 }
		     }
	  
		     else{
		         alert("Error " + ((action == 1) ? "liking": "unliking") + " post\n"); 
		     }
		 
	  }
};

// Finally, send our data.
XHR.send();
}