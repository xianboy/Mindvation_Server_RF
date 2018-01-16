package com.mdvns.mdvn.staff.domain;

import com.mdvns.mdvn.common.bean.model.DepartmentDetail;
import com.mdvns.mdvn.common.bean.model.PositionModel;
import com.mdvns.mdvn.common.bean.model.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class StaffDetail {
    //id
    private Long id;
    //员工编号
    private String serialNum;
    /*账号*/
    private String account;
    /*密码*/
    private String password;
    /*姓名*/
    private String name;
    /*头像*/
    private String avatar;
    /*入职时间*/
    private Long hiredate;
    /*效率值*/
    private Double effective;
    /*贡献值*/
    private Double contribution;
    /*推荐度*/
    private Double recommendation;
    /*工作饱和度*/
    private Double workSaturation;
    /*添加人*/
    private Long creatorId;
    /*职位等级*/
    private String positionLvl;
    /*email*/
    private String email;
    /*手机号*/
    private String mobile;
    /*状态:active, inactive, unregistered*/
    private String status;
    /*性别*/
    private String gender;
    /*添加时间*/
    private Long createTime;
    /*标签*/
    private List<Tag> tags;
    /*所属部门*/
    private DepartmentDetail dept;
    /*职位id*/
    private PositionModel position;
}
