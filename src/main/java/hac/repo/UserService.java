package hac.repo;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The UserService class is responsible for managing user-related operations and interactions with the database.
 * It provides methods for registering and managing users, as well as retrieving user information and visits.
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VisitRepository visitRepository;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String ADMIN_PASSWORD = "1234";

    /**
     * Registers the admin user in the database when the application starts.
     * The admin user has predefined credentials.
     */
    @EventListener({ApplicationStartedEvent.class})
    public void registerAdmin() {
        List<Visit> visits = new ArrayList<>();
        userRepository.save(new User(ADMIN_USERNAME, ADMIN_EMAIL, ADMIN_PASSWORD, visits));
    }

    /**
     * Deletes a user from the database based on the provided ID.
     *
     * @param id the ID of the user to be deleted
     */
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Saves a new user to the database.
     *
     * @param user the user to be saved
     */
    @Transactional
    public void saveNewUser(User user) {
        userRepository.save(new User(user.getUserName(), user.getEmail(), user.getPassword(), user.getVisits()));
    }

    /**
     * Updates an existing user in the database.
     *
     * @param user the user to be updated
     */
    @Transactional
    public void updateUser(User user) {
        userRepository.save(user);
    }

    /**
     * Finds a user in the database by their email address.
     *
     * @param email the email address of the user to be found
     * @return the found user, or null if not found
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Finds all visits associated with a specific user.
     *
     * @param email the email address of the user
     * @return a list of visits associated with the user
     */
    public List<Visit> findAllVisitsByEmail(String email) {
        return visitRepository.findByEmail(email);
    }

    /**
     * Creates a new visit for a user and saves it to the database.
     *
     * @param user the user for whom the visit is created
     */
    @Transactional
    public void createVisit(User user) {
        Visit visit = new Visit(user.getEmail(), new Date());

        user.setVisits(visit);

        visitRepository.save(visit);
    }

    /**
     * Retrieves a list of all registered users from the database.
     *
     * @return a list of all users
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a list of all visits from the database.
     *
     * @return a list of all visits
     */
    public List<Visit> findAllVisits() {
        return visitRepository.findAll();
    }

    public String getAdminEmail() {
        return ADMIN_EMAIL;
    }
}
