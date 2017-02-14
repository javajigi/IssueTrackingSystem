package infinitefire.project.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import infinitefire.project.domain.Issue;
import infinitefire.project.domain.IssueRepository;
import infinitefire.project.domain.IssueState;
import infinitefire.project.security.GetContextPath;

@Controller
public class MainController {
	private static final Logger log = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private IssueRepository issueRepository;
	
	@GetMapping("/back")
	public String backToPage(HttpServletRequest req) {
		String getContextPath = req.getHeader("REFERER");
		//getContextPath.replace("http://localhost:8080/", "");
		log.debug("Replace >> "+getContextPath.replace("http://localhost:8080/", ""));
		return "redirect:/"+getContextPath.replace("http://localhost:8080/", "");
	}
	
	@GetMapping("/")
	public String index(@GetContextPath String getContextPath, 
						@RequestParam(value="state",  defaultValue = "OPEN") IssueState state, 
						Model model, HttpServletRequest request) {
		List<Issue> issueList;
		if(state.equals(IssueState.OPEN)){
			issueList = issueRepository.findByState(IssueState.OPEN);
			model.addAttribute("isOpen", true);
		} else {
			issueList = issueRepository.findByState(IssueState.CLOSE);
			model.addAttribute("isClose", false);
		}
		model.addAttribute("issueList", issueList);
		log.debug("GetContextPath : "+request.getHeader("REFERER"));
		return "index";
	}
}