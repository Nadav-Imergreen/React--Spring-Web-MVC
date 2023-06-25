package hac;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import hac.filters.CustomInterceptor;
import hac.repo.UserSession;
/*
  this is a class for configuring StringMVC
  here we register our interceptor class and the session listener
  WebMvcConfigurer allows configuring all the MVC:
 */
@EnableWebMvc
@Configuration
public class FiltersConfig implements WebMvcConfigurer {

    // we inject a bean in the config class then pass it filter
    @Autowired
    UserSession userSession;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        // define the URL to intercept with the pattern you want
        registry.addInterceptor(new CustomInterceptor(userSession)).addPathPatterns("/user/profile");
        registry.addInterceptor(new CustomInterceptor(userSession)).addPathPatterns("/admin/profiles");
    }
}