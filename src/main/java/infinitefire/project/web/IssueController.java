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
import infinitefire.project.utils.HttpSessionUtils;

@Controller
public class IssueController {
	@Autowired
	IssueRepository issueRepository;
	
	private static final Logger log = LoggerFactory.getLogger(IssueController.class);

	@GetMapping("/")
	public String index(Model model) {
		log.debug("Access >> /");
		
		model.addAttribute("issueList", issueRepository.findAll());
		return "index";
	}

	@GetMapping("/issue/new")
	public String createIssueForm(HttpSession session) {
		log.debug("Access >> /issue/new-Get");
		
		if(!HttpSessionUtils.isLoginUser(session)) {
			log.info("info >> is not LoginUser");
			return "issue/new";
		}else {
			log.info("info >> is LoginUser");
			return "redirect:/";
		}
	}
	@PostMapping("/issue/new")
	public String createIssue(HttpSession session, Issue newIssue) {
		log.debug("Access >> /issue/new-Post");
		
		if(!HttpSessionUtils.isLoginUser(session)) {
			log.info("info >> is not LoginUser");
			
			issueRepository.save(newIssue);
			return "redirect:/";
		}else {
			return "redirect:/";
		}
	}
	
	@GetMapping("issue/{id}/modify")
	public String modifyIssueForm(HttpSession session, @PathVariable Long id, Model model) {
		log.debug("Access >> /issue/modify");
		
		if(HttpSessionUtils.isLoginUser(session)) {
			model.addAttribute("issue", issueRepository.findOne(id));
			return "issue/modify";
		}else
			return "redirect:/";
	}
	@PutMapping("issue/{id}/modify")
	public String modifiedIssue(HttpSession session, @PathVariable Long id, Issue modifIssue) {
		log.debug("Access >> /issue/{" + id +"}/modify");
		
		if(HttpSessionUtils.isLoginUser(session)) {
			issueRepository.save(modifIssue);
			return "/";
		}else
			return "redirect:/";
	}
	
	@DeleteMapping("issue/{id}/delete")
	public String deleteIssue(HttpSession session, @PathVariable Long id) {
		log.debug("Access >> /issue/{" + id + "}/delete");
		
		if(HttpSessionUtils.isLoginUser(session)) {
			issueRepository.delete(id);
			return "/";
		}else
			return "redirect:/";
	}

	@GetMapping("/issue/detail")
	public String showIssueDetail() {
		return "issue/detail";
	}
}