package lk.sdp.Service;

import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface AdminService {

    JSONObject getUsers(Integer page, Integer perPage);

    JSONObject userUpdate(JSONObject user, HttpServletRequest request);
}
