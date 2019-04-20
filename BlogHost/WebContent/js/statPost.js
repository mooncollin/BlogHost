function readPost(post, baseUri) {


var XHR = new XMLHttpRequest();
 
String : str = "post="+post;
 
XHR.open('Get', baseUri.concat('', "/ReadPost?" +str));
  
XHR.send();
}