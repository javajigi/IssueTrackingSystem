package infinitefire.project.security;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import infinitefire.project.domain.User;
import infinitefire.project.utils.HttpSessionUtils;

/*
 * Controller에 접근시, default로 실행된다.
 * suppoertsParameter로 파라메터에 해당하는 타입이 포함되어있는지를 판별, resolveArgument 메서드를 실행할지 안할지를 결정한다.
 * */

public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		/*
		 * 반드시, 이 method가 먼저 실행된다. -> 파라메터를 검사, 일치하는 타입의 파라메터가 존재하면 resolveArgument 메서드가 실행
		 * */
		return parameter.hasParameterAnnotation(LoginUser.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Object userObject = webRequest.getAttribute(HttpSessionUtils.USER_SESSION_KEY, WebRequest.SCOPE_SESSION);
		User user = getUser(userObject);
		if (!user.isGuestUser()) {
			return user;
		}
		
		LoginUser loginUser = parameter.getParameterAnnotation(LoginUser.class);
		if (loginUser.required()) {
			throw new LoginRequiredException("You're required Login!");
		}
		return user;
	}

	private User getUser(Object user) {
		if (user == null) {
			return User.GUEST_USER;
		}
		return (User)user;
	}
}
