package infinitefire.project.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import infinitefire.project.domain.MilestoneRepository;

@Controller
@RequestMapping("/milestone")
public class MilestoneController {
	
	@Autowired
	private MilestoneRepository milestoneRepository;
	
	@GetMapping("/new")
	public String form() {
		return "milestone/form";
	}
	
	@GetMapping("/list")
	public String show(Model model) {
		model.addAttribute("milestones", milestoneRepository.findAll());
		return "milestone/list";
	}
	
	
}
