package com.mdvns.mdvn.story.domain.entity;

import com.mdvns.mdvn.common.constant.MdvnConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 成员映射
 */
@Entity
@Data
@NoArgsConstructor
public class StoryMember implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    /*数据创建人id*/
    @Column(nullable = false)
    private Long creatorId;

    /*storyId*/
    @Column(nullable = false)
    private Long storyId;

    /*角色Id*/
    @Column(nullable = false)
    private Long roleId;

    /*成员Id*/
    @Column(nullable = false)
    private Long memberId;

    /*创建时间*/
    @Column(nullable = false)
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    /*是否已删除*/
    private Integer isDeleted = MdvnConstant.ZERO;
}
