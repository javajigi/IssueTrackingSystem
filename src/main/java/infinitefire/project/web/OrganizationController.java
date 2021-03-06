package infinitefire.project.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import infinitefire.project.domain.CommentRepository;
import infinitefire.project.domain.Issue;
import infinitefire.project.domain.IssueRepository;
import infinitefire.project.domain.LabelRepository;
import infinitefire.project.domain.MilestoneRepository;
import infinitefire.project.domain.Organization;
import infinitefire.project.domain.OrganizationRepository;
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
	public String createGroup(@LoginUser User loginUser, Organization organization, String memberList) {
		log.debug("Post-Group-New >>");
		organization.setOrganizationMaker(loginUser);
		Set<User> members = new HashSet<User>();
		members.add(userRepository.findOne(loginUser.getId()));
		try {
			String[] memberIds = memberList.split(",");
			for(String strId : memberIds) {
				long id = Long.parseLong(strId);
				members.add(userRepository.findOne(id));
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		organization.setMemberList(members);
		organizationRepository.save(organization);
		return "redirect:/";
	}
	

	@GetMapping("/{groupId}/modify")
	public String modifyOrganization(@LoginUser User loginUser, @PathVariable Long groupId, Model model) {
		log.debug("Access-Get-modify >>");
		
		Organization group = organizationRepository.findOne(groupId);
		
		if(group.isMatchWriter(loginUser)) {
			model.addAttribute("modifyGroup", group);
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
		Organization group = organizationRepository.findOne(groupId);
		model.addAttribute("group", group);
		
		boolean isOwner = group.isMatchWriter(loginUser);
		model.addAttribute("owner", isOwner);
		
		Set<User> assigneeList = group.getMemberList();
		model.addAttribute("assigneeList", assigneeList);
		
		return "/organization/detail";
	}
	
	@PostMapping("/{groupId}/like")
	public @ResponseBody Organization like(@LoginUser User loginUser, 
											@PathVariable Long groupId) {
		Organization organization= organizationRepository.findOne(groupId);
		/* 권한 관리
		 *
		*/
		organization.toggleState();
		return organizationRepository.save(organization);
	}
	
	@PostMapping("/list")
	public @ResponseBody Set<Organization> list(@LoginUser User loginUser) {
		/* 권한 관리
		 *
		*/
		User user = userRepository.findOne(loginUser.getId());
		Set<Organization> organizationList = user.getOrganizationList();
		
		return organizationList;
	}
}
