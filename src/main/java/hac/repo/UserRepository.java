package hac.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/* default scope of this Bean is "singleton" */
public interface UserRepository extends JpaRepository<User, Long> {
//
    /** SOME EXAMPLES:
     *  defining some queries using the method names
     *  Spring will implement the method for us based on the method name
     *  https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
     */
    List<User> findByUserName(String userName);
    List<User> findUserByUserName(String userName);
    List<User> findByEmail(String email);
    List<User> findByUserNameAndEmail(String userName, String email);
    List<User> findFirst10ByOrderByUserNameDesc(); // find first 10 users ordered by userName desc

}