//package lk.sdp.Controller;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lk.sdp.Entity.User;
//import lk.sdp.JPARepo.UserRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.context.request.RequestContextHolder;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/users")
//
//public class UserController {
//    @Autowired
//    private UserRepo userRepository;
//
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
////    @Autowired
////    private JwtUtil jwtUtil;
//
//    @PostMapping("/register")
//    public String registerUser(@RequestBody User user) {
//        // username validate
//        if (userRepository.findByUsername(user.getUsername()) != null) {
//            return "Username is already taken. Please choose another.";
//        }
//
//        user.setRole("USER"); // "manual assign role for user"
//        userRepository.save(user);
//        return "User registered successfully!";
//    }
//
//    @PostMapping("/login" )
//    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody User user) {
//        try {
//            System.out.println("Username: " + user.getUsername());
//            System.out.println("Password: " + user.getPassword());
//            System.out.println("OFFICERTYPE: " + user.getOfficerType());
//
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
//            );
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
////            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//         String authorities = "";
//            if (authentication.getPrincipal() instanceof UserDetails) {
//                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//                String username = userDetails.getUsername();
//                authorities = userDetails.getAuthorities().toString();
//                System.out.println("Username from UserDetails: " + username);
//            } else {
//                System.out.println("UserDetails not found in Authentication");
//            }
//
//            String roleType = authorities
//                    .replace("[", "")           // Remove the opening bracket
//                    .replace("]", "")           // Remove the closing bracket
//                    .replace("ROLE_", "");      // Remove the "ROLE_" prefix
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("message", "Successfully logged in");
//            response.put("username", user.getUsername());
//            response.put("officerType", user.getOfficerType());
//            response.put("role", roleType);
//            System.out.println(response);
//            return ResponseEntity.ok(response);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        }
//    }
//}