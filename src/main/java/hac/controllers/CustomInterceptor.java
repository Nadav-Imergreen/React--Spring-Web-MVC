package hac.controllers;

import hac.repo.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class CustomInterceptor implements HandlerInterceptor {

    private final UserSession userSession;

    public CustomInterceptor(UserSession userSession) {
        this.userSession = userSession;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (!userSession.isAuthenticated()){
            response.sendRedirect("/user/login");  // prevent access to non-authorized pages
            return false;
        }
        // otherwise grant access
        return true;
    }
}
