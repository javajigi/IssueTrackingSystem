package infinitefire.project.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import infinitefire.project.domain.Milestone;
import infinitefire.project.domain.MilestoneRepository;
import infinitefire.project.domain.User;
import infinitefire.project.security.LoginUser;

@Controller
@RequestMapping("/milestone/")
public class MilestoneController {
	// TODO 사용하지 않는 코드이면 제거한다.
	private static final Logger log = LoggerFactory.getLogger(MilestoneController.class);
		
	@Autowired
	private MilestoneRepository milestoneRepository;
	
	@GetMapping("/new")
	public String createform(@LoginUser User loginUser) {		
		return "/milestone/new";
	}
	
	@PostMapping("/new")
	public String create(@LoginUser User loginUser, Milestone milestone){
		log.debug("Access >> /milestone/new - " + milestone);
		
		milestoneRepository.save(milestone);
		return "redirect:/milestone/list";
	}
	
	
	@GetMapping("/list")
	public String show(@LoginUser User loginUser, Model model) {
		List<Milestone> milestoneList = (List<Milestone>) milestoneRepository.findAll();
		model.addAttribute("milestones", milestoneList);
		
		return "milestone/list";
	}
	
	@GetMapping("/{id}/detail")
	public String detail(@LoginUser User loginUser, @PathVariable Long id, Model model, HttpServletRequest req){
		log.debug("getContextPath : "+req.getHeader("REFERER"));
		Milestone milestone = milestoneRepository.findOne(id);
		model.addAttribute("milestone", milestone);
		
		return "milestone/detail";
	}
	
}
