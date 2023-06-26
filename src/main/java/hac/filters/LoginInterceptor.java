package hac.filters;

import hac.repo.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private final UserSession userSession;

    public LoginInterceptor(UserSession userSession) {
        this.userSession = userSession;
    }

    /**
     * Pre-handle method of the interceptor.
     * Checks if the user is authenticated. If not, redirects to the home page ("/").
     * Check if admin is authenticated, If not, redirects to the home page ("/").
     *
     * @param request  the HTTP servlet request
     * @param response the HTTP servlet response
     * @param handler  the handler object
     * @return true if the user is authenticated and can proceed, false otherwise
     * @throws Exception in case of any error during pre-handle
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (userSession.isAuthenticated() && (userSession.getUser().getId() == 1)) {
            response.sendRedirect("/admin/profiles");  // prevent access to non-authorized pages
            return false;
        } else if (userSession.isAuthenticated()) {
            response.sendRedirect("/user/profile");  // prevent access to non-authorized pages
            return false;
        }

        // otherwise grant access
        return true;
    }
}





