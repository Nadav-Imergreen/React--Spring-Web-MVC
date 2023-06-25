package hac.controllers;

import hac.InvalidPasswordException;
import hac.UserNotFoundException;
import hac.repo.User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.ui.Model;

/**
 * Global exception handler for handling specific exceptions in the application.
 */
@ControllerAdvice
public class ExceptionHandlerController {

    /**
     * Handles the exception when a user is not found.
     *
     * @param ex    the UserNotFoundException instance
     * @param model the model for the view
     * @return the login view with an error message
     */
    @ExceptionHandler(UserNotFoundException.class)
    public String userNotFound(UserNotFoundException ex, Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("email", ex.getMessage());
        return "/user/login";
    }

    /**
     * Handles the exception when an invalid password is provided by the user.
     *
     * @param ex    the InvalidPasswordException instance
     * @param model the model for the view
     * @return the login view with an error message
     */
    @ExceptionHandler(InvalidPasswordException.class)
    public String invalidUserPassword(InvalidPasswordException ex, Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("password", ex.getMessage());
        return "/user/login";
    }
}
