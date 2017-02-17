package infinitefire.project.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import infinitefire.project.domain.Milestone;
import infinitefire.project.domain.MilestoneRepository;
import infinitefire.project.domain.Organization;
import infinitefire.project.domain.OrganizationRepository;
import infinitefire.project.domain.User;
import infinitefire.project.security.LoginUser;

@Controller
public class MilestoneController {
	private static final Logger log = LoggerFactory.getLogger(MilestoneController.class);
	
	@Autowired
	private OrganizationRepository organizationRepository;
	@Autowired
	private MilestoneRepository milestoneRepository;
	
	@GetMapping("/group/{organizationId}/milestone/new")
	public String createform(@LoginUser User loginUser, @PathVariable Long organizationId, Model model) {
		model.addAttribute("organizationId", organizationId);
		return "/milestone/new";
	}
	
	@PostMapping("/group/{organizationId}/milestone/new")
	public String create(@LoginUser User loginUser, @PathVariable Long organizationId, Milestone milestone, Model model){
		log.debug("Access >> /milestone/new - " + milestone);
		model.addAttribute("organizationId", organizationId);
		Organization organization = organizationRepository.findOne(organizationId);
		milestone.setOrganization(organization);
		milestoneRepository.save(milestone);
		return "redirect:/group/"+organizationId+"/milestone/list";
	}
	
	
	@GetMapping("/group/{organizationId}/milestone/list")
	public String show(@LoginUser User loginUser, @PathVariable Long organizationId, Model model) {
		Organization organization = organizationRepository.findOne(organizationId);
		List<Milestone> milestoneList = (List<Milestone>) milestoneRepository.findByOrganizationOrderByStartDateDesc(organization);
		model.addAttribute("milestones", milestoneList);
		model.addAttribute("organizationId", organizationId);
		
		return "/milestone/list";
	}
	
	@GetMapping("/group/{organizationId}/milestone/{id}/detail")
	public String detail(@LoginUser User loginUser, @PathVariable Long organizationId, @PathVariable Long id, Model model){
		Milestone milestone = milestoneRepository.findOne(id);
		model.addAttribute("milestone", milestone);
		model.addAttribute("organizationId", organizationId);
		
		return "milestone/detail";
	}
	
}
