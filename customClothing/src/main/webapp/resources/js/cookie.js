function createCookie(name,value,days,path,domain,secure) {
	if(days){
		var date = new Date();
		date.setTime(date.getTime()+(days*24*60*60*1000));
		var expires = date.toGMTString();
	}else{
		var expires = "";
	}
	var cookieString = name+"="+encodeURIComponent(value);
	if(expires) cookieString+=";expires="+expires;
	if(path) cookieString +=";path="+encodeURIComponent(path);
	if(domain) cookieString +=";domain="+encodeURIComponent(domain);
	if(secure) cookieString +=";secure";
	document.cookie = cookieString;
	
}

function getCookie(name){
	console.log(name);
	var nameEquals = name+"=";
	var crumbs = document.cookie.split('; ');
	for(var i=0;i<crumbs.length;i++){
		var crumb = crumbs[i];
		console.log(crumb);
		if(crumb.indexOf(nameEquals)==0){
			return decodeURIComponent(crumb.substring(nameEquals.length,crumb.length));
		}
	}
	return null;
}

function deleteCookie(name) {
	createCookie(name,"",-1);
}
