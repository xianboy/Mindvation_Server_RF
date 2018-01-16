package com.mdvns.mdvn.staff.domain.entity;

import com.mdvns.mdvn.common.bean.model.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
public class StaffTag {

    @Id
    @GeneratedValue
    private Long id;
    /*创建人id*/
    @Column(nullable = false)
    private Long creatorId;
    /*staffId*/
    @Column(nullable = false)
    private Long staffId;
    /*标签Id*/
    private Long tagId;
    /*是否已删除*/
    private Integer isDeleted;
    /*最后更新时间*/
    private Timestamp lastUpdateTime;
    /*创建时间*/
    private Timestamp createTime;
}
