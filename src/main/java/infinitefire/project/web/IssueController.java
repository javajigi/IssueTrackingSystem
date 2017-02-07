package infinitefire.project.web;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import infinitefire.project.domain.CommentRepository;
import infinitefire.project.domain.Issue;
import infinitefire.project.domain.IssueRepository;
import infinitefire.project.domain.LabelRepository;
import infinitefire.project.domain.MilestoneRepository;
import infinitefire.project.domain.User;
import infinitefire.project.domain.UserRepository;
import infinitefire.project.security.LoginUser;
import infinitefire.project.utils.HttpSessionUtils;

@Controller
public class IssueController {
	@Autowired
	IssueRepository issueRepository;	
	@Autowired
	UserRepository userRepository;	
	@Autowired
	CommentRepository commentRepository;
	@Autowired
	MilestoneRepository milestoneRepository;
	@Autowired
	LabelRepository labelRepository;
	
	private static final Logger log = LoggerFactory.getLogger(IssueController.class);

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("issueList", issueRepository.findAll());
		return "index";
	}
	
	@GetMapping("/issue/new")
	public String createIssueForm(@LoginUser User loginUser) {
		log.debug("Access >> /issue/new-Get");
		
		return "issue/new";
	}
	@PostMapping("/issue/new")
	public String createIssue(@LoginUser User loginUser, Issue newIssue) {
		log.debug("Access >> /issue/new-Post : " + newIssue);
		
		newIssue.setWriter(loginUser);
		issueRepository.save(newIssue);
		return "redirect:/";
	}
	
	@GetMapping("issue/{id}/modify")
	public String modifyIssueForm(@LoginUser User loginUser, @PathVariable Long id, Model model) {
		log.debug("Access >> /issue/modify");
		
		Issue modifyIssue = issueRepository.findOne(id);
		
		if(modifyIssue.isMatchWriter(loginUser)){
			model.addAttribute("modifyIssue", modifyIssue);
			return "issue/modify";
		} else
			return "redirect:/";
	}
	@PostMapping("issue/{id}/modify")
	public String modifiedIssue(@LoginUser User loginUser, @PathVariable Long id,
			String subject, String contents, Model model) {
		log.debug("Access >> /issue/{" + id +"}/modify-put");
		
		Issue modifyIssue = issueRepository.findOne(id);
		if(modifyIssue.isMatchWriter(loginUser)) {
			modifyIssue.setSubject(subject);
			modifyIssue.setContents(contents);
			issueRepository.save(modifyIssue);
			
			model.addAttribute("issueInfo", modifyIssue);
			return "/issue/detail";
		} else
			return "redirect:/";
	}
	
	@GetMapping("issue/{id}/delete")
	public String deleteIssue(@LoginUser User loginUser, @PathVariable Long id) {
		log.debug("Access >> /issue/{" + id + "}/delete");
		
		Issue deleteIssue = issueRepository.findOne(id);
		if(deleteIssue.isMatchWriter(loginUser))
			issueRepository.delete(id);
		
		return "redirect:/";
	}

	@GetMapping("/issue/{id}/detail")
	public String showIssueDetail(@PathVariable Long id, Model model) {
		log.debug("Access >> /issue/{" + id + "}/detail");
		Issue issue = issueRepository.findOne(id);
		model.addAttribute("issueInfo", issue);
		model.addAttribute("allLabel", labelRepository.findAll());
		model.addAttribute("allUser", userRepository.findAll());
		model.addAttribute("allMilestone", milestoneRepository.findAll());
		log.debug("View Issue Property : " + issue);
		return "issue/detail";
	}
}