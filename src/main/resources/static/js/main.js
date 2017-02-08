$(function() {
	$('.add-comment-btn').click(addComment);
	$('.mdl-switch__input').bind("change", modifyIssueState);
//	$('.btn_comment_modify').click(openCommentForm);
//	$(".btn_comment_delete").click(delComment);
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
			//console.error 메서드가 있으니 그걸 사용하는게 나을 듯.
			console.log('fail-RequestData');
		}
	});
}

function delComment(commentId) {
	event.preventDefault();
//	var commentId = $(this).data("id");
	var url = '/comment/' + commentId + '/delete';
	$.ajax({
		type: 'delete',
		url: url,
		success: function(result) {
			//if(result) 라고만 하면 될 듯.
			if(result == true) {
				var selectedDiv = $("#comment_" + commentId);
				selectedDiv.remove();
			}
		},
		error: function(error) {
			console.log('fail-RequestData');
			//refresh를 해야 해결되는 경우는 언제죠..?  정상적이지 않은 거 같아요.
			alert('please, your browser must be refresh : [f5]');
		}
	});
}

function openCommentForm(commentId, commentContents) {
	event.preventDefault();
	
	// close pre comment
	var preId = $("#comment_input").data("id");
	$("#comment_input").remove();
	$('#comment_contents_'+preId).show();	
	
	// click modify btn	
	var template = $("#comment_input_template").html();
	var commentInputForm = template.format(commentId, commentContents);
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