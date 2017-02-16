$(function() {
	$(".user_checkbox").change(addMember);
	$('#member_list').delegate('.delete_member', 'click', deleteMember);
});

function addMember(e) {
	e.preventDefault();
	var id = $(this).data("id");
	var userId = $(this).data("userid");
	var profile = $(this).data("profile");
	if(this.checked) {
		var memberList = $("#member_item").html();
		var template = memberList.format(id, userId, profile);
		$("#member_list").append(template);
    } else {
    	$("#member_" + id).remove();
    }
}
function deleteMember(e) {
	e.preventDefault();
	var id = $(this).data("id");
	$("#member_" + id).remove();
	$("#user_checkbox_" + id).prop("checked", false);
}

String.prototype.format = function() {
	  var args = arguments;
	  return this.replace(/{(\d+)}/g, function(match, number) {
	    return typeof args[number] != 'undefined'
	        ? args[number]
	        : match
	        ;
	  });
};