package lk.sdp.Controller;

import lk.sdp.JPARepo.IncidentReportRepo;
import lk.sdp.JPARepo.UserRepo;
import lk.sdp.Service.AdminService;
import lk.sdp.Service.IncidentReportService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Autowired
    UserRepo userRepo;

    @Autowired
    IncidentReportRepo incidentReportRepo;

    @GetMapping("/users")
    public ResponseEntity<JSONObject> getUsers(HttpServletRequest request, @RequestParam Integer page, @RequestParam Integer perPage) {
        JSONObject data = null;
        try {
            data = adminService.getUsers(page, perPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<JSONObject>(data, HttpStatus.OK);
    }

    @PutMapping(value = "userupdate")
    public ResponseEntity<JSONObject> userUpdate(@RequestBody JSONObject user, HttpServletRequest request){
        JSONObject data = null;
        try{
            data = adminService.userUpdate(user,request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<JSONObject>(data, HttpStatus.OK);
    }

    @GetMapping("/total-users")
    public ResponseEntity<Long> getTotalRegisteredUsersCount() {
        try {
            Long totalUsers = userRepo.count();
            return ResponseEntity.ok(totalUsers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/total-officers")
    public ResponseEntity<Long> getTotalOfficerCount() {
        try {
            Long totalUsers = userRepo.officerCount();
            return ResponseEntity.ok(totalUsers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/pending-incidents")
    public ResponseEntity<Long> getPendingIncidentCount() {
        try {
            Long totalUsers = incidentReportRepo.incidentCount();
            return ResponseEntity.ok(totalUsers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/deleteincident/{incidentId}")
    public ResponseEntity<String> deleteIncident(@PathVariable Long incidentId) {
        try {
            // Check if the incident exists
            if (incidentReportRepo.existsById(incidentId)) {
                incidentReportRepo.deleteById(incidentId);
                return new ResponseEntity<>("Incident deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Incident not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while deleting the incident", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteuser/{Id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long Id) {
        try {
            // Check if the incident exists
            if (userRepo.existsById(Id)) {
                userRepo.deleteById(Id);
                return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while deleting the User", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
