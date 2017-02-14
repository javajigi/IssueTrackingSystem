$(function() {
	$(".user_checkbox").change(addAssignee);
	$('#assignee_list').delegate('.delete_assignee', 'click', deleteAssignee);
	
	$(".milestone_checkbox").change(setMilestone);
	$('.milestone_wrap').delegate('.btn_milestone_delete', 'click', deleteMilestone);
	
	$(".label_checkbox").change(addLabel);
	$('.label_list').delegate('.delete_label', 'click', deleteLabel);

});

function addAssignee(e) {
	e.preventDefault();
	var id = $(this).data("id");
	var userId = $(this).data("userid");
	var profile = $(this).data("profile");
	if(this.checked) {
		var assigneeList = $("#asignee_item").html();
		var template = assigneeList.format(id, userId, profile);
		$("#assignee_list").append(template);
    } else {
    	$("#assignee_" + id).remove();
    }
}
function deleteAssignee(e) {
	e.preventDefault();
	var id = $(this).data("id");
	$("#assignee_" + id).remove();
	$("#assignee_checkbox_" + id).prop("checked", false);
}

function setMilestone(e) {
	e.preventDefault();
	var limit = 1;
	var id = $(this).data("id");
	var subject = $(this).data("subject");
	
	if($(".milestone_checkbox:checked" ).length > limit) {
		$(".milestone_checkbox:checked").prop("checked", false);	
		$(this).prop("checked", true);
		$(".milestone_wrap").empty();
	} else if ($(".milestone_checkbox:checked" ).length == 0) {
		$(".milestone_wrap").empty();
		return;
	}
	
	var milestoneList = $("#milestone_item").html();
	var template = milestoneList.format(id, subject);
	$(".milestone_wrap").append(template);
	$(".milestone_wrap")
	
}

function deleteMilestone(e) {
	$(".milestone_wrap").empty();
	var id = $(this).data("id");
	$("#milestone_checkbox_" + id).prop("checked", false);
}

function addLabel(e) {
	e.preventDefault();
	var id = $(this).data("id");
	var color = $(this).data("color");
	var name = $(this).data("name");
	
	if(this.checked) {
		var labelList = $("#label_item").html();
		var template = labelList.format(id, color, name);
		$("#label_list").append(template);
    } else {
    	$("#label_" + id).remove();
    	$("#label_checkbox_" + id).prop("checked", false);
    }
}

function deleteLabel(e) {
	e.preventDefault();
	var id = $(this).data("id");
	$("#label_" + id).remove();
	$("#label_checkbox_" + id).prop("checked", false);
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