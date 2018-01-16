package com.mdvns.mdvn.staff.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Component
@Data
public class StaffAuthInfo {

    @Id
    @GeneratedValue
    private Integer id;

    /*项目Id*/
//    private String projId;
    private String projSerialNo;

    /*员工Id*/
<<<<<<< HEAD
=======
//    private String staffId;
>>>>>>> d3bc18c747396a1b3b704de7144e67dd8f4159ef
    private Long staffId;

    /*权限编号*/
    private Integer authCode;

    /*项目模块Id*/
<<<<<<< HEAD
=======
//    private String hierarchyId;
>>>>>>> d3bc18c747396a1b3b704de7144e67dd8f4159ef
    private String hostSerialNo;

    /*权限添加人Id*/
//    private String assignerId;
    private Long assignerId;

    /*添加时间*/
    private Timestamp createTime;

    /*是否权限已取消*/
    @JsonIgnore
    private Integer isDeleted;

}
