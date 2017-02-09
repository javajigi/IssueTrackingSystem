package infinitefire.project.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import infinitefire.project.domain.Comment;
import infinitefire.project.domain.CommentRepository;
import infinitefire.project.domain.Issue;
import infinitefire.project.domain.IssueRepository;
import infinitefire.project.domain.User;
import infinitefire.project.domain.UserRepository;
import infinitefire.project.security.LoginUser;

@RestController
@RequestMapping("/comment") //api라는 url을 보통 많이 쓴다.
public class CommentController {
	private static final Logger log = LoggerFactory.getLogger(CommentController.class);
	
	@Autowired
	IssueRepository issueRepository;
	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@PostMapping("/{issueId}/new")
	public Comment createComment(@LoginUser User loginUser, @PathVariable Long issueId, Comment comment) {
		log.debug("Access >> CreateComment");
		
		Issue issue = issueRepository.findOne(issueId);
		comment.setIssue(issue);
		comment.setWriter(loginUser);
		comment.setIsMyComment(true);
		log.debug("print comment : "+comment);
		
		return commentRepository.save(comment);
	}
	
	@PutMapping("/{commentId}/modify")
	public boolean modifyComment(@LoginUser User loginUser,
							     @PathVariable Long commentId, 
							     @RequestParam(value="contents") String contents) {
		
		Comment comment = commentRepository.findOne(commentId);
		if(comment.isMatchWriter(loginUser)) {
			comment.setContents(contents);
			commentRepository.save(comment);
			return true;
		} else
			return false;
	}
	
	@DeleteMapping("/{commentId}/delete")
	public boolean deleteComment(@LoginUser User loginUser, @PathVariable Long commentId) {
		log.debug("Access >> CommentDelete");
		
		Comment getComment = commentRepository.findOne(commentId);
		if(getComment.isMatchWriter(loginUser)) {
			commentRepository.delete(commentId);
			return true;
		} else
			return false;
	}
}
