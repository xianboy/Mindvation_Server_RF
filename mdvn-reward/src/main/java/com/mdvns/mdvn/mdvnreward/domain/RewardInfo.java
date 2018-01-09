package com.mdvns.mdvn.mdvnreward.domain;


import com.mdvns.mdvn.common.bean.model.Staff;
import com.mdvns.mdvn.common.bean.model.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import java.sql.Timestamp;

@Component
@Data
@NoArgsConstructor
public class RewardInfo {

    private Long id;
    //悬赏榜编号
    private String serialNo;
    private String name;
    private Long creatorId;//创建者Id
    private Long unveilId;//揭榜者Id
    //求助描述
    private String content;
    private Long createTime;
    private Integer welfareScore;//福利积分
    private Integer contribution;//贡献度
    private Integer isUnveil;//是否已揭榜 (1代表已解决，0代表未解决)
    private Long tagId;
    private Integer isDeleted;

    private Staff creatorInfo;
    private Staff unveilInfo;
    private Tag tagInfo;
}
