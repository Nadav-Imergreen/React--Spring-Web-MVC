package hac.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    //User findByUserName(String userName);
    User findByUserName(String userName);
    User findByEmail(String email);
    @Query("SELECT u FROM User u ORDER BY u.userName ASC")
    List<User> findAllByUserName(String userName);
    List<User> findFirst10ByOrderByUserNameDesc(); // find first 10 users ordered by userName desc
}