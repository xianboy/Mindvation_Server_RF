package com.mdvns.mdvn.mdvnreward.domain;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@NoArgsConstructor
public class RewardListResponse {

    private List<RewardInfo> rewardInfos;
    private Long totalElements;
}
