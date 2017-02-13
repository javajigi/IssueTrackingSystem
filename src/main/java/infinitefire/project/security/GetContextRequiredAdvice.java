package infinitefire.project.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GetContextRequiredAdvice {
	private static final Logger log = LoggerFactory.getLogger(GetContextRequiredAdvice.class);
	
	@ExceptionHandler(GetContextRequiredException.class)
	public String handleConflict() {
		log.debug("GetContextRequiredException is happened!");
		return "redirect:/";
	}
}
