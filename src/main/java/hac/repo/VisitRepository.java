package hac.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByEmail(String email);
}
//    @Query("SELECT u FROM Visit u WHERE u.email = :email ORDER BY u.lastVisit DESC")
//    List<Visit> findLatestByEmail(String email);