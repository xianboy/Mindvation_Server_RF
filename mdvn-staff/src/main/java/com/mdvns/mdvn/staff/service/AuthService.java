package com.mdvns.mdvn.staff.service;

import com.mdvns.mdvn.staff.domain.RemoveAuthRequest;
import com.mdvns.mdvn.staff.domain.AssignAuthRequest;
import com.mdvns.mdvn.staff.domain.RtrvStaffAuthInfoRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> assignAuth(AssignAuthRequest assignAuthRequest);

    ResponseEntity<?> rtrvAuth(RtrvStaffAuthInfoRequest rtrvAuthRequest);


//    ResponseEntity<?> removeAllAuth(String projId, String hierarchyId);

    ResponseEntity<?> removeAuth(RemoveAuthRequest removeAuthRequest);


}
