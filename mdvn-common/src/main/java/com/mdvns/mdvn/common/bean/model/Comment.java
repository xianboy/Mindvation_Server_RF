package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;

/**
 * 评论类
 */
@Component
@Data
@NoArgsConstructor
public class Comment {
    private Integer uuId;
    //所属的项目Id
    private String projId;
    //所属需求或者story的Id
    private String subjectId;
    //评论的Id
    private String commentId;
    //回复的时哪条评论的Id(可为空)
    private String replyId;
    //发起@的人(创建这条评论的人)
    private Long creatorId;
    //被@的人(可以是多个人)
    private String passiveAts;
    //评论的内容
    private String content;
    //点赞这个评论的人数
    private Integer likeQty;
    //踩这个评论的人数
    private Integer dislikeQty;
    //点赞的所有人的Id
    private String likeIds;
    //踩的所有人的Id
    private String dislikeIds;
    //创建评论的时间
    private Timestamp createTime;
    //回复上一次被@的差值(间隔时间)
    private Long intervalTime;
    //是否已删除
    private Integer isDeleted;

    private Staff creatorInfo;
}


