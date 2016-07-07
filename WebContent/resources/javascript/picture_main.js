/**
 * 
 */
var windowClosing = true; //will reset to false if any submit input is pressed

var startModal = function(){
	
	var options = {
			backdrop:"static",
			keyboard:false,
			show: true			
	};
	$("#loginModal").modal(options);
	
	
};
var showEmptyAlert = function(){
	
	 $("#clientErrorAlert, #deleteSuccessAlert").hide();	
	 
	 window.scrollTo(0,0);
	 
	 $("#selectErrorAlert").animate({top:0},500);
	
};

var hideEmptyAlert = function() {
	 
	
	 $("#selectErrorAlert").animate({top:-300},500);
}
//rowNum is the same as the anchor text
//accumulate heights including the header, and not including the row to scroll to
var scrollToPic = function(rowNum){
	
	var height=0;	
	
	var selector = ".picTable tr:lt(" + rowNum + ")";
	
	 $(selector).each(function(){
		 height += $(this).outerHeight(); 
	 });
	 
	 var top = $(".picTable").offset().top + height;	 
		 
	 window.scrollTo(0,top);
	 
	// $("#debug").text(height);
	
};
/* Removed this code since SessionDestroy() should run automatically
 * when browser disconnects -- not tested
 * Hangs IE when more than one tab opened.
 * If more than one tab opened, the server creates a new session, and if
 * other tab is accessed and is unsynched, the session is destroyed. A
 * logout page is returned with a link to the welcome page
 * 
 * Do not AJAX the window close, since the session will be invalidated,
 * and it will no longer work. (Session expired or null exceptions)
 * Chrome, Firefox do not change location to logout,if user opts to stay.
 * IE requests /logout - invalidates - and the logged out page is returned.
 */
$(document).ready(function(){
	
	if($("#showModal").val() === "show")
		startModal();
	
 /*	$("input[type='submit']").click(function(){
		windowClosing = false;
	}); */
	
 /*	$(window).bind("beforeunload", function(){
		if(windowClosing) {
			window.location = "logout";
			return "Your session is being closed.\nPlease log-in again.";
		}
	});	 */
	
	$("input[name='btnDelete']").click(function(event){
		
		var n = $("input[name='chkDeletePic']:checked").length;	
		if(n == 0){
		  event.preventDefault();	
		  showEmptyAlert();
		}
		else hideEmptyAlert();
	});
	
   $("input[name='btnZipSelect']").click(function(event){
		
		var n = $("input[name='chkDownloadPic']:checked").length;	
		if(n == 0){
		  event.preventDefault();	
		  showEmptyAlert();
		}
		else hideEmptyAlert();
	});
   
   $(".scrollToTable a").click(function(event){
	   event.preventDefault();
	   var textNode = $(this).text();
	   scrollToPic(parseInt(textNode));
	   
   });
	
});