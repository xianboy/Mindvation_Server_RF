package com.mdvns.mdvn.common.bean.model;

import com.mdvns.mdvn.common.constant.MdvnConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 成员映射
 */
@Data
@NoArgsConstructor
public class StoryMember implements Serializable {
    private Long id;

    /*数据创建人id*/
    private Long creatorId;

    /*storyId*/
    private Long storyId;

    /*角色Id*/
    private Long roleId;

    /*成员Id*/
    private Long memberId;

    /*创建时间*/
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    /*是否已删除*/
    private Integer isDeleted = MdvnConstant.ZERO;
}
