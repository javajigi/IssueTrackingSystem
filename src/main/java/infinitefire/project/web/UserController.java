package infinitefire.project.web;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import infinitefire.project.domain.User;
import infinitefire.project.domain.UserRepository;
import infinitefire.project.security.LoginUser;
import infinitefire.project.storage.FileType;
import infinitefire.project.storage.StorageService;
import infinitefire.project.utils.HttpSessionUtils;

@Controller
@RequestMapping("/user")
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepository;
	
	private final StorageService storageService;
	
	@Value("${file.upload_path}")
	private String basicPath; 
	
	@Value("${file.upload_path_profile}")
	private String profilePath;
	
	public UserController(StorageService storageService) {
		this.storageService = storageService;
	}
	
	@GetMapping("/new")
	public String newPage() {
		log.debug("/user/new [{}] - newPage()", HttpMethod.GET);
		
		return "/user/new";
	}
	
	@PostMapping("/new")
	public String newUser(User newUser, @RequestParam("file") MultipartFile file) {
		log.debug("/user/new [{}] - newUser()", HttpMethod.POST);
		if (userRepository.findByUserId(newUser.getUserId()) != null) {
			log.debug("해당 아이디는 사용 할 수 없습니다.");
			return "redirect:/user/new";
		}

		if (!file.isEmpty()) {	// 업로드 한 파일이 있을 경우
			String newFileName = UUID.randomUUID().toString() + ".png";
			storageService.store(file, newFileName, FileType.PROFILE);
			newUser.setProfile(newFileName);
		} else {	// 업로드한 파일이 없을 경우에 대한 예외처리
			newUser.setDefaultProfile();
		}
		userRepository.save(newUser);
		
		return "redirect:/user/login";
	}
	
	@GetMapping("/login")
	public String loginPage() {
		log.debug("/user/login [{}] - loginPage()", HttpMethod.GET);
		return "/user/login";
	}
	
	@PostMapping("/login")
	public String login(String userId, String password, HttpSession session) {
		log.debug("/user/login [{}] - login()", HttpMethod.POST);
		log.debug("userId=" + userId + ", password=" + password);
		User loginUser = userRepository.findByUserId(userId);
		if (loginUser == null) {	// 예외처리, 유저가 존재하지 않는 경우
			log.debug("해당하는 유저가 존재하지 않습니다!");
			return "/user/login_error";
		}
		
		if (loginUser.isWithdraw()) {
			log.debug("이미 탈퇴한 유저입니다.");
			return "/user/login_error";
		}
		
		//테스트용 배포시 삭제 예정 구문.
		if(userId.equals("test") || userId.equals("test2") || userId.equals("test3")) {
			log.debug("로그인 성공. inputId=" + userId + ", inputPw=" + password);
			session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, loginUser);

			log.info("Session : " + session.getAttribute(HttpSessionUtils.USER_SESSION_KEY));
			return "redirect:/";
		}
		
		if (!loginUser.isMatchPassword(password)) {
			log.debug("로그인 실패. 비밀번호를 확인해주세요.");
			return "/user/login_error";
		}
		
		log.debug("로그인 성공. inputId=" + userId + ", inputPw=" + password);
		session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, loginUser);

		log.info("Session : " + session.getAttribute(HttpSessionUtils.USER_SESSION_KEY));
		return "redirect:/";
	}
	
	@GetMapping("/login_error")
	public String loginError() {
		return "/user/login_error";
	}
	
	@GetMapping("/login_require")
	public String loginRequire() {
		return "/user/login_require";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		log.debug("/user/logout [{}] logout()", HttpMethod.GET);
		session.removeAttribute(HttpSessionUtils.USER_SESSION_KEY);
		
		return "redirect:/user/login";
	}
	
	@GetMapping("/{id}/detail")
	public String profile(@PathVariable Long id, Model model) {
		User selectedUser = userRepository.findOne(id);
		model.addAttribute("user", selectedUser);
		log.debug("SRC : " + selectedUser.getProfile());
		
		return "/user/detail";
	}
	
	@GetMapping("/{id}/modify")
	public String modifyPage(@LoginUser User loginUser, @PathVariable Long id, Model model) {
		log.debug("/{id}/modify [{}] - modifyPage()", HttpMethod.GET);
		if (!loginUser.isMatchId(id)) {
			log.debug("해당 유저의 정보를 수정할 권한이 없습니다.");
			return "/user/login?error=true";
		}
		
		User user = userRepository.findOne(id);
		model.addAttribute("user", user);
		return "/user/modify";
	}
	
	@PutMapping("/{id}/modify")
	public String modify(@LoginUser User loginUser, @PathVariable Long id, User modifiedUser, @RequestParam("file") MultipartFile file, String newPassword) {
		log.debug("/user/{id}/modify [{}] - modify() : "+modifiedUser, HttpMethod.PUT);
		
		if (!loginUser.isMatchId(id)) {
			log.debug("해당 유저의 정보를 수정할 권한이 없습니다.");
			return "/user/login?error=true";
		}
		if (!loginUser.isMatchPassword(modifiedUser)) {	
			log.debug("해당 유저의 정보를 수정할 권한이 없습니다.");
			return "/user/login?error=true";
		}
		
		modifiedUser.setPassword(newPassword);
		
		User user = userRepository.findOne(id);
		String newFileName = user.getProfile();
		if (user.isDefaultProfile() && !file.isEmpty()) {
			// Profile Image가 Default이며 새로운 사진을 등록할 경우
			newFileName = UUID.randomUUID().toString() + ".png";
		}
		if (!file.isEmpty()) {	// 사진을 변경할 경우
			storageService.store(file, newFileName, FileType.PROFILE);
		}
		modifiedUser.setProfile(newFileName);
		//user.modify(modifiedUser);
		userRepository.save(modifiedUser);
		
		return "redirect:/";
	}
	
	@GetMapping("/{id}/delete")
	public String delete(@LoginUser User loginUser, @PathVariable long id, Model model) {
		log.debug("/user/{id}/delete [{}] - delete()", HttpMethod.GET);
		if (!loginUser.isMatchId(id)) {
			log.debug("해당 유저를 탈퇴시킬 권한이 없습니다.");
			return "/user/login";
		}
		User outUser = userRepository.findOne(id);
		model.addAttribute("user", outUser);
		
		return "/user/delete";
	}
	
	@DeleteMapping("/{id}/delete")
	public String delete(@LoginUser User loginUser, @PathVariable Long id, String password, HttpSession session) {
		log.debug("/user/{id}/delete [{}] - delete()", HttpMethod.DELETE);
		
		if (!loginUser.isMatchId(id)) {
			log.debug("해당 유저를 탈퇴시킬 권한이 없습니다.");
			return "/user/login";
		}
		
		if (!loginUser.isMatchPassword(password)) {
			log.debug("비밀번호가 일치하지 않아 탈퇴할 수 없습니다.");
			return "/user/delete";
		}
				
		User outUser = userRepository.findOne(id);
		
		// 탈퇴 유저의 프로필 이미지 파일 제거 & 프로필 이미지 이름 DB에서 삭제		
		String fileName = outUser.getProfile();
		storageService.deleteFile(basicPath + profilePath + fileName);
		
		outUser.setNoneProfile();
		outUser.withdraw();
		userRepository.save(outUser);
		session.removeAttribute(HttpSessionUtils.USER_SESSION_KEY);
		
		return "redirect:/user/login";
	}
}
