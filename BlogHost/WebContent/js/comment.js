function comment(reader, post, baseUri) {


  var XHR = new XMLHttpRequest();

  
  // Turn the data object into an array of URL-encoded key/value pairs.
  var com = document.getElementsByName('commentText'+post)[0].value;
  if(com == "" || com == null){
	  alert("Error submitting comment"); 
	  return;
  }
  String : str = "reader="+reader+"&post="+post+"&comment="+com;
	console.log(str);
  // Define what happens on successful data submission
//  XHR.addEventListener('load', function(event) {
//    alert('Yeah! Data sent and response loaded.');
//  });
//  // Define what happens in case of error
//  XHR.addEventListener('error', function(event) {
//	  alert('Oops! Something goes wrong.');
//  });
  XHR.open('Get', baseUri.concat('', "/CommentAdd?" +str));
  XHR.onreadystatechange = function (aEvt) {
	  if (XHR.readyState == 4) {
		     if(XHR.status == 200) {
		    	 var count = document.getElementsByName('comCount'+post);
		    	 var countPrev = count[0].innerHTML;
		    	 countPrev = parseInt(countPrev.split(" ")[1]);
		    	 var countAft = countPrev+1;
		    	 for (var i = 0; i < count.length;i++){	
		    		 count[i].innerHTML = "Comments: " + countAft;
		    	 }
		    	 var list = document.getElementById('commentList'+post);
		    	 document.getElementById('commentText'+post).value = "";
		    	 var listNew = this.responseText;
		    	
		    	 var listAbove = list.parentNode;
		    	 listAbove.removeChild(list);
		    	 listAbove.innerHTML = listAbove.innerHTML + listNew;
		    	 
		     }
		     else{
		    	 alert("Error submitting comment"); 
		     }
		 
	  }
};

// Finally, send our data.
XHR.send();
}