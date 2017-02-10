package infinitefire.project.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import infinitefire.project.domain.User;
import infinitefire.project.domain.UserRepository;

@RestController
@RequestMapping("/api")
public class ApiUserController {
	private static final Logger log = LoggerFactory.getLogger(ApiUserController.class);
	
	@Autowired
	UserRepository userRepository;
	
	@PostMapping("/user/check")
	public boolean checkUserId(String checkUserId) {
		log.debug("Access Ajax >> checkUserId");
		
		User checkUser = userRepository.findByUserId(checkUserId);
		
		if(checkUser != null)
			return true;
		else
			return false;
	}
}
