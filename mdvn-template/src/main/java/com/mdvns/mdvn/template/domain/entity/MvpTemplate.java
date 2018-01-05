package com.mdvns.mdvn.template.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
public class MvpTemplate {

    @Id
    @GeneratedValue
    private Long id;

    /*标签创建人id*/
    @Column(nullable = false)
    private Long creatorId;

    /*上层模块(模板/项目)编号*/
    @Column(nullable = false)
    private String hostSerialNo;

    /*顺序*/
    @Column(nullable = false)
    private Integer index;

    /*被引用的次数*/
    @JsonIgnore
    private Integer quoteCnt = MdvnConstant.ZERO;

    /*标签创建时间*/
    @Column(nullable = false)
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    /*后加的字段，1~7随机给一个数字*/
    private Integer style = MdvnConstant.ONE;

    /*是否已删除*/
    @JsonIgnore
    private Integer isDeleted = MdvnConstant.ZERO;

}
