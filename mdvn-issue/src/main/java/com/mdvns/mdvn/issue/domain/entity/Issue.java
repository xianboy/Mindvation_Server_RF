package com.mdvns.mdvn.issue.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * 发布求助（悬赏榜）
 */
@Component
@Entity
@Data
@NoArgsConstructor
public class Issue {

    @Id
    @GeneratedValue
    private Integer uuId;
    private String issueId;
    //所属需求或者story的Id
    private String subjectId;
    private Long creatorId;
    //求助描述
    @Column(columnDefinition = "text",nullable = false)
    private String content;
    private Timestamp createTime;
    private Integer reward;//赏金
    private Integer isResolved;//问题是否已解决 (1代表已解决，0代表未解决)
    private Long tagId;
    private String projId;

}
