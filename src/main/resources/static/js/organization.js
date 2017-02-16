$(function() {
	$(".user_checkbox").change(addMember);
	$('#member_list').delegate('.delete_member', 'click', deleteMember);
	
	$('.org_favorite').click(likeOrganization);
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

function likeOrganization(e) {
	e.preventDefault();
	var id = $(this).data("id");
	$.ajax({
		type: 'post',
		url: '/group/' + id + '/like',
		success: function(result) {
			console.log(result);
			
			var like = result.stateCheck;
			console.log(like);
			$("#organization_" + id).text(like);
			
//			var listDiv = $('#issueList');
//			listDiv.children().remove();
//			for(var i = 0 ; i < result.length ; i++) {
//				var temp = Handlebars.templates['precompile/sorting_template'];
//				var list = temp(result[i]);
//				listDiv.append(list);
//			}
		},
		error: function(error) {
			alert('에러');
		}
	});
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