package com.mdvns.mdvn.template.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdvns.mdvn.common.bean.model.TerseInfo;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
 * 过程方法
 */
@Entity
@Data
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"serialNo"})})
public class FunctionLabel {

    @Id
    @GeneratedValue
    private Long id;

    /*创建人ID*/
    @NotNull(message = "id不能为空")
    @Min(value = 1, message = "id不能小于1")
    @Column(nullable = false)
    private Long creatorId;

    /*编号*/
    @Column(nullable = false)
    private String serialNo;

    /*上层关联对象的编号*/
    @NotBlank(message = "hostSerialNo不能为空")
    private String hostSerialNo;

    /*名称*/
    @NotBlank(message = "名称不能为空")
    @Column(nullable = false)
    private String name;

    /*创建时间*/
    @Column(columnDefinition = "timestamp", nullable = false)
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    /*是否已删除*/
    @JsonIgnore
    private Integer isDeleted = MdvnConstant.ZERO;



    /*子模块名称*/
    @Transient//非持久化字段
    private List<FunctionLabel> subLabels;

}
