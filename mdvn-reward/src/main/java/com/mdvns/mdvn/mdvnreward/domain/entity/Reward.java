package com.mdvns.mdvn.mdvnreward.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * （悬赏榜）
 */
@Component
@Entity
@Data
@NoArgsConstructor
public class Reward {

    @Id
    @GeneratedValue
    private Long id;
    //悬赏榜编号
    private String serialNo;
    /*悬赏名称*/
    private String name;
    private Long creatorId;//创建者Id
    private Long unveilId;//揭榜者Id
    //求助描述
    @Column(columnDefinition = "text", nullable = false)
    private String content;
    private Timestamp createTime;
    private Integer welfareScore;//福利积分
    private Integer contribution;//贡献度
    private Integer isUnveil = 0;//是否已揭榜 (1代表已揭榜，0代表未揭榜)
    private Integer isResolved = 0;//是否已解决 (1代表已解决，0代表未解决)
    private Long tagId;
    /*是否被删除*/
    private Integer isHaveProject = 0;//是否已经创建项目
    private String projSerialNo;//项目编号
    @JsonIgnore
    private Integer isDeleted = 0;
}
