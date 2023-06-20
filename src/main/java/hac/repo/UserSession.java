package hac.repo;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class UserSession {

    private boolean isConnected;

    public UserSession() {
        isConnected = false;
    }

    public void setLogin() {
        isConnected = true;
    }

    public boolean isAuthenticated() {
        return isConnected;
    }
}
