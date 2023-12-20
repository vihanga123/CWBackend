package lk.sdp.Service;

import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface IncidentReportService {

    JSONObject saveIncident(JSONObject inquiry, HttpServletRequest request);

    JSONObject incidentComment(JSONObject incidentReport, HttpServletRequest request);

    JSONObject getIncidents(Integer page, Integer perPage,HttpServletRequest request);

    JSONObject getIncidentReply(HttpServletRequest request);

    JSONObject getIncidentDetails(Long incidentId);

}
