$(function() {
	$(".user_checkbox").change(addMember);
	$('#member_list').delegate('.delete_member', 'click', deleteMember);
	$('.main_wrap').delegate('.org_favorite', 'click', likeOrganization);
//	$('.org_favorite').click(likeOrganization);
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
			var like = result.stateCheck;
			$("#org_like_" + id).text(like);
			
			if(result.state === "ordinary"){
				moveOrganization(result, 'favorite', 'default');
			} else {
				moveOrganization(result, 'default', 'favorite');
			}
			
		},
		error: function(error) {
			alert('에러');
		}
	});
}

function moveOrganization(result, remove, add) {
	
	var $remove = $('#' + remove + '_org.masonry').masonry({
        itemSelector: '.masonry-item',
    });
	$remove.attr("color" , "#000");
	$remove.masonry( 'remove', $("#org_item_" + result.id)).masonry();
	
	var template = $("#org_item").html();
	var $orgItem = template.format(result.id, result.groupName, result.description, result.stateCheck);

	var $add = $('#' + add + '_org.masonry').masonry({
        itemSelector: '.masonry-item',
    });
	$add.append( $orgItem ).masonry( 'appended', $orgItem );
	window.location.replace("/");
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