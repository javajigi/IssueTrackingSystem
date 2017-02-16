package infinitefire.project.web;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import infinitefire.project.domain.CommentRepository;
import infinitefire.project.domain.Issue;
import infinitefire.project.domain.IssueRepository;
import infinitefire.project.domain.IssueState;
import infinitefire.project.domain.LabelRepository;
import infinitefire.project.domain.MilestoneRepository;
import infinitefire.project.domain.Organization;
import infinitefire.project.domain.OrganizationRepository;
import infinitefire.project.domain.OrganizationState;
import infinitefire.project.domain.User;
import infinitefire.project.domain.UserRepository;
import infinitefire.project.security.LoginUser;

@Controller
@RequestMapping("/group")
public class OrganizationController {
	@Autowired
	OrganizationRepository organizationRepository;
	@Autowired
	MilestoneRepository milestoneRepository;
	@Autowired
	IssueRepository issueRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CommentRepository commentRepository;
	@Autowired
	LabelRepository labelRepository;
	
	private static final Logger log = LoggerFactory.getLogger(OrganizationController.class);
	
	@GetMapping("/new")
	public String createGroupForm(@LoginUser User loginUser, Model model) {
		log.debug("Access >> /group/new");
		model.addAttribute("allUser", userRepository.findAll());
		return "organization/new";
	}
	
	@PostMapping("/new")
	public String createGroup(@LoginUser User loginUser, Organization organization, String assigneeList) {
		log.debug("Post-Group-New >>");
		try {
			String[] assigneeIds = assigneeList.split(",");
			List<User> assignees = new ArrayList<User>();
			for(String strId : assigneeIds) {
				long id = Long.parseLong(strId);
				assignees.add(userRepository.findOne(id));
			}
			organization.setAsigneeList(assignees);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		organization.setOrganizationMaker(loginUser);
		organization.setOrganizationState(OrganizationState.INITIALIZE);
		organizationRepository.save(organization);
		return "redirect:/";
	}
	
	@PostMapping("/list")
	public String totalGroupList() {
		return "";
	}
	
	@GetMapping("/{groupId}/modify")
	public String modifyOrganization(@LoginUser User loginUser, @PathVariable Long groupId, Model model) {
		log.debug("Access-Get-modify >>");
		
		Organization modify = organizationRepository.findOne(groupId);
		
		if(modify.isMatchWriter(loginUser)) {
			model.addAttribute("modifyGroup", modify);
			return "group/modify";
		} else
			return "redirect:/";
	}
	
	@PostMapping("/{groupId}/modify")
	public String modifiedOrganization(@LoginUser User loginUser, @PathVariable Long groupId) {
		return "";
	}
	
	@GetMapping("/{groupId}/delete")
	public String deleteOrganization(@LoginUser User loginUser) {
		
		return "redirect:/";
	}
	
	@GetMapping("/{groupId}/detail")
	public String showGroupDetail(@LoginUser User loginUser, @PathVariable Long groupId, Model model) {
		log.debug("Access-Get-deatil >>");
		List<Issue> groupIssueList = issueRepository.findByOrganizationIdAndState(groupId, IssueState.OPEN);
		model.addAttribute("issueList", groupIssueList);
		
		return "/issue/list";
	}
}
