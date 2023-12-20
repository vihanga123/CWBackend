package lk.sdp.JPARepo;
import lk.sdp.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);


    @Query("SELECT u.officerType FROM User u WHERE u.username = :username")
    String findOfficerByUsername(@Param("username") String username);


    @Query("SELECT COUNT(u) FROM User u WHERE u.role IN ('OFFICERWILDLIFE', 'OFFICERFORESTRY')")
    Long officerCount();
}