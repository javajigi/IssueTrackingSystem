package infinitefire.project.web;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import infinitefire.project.domain.Issue;
import infinitefire.project.domain.IssueRepository;
import infinitefire.project.domain.User;
import infinitefire.project.domain.UserRepository;
import infinitefire.project.utils.HttpSessionUtils;

@Controller
public class IssueController {
	@Autowired
	IssueRepository issueRepository;
	
	@Autowired
	UserRepository userRepository;
	
	private static final Logger log = LoggerFactory.getLogger(IssueController.class);

	@GetMapping("/")
	public String index(HttpSession session, Model model) {
		log.debug("Access >> / " + session.getAttribute(HttpSessionUtils.USER_SESSION_KEY));
		
		model.addAttribute("issueList", issueRepository.findAll());
		return "index";
	}
	
	@GetMapping("/issue/new")
	public String createIssueForm(HttpSession session) {
		log.debug("Access >> /issue/new-Get");
		
		if(HttpSessionUtils.isLoginUser(session)) {
			log.info("info >> is LoginUser");
			return "issue/new";
		}else {
			log.info("info >> is not LoginUser");
			return "redirect:/user/login";
		}
	}
	@PostMapping("/issue/new")
	public String createIssue(HttpSession session, Issue newIssue) {
		log.debug("Access >> /issue/new-Post");
		
		if(HttpSessionUtils.isLoginUser(session)) {
			log.info("info >> is not LoginUser");
			newIssue.setWriter(HttpSessionUtils.getUserFromSession(session));
			newIssue.setLabel(0);
			newIssue.setState("close");
			issueRepository.save(new Issue(
					newIssue.getSubject(),
					newIssue.getContents(),
					HttpSessionUtils.getUserFromSession(session),
					0, "close", null, null, null));
			return "redirect:/";
		}else {
			return "redirect:/user/login";
		}
	}
	
	@GetMapping("issue/{id}/modify")
	public String modifyIssueForm(HttpSession session, @PathVariable Long id, Model model) {
		log.debug("Access >> /issue/modify");
		
		if(HttpSessionUtils.isLoginUser(session)) {
			Issue modifyIssue = issueRepository.findOne(id);
			User myAccount = HttpSessionUtils.getUserFromSession(session);
			
			if(myAccount.isMatchId(modifyIssue.getWriter().getId())) {
				model.addAttribute("modifyIssue", modifyIssue);
				return "issue/modify";
			} else
				return "redirect:/";
			
		} else
			return "redirect:/";
	}
	@PostMapping("issue/{id}/modify")
	public String modifiedIssue(HttpSession session, @PathVariable Long id,
			String subject, String contents, Model model) {
		log.debug("Access >> /issue/{" + id +"}/modify-put");
		
		if(HttpSessionUtils.isLoginUser(session)) {
			Issue modifyIssue = issueRepository.findOne(id);
			User myAccount = HttpSessionUtils.getUserFromSession(session);
			
			if(myAccount.isMatchId(modifyIssue.getWriter().getId())) {
				modifyIssue.setSubject(subject);
				modifyIssue.setContents(contents);
				issueRepository.save(modifyIssue);
				
				model.addAttribute("issueInfo", modifyIssue);
				return "/issue/detail";
			} else
				return "redirect:/";
		}
		
		return "redirect:/";
	}
	
	@GetMapping("issue/{id}/delete")
	public String deleteIssue(HttpSession session, @PathVariable Long id) {
		log.debug("Access >> /issue/{" + id + "}/delete");
		
		if(HttpSessionUtils.isLoginUser(session)) {
			Issue modifyIssue = issueRepository.findOne(id);
			User myAccount = HttpSessionUtils.getUserFromSession(session);
			
			if(myAccount.isMatchId(modifyIssue.getWriter().getId())) {
				issueRepository.delete(id);
			}
		}
		return "redirect:/";
	}

	@GetMapping("/issue/{id}/detail")
	public String showIssueDetail(@PathVariable Long id, Model model) {
		log.debug("Access >> /issue/{" + id + "}/detail");
		
		model.addAttribute("issueInfo", issueRepository.findOne(id));
		return "issue/detail";
	}
}