package net.slipp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IssueController {

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/issue/new")
	public String form() {
		return "issue/form";
	}

	@GetMapping("/issue/list")
	public String show() {
		return "issue/show";
	}

}