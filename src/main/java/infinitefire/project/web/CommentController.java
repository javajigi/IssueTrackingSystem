package infinitefire.project.web;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import infinitefire.project.domain.CommentRepository;
import infinitefire.project.domain.UserRepository;

@Controller
@RequestMapping("/comment")
public class CommentController {
	private static final Logger log = LoggerFactory.getLogger(CommentController.class);
	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@PostMapping("/new")
	public String createComment(HttpSession session) {
		
		return "redirect:/";
	}
}
