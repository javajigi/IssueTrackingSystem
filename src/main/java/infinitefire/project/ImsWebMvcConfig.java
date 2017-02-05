package infinitefire.project;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import infinitefire.project.security.LoginUserHandlerMethodArgumentResolver;

@Configuration
public class ImsWebMvcConfig extends WebMvcConfigurerAdapter {
	@Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new LoginUserHandlerMethodArgumentResolver());
    }
}
