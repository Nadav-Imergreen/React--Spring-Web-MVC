package hac.filters;

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

    /**
     * Pre-handle method of the interceptor.
     * Checks if the user is authenticated. If not, redirects to the home page ("/").
     *
     * @param request  the HTTP servlet request
     * @param response the HTTP servlet response
     * @param handler  the handler object
     * @return true if the user is authenticated and can proceed, false otherwise
     * @throws Exception in case of any error during pre-handle
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!userSession.isAuthenticated()) {
            response.sendRedirect("/");  // prevent access to non-authorized pages
            return false;
        }
        // otherwise grant access
        return true;
    }
}
