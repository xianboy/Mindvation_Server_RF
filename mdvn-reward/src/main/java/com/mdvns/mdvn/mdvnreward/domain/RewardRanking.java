package com.mdvns.mdvn.mdvnreward.domain;

import com.mdvns.mdvn.common.bean.model.Staff;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@Data
@NoArgsConstructor
public class RewardRanking {

    private Long noun;//名词
    private Long unveilNum;//已揭榜的数量
    private Long resolveNum;//已解决的数量
    private Staff creatorInfo;
    private Float proportion;//比例
}
