package hac.repo;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<Visit> visits= new ArrayList<>();
        userRepository.save(new User(ADMIN_USERNAME, ADMIN_EMAIL, ADMIN_PASSWORD, visits));
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void saveNewUser(User user) {
        userRepository.save(new User(user.getUserName(), user.getEmail(), user.getPassword(), user.getVisits()));
    }

    @Transactional
    public void updateUser(User user) {
        userRepository.save(user);
    }

    public User findByEmail(String email) {
       return userRepository.findByEmail(email);
    }


    public List<Visit> findAllVisitsByEmail(String email) {
        return visitRepository.findByEmail(email);
    }

    @Transactional
    public void createVisit(User user) {
        Visit visit = new Visit(user.getEmail(), new Date());

        user.setVisits(visit);

        visitRepository.save(visit);
    }

    public String getAdminEmail() {
        return  ADMIN_EMAIL;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<Visit> findAllVisits() {
        return visitRepository.findAll();
    }
}
