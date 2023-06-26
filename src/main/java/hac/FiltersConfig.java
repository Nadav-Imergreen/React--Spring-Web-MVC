package hac;

import hac.filters.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import hac.filters.CustomInterceptor;
import hac.repo.UserSession;

/**
 * Configuration class for configuring Spring MVC.
 * Enables Web MVC and registers the custom interceptor and session listener.
 */
@EnableWebMvc
@Configuration
public class FiltersConfig implements WebMvcConfigurer {

    // we inject a bean in the config class then pass it filter
    @Autowired
    UserSession userSession;

    /**
     * Registers the custom interceptor with specific URL patterns.
     * The interceptor is applied to "/user/profile" and "/admin/profiles" URLs.
     *
     * @param registry The interceptor registry.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        // define the URL to intercept with the pattern you want
        registry.addInterceptor(new CustomInterceptor(userSession)).addPathPatterns("/user/profile");
        registry.addInterceptor(new CustomInterceptor(userSession)).addPathPatterns("/admin/profiles");
        registry.addInterceptor(new LoginInterceptor(userSession)).addPathPatterns("/user/login");
        registry.addInterceptor(new LoginInterceptor(userSession)).addPathPatterns("/admin/login");
    }
}