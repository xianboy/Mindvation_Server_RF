package com.mdvns.mdvn.staff.web;

import com.mdvns.mdvn.staff.domain.AssignAuthRequest;
import com.mdvns.mdvn.staff.domain.RemoveAuthRequest;
import com.mdvns.mdvn.staff.domain.RtrvStaffAuthInfoRequest;
import com.mdvns.mdvn.staff.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = {"/staff", "/v1.0/staff"})
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 给项目相关员工添加权限
     * @param authRequest
     * @return
     */
    @PostMapping(value = "/assignAuth")
    public ResponseEntity<?> assignAuth(@RequestBody AssignAuthRequest authRequest) {
        ResponseEntity<?> responseEntity = this.authService.assignAuth(authRequest);
        return responseEntity;
    }


    /**
     * 获取员工在指定项目中的权限信息
     * @param rtrvAuthRequest
     * @return
     */
    @PostMapping(value = "/rtrvAuth")
    public ResponseEntity<?> rtrvAuth(@RequestBody RtrvStaffAuthInfoRequest rtrvAuthRequest) {
        return this.authService.rtrvAuth(rtrvAuthRequest);
    }


    @PostMapping(value = "/removeAuth")
    public ResponseEntity<?> removeAuth(@RequestBody RemoveAuthRequest removeAuthRequest) {
        return this.authService.removeAuth(removeAuthRequest);
    }


}
