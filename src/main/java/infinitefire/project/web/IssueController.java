package infinitefire.project.web;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import infinitefire.project.domain.Comment;
import infinitefire.project.domain.CommentRepository;
import infinitefire.project.domain.Issue;
import infinitefire.project.domain.IssueRepository;
import infinitefire.project.domain.IssueState;
import infinitefire.project.domain.Label;
import infinitefire.project.domain.LabelRepository;
import infinitefire.project.domain.Milestone;
import infinitefire.project.domain.MilestoneRepository;
import infinitefire.project.domain.User;
import infinitefire.project.domain.UserRepository;
import infinitefire.project.security.GetContextPath;
import infinitefire.project.security.LoginUser;

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

	@GetMapping("/back")
	public String backToPage(HttpServletRequest req) {
		String getContextPath = req.getHeader("REFERER");
		//getContextPath.replace("http://localhost:8080/", "");
		log.debug("Replace >> "+getContextPath.replace("http://localhost:8080/", ""));
		return "redirect:/"+getContextPath.replace("http://localhost:8080/", "");
	}
	
	@GetMapping("/")
	public String index(@GetContextPath String getContextPath, Model model, HttpServletRequest request) {
		
		List<Issue> issueList = issueRepository.findAll();
		List<Issue> openedIssueList = issueRepository.findByState(IssueState.OPEN);
		List<Issue> closedIssueList = issueRepository.findByState(IssueState.CLOSE);
    model.addAttribute("issueList", issueList);
		model.addAttribute("opendList", openedIssueList);
		model.addAttribute("closedList", closedIssueList);
		log.debug("GetContextPath : "+request.getHeader("REFERER"));
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
	public String showIssueDetail(@LoginUser User loginUser, @PathVariable Long id, HttpServletRequest req, Model model) {
		log.debug("Access >> /issue/{" + id + "}/detail : --"+req.getHeader("REFERER"));
		Issue issue = issueRepository.findOne(id);
		model.addAttribute("issueInfo", issue);

		// TODO 데이터 목록을 조회할 때 어떤 기준을 가지고 정렬을 하도록 개선해 보면 좋겠네요.
		model.addAttribute("allLabel", labelRepository.findAll());
		model.addAttribute("allUser", userRepository.findAll());
		model.addAttribute("allMilestone", milestoneRepository.findAll());
		model.addAttribute("loginUser", loginUser);

		// TODO 이 로직을 issue에서 구현하면 어떻게 될까요? 그런데 isMyComment 속성을 이처럼 매번 처리해야 되나요? 다른 방법은 없을까요?
		for(Comment comment : issue.getCommentList()) {
			if (comment.isMyComment(loginUser.getId()))
				comment.setIsMyComment(true);
		}

		log.debug("View Issue Property : " + issue);
		return "issue/detail";
	}

	@PostMapping("/issue/{issueId}/modifyState")
	public @ResponseBody Issue modifyState(@PathVariable Long issueId,
									       @RequestParam(value="check") boolean isChecked) {
		Issue issue = issueRepository.findOne(issueId);
		/* 권한 관리
		 *
		*/
		issue.toggleState(isChecked);
		return issueRepository.save(issue);
	}

	@PutMapping("/issue/{issueId}/addAssignee/{userId}")
	public @ResponseBody User addAssignee(@LoginUser User loginUser,
										  @PathVariable Long issueId,
										  @PathVariable Long userId) {
		Issue issue = issueRepository.findOne(issueId);
		User assignee = userRepository.findOne(userId);
		/* 권한 관리
		 *
		*/
		if(issue.addAssignee(assignee)) {
			issueRepository.save(issue);
			return assignee;
		}
		return null;
	}
	
	@DeleteMapping("/issue/{issueId}/deleteAssignee/{userId}")
	public @ResponseBody boolean deleteAssignee(@LoginUser User loginUser,
								  @PathVariable Long issueId,
								  @PathVariable Long userId) {
		
		Issue issue = issueRepository.findOne(issueId);
		User assignee = userRepository.findOne(userId);
		/* 권한 관리
		 *
		*/
		boolean isDelete = issue.deleteAssignee(assignee); 
		if(isDelete) {
			issueRepository.save(issue);
		}
		return isDelete;
	}
	
	@PutMapping("/issue/{issueId}/setMilestone/{milestoneId}")
	public @ResponseBody Milestone setMilestone(@LoginUser User loginUser,
										  @PathVariable Long issueId,
										  @PathVariable Long milestoneId) {
		Issue issue = issueRepository.findOne(issueId);
		Milestone milestone = milestoneRepository.findOne(milestoneId);
		/* 권한 관리
		 * 	*
		 * 	*/
		issue.setMilestone(milestone);
		issueRepository.save(issue);
		return milestone;
	}
	
	@DeleteMapping("/issue/{issueId}/deleteMilestone/{milestoneId}")
	public @ResponseBody Milestone deleteMilestone(@LoginUser User loginUser,
										  @PathVariable Long issueId,
										  @PathVariable Long milestoneId) {
		Issue issue = issueRepository.findOne(issueId);
		Milestone milestone = milestoneRepository.findOne(milestoneId);
		/* 권한 관리
		 * 	*
		 * 	*/
		if(issue.deleteMilestone(milestone)) {
			log.info("milestone");
			issueRepository.save(issue);
			return milestone;
		}
		return null;
	}

	@PutMapping("/issue/{issueId}/addLabel/{labelId}")
	public @ResponseBody Label addLabel(@LoginUser User loginUser,
										  @PathVariable Long issueId,
										  @PathVariable Long labelId) {
		Issue issue = issueRepository.findOne(issueId);
		Label label = labelRepository.findOne(labelId);
		/* 권한 관리
		 *
		*/
		if(issue.addLabel(label)) {
			issueRepository.save(issue);
			return label;
		}
		return null;
	}
	
	@DeleteMapping("/issue/{issueId}/deleteLabel/{labelId}")
	public @ResponseBody boolean deleteLabel(@LoginUser User loginUser,
								  @PathVariable Long issueId,
								  @PathVariable Long labelId) {
		
		Issue issue = issueRepository.findOne(issueId);
		Label label = labelRepository.findOne(labelId);
		/* 권한 관리
		 *
		*/
		boolean isDelete = issue.deleteLabel(label); 
		if(isDelete) {
			issueRepository.save(issue);
		}
		return isDelete;
	}
}
