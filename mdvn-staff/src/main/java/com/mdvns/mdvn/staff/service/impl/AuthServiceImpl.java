package com.mdvns.mdvn.staff.service.impl;


import com.mdvns.mdvn.staff.domain.AssignAuthRequest;
import com.mdvns.mdvn.staff.domain.RtrvStaffAuthInfoRequest;
import com.mdvns.mdvn.staff.domain.entity.StaffAuthInfo;
import com.mdvns.mdvn.staff.repository.StaffAuthInfoRepository;
import com.mdvns.mdvn.staff.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private StaffAuthInfoRepository authInfoRepository;

    @Autowired
    private StaffAuthInfo staffAuthInfo;

    @Override
    public ResponseEntity<?> assignAuth(AssignAuthRequest assignAuthRequest) {

        List<String> assignees = assignAuthRequest.getAssignees();
        String assignerId = assignAuthRequest.getAssignerId();

        List<StaffAuthInfo> staffAuthInfos = new ArrayList<StaffAuthInfo>();
        for (String assigneeId : assignees) {
            staffAuthInfo = new StaffAuthInfo();
            staffAuthInfo.setProjId(assignAuthRequest.getProjId());
            staffAuthInfo.setStaffId(assigneeId);
            staffAuthInfo.setAuthCode(assignAuthRequest.getAuthCode());
            staffAuthInfo.setHierarchyId(assignAuthRequest.getHierarchyId());
            staffAuthInfo.setAssignerId(assignAuthRequest.getAssignerId());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            staffAuthInfo.setCreateTime(timestamp);
            staffAuthInfo.setIsDeleted(0);
            staffAuthInfo = this.authInfoRepository.save(staffAuthInfo);
            staffAuthInfos.add(staffAuthInfo);
        }

        LOG.info("添加权限的request为：{}", assignAuthRequest.toString());

        LOG.info("SAPI添加权限结束：{}", staffAuthInfo.toString());
        return ResponseEntity.ok(staffAuthInfos);
    }

    /**
     * 获取权限信息
     * @param rtrvAuthRequest
     * @return
     */
    @Override
    public ResponseEntity<?> rtrvAuth(RtrvStaffAuthInfoRequest rtrvAuthRequest) {
        LOG.info("获取权限Request：{}", rtrvAuthRequest.toString());
        String projId = rtrvAuthRequest.getProjId();
        String staffId = rtrvAuthRequest.getStaffId();
        String hierarchyId = rtrvAuthRequest.getHierarchyId();
        LOG.info("SPAPI获取员工权限的request为：{}", rtrvAuthRequest.toString());
        List<StaffAuthInfo> staffAuthInfos =  this.authInfoRepository.findByProjIdAndHierarchyIdAndStaffId(projId, hierarchyId, staffId);

        LOG.info("SAPI查到的员工权限为：{}", staffAuthInfos.size());
        return ResponseEntity.ok(staffAuthInfos);
    }
/*
    public ResponseEntity<?> removeAuth() {
        StaffAuthInfo staffAuthInfo = this.authInfoRepository.findOne(removeAuthRequest.getStaffAuthInfo().getId());


    }*/

    public ResponseEntity<?> removeAllAuth(String projId, String hierarchyId) {
        int nums = this.authInfoRepository.deleteAllByProjIdAndHierarchyId(projId, hierarchyId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
