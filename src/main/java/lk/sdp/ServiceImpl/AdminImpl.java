package lk.sdp.ServiceImpl;

import lk.sdp.Entity.User;
import lk.sdp.JPARepo.UserRepo;
import lk.sdp.Service.AdminService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AdminImpl implements AdminService {

    @Autowired
    UserRepo userRepo;

    @Override
    public JSONObject getUsers(Integer page, Integer perPage) {
        try {
            JSONObject resData = new JSONObject();
            List<JSONObject> data = new ArrayList<JSONObject>();
            Page<User> dataList = null;

            dataList = userRepo.findAll(PageRequest.of(page - 1, perPage));

            for (User user : dataList) {
                JSONObject res = new JSONObject();
                res.put("userid", user.getId());
                res.put("username", user.getUsername());
                res.put("role", user.getRole());
                res.put("createdAt", user.getCreatedDateTime().toLocalDate());
                res.put("email", user.getEmail());
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
    public JSONObject userUpdate(JSONObject user, HttpServletRequest request) {
        try {
            Optional<User> optional = userRepo.findById(new Long(user.get("id").toString()));

            if (optional.isPresent()) {

                User userupdate = userRepo.findById(Long.valueOf(user.get("id").toString())).get();

                userupdate.setRole(user.get("role").toString());

                userRepo.save(userupdate);

            }

            JSONObject res = new JSONObject();
            res.put("status", "success");
            res.put("statusCode", "00");
            res.put("data", optional.get());

            return res;

        } catch (Exception e) {
            e.printStackTrace();
            JSONObject res = new JSONObject();
            res.put("status", "Action Failed");
            res.put("statusCode", "02");
            res.put("data", e.getMessage());
            return res;
        }
    }
}
