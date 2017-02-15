package infinitefire.project.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

// TODO 불필요한 코드로 보여짐 제거한다.
public class GetContextPathHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		/*
		 * 반드시, 이 method가 먼저 실행된다. -> 파라메터를 검사, 일치하는 타입의 파라메터가 존재하면 resolveArgument 메서드가 실행
		 * */
		return parameter.hasParameterAnnotation(GetContextPath.class);
	}

	@Override
	public String resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		// @GetContextPath 어노테이션이 존재할 시, 실행되는 구문
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		
		return request.getContextPath();
	}

}
