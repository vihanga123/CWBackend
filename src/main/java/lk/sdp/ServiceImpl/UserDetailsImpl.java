//package lk.sdp.ServiceImpl;
//import lk.sdp.Service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserDetailsImpl implements UserDetailsService {
//
//    private final UserService userService;
//
//    public UserDetailsImpl(UserService userService) {
//        this.userService = userService;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        lk.sdp.Entity.User user = userService.findByUsername(username);
//        //Checks whther user exist when logging in
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found with username: " + username);
//        }
//        return User.builder()
//                .username(user.getUsername())
//                .password(user.getPassword())
//                .roles(user.getRole())
//                .build();
//    }
//}