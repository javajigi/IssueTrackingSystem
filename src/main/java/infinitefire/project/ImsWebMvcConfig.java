package infinitefire.project;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import infinitefire.project.security.GetContextPathHandlerMethodArgumentResolver;
import infinitefire.project.security.LoginUserHandlerMethodArgumentResolver;

/*
 * Handler 등록을 위한 ConfigurerAdapter 구현
 * argumentResolvers에 add해줘야만 Controller 접근 시 해당하는 HandlerMethodArgumentResolver가 실행된다
 */

@Configuration
public class ImsWebMvcConfig extends WebMvcConfigurerAdapter {
	@Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new LoginUserHandlerMethodArgumentResolver());
        argumentResolvers.add(new GetContextPathHandlerMethodArgumentResolver());
    }
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry){
		//registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		//registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
}