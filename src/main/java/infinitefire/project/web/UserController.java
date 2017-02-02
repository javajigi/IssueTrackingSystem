package infinitefire.project.web;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import infinitefire.project.domain.User;
import infinitefire.project.domain.UserRepository;
import infinitefire.project.utils.HttpSessionUtils;

@Controller
@RequestMapping("/user")
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);	
	
	@Autowired
	private UserRepository userRepository;
	
	public UserController() {
		LogManager.getRootLogger().setLevel(Level.DEBUG);
	}
	
	@GetMapping("/new")
	public String newPage() {
		log.debug("/user/new [GET] - newPage()");
		
		return "/user/new";
	}
	
	@PostMapping("/new")
	public String newUser(User newUser) {
		log.debug("/user/new [POST] - newUser()");
		newUser.setProfileUrl("none");
		newUser.setState("alive");
		userRepository.save(newUser);
		
		return "redirect:/user/login";
	}
	
	@GetMapping("/login")
	public String loginPage() {
		log.debug("/user/login [GET] - loginPage()");
		
		return "/user/login";
	}
	
	@PostMapping("/login")
	public String login(String userId, String password, HttpSession session) {
		log.debug("/user/login [post] - login()");
		log.debug("userId=" + userId + ", password=" + password);
		User loginUser = userRepository.findByUserId(userId);
		if (loginUser == null) {	// 예외처리, 유저가 존재하지 않는 경우
			log.debug("해당하는 유저가 존재하지 않습니다!");
			return "redirect:/user/login";
		}
		
		if (!loginUser.isMatchPassword(password)) {
			log.debug("로그인 실패. 비밀번호를 확인해주세요.");
			return "redirect:/user/login";
		}
		
		log.debug("로그인 성공. inputId=" + userId + ", inputPw=" + password);
		session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, loginUser);
		log.info("Session : " + session.getAttribute(HttpSessionUtils.USER_SESSION_KEY));
		return "redirect:/";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		log.debug("/user/logout [GET] logout()");
		session.removeAttribute("loginUser");
		
		return "redirect:/user/login";
	}
	
	@PutMapping("/modify")
	public String update() {
		return "/";
	}
	
	
	@DeleteMapping("/{id}/delete")
	public String userGoOut() {
		return "redirect:/";
	}
	
}
