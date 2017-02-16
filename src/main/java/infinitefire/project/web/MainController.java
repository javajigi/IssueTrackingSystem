package infinitefire.project.web;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import infinitefire.project.domain.IssueRepository;
import infinitefire.project.domain.Organization;
import infinitefire.project.domain.OrganizationRepository;
import infinitefire.project.domain.User;
import infinitefire.project.utils.HttpSessionUtils;

@Controller
public class MainController {
	private static final Logger log = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private OrganizationRepository organizationRepository;
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
	public String index(HttpSession session, Model model) {
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		List<Organization> groupList, myGroupList, list;
		myGroupList = list = organizationRepository.findAll();
		if(loginUser != null) {
			for (Iterator<Organization> iter = list.iterator(); iter.hasNext();) {
				Organization group = iter.next();
				if (!group.isAssignee(loginUser))
					iter.remove();
			}
			groupList = organizationRepository.findAll();
		} else {
			myGroupList = null;
			groupList = organizationRepository.findAll();
		}
		model.addAttribute("myGroupList", myGroupList);
		model.addAttribute("groupList", groupList);
		return "index";
	}
}
