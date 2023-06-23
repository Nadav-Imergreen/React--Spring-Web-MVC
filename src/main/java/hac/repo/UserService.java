package hac.repo;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserService {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String ADMIN_PASSWORD = "1234";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VisitRepository visitRepository;

    @EventListener({ApplicationStartedEvent.class})
    public void registerAdmin() {
        if (findByEmail(ADMIN_EMAIL) == null)
            save(new User(ADMIN_USERNAME, ADMIN_EMAIL, ADMIN_PASSWORD));
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public void save(User user) {
        userRepository.save(new User(user.getUserName(), user.getEmail(), user.getPassword()));
    }

    public User findByEmail(String email) {
       return userRepository.findByEmail(email);
    }


    public List<Visit> findAllVisitsByEmail(String email) {
        return visitRepository.findByEmail(email);
    }

    public void createVisit(User user) {
        Visit visit = new Visit(user.getEmail(), new Date());

        System.out.println(user);
//        userRepository.save(user);
//        visit.setUser(user); // Set the user for the visit
        visitRepository.save(visit);
    }

    public User getAdminDetails() {
        return new User(ADMIN_USERNAME, ADMIN_EMAIL, ADMIN_PASSWORD);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<Visit> findAllVisits() {
        return visitRepository.findAll();
    }
}
