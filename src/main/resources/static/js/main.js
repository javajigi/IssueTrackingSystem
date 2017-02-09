$(function() {
	$('.add-comment-btn').click(addComment);
	$('.mdl-switch__input').bind("change", modifyIssueState);
	$('#comment_list').delegate('#btn_comment_modify', "click", openCommentForm);
	$('#comment_list').delegate('#btn_comment_delete', "click", deleteComment);
	$('#comment_list').delegate('#btn_comment_complete', "click", modifyComment);
});

function addComment(e) {
	e.preventDefault();
	console.log('create Comment to Issue Page');
	
	var url = $('.comment-create').attr("action");
	var queryString = $('.comment-create').serialize();
	console.log("url : "+url+"\nqueryString : "+queryString);
	
	$.ajax({
		type: 'post',
		url: url,
		data: queryString,
		dataType:"json",
		success: function(result) {
			console.log(result);
			var template = $("#commentTemplate").html();
			var returntemp = template.format(result.id, result.contents, result.formattedWriteDate, result.writer.id, result.writer.userId, result.isMyComment);
			$(".comment-form").append(returntemp);
			$("textarea[name=contents]").val("");
		},
		error: function(error) {
			console.log('fail-RequestData');
		}
	});
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

String.prototype.format = function() {
	  var args = arguments;
	  return this.replace(/{(\d+)}/g, function(match, number) {
	    return typeof args[number] != 'undefined'
	        ? args[number]
	        : match
	        ;
	  });
};