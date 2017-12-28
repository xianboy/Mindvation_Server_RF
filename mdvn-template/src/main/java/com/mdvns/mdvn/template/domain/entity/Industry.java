package com.mdvns.mdvn.template.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Industry {
    @Id
    @GeneratedValue
    private Long id;

    /*创建人Id*/
    @NotNull(message = "creatorId不能为空")
    @Min(value = 1, message = "id不能小于1")
    @Column(nullable = false)
    private Long creatorId;

    /*名称*/
    @NotBlank(message = "name不能为空")
    @Column(nullable = false)
    private String name;

    /*创建时间*/
    @Column(columnDefinition = "timestamp", nullable = false)
    private Timestamp createTime;

    /*引用次数*/
    @Column(nullable = false)
    private Integer quoteCnt = MdvnConstant.ZERO;

    //1~7的随机数
    @NotNull(message = "style不能为空")
    @Min(value = 1, message = "style的值不能小于1")
    @Column(nullable = false)
    private Integer style = MdvnConstant.ONE;

    /*是否已删除*/
    @JsonIgnore
    private Integer isDeleted;
}
