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
public class IterationTemplate {

    @Id
    @GeneratedValue
    private Long id;

    /*标签创建人id*/
    @NotNull(message = "创建人的id不能为空")
    @Min(value = 1, message = "id的值不能小于1")
    private Long creatorId;

    /*标签名称*/
    @NotBlank(message = "标签名称不能为空")
    @Column(nullable = false)
    private String name;

    /*被引用的次数*/
    private Integer quoteCnt = MdvnConstant.ZERO;


    /*标签创建时间*/
    @Column(name = "create_time", columnDefinition = "timestamp default current_timestamp", nullable = false)
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    /*后加的字段，1~7随机给一个数字*/
    @NotNull(message = "style不能为空")
    @Min(value = 1, message = "style的值不能小于1")
    private Integer style = MdvnConstant.ONE;

    /*是否已删除*/
    @JsonIgnore
    private Integer isDeleted = MdvnConstant.ZERO;

    /*模板id*/
    private Long templateId;
}
