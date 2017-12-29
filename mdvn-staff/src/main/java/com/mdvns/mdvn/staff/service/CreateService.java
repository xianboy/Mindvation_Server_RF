package com.mdvns.mdvn.staff.service;


import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.staff.domain.CreateStaffRequest;
import com.mdvns.mdvn.staff.domain.LoginRequest;
import com.mdvns.mdvn.staff.domain.UpdateStaffRequest;

public interface CreateService {

    /*添加员工*/
    RestResponse<?> create(CreateStaffRequest request) throws BusinessException;

    RestResponse<?> login(LoginRequest loginRequest) throws BusinessException;

    //更新staff信息
    RestResponse<?> updateStaff(UpdateStaffRequest updateRequest) throws BusinessException;
/*
    List<StaffTag> rtrvStaffTagList(String staffId);

    Boolean updateStaffDetail(UpdateStaffDetailRequest request);
    Boolean deleteStaff(String staffId);


    RestResponse<?> findByAccountAndPassword(String account, String password);

    //获取拥有指定标签集中任意标签的所有StaffTag
    RestResponse<?> getStaffByTags(List<String> tags);

    //查询name以指定字符串开始的所有Staff
    RestResponse<?> findByNameStartingWith(String startingStr);
    //查询staffId为指定id的所有tagId集合
    List<String> rtrvTagsByStaffId(String staffId);

    RestResponse<?> login(LoginRequest loginRequest);*/
}
