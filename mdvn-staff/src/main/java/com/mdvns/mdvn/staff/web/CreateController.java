package com.mdvns.mdvn.staff.web;


import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.util.BindingResultUtil;
import com.mdvns.mdvn.staff.domain.CreateStaffRequest;
import com.mdvns.mdvn.staff.domain.LoginRequest;
import com.mdvns.mdvn.staff.domain.UpdateStaffRequest;
import com.mdvns.mdvn.staff.service.CreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = {"/staff", "/v1.0/staff"})
public class CreateController {

    @Autowired
    private CreateService staffService;


    /**
     * 添加员工
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/create")
    public RestResponse<?> create(@RequestBody @Validated CreateStaffRequest request) throws BusinessException {
//        BindingResultUtil.brResolve(bindingResult);
        return this.staffService.create(request);
    }

    /**
     * 登录
     *
     * @param loginRequest
     * @return
     */
    @PostMapping(value = "/login")
    public RestResponse<?> login(@RequestBody @Validated LoginRequest loginRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.staffService.login(loginRequest);
    }

    /**
     * 更新用户信息
     *
     * @param updateRequest
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "/update")
    public RestResponse<?> updateStaff(@RequestBody @Validated UpdateStaffRequest updateRequest, BindingResult bindingResult) throws BusinessException {
        BindingResultUtil.brResolve(bindingResult);
        return this.staffService.updateStaff(updateRequest);
    }

    /**
     * 获取staff列表信息
     *
     * @param retrieveStaffListRequest
     * @return
     */
    /*
    @PostMapping(value = "/rtrvStaffList")
    public ResponseEntity<?> rtrvStaffList(@RequestBody RetrieveStaffListRequest retrieveStaffListRequest) {
        return this.staffService.rtrvStaffList(retrieveStaffListRequest);
    }

    *//**
     * 通过staffID的list获取staff对象列表信息
     *
     * @param request
     * @return
     *//*
    @PostMapping(value = "/rtrvStaffListByStaffIdList")
    public ResponseEntity<?> rtrvStaffListById(@RequestBody RtrvStaffListByIdRequest request) {
        return this.staffService.rtrvStaffListById(request);
    }*/

   /* *//**
     * 通过staffID获取staff对象信息
     *
     * @param request
     * @return
     *//*
    @PostMapping(value = "/rtrvStaffInfo")
    public RestResponse rtrvStaffInfo(@RequestBody RtrvStaffInfoRequest request) {
        return this.staffService.rtrvStaffInfo(request.getStaffId());
    }

    *//**
     * 模糊查询
     *
     * @param request
     * @return
     *//*
    @PostMapping(value = "/rtrvStaffListByName")
    public ResponseEntity<?> rtrvStaffListByName(@RequestBody RtrvStaffListByNameRequest request) {
        return this.staffService.rtrvStaffListByName(request);
    }

    *//**
     * 根据指定Id获取Staff信息
     * @param id
     * @return
     *//*
    @PostMapping(value = "/{id}")
    public RestResponse<Staff> retrieve(@PathVariable String id) {
        return this.staffService.retrieve(id);
    }

    *//**
     * 给项目相关员工添加权限
     * @param authRequest
     * @return
     *//*
    @PostMapping(value = "/assignAuth")
    public ResponseEntity<?> assignAuth(@RequestBody AssignAuthRequest authRequest) {
        ResponseEntity<?> responseEntity = this.authService.assignAuth(authRequest);
        return responseEntity;
    }

    *//**
     * 获取员工在指定项目中的权限信息
     * @param rtrvAuthRequest
     * @return
     *//*
    @PostMapping(value = "/rtrvAuth")
    public ResponseEntity<?> rtrvAuth(@RequestBody RtrvStaffAuthInfoRequest rtrvAuthRequest) {

        return this.authService.rtrvAuth(rtrvAuthRequest);
    }

    @PostMapping(value = "/removeAuth")
    public ResponseEntity<?> removeAuth(@RequestBody RemoveAuthRequest removeAuthRequest) {
        return this.authService.removeAuth(removeAuthRequest);
    }

    @RequestMapping(value = "/removeAllAuth/{projId}/{hierarchyId}")
    public ResponseEntity<?> removeAllAuth(@PathVariable String projId, @PathVariable String hierarchyId) {
        return this.authService.removeAllAuth(projId, hierarchyId);
    }

    @PostMapping(value = "/deleteStaff/{staffId}")
    public ResponseEntity<?> deleteStaff(@PathVariable String staffId) {
        return this.staffService.deleteStaff(staffId);
    }

    @PostMapping(value = "/rtrvStaffDetail/{staffId}")
    public ResponseEntity<?> rtrvStaffDetail(@PathVariable String staffId) {
        return this.staffService.rtrvStaffDetail(staffId);
    }

    @PostMapping(value = "/updateStaffDetail")
    public ResponseEntity<?> updateStaffDetail(@RequestBody UpdateStaffDetailRequest request) {
        return this.staffService.updateStaffDetail(request);
    }
*/
   /* @PostMapping(value = "/rtrvStaff")
    public ResponseEntity<?> rtrvStaff(@RequestBody RtrvStaffRequest rtrvStaffRequest) {
        return this.staffService.rtrvStaff(rtrvStaffRequest);
    }*/


}
