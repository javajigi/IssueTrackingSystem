package infinitefire.project.web;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import infinitefire.project.domain.Milestone;
import infinitefire.project.domain.MilestoneRepository;
import infinitefire.project.utils.HttpSessionUtils;

@Controller
@RequestMapping("/milestone/")
public class MilestoneController {
	
	private static final Logger log = LoggerFactory.getLogger(MilestoneController.class);
		
	@Autowired
	private MilestoneRepository milestoneRepository;
	
	@GetMapping("/new")
	public String createform(HttpSession session) {		
		if(HttpSessionUtils.isLoginUser(session)) {
			return "/milestone/new";
		}else {
			return "/user/login";
		}
	}
	
	@PostMapping("/new")
	public String create(Milestone milestone, HttpSession session){
		
		if(HttpSessionUtils.isLoginUser(session)) {
			milestoneRepository.save(milestone);
			return "redirect:/milestone/list";
		} else {
			return "redirect:/user/login";
		}
	}
	
	
	@GetMapping("/list")
	public String show(Model model) {			
		model.addAttribute("milestones", milestoneRepository.findAll());
		return "milestone/list";
	}
	
	@GetMapping("/{id}/detail")
	public String detail(@PathVariable long id, Model model){
		
		Milestone milestone = milestoneRepository.findOne(id);
		model.addAttribute("milestone", milestone);
		
		return "milestone/detail";
	}
	
}
