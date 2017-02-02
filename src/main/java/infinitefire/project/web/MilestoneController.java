package infinitefire.project.web;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import infinitefire.project.domain.Milestone;
import infinitefire.project.domain.MilestoneRepository;

@Controller
@RequestMapping("/milestone/*")
public class MilestoneController {
	
	private static final Logger log = LoggerFactory.getLogger(MilestoneController.class);
		
	@Autowired
	private MilestoneRepository milestoneRepository;
	
	@GetMapping("/new")
	public String createform() {
		return "milestone/new";
	}
	
	
	@PostMapping("/new")
	public String create(Milestone milestone){
		
		milestoneRepository.save(milestone);
		log.info("milestone : " + milestone.getSubject());
		return "redirect:/milestone/list";
	}
	
	
	@GetMapping("/list")
	public String show(Model model) {
		model.addAttribute("milestones", milestoneRepository.findAll());
		return "milestone/list";
	}
	
}
