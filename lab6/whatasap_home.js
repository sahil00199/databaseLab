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
}

function modifyTable(uid)
{
	document.getElementById("content").innerHTML = "<table id = \"contentTable\" class = \"display\">" +
	"<thead><tr>" +
	"<th>User Id</th>" +
	"<th>Timestamp</th>" +
	"<th>Text</th>" +
	"</tr></thead></table>";
	contentTable = $("#contentTable").DataTable({
    	columns: [{data:"uid"}, {data:"timestamp"}, {data:"text"}]
    });
    contentTable.ajax.url("ConversationDetail?other_id=" + uid).load();
}
