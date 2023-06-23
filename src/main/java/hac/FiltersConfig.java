package hac;

import hac.filters.CustomInterceptor;
import hac.repo.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
  this is a class for configuring SringMVC
  here we register our interceptor class and the session listener
  WebMvcConfigurer allows configuring all of the MVC:
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

// STATIC FOLDER IS NOT REACHABLE WHEN ADDING FILTER, so i add this piece of code to enable access:
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("/static/");
    }
}