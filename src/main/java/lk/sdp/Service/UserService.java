package lk.sdp.Service;
import lk.sdp.Entity.User;

public interface UserService {
    User registerUser(User user);
    User findByUsername(String username);
}