package hac.repo;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import hac.repo.User;

@Component
@SessionScope
public class UserSession {

    private boolean isAuthenticated;
    private User user;

    public UserSession() {
        isAuthenticated = false;
    }

    public void setLogin(User user) {
        this.user = user;
        isAuthenticated = true;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public User getUser() {
        return user;
    }

}
