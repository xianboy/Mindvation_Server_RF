package com.mdvns.mdvn.mdvnreward.domain;


import com.mdvns.mdvn.common.bean.model.Tag;
import com.mdvns.mdvn.mdvnreward.domain.entity.Reward;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class RewardDetail {
    /*热门标签*/
    private Object hotTags;
    /*未解决的悬赏榜list*/
    private Object unsolvedRewards;
}
