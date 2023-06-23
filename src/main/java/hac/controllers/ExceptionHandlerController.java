package hac.controllers;
import hac.InvalidPasswordException;
import hac.UserNotFoundException;
import hac.repo.User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.ui.Model;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException ex, Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("email", ex.getMessage());
        return "/user/login";
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public String handleInvalidPasswordException(InvalidPasswordException ex, Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("password", ex.getMessage());
        return "/user/login";
    }
}
