package lk.sdp.Controller;
import lk.sdp.Entity.IncidentReport;
import lk.sdp.JPARepo.IncidentReportRepo;
import lk.sdp.Service.IncidentReportService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentReportService incidentReportService;

    @Autowired
    public IncidentController(IncidentReportService incidentReportService) {
        this.incidentReportService = incidentReportService;
    }

    @Autowired
    IncidentReportRepo incidentReportRepo;

    @PostMapping("/save")
    public ResponseEntity<JSONObject> saveIncident(@RequestBody JSONObject incidentReport,
                                                   HttpServletRequest request) {
        JSONObject response;
        try {
            response = incidentReportService.saveIncident(incidentReport, request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response = new JSONObject();
            response.put("status", "Error occured when saving the incident");
            response.put("statusCode", "02");
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{incidentId}/image1")
    public ResponseEntity<byte[]> getImage1(@PathVariable Long incidentId) {
        Optional<IncidentReport> optional = incidentReportRepo.findById(incidentId);

        if (optional.isPresent()) {
            IncidentReport incident = optional.get();

            if (incident.getImage1() != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(incident.getImage1());
            }
        }

        // If image is not found, you can return an appropriate response, for example, HttpStatus.NOT_FOUND
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/{incidentId}/image2")
    public ResponseEntity<byte[]> getImage2(@PathVariable Long incidentId) {
        Optional<IncidentReport> optional = incidentReportRepo.findById(incidentId);

        if (optional.isPresent()) {
            IncidentReport incident = optional.get();

            if (incident.getImage2() != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(incident.getImage2());
            }
        }

        // If image is not found, you can return an appropriate response, for example, HttpStatus.NOT_FOUND
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/{incidentId}/image3")
    public ResponseEntity<byte[]> getImage3(@PathVariable Long incidentId) {
        Optional<IncidentReport> optional = incidentReportRepo.findById(incidentId);

        if (optional.isPresent()) {
            IncidentReport incident = optional.get();

            if (incident.getImage3() != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(incident.getImage3());
            }
        }

        // If image is not found, you can return an appropriate response, for example, HttpStatus.NOT_FOUND
        return ResponseEntity.notFound().build();
    }


    @PutMapping(value = "incidentcomment")
    public ResponseEntity<JSONObject> incidentComment(@RequestBody JSONObject incident, HttpServletRequest request) {
        JSONObject data = null;
        JSONObject response;
        try {
            data = incidentReportService.incidentComment(incident, request);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("An error occurred: " + e.getMessage());
            response = new JSONObject();
            response.put("status", "Error occurred when saving");
            response.put("statusCode", "02");
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/all")
    public ResponseEntity<JSONObject> getIncidents(HttpServletRequest request, @RequestParam Integer page, @RequestParam Integer perPage) {
        JSONObject data = null;
        try {
            data = incidentReportService.getIncidents(page, perPage,request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<JSONObject>(data, HttpStatus.OK);
    }

    @GetMapping("/allinprogress")
    public ResponseEntity<JSONObject> getIncidentsInProgress(HttpServletRequest request, @RequestParam Integer page, @RequestParam Integer perPage) {
        JSONObject data = null;
        try {
            data = incidentReportService.getIncidentsInProgress(page, perPage,request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<JSONObject>(data, HttpStatus.OK);
    }


    @GetMapping("/{incidentId}")
    public ResponseEntity<JSONObject> getIncidentDetails(@PathVariable Long incidentId) {
        JSONObject incidentDetails = incidentReportService.getIncidentDetails(incidentId);

        if (incidentDetails != null) {
            return ResponseEntity.ok(incidentDetails);
        } else {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<JSONObject> getLatestIncidentForUser(HttpServletRequest request) {
        JSONObject response;
        HttpStatus status;

        try {
            response = incidentReportService.getIncidentReply(request);
            if (response != null) {
                status = HttpStatus.OK;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } catch (Exception e) {
            response = new JSONObject();
            response.put("error", "An error occurred while processing the request.");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(response, status);
    }
}