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
			console.log('success-RequestData\n',result);
			
			
		},
		error: function(error) {
			console.log('fail-RequestData');
		}
	});
}