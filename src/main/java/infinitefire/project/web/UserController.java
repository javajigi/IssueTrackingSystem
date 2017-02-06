package infinitefire.project.web;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import infinitefire.project.domain.User;
import infinitefire.project.domain.UserRepository;
import infinitefire.project.domain.UserState;
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
		log.debug("/user/new [{}] - newPage()", HttpMethod.GET);
		
		return "/user/new";
	}
	
	@PostMapping("/new")
	public String newUser(User newUser) {
		log.debug("/user/new [{}] - newUser()", HttpMethod.POST);
		if (userRepository.findByUserId(newUser.getUserId()) != null) {
			log.debug("해당 아이디는 사용 할 수 없습니다.");
			return "redirect:/user/new";
		}
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
		
		if (loginUser.isWithdraw()) {
			log.debug("이미 탈퇴한 유저입니다.");
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
	
	@GetMapping("/login_error")
	public String loginError() {
		return "/user/login";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		log.debug("/user/logout [GET] logout()");
		session.removeAttribute(HttpSessionUtils.USER_SESSION_KEY);
		
		return "redirect:/user/login";
	}
	
	@GetMapping("/{id}/modify")
	public String modifyPage(@PathVariable long id, Model model, HttpSession session) {
		log.debug("/{id}/modify [GET] - modifyPage()");
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/user/login";
		}
		
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		if (!loginUser.isMatchId(id)) {
			log.debug("해당 유저의 정보를 수정할 권한이 없습니다.");
			return "/user/login";
		}
		
		User user = userRepository.findOne(id);
		model.addAttribute("user", user);
		return "/user/modify";
	}
	
	@GetMapping("/{id}/detail")
	public String profile(@PathVariable Long id, Model model) {
		User selectedUser = userRepository.findOne(id);
		model.addAttribute("user", selectedUser);
		
		return "/user/detail";
	}
	
	@PutMapping("/{id}/modify")
	public String modify(@PathVariable Long id, User modifiedUser, String newPassword, HttpSession session) {
		log.debug("/user/{id}/modify [PUT] - modify()");
		log.debug("newPassword : " + newPassword);
		log.debug("Before : " + modifiedUser.toString());
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/user/login";
		}
		
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		if (!loginUser.isMatchId(id)) {
			log.debug("해당 유저의 정보를 수정할 권한이 없습니다.");
			return "/user/login";
		}
		if (!loginUser.isMatchPassword(modifiedUser.getPassword())) {	
			log.debug("해당 유저의 정보를 수정할 권한이 없습니다.");
			return "/user/login";
		}
		
		if (!modifiedUser.isMatchPassword(newPassword))
			modifiedUser.setPassword(newPassword);
		
		log.debug("After : " + modifiedUser.toString());
		User user = userRepository.findOne(id);
		user.modify(modifiedUser);
		userRepository.save(user);		
		return "redirect:/";
	}
	
	@GetMapping("/{id}/delete")
	public String delete(@PathVariable long id, Model model, HttpSession session) {
		log.debug("/user/{id}/delete [GET] - delete()");
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/user/login";
		}
		
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		if (!loginUser.isMatchId(id)) {
			log.debug("해당 유저를 탈퇴시킬 권한이 없습니다.");
			return "/user/login";
		}
		User outUser = userRepository.findOne(id);
		model.addAttribute("user", outUser);
		
		return "/user/delete";
	}
	
	@DeleteMapping("/{id}/delete")
	public String delete(@PathVariable Long id, String password, HttpSession session) {
		log.debug("/user/{id}/delete [DELETE] - delete()");
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/user/login";
		}
		
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		if (!loginUser.isMatchId(id)) {
			log.debug("해당 유저를 탈퇴시킬 권한이 없습니다.");
			return "/user/login";
		}
		
		if (!loginUser.isMatchPassword(password)) {
			log.debug("비밀번호가 일치하지 않아 탈퇴할 수 없습니다.");
			return "/user/delete";
		}
		
		User outUser = userRepository.findOne(id);
		outUser.withdraw();
		userRepository.save(outUser);
		session.removeAttribute(HttpSessionUtils.USER_SESSION_KEY);
		
		return "redirect:/user/login";
	}
}
