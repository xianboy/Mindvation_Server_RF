package com.mdvns.mdvn.tag.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 标签类，映射表tag
 * 添加联合约束 uniqueConstraints: tagId,name
 * name不能重复
 */
@Entity
@Data
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames ={"name"})})
public class Tag implements Serializable{
    @Id
    @GeneratedValue
    private Long id;

    /*创建人id*/
    @NotNull(message = "创建人的id不能为空")
    @Min(value = 1, message = "id的值不能小于1")
    private Long creatorId;

    /*编号*/
    @Column(nullable = false)
    private String serialNo;

    /*名称*/
    @NotBlank(message = "名称不能为空")
    @Column(nullable = false)
    private String name;

    /*被引用的次数*/
    private Integer quoteCnt = MdvnConstant.ZERO;

    /*色值：由于移动端还在使用，暂时保留*/
    private String color;

    /*创建时间*/
    @Column(name = "create_time", columnDefinition = "timestamp default current_timestamp", nullable = false)
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    /*后加的字段，1~7随机给一个数字*/
    @NotNull(message = "style不能为空")
    @Min(value = 1, message = "style的值不能小于1")
    private Integer style = MdvnConstant.ONE;

    /*是否已删除*/
    @JsonIgnore
    private Integer isDeleted = MdvnConstant.ZERO;

}
