package infinitefire.project.web;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import infinitefire.project.domain.Organization;
import infinitefire.project.domain.OrganizationRepository;
import infinitefire.project.domain.OrganizationState;
import infinitefire.project.domain.User;
import infinitefire.project.domain.UserRepository;
import infinitefire.project.security.LoginUser;
import infinitefire.project.utils.HttpSessionUtils;

@Controller
public class MainController {
	private static final Logger log = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	OrganizationRepository organizationRepository;
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/back")
	public String backToPage(HttpServletRequest req) {
		String getContextPath = req.getHeader("REFERER");
		log.debug("Replace >> "+getContextPath.replace("http://localhost:8080/", ""));
		return "redirect:/"+getContextPath.replace("http://localhost:8080/", "");
	}
	
	@GetMapping("/")
	public String index(@LoginUser User loginUser, Model model) {
//		List<Organization> groupList = null;
//		List<Organization> myGroupList = null;
//		if(loginUser != null) {
//			groupList = getAssignedOrganization(loginUser);
//			myGroupList = getMyOrganization(loginUser);
//		}
//		model.addAttribute("myGroupList", myGroupList);
//		model.addAttribute("groupList", groupList);
		User user = userRepository.findOne(loginUser.getId());
		Set<Organization> organizationList = user.getOrganizationList();
		
		Set<Organization> favoriteList = getOrganizationList(organizationList, OrganizationState.favorite);
		Set<Organization> ordinaryList = getOrganizationList(organizationList, OrganizationState.ordinary);
		model.addAttribute("favoriteList", favoriteList);
		model.addAttribute("ordinaryList", ordinaryList);


//		log.debug(organizationList.size() + "");
		
		return "index";
	}
	
	public Set<Organization> getOrganizationList(Set<Organization> organizationList, OrganizationState state) {
		Set<Organization> resultSet = new HashSet<>();
		for (Organization org : organizationList) {
			if(org.getState().equals(state)) {
				resultSet.add(org);
			}
		}
		return resultSet;
	}
	
	public List<Organization> getMyOrganization(User user) {
		List<Organization> allGroup = organizationRepository.findAll();
		List<Organization> myGroup = null;
		
		for (Iterator<Organization> iter = allGroup.iterator(); iter.hasNext();) {
			Organization group = iter.next();
			if (!group.isMatchWriter(user))
				iter.remove();
		}
		myGroup = allGroup;
		return myGroup;
	}
	
	public List<Organization> getAssignedOrganization(User user) {
		List<Organization> allGroup = organizationRepository.findAll();
		List<Organization> assignedGroup = null;
		
		for (Iterator<Organization> iter = allGroup.iterator(); iter.hasNext();) {
			Organization group = iter.next();
			if (!group.isAssignee(user) || group.isMatchWriter(user))
				iter.remove();
		}
		assignedGroup = allGroup;
		return assignedGroup;
	}
}
