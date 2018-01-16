package com.mdvns.mdvn.template.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdvns.mdvn.common.bean.model.TerseInfo;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Persistent;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name","serialNo"})})
public class Template {

    @Id
    @GeneratedValue
    private Long id;

    /*创建人ID*/
    @NotNull(message = "creatorId不能为空")
    @Min(value = 1, message = "id不能小于1")
    @Column(nullable = false)
    private Long creatorId;

    /*名称*/
    @NotBlank(message = "name不能为空")
    @Column(nullable = false)
    private String name;

    /*编号*/
    @Column(nullable = false)
    private String serialNo;

    /*引用次数*/
    @Column(nullable = false)
    private Integer quoteCnt = MdvnConstant.ZERO;

    //模板行业类型
    @NotNull(message = "industryId不能为空")
    @Min(value = 1, message = "id不能小于1")
    @Column(nullable = false)
    private Long industryId;

    /*创建时间*/
    @Column(columnDefinition = "timestamp", nullable = false)
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    //1~7的随机数
    @NotNull(message = "style不能为空")
    @Min(value = 1, message = "style的值不能小于1")
    @Column(nullable = false)
    private Integer style = MdvnConstant.ONE;

    /*是否已删除*/
    @JsonIgnore
    @Column(nullable = false)
    private Integer isDeleted = MdvnConstant.ZERO;

    /*过程方法模块*/
    @Transient//非持久化字段
    private List<TerseInfo> functionLabels;

    /*模板角色*/
    @Transient//非持久化字段
    private List<TerseInfo> roles;

}
