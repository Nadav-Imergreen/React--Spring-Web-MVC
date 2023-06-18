package hac.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/* default scope of this Bean is "singleton" */
public interface UserRepository extends JpaRepository<User, Long> {
//
    /** SOME EXAMPLES:
     *  defining some queries using the method names
     *  Spring will implement the method for us based on the method name
     *  https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
     */
    //User findByUserName(String userName);
    User findByUserName(String userName);
    User findByEmail(String email);
    @Query("SELECT u FROM User u ORDER BY u.userName ASC")
    List<User> findAllByUserName(String userName);
    List<User> findFirst10ByOrderByUserNameDesc(); // find first 10 users ordered by userName desc

}