package infinitefire.project;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.github.jknack.handlebars.springmvc.HandlebarsViewResolver;

import infinitefire.project.helpers.OwnerHelper;
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
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        // registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Configuration
    @ConditionalOnClass(OwnerHelper.class)
    static class SpringSecurityHelperAutoConfiguration {
        @Autowired
        private HandlebarsViewResolver handlebarsViewResolver;

        @Autowired
        private OwnerHelper ownerHelper;

        @PostConstruct
        public void registerHelper() {
            handlebarsViewResolver.registerHelper(OwnerHelper.NAME, ownerHelper);
        }
    }
}