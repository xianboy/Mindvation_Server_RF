package com.mdvns.mdvn.common.bean.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffTagScore {

    //标签id
    private List<Long> tagId;

    //staffId
    private Long staffId;

    private Double tagScore;


}
