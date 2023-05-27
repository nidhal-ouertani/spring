package com.main.catchy.controller;

import com.main.catchy.security.jwt.AuthTokenFilter;
import com.main.catchy.services.AppointmentServices;
import com.main.catchy.services.CompetenceSrvImp;
import com.main.catchy.services.ContactServicesImp;
import com.main.catchy.services.UserServicesImp;
import com.main.catchy.utils.*;
import com.main.catchy.utils.Responce.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MENTOR') or hasAuthority('MENTEE')")
@CrossOrigin("*")
@RequestMapping("api/catchy")
@RestController
@RequiredArgsConstructor
public class MainController {
    private final AuthTokenFilter filter;
    private final UserServicesImp userServices;
    private final CompetenceSrvImp cmpServices;
    private final ContactServicesImp ctcServices;
    private final AppointmentServices appointmentServices;


    @PutMapping("/validate")
    public void valiadeMentor(@RequestParam(value = "validate", required = true) boolean validate,
                              @RequestParam(value = "competenceId", required = false) List<Long> competenceId,
                              @RequestParam(value = "userId", required = true) Long userId) {

        userServices.valiadeMentor(validate, competenceId, userId);

    }

    @GetMapping("/checkPassword/{id}/{password}")
    public boolean checkPassword(@PathVariable(name = "id") Long id, @PathVariable(name = "password") String password) {

        return userServices.existpassword(id, password);

    }

    @GetMapping("/checkUserName/{username}")
    public Boolean checkUserName(@PathVariable(name = "username") String username) {

        return userServices.checkUserName(username);

    }

    @PostMapping("/updateUserPassword")
    public Object updatePassword(@RequestBody UserBody ursbody) {

        return userServices.updatePassword(ursbody);

    }

    // Ajout competence
    @PostMapping("/addSkill")
    public void addSkill(@RequestBody List<CompetenceBody> data) {
        data.stream().forEach(skill -> {
            cmpServices.addSkill(skill);
        });

    }
    @GetMapping("/getUserList")
    public List<UserProfile> getUserList(@RequestParam(value = "type", required = false) String type) {

        return userServices.getUserList(type);

    }

    @GetMapping("/getUserProfil")
    public UserProfile getUserProfile(@RequestParam(value = "userID") Long userID) {

        return userServices.getUserProfile(userID);

    }

    @PutMapping("/updUser")
    public Response<Object> addUser(@RequestBody UserProfile ursbody) {

        return userServices.UpdateUser(ursbody);
    }

    @PostMapping("/addSchedTime")
    public Response<Object> addSchedTime(@RequestBody SchedTimeBody data) {
        return ctcServices.addSchedTime(data);
    }


    @PutMapping("/updateSchedTime")
    public Response<Object> updateSchedTime(@RequestBody SchedTimeBody data) {
        return ctcServices.updateSchedTime(data);
    }

    @GetMapping("/getSchedTime")
    public Response<Object> getSchedTime(@RequestParam(value = "userId") Long userId) {
        return ctcServices.getSchedTime(userId);
    }


    @PostMapping("/createAppointment")
    public Response<Object> createAppointment(@RequestBody AppointmentBody data, HttpServletRequest request) {
      var token=  filter.parseJwt(request);
        return appointmentServices.createAppointment(data,token);
    }
    @PutMapping("/confirmeAppointment")
    public Response<Object> confirmeAppointment(@RequestBody AppointmentBody data, HttpServletRequest request) {
        var token=  filter.parseJwt(request);
        return appointmentServices.confirmeAppointment(data,token);
    }
}
