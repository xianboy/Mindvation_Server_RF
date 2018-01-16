package com.mdvns.mdvn.staff.domain.entity;

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
    private String projId;

    /*员工Id*/
<<<<<<< HEAD
    private String staffId;
=======
    private Long staffId;
>>>>>>> parent of c74f720... Merge branch 'master' of https://github.com/xianboy/Mindvation_Server_RF

    /*权限编号*/
    private Integer authCode;

    /*项目模块Id*/
<<<<<<< HEAD
    private String hierarchyId;
=======
    private String hostSerialNo;
>>>>>>> parent of c74f720... Merge branch 'master' of https://github.com/xianboy/Mindvation_Server_RF

    /*权限添加人Id*/
    private String assignerId;

    /*添加时间*/
    private Timestamp createTime;

    /*是否权限已取消*/

    private Integer isDeleted;

}
