/**
 * Sample javascript file. Read the contents and understand them, 
 * then modify this file for your use case.
 */

var myTable;
$(document).ready(function() {
	/*myTable = $("#usersTable").DataTable({
        columns: [{data:"uid"}, {data:"last_timestamp"}, {data:"num_msgs"}]
    });
    
    */
    document.getElementById("content").innerHTML = "<table id = \"contentTable\" class = \"display\">" +
    		"<thead><tr>" +
    		"<th>User Id</th>" +
    		"<th>Timestamp of last message</th>" +
    		"<th>Number of messages exchanged</th>" +
    		"</tr></thead></table>";
    contentTable = $("#contentTable").DataTable({
    	columns: [{data:"uid"}, {data:"last_timestamp"}, {data:"num_msgs"}]
    });
    contentTable.ajax.url("AllConversations").load();
    
    $('#contentTable tbody').on( 'click', 'tr', function () {
        if ( $(this).hasClass('selected') ) {
            $(this).removeClass('selected');
        }
        else {
            contentTable.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
        }
        
        modifyTable(contentTable.row(this).data()["uid"]);
        //$('#trial').html(contentTable.row(this).data()["uid"]);
    } );
    
    
    
    //load div contents asynchronously, with a call back function
    //alert("Page loaded. Click to load div contents.");
	//$("#content").load("content.html", function(response){	
    //callback function
		//alert("Div loaded. Size of content: " + response.length + " characters.");
	//});
});

function resetTable()
{
	document.getElementById("content").innerHTML = "<table id = \"contentTable\" class = \"display\">" +
	"<thead><tr>" +
	"<th>User Id</th>" +
	"<th>Timestamp of last message</th>" +
	"<th>Number of messages exchanged</th>" +
	"</tr></thead></table>  <br>";
	document.getElementById("newConvo").innerHTML = "";
	contentTable = $("#contentTable").DataTable({
	columns: [{data:"uid"}, {data:"last_timestamp"}, {data:"num_msgs"}]
	});
	contentTable.ajax.url("AllConversations").load();
	  $("#newconversation").on('submit', function ()
			    {
			    	createNewConversation($("#conversation").val());
			    	return false;
			    });
	$('#contentTable tbody').on( 'click', 'tr', function () {
        if ( $(this).hasClass('selected') ) {
            $(this).removeClass('selected');
        }
        else {
            contentTable.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
        }
        
        modifyTable(contentTable.row(this).data()["uid"]);
        //$('#trial').html(contentTable.row(this).data()["uid"]);
    } );
}

function modifyTable(uid)
{
// 	document.getElementById("content").innerHTML = "<table id = \"contentTable\" class = \"display\">" +
// 	"<thead><tr>" +
// 	"<th>User Id</th>" +
// 	"<th>Timestamp</th>" +
// 	"<th>Text</th>" +
// 	"</tr></thead></table> + <br>"
// 	+
// 	"<form id=newmessage>" +
// 	" Enter your message: <input type=\"text\" id = \"message\">"+
// "<input type=\"hidden\" name=\"uid\" value=\""+uid+"\" />"+
// //"<input type=\"submit\""
// "<input class=\"button\" name=\"submit\" type=\"submit\" " +
// "value=\"Submit\" />"+
// "</form>";
// 	contentTable = $("#contentTable").DataTable({
//     	columns: [{data:"uid"}, {data:"timestamp"}, {data:"text"}]
//     });
//     contentTable.ajax.url("ConversationDetail?other_id=" + uid).load();
//     $("#newmessage").on('submit', function ()
//     {
//     	createNewMessage(uid,$("#message").val());
//     	return false;
//     });

	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function()
	{
		if (this.readyState == 4 && this.status == 200)
		{
			var currentData = JSON.parse(this.responseText).data;
			var currentStr = "<table> <tr> <th> User Id </th>" + 
	    	 "<th> Timestamp </th> <th> Text </th> </tr>";
	    	 for (var i = 0 ; i < currentData.length ; i ++)
	    		 {
	    		 	currentStr += "<tr>";
	    		 	currentStr += "<td>" + currentData[i].uid + " </td>";
	    		 	currentStr += "<td>" + currentData[i].timestamp + " </td>";
	    		 	currentStr += "<td>" + currentData[i].text + " </td>";
	    		 	currentStr += "</tr>";
	    		 	//document.getElementById("txtHint").innerHTML += currentStr;
	    		 }
	    	 currentStr += "</table>"
	    	 document.getElementById("content").innerHTML = currentStr;
		}
	};
    xhttp.open("GET", "ConversationDetail?other_id=" + uid, true);
    xhttp.send();
}

function showCreateConversation()
{
	var currentHTML = 	"<form id=newconversation onsubmit=\"createNewConversation(this.conversation.value)\">" +
	" Enter the uid: <input type=\"text\" id = \"conversation\" name=\"uid\">"+
	//"<input type=\"submit\""
	"<input class=\"button\" name=\"submit\" type=\"submit\" " +
	"value=\"Submit\" />"+
	"</form>";
	document.getElementById("newConvo").innerHTML = currentHTML;
}

function createNewMessage(uid,message)
{
//	var uid=document.getElementById("uid").value;
//	var message=document.getElementById("message").value;
	//console.log(uid+message+"came here");
	var xhttp;
	xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function(){
		 if (this.readyState == 4 && this.status == 200){
	json_object = JSON.parse(this.responseText);
	if(!(json_object.status))
		{
		alert("Message 1 not sent due to some error");
		}
		 }
		 modifyTable(uid);
	}
	xhttp.open("GET", "NewMessage?other_id="+uid+"&msg="+message, true);
	  xhttp.send();
	  
	  
	  

}

function createNewConversation(uid)
{
//	var uid=document.getElementById("uid").value;
//	var message=document.getElementById("message").value;
	//console.log(uid+message+"came here");
	var xhttp;
	xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function(){
		 if (this.readyState == 4 && this.status == 200){
	json_object = JSON.parse(this.responseText);
	if(!(json_object.status))
		{
		alert("Conversation not created due to some error");
		}else
		{
			alert("New conversation created successfully!");
		}
		 }
		 resetTable();
	}
	xhttp.open("GET", "CreateConversation?other_id="+uid, true);
	  xhttp.send();
	  
	  
	  

}

