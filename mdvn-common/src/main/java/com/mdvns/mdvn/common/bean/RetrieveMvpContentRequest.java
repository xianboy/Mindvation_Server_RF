package com.mdvns.mdvn.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveMvpContentRequest implements Serializable {

    /*当前用户ID*/
    @NotNull(message = "staffId 不能为空")
    private Long staffId;

    /*mvpId*/
    private List<Long> top2MvpId;

    /*模板对应的需求编号(四层)*/
    private List<String> serialNoList;

    /*isDeleted*/
    private Integer isDeleted;

    public RetrieveMvpContentRequest(Long staffId, List<Long> top2MvpId, List<String> serialNoList) {
        this.staffId = staffId;
        this.top2MvpId = top2MvpId;
        this.serialNoList = serialNoList;
    }
}

