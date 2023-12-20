package lk.sdp.ServiceImpl;

import lk.sdp.Entity.IncidentReport;
import lk.sdp.Entity.IncidentType;
import lk.sdp.JPARepo.IncidentReportRepo;
import lk.sdp.JPARepo.IncidentTypeRepo;
import lk.sdp.JPARepo.UserRepo;
import lk.sdp.Service.IncidentReportService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.time.LocalDate;


import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class IncidentReportImpl implements IncidentReportService {

    @Autowired
    IncidentReportRepo incidentReportRepo;

    @Autowired
    IncidentTypeRepo incidentTypeRepo;

    @Autowired
    UserRepo userRepo;

    @Override
    public JSONObject saveIncident(JSONObject incidentReport, HttpServletRequest request) {
        System.out.println(incidentReport);
        try {
            IncidentReport incident = new IncidentReport();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
                // Retrieve the username
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String username = userDetails.getUsername();
                String role = userDetails.getAuthorities().toString();
                System.out.println("Username from UserDetails: " + username + "role: " + role);
                incident.setUsername(username);
            } else {
                System.out.println("UserDetails not found in Authentication");
                incident.setUsername(incidentReport.get("username").toString());
            }

            //incident.setUserId(Long.valueOf(incidentReport.get("userId").toString()));
            incident.setName(incidentReport.get("name").toString());
            incident.setLocation(incidentReport.get("location").toString());
            incident.setIncident(incidentReport.get("incident").toString());
            incident.setStatus("PENDING");
            Long incidentTypeId = Long.parseLong(incidentReport.get("incidentTypeId").toString());
            IncidentType incidentType = incidentTypeRepo.findTypeById(incidentTypeId).get();
            incident.setIncidentType(incidentType);
            incidentReportRepo.save(incident);

            JSONObject res = new JSONObject();
            res.put("status", "success");
            res.put("statusCode", "00");
            res.put("data", incident);

            return res;
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject res = new JSONObject();
            res.put("status", "Incident Request Failed");
            res.put("statusCode", "02");
            res.put("data", e.getMessage());
            return res;
        }

    }

    @Override
    public JSONObject incidentComment(JSONObject incidentReport, HttpServletRequest request) {
        try {
            Optional<IncidentReport> optional = incidentReportRepo.findById(new Long(incidentReport.get("incidentId").toString()));

            if (optional.isPresent()) {

                IncidentReport incident = incidentReportRepo.findById(Long.valueOf(incidentReport.get("incidentId").toString())).get();

                incident.setComment(incidentReport.get("comment").toString());
                incident.setProgress(incidentReport.get("progress").toString());
                incident.setStatus(incidentReport.get("status").toString());
                incident.setTimeframe(incidentReport.get("timeframe").toString());
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                incident.setStartdate(LocalDate.parse(incidentReport.get("startdate").toString(), dateFormatter));

                if (incidentReport.containsKey("image1")) {
                    String image1Base64 = incidentReport.get("image1").toString();
                    byte[] image1Bytes = Base64.getDecoder().decode(image1Base64);
                    incident.addImage(image1Bytes);
                }

                if (incidentReport.containsKey("image2")) {
                    String image2Base64 = incidentReport.get("image2").toString();
                    byte[] image2Bytes = Base64.getDecoder().decode(image2Base64);
                    incident.addImage(image2Bytes);
                }

                if (incidentReport.containsKey("image3")) {
                    String image3Base64 = incidentReport.get("image3").toString();
                    byte[] image3Bytes = Base64.getDecoder().decode(image3Base64);
                    incident.addImage(image3Bytes);
                }
                incidentReportRepo.save(incident);
            }

            JSONObject res = new JSONObject();
            res.put("status", "success");
            res.put("statusCode", "00");

            return res;

        } catch (Exception e) {
            e.printStackTrace();
            JSONObject res = new JSONObject();
            res.put("status", "Bad Request");
            res.put("statusCode", "400");
            res.put("data", e.getMessage());
            return res;
        }
    }

    @Override
    public JSONObject getIncidents(Integer page, Integer perPage,HttpServletRequest request) {
        try {
            JSONObject resData = new JSONObject();
            List<JSONObject> data = new ArrayList<JSONObject>();
            Page<IncidentReport> dataList = null;

            String officerType = request.getHeader("role");
            System.out.println(officerType);


              if ("OFFICERWILDLIFE".equals(officerType)) {
                    dataList = incidentReportRepo.findWildlifeIncidents(PageRequest.of(page - 1, perPage));
              } else if ("OFFICERFORESTRY".equals(officerType)) {
                    dataList = incidentReportRepo.findForestryIncidents(PageRequest.of(page - 1, perPage));
              } else if ("ADMIN".equals(officerType)) {
                  dataList = incidentReportRepo.findInquiries(PageRequest.of(page - 1, perPage));
                } else {
                  System.out.println("You do not have access");
               }

            for (IncidentReport incident : dataList) {
                JSONObject res = new JSONObject();
                res.put("incidentId", incident.getIncidentId());
                res.put("name", incident.getName());
                res.put("createdAt", incident.getCreatedDateTime().toLocalDate());
                res.put("incident", incident.getIncident());
                res.put("status", incident.getStatus());
                res.put("location", incident.getLocation());

                data.add(res);
            }

            resData.put("data", data);
            resData.put("total", dataList.getTotalElements());

            return resData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONObject getIncidentDetails(Long incidentId) {
        try {
            Optional<IncidentReport> optionalIncident = incidentReportRepo.findById(incidentId);
            if (optionalIncident.isPresent()) {
                JSONObject resData = new JSONObject();
                IncidentReport incident = optionalIncident.get();

                resData.put("incidentId", incident.getIncidentId());
                resData.put("name", incident.getName());
                resData.put("createdAt", incident.getCreatedDateTime().toLocalDate());
                resData.put("incident", incident.getIncident());
                resData.put("status", incident.getStatus());
                resData.put("location", incident.getLocation());
                if (incident.getComment() != null) {
                    resData.put("comment", incident.getComment());
                }
                if (incident.getStartdate() != null) {
                    resData.put("startdate", incident.getStartdate());
                }
                if (incident.getProgress() != null) {
                    resData.put("progress", incident.getProgress());
                }
                if (incident.getTimeframe() != null) {
                    resData.put("timeframe", incident.getTimeframe());
                }
                if (incident.getImage1() != null) {
                    resData.put("image1", incident.getImage1());
                }
                IncidentType incidentType = incident.getIncidentType();
                if (incidentType != null) {
                    resData.put("incidentTypeId", incidentType.getIncidentType());
                }

                return resData;
            } else {
                // Handle the case where the incident with the given ID is not found
                JSONObject errorData = new JSONObject();
                errorData.put("error", "Incident not found with ID: " + incidentId);
                return errorData;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle other exceptions if needed
            return null;
        }
    }

    @Override
    public JSONObject getIncidentReply(HttpServletRequest request) {
        try {
            JSONObject resData = new JSONObject();
            List<JSONObject> data = new ArrayList<>();

            String username = request.getHeader("username");
            System.out.println(username);

            Page<IncidentReport> latestIncidentPage = incidentReportRepo.findLatestIncidentByUsername(username, PageRequest.of(0, 1));
            List<IncidentReport> latestIncidents = latestIncidentPage.getContent();
            if (!latestIncidents.isEmpty()) {

                IncidentReport latestIncident = latestIncidents.get(0);
                List<byte[]> image1 = incidentReportRepo.findImage1ByIncidentId(latestIncident.getIncidentId());
                List<byte[]> image2 = incidentReportRepo.findImage2ByIncidentId(latestIncident.getIncidentId());
                List<byte[]> image3 = incidentReportRepo.findImage3ByIncidentId(latestIncident.getIncidentId());

                JSONObject res = new JSONObject();
                res.put("incidentId", latestIncident.getIncidentId());
                res.put("name", latestIncident.getName());
                res.put("startDate", latestIncident.getStartdate());
                res.put("timeframe", latestIncident.getTimeframe());
                res.put("incident", latestIncident.getIncident());
                res.put("status", latestIncident.getStatus());
                res.put("progress", latestIncident.getProgress());
                res.put("location", latestIncident.getLocation());
                res.put("comment", latestIncident.getComment());
                IncidentType incidentType = latestIncident.getIncidentType();
                if (incidentType != null) {
                    res.put("incidentTypeId", incidentType.getIncidentType());
                }

//                if (!image1.isEmpty()) {
//                    res.put("image1", convertImagesToBase64(image1));
//                }
//
//                if (!image2.isEmpty()) {
//                    res.put("image2", convertImagesToBase64(image2));
//                }
//
//                if (!image3.isEmpty()) {
//                    res.put("image3", convertImagesToBase64(image3));
//                }

                data.add(res);
            }

            resData.put("data", data);
            resData.put("total", data.size());

            return resData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    private List<String> convertImagesToBase64(List<byte[]> images) {
//        List<String> imageBase64List = new ArrayList<>();
//        for (byte[] imageBytes : images) {
//            String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
//            imageBase64List.add(imageBase64);
//        }
//        return imageBase64List;
//    }
}