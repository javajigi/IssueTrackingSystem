package infinitefire.project.web;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.net.HttpHeaders;

import infinitefire.project.domain.User;
import infinitefire.project.domain.UserRepository;
import infinitefire.project.storage.StorageFileNotFoundException;
import infinitefire.project.storage.StorageService;
import infinitefire.project.utils.HttpSessionUtils;

@Controller
public class FileUploadController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);	
	private final StorageService storageService;
	
	// TODO 다음과 같이 하드코딩하면 서버에 배포했을 때 문제가 될 수 있습니다. 
	private String basicURL = "http://localhost:8080/file/"; 
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	public FileUploadController(StorageService storageService) {
		this.storageService = storageService;
		LogManager.getRootLogger().setLevel(Level.DEBUG);
	}
	
	@GetMapping("/file/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}
	
	@PostMapping("/user/new")
	public String newUser(User newUser, @RequestParam("file") MultipartFile file) {
		log.debug("/user/new [{}] - newUser()", HttpMethod.POST);
		if (userRepository.findByUserId(newUser.getUserId()) != null) {
			log.debug("해당 아이디는 사용 할 수 없습니다.");
			return "redirect:/user/new";
		}
		
		String newFileName = newUser.getUserId() + "_profile.png";
		storageService.store(file, newFileName);
		newUser.setProfileUrl(basicURL + newFileName);
		userRepository.save(newUser);
		
		return "redirect:/user/login";
	}
	
	@PutMapping("/user/{id}/modify")
	public String modify(@PathVariable Long id, User modifiedUser, @RequestParam("file") MultipartFile file, String newPassword, HttpSession session) {
		log.debug("/user/{id}/modify [{}] - modify()", HttpMethod.PUT);
		log.debug("Before : " + modifiedUser.toString());
		
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
		String newFileName = user.getUserId() + "_profile.png";
		storageService.store(file, newFileName);
		modifiedUser.setProfileUrl(basicURL + newFileName);
		user.modify(modifiedUser);
		userRepository.save(user);
		
		return "redirect:/";
	}
	
	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}
}
