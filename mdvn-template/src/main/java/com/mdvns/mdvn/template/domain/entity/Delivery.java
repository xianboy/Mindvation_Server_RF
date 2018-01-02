package com.mdvns.mdvn.template.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"serialNo"})})
public class Delivery implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    /*创建人*/
    @Column(nullable = false)
    @JsonIgnore
    private Long creatorId;

    /*上层关联对象编号*/
    @Column(nullable = false)
    private String hostSerialNo;

    /*编号*/
    @Column(nullable = false)
    private String serialNo;

    /*名称*/
    @Column(nullable = false)
    private String name;

    /*类型(三种):1.附件和进度;2.进度;3.附件*/
    @Column(nullable = false)
    private Integer typeId;

    /*创建时间*/
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    /*是否已删除*/
    @Column(nullable = false)
    @JsonIgnore
    private Integer isDeleted = MdvnConstant.ZERO;


}
