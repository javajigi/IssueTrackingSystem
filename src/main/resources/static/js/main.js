$('.add-comment-btn').click(addComment);

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
		success: function(result) {
			console.log(result);
			
			var template = $("#commentTemplate").html();
			var returntemp = template.format(result.id, result.contents, result.formattedWriteDate, result.writer.id, result.writer.userId);
			$(".comment-form").append(returntemp);
			$("textarea[name=contents]").val("");
		},
		error: function(error) {
			console.log('fail-RequestData');
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