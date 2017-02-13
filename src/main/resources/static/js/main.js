$(function() {
	$('.add-comment-btn').click(addComment);
	$('.mdl-switch__input').bind("change", modifyIssueState);
	$('#comment_list').delegate('#btn_comment_modify', 'click', openCommentForm);
	$('#comment_list').delegate('#btn_comment_delete', 'click', deleteComment);
	$('#comment_list').delegate('#btn_comment_complete', 'click', modifyComment);

	$('.add_assignee').bind("click", addAssignee);
	$('#assignee_list').delegate('.delete_assignee', 'click', deleteAssignee);
	$('.set_milestone').bind("click", setMilestone);
  $('#milestone_category').delegate('.btn_milestone_delete', 'click', deleteMilestone);	
	$('.add_label').bind("click", addLabel);
	$('#label_list').delegate('.delete_label', 'click', deleteLabel);
	
	//$('.user-new-btn').click(checkLength);
	$('#userId').keyup(checkValue);
	$('#name').keyup(checkValue);
	$('#password').keyup(checkValue);
	$('.modify-issue-btn').click(checkLogin);
	$('.delete-issue-btn').click(checkLogin);
});

function checkValue(e) {
	var check = $(this).val();
	var thisName = $(this).attr('id');
	
	if(check.length < 4) {
		console.log(thisName);
		$('#'+thisName+'_alert').html('4자 이상 입력');
	} else if (check.length > 16) {
		$('#'+thisName+'_alert').css("color", "red");
		$('#'+thisName+'_alert').html('16자 이하 입력');
	} else if (thisName == 'userId') {
		$.ajax({
			type: 'post',
			url: '/api/user/check',
			data: 'checkUserId='+check,
			success: function(result) {
				console.log(result);
				if(result){
					$('#'+thisName+'_alert').css("color", "red");
					$('#'+thisName+'_alert').html('존재하는 ID입니다');
				} else {
					$('#'+thisName+'_alert').css("color", "blue");
					$('#'+thisName+'_alert').html('사용가능한 ID입니다');
				}
					
			},
			error: function(error) {
				alert('로그인후 댓글을 달 수 있습니다.');
			}
		});
	} else
		$('#'+thisName+'_alert').html('');
}

function checkLength(e) {
	var checkId = $('#userId').val();
	if(checkId.length > 16) {
		$('#userId_alert').html('ID가 너무 깁니다.');
		e.preventDefault();
	}
	var checkName = $('#name').val();
	if(checkName.length > 20) {
		$('#name_alert').html('NAME이 너무 깁니다.');
		e.preventDefault();
	}
	var checkPwd = $('#password').val();
	if(checkPwd.length > 20) {
		$('#password_alert').html('PASSWORD가 너무 깁니다.');
		e.preventDefault();
	}
}

function addComment(e) {
	e.preventDefault();
	console.log('create Comment to Issue Page');
	
	var formData = new FormData($('form')[0]);
	var contents = $('.mdl-textfield__input').val();
	
	if(contents != '') {
		var url = $('.comment_new').attr("action");
		var queryString = $('.comment_new').serialize();
		var sendData = formData;
//		console.log("url : "+url+"\nqueryString : "+queryString);
	
		$.ajax({
			type: 'post',
			url: url,
			data: formData,
			contentType: false,
			processData: false,
			dataType:"json",
			success: function(result) {
				console.log(result);
				var template = $("#comment_template").html();
				var comment = template.format(result.id, result.contents, result.formattedWriteDate, result.writer.id, result.writer.userId, result.isMyComment, result.writer.profile, result.isAttachmentExist, result.attachment);;
				$(".article_comment").append(comment);
				$(".comment_new #contents").val('');
				$("#file").val('');
			},
			error: function(error) {
				console.log(error);
				alert('로그인후 댓글을 달 수 있습니다.');
			}
		});
	} else {
		alert('입력값이 없습니다.');
	}
}

function modifyIssueState(e) {
	e.preventDefault();
	var isChecked = $(this).is(":checked");
	var url = '/issue/' + $(this).val() + '/modifyState';
	$.ajax({
		type: 'post',
		url: url,
		data: {'check' : isChecked},
		dataType:"json",
		success: function(result) {
			$('.nav_category .state').html(result.state);

		},
		error: function(error) {
			console.log('fail-RequestData');
		}
	});
}

function deleteComment(e) {
	e.preventDefault();

	var commentId = $(this).data("id");
	var url = '/comment/' + commentId + '/delete';
	$.ajax({
		type: 'delete',
		url: url,
		success: function(result) {
			if(result) {
				var selectedDiv = $("#comment_" + commentId);
				selectedDiv.remove();
			}
		},
		error: function(error) {
			console.log('fail-RequestData');
			alert('please, your browser must be refresh : [f5]');
		}
	});
}

function modifyComment(e) {
	event.preventDefault();

	var commentId = $(this).data("id");
	var contents = $(".comment_input").val();
	var url = '/comment/' + commentId + '/modify';
	$.ajax({
		type: 'put',
		url: url,
		data: {'contents' : contents},
		success: function(result) {
			console.log(contents);
			if(result) {
				console.log("aaa");
				$('#comment_input').remove();
				$('#comment_contents_'+commentId).text(contents);
				$('#comment_contents_'+commentId).show();
			}
		},
		error: function(error) {
			console.log('fail-RequestData');
			alert('please, your browser must be refresh : [f5]');
		}
	});
}

function openCommentForm(e) {
	e.preventDefault();

	// get comment id
	var commentId = $(this).data("id");
	console.log(commentId);

	// close pre comment
	var preId = $("#comment_input").data("id");
	$("#comment_input").remove();
	$('#comment_contents_'+preId).show();

	// click modify btn
	var contents = $("#comment_contents_" + commentId).text();
	var template = $("#comment_input_template").html();
	var commentInputForm = template.format(commentId, contents);
	$('#comment_contents_'+commentId).after(commentInputForm);
	$('#comment_contents_'+commentId).hide();
}

function addAssignee(e) {
	e.preventDefault();
	var userId = $(this).data("id");
	var issueId = $(this).data("issue");
	var url = "/issue/" + issueId + "/addAssignee/" + userId;
	$.ajax({
		type: 'put',
		url: url,
		success: function(result) {
			if(result != "") {
				var assigneeList = $("#asignee_item").html();
				var template = assigneeList.format(result.id, result.profile, result.userId, issueId);
				$("#assignee_list").append(template);
			}
		},
		error: function(error) {
			console.log('fail-RequestData');
		}
	});
}

function deleteAssignee(e) {
	e.preventDefault();
	var userId = $(this).data("id");
	var issueId = $(this).data("issue");
	var url = "/issue/" + issueId + "/deleteAssignee/" + userId;
	$.ajax({
		type: 'delete',
		url: url,
		success: function(result) {
			if(result) {
				var assignee = $("#assignee_" + userId);
				assignee.remove();
			}
		},
		error: function(error) {
			console.log('fail-RequestData');
			alert('please, your browser must be refresh : [f5]');
		}
	});
}

function setMilestone(e) {
	e.preventDefault();
	var milestoneId = $(this).data("id");
	var issueId = $(this).data("issue");
	var url = "/issue/" + issueId + "/setMilestone/" + milestoneId;
	$.ajax({
		type: 'put',
		url: url,
		success: function(result) {
			if(result != "") {
				// $("#milestone").attr("href", "/milestone/" + result.id + "/detail");
				// $("#milestone .mdl-chip__text").text(result.subject);
				$(".milestone_wrap").remove();
				var milestone = $("#milestone_item").html();
				var template = milestone.format(result.id, result.subject, issueId);
				$("#milestone_category").append(template);
			}
		},
		error: function(error) {
			console.log('fail-RequestData');
		}
	});
}

function deleteMilestone(e) {
	e.preventDefault();
	var milestoneId = $(this).data("id");
	var issueId = $(this).data("issue");
	var url = "/issue/" + issueId + "/deleteMilestone/" + milestoneId;
	$.ajax({
		type: 'delete',
		url: url,
		success: function(result) {
			if(result != "") {
				$(".milestone_wrap").remove();
			}
		},
		error: function(error) {
			console.log('fail-RequestData');
		}
	});
}

function addLabel(e) {
	e.preventDefault();
	var labelId = $(this).data("id");
	var issueId = $(this).data("issue");
	var url = "/issue/" + issueId + "/addLabel/" + labelId;
	$.ajax({
		type: 'put',
		url: url,
		success: function(result) {
			if(result != "") {
				var labelList = $("#label_item").html();
				var template = labelList.format(result.id, result.color, result.name, issueId);
				$("#label_list").append(template);
			}
		},
		error: function(error) {
			console.log('fail-RequestData');
		}
	});
}

function deleteLabel(e) {
	e.preventDefault();
	var labelId = $(this).data("id");
	var issueId = $(this).data("issue");
	var url = "/issue/" + issueId + "/deleteLabel/" + labelId;
	$.ajax({
		type: 'delete',
		url: url,
		success: function(result) {
			if(result) {
				var label = $("#label_" + labelId);
				label.remove();
			}
		},
		error: function(error) {
			console.log('fail-RequestData');
			alert('please, your browser must be refresh : [f5]');
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
