package infinitefire.project.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import infinitefire.project.domain.Organization;
import infinitefire.project.domain.OrganizationRepository;
import infinitefire.project.domain.User;
import infinitefire.project.domain.UserRepository;
import infinitefire.project.security.LoginUser;

@Controller
//@RequestMapping("/issue")
public class IssueController {
	@Autowired
	OrganizationRepository organizationRepository;
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

	@GetMapping("/group/{organizationId}/issue/new")
	public String createIssueForm(@LoginUser User loginUser, @PathVariable Long organizationId,  Model model) {
		log.debug("Access >> /issue/new-Get");
		model.addAttribute("allLabel", labelRepository.findAll());
		model.addAttribute("allUser", userRepository.findAll());
		model.addAttribute("allMilestone", milestoneRepository.findAll());
		model.addAttribute("organizationId", organizationId);
		return "/issue/new";
	}
	@PostMapping("/group/{organizationId}/issue/new")
	public String createIssue(@LoginUser User loginUser, @PathVariable Long organizationId, Issue issue,
							  String assigneeList, String milestone, String labelList) {
		
		try {
			long id = Long.parseLong(milestone);
			issue.setMilestone(milestoneRepository.findOne(id));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} 
		
		try {
			String[] assigneeIds = assigneeList.split(",");
			List<User> assignees = new ArrayList<User>();
			for(String strId : assigneeIds) {
				long id = Long.parseLong(strId);
				assignees.add(userRepository.findOne(id));
			}
			issue.setAssigneeList(assignees);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} 
		
		try {
			String[] labelIds = labelList.split(",");
			List<Label> labels = new ArrayList<Label>();
			for(String strId : labelIds) {
				long id = Long.parseLong(strId);
				labels.add(labelRepository.findOne(id));
			}
			issue.setLabelList(labels);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} 
		
		issue.setOrganization(organizationRepository.findOne(organizationId));
		issue.setWriter(loginUser);
		issueRepository.save(issue);
		return "redirect:/group/"+organizationId+"/issue/list";
	}
	
	@GetMapping("/group/{groupId}/issue/list")
	public String index(@LoginUser User loginUser, @PathVariable Long groupId, @RequestParam(value="state",  defaultValue = "OPEN") IssueState state, 
						Model model) {
		Organization organization = organizationRepository.findOne(groupId);
		if (!organization.isAssignee(loginUser)) {
			log.debug("Group에 접근할 권한이 없습니다.");
			return "redirect:/";
		}
			
		List<Issue> issueList;
		if(state.equals(IssueState.OPEN)){
			issueList = issueRepository.findByOrganizationAndState(organization, IssueState.OPEN);
			model.addAttribute("isOpen", true);
		} else {
			issueList = issueRepository.findByOrganizationAndState(organization, IssueState.CLOSE);
			model.addAttribute("isOpen", false);
		}
		model.addAttribute("issueList", issueList);
		model.addAttribute("organizationId", groupId);
		return "/issue/list";
	}

	@GetMapping("/group/{organizationId}/issue/{id}/modify")
	public String modifyIssueForm(@LoginUser User loginUser, @PathVariable Long organizationId, @PathVariable Long id, Model model) {
		log.debug("Access >> /issue/modify");

		Issue modifyIssue = issueRepository.findOne(id);

		if(modifyIssue.isMatchWriter(loginUser)){
			model.addAttribute("modifyIssue", modifyIssue);
			model.addAttribute("organizationId", organizationId);
			return "issue/modify";
		} else
			return "redirect:/group/"+organizationId+"/issue/list";
	}
	@PostMapping("/group/{organizationId}/issue/{id}/modify")
	public String modifiedIssue(@LoginUser User loginUser, @PathVariable Long organizationId, @PathVariable Long id,
			String subject, String contents, Model model) {
		log.debug("Access >> /issue/{" + id +"}/modify-put");

		Issue modifyIssue = issueRepository.findOne(id);
		if(modifyIssue.isMatchWriter(loginUser)) {
			modifyIssue.setSubject(subject);
			modifyIssue.setContents(contents);
			issueRepository.save(modifyIssue);

			model.addAttribute("issueInfo", modifyIssue);
			model.addAttribute("organizationId", organizationId);
			return "/issue/detail";
		} else
			return "redirect:/group/"+organizationId+"/issue/list";
	}

	@GetMapping("/group/{organizationId}/issue/{id}/delete")
	public String deleteIssue(@LoginUser User loginUser, @PathVariable Long organizationId, @PathVariable Long id) {
		log.debug("Access >> /issue/{" + id + "}/delete");

		Issue deleteIssue = issueRepository.findOne(id);
		if(deleteIssue.isMatchWriter(loginUser))
			issueRepository.delete(id);

		return "redirect:/group/"+organizationId+"/issue/list";
	}

	@GetMapping("/group/{organizationId}/issue/{id}/detail")
	public String showIssueDetail(@LoginUser User loginUser,@PathVariable Long organizationId, @PathVariable Long id, HttpServletRequest req, Model model) {
		log.debug("Access >> /issue/{" + id + "}/detail : --"+req.getHeader("REFERER"));		
		Issue issue = issueRepository.findOne(id);
		model.addAttribute("issueInfo", issue);

		// TODO 데이터 목록을 조회할 때 어떤 기준을 가지고 정렬을 하도록 개선해 보면 좋겠네요.
		model.addAttribute("allLabel", labelRepository.findAll());
		model.addAttribute("allUser", userRepository.findAll());
		model.addAttribute("allMilestone", milestoneRepository.findAll());
		model.addAttribute("loginUser", loginUser);
		model.addAttribute("organizationId", organizationId);

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
	
	@PostMapping("/group/{groupId}/sortby/{sortId}/{state}")
	public @ResponseBody List<Issue> sortIssue(@LoginUser User loginUser, @PathVariable Long groupId, @PathVariable String sortId,
			@PathVariable String state) {
		log.debug("Access sortby-post >> "+Boolean.valueOf(state));
		IssueState issueState;
		if(Boolean.valueOf(state))
			issueState = IssueState.OPEN;
		else
			issueState = IssueState.CLOSE;
		List<Issue> getIssueList = null;
		Organization organization = organizationRepository.findOne(groupId);
		switch (sortId) {
		case "descDate":
			getIssueList = issueRepository.findByOrganizationAndStateOrderByWriteDateAsc(organization, issueState);
			break;
		case "ascDate":
			getIssueList = issueRepository.findByOrganizationAndStateOrderByWriteDateDesc(organization, issueState);
			break;
		case "ascComment":
			getIssueList = issueRepository.findByOrganizationAndStateOrderByCountCommentAsc(organization, issueState);
			break;
		case "descComment":
			getIssueList = issueRepository.findByOrganizationAndStateOrderByCountCommentDesc(organization, issueState);
			break;
		}
		return getIssueList;
	}
}
