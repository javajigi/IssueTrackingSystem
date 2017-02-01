package infinitefire.project.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/milestone")
public class MilestoneController {
	
	@GetMapping("/new")
	public String form() {
		return "milestone/form";
	}

	@GetMapping("/list")
	public String show() {
		return "milestone/list";
	}
	
}
