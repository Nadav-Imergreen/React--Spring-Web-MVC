package hac.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    //Visit findByDate(Date lastVisit);
//    @Query("SELECT u FROM Visit u ORDER BY u.lastVisit ASC")
//    Visit findFirstByEmail(String email);

    @Query("SELECT u FROM Visit u WHERE u.email = :email ORDER BY u.lastVisit DESC")
    List<Visit> findLatestByEmail(String email);

//    @Query("SELECT u FROM User u ORDER BY u.userName ASC")
//    List<Visit> findAllByUserName(String userName);
//    List<Visit> findFirst10ByOrderByUserNameDesc(); // find first 10 users ordered by userName desc
}