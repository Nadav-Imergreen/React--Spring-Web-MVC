package hac.repo;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
/**
 * The UserSession class represents the user session and stores information about the current user's authentication status and user object.
 * It is a session-scoped component that maintains its state throughout the user session.
 */
@Component
@SessionScope
public class UserSession {

    private boolean isAuthenticated;
    private User user;

    /**
     * Constructs a new UserSession object with the initial authentication status set to false.
     */
    public UserSession() {
        isAuthenticated = false;
    }

    /**
     * Sets the user object and updates the authentication status to indicate a successful login.
     *
     * @param user The authenticated user object.
     */
    public void setLogin(User user) {
        this.user = user;
        isAuthenticated = true;
    }

    /**
     * Checks if the user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    /**
     * Retrieves the currently authenticated user object.
     *
     * @return The authenticated user object.
     */
    public User getUser() {
        return user;
    }
}
