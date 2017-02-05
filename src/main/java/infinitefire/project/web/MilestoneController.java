package infinitefire.project.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import infinitefire.project.domain.Milestone;
import infinitefire.project.domain.MilestoneRepository;
import infinitefire.project.domain.User;
import infinitefire.project.security.LoginUser;

@Controller
@RequestMapping("/milestone/")
public class MilestoneController {
	@Autowired
	private MilestoneRepository milestoneRepository;

	@GetMapping("/new")
	public String createform(@LoginUser User loginUser) {
		return "/milestone/new";
	}

	@PostMapping("/new")
	public String create(@LoginUser User loginUser, Milestone milestone) {
		milestoneRepository.save(milestone);
		return "redirect:/milestone/list";
	}

	@GetMapping("/list")
	public String show(Model model) {
		model.addAttribute("milestones", milestoneRepository.findAll());
		return "milestone/list";
	}

}
