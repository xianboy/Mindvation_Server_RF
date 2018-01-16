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
    private Long mvpId;

    /*模板对应的需求编号(四层)*/
    private List<String> hostSerialNoList;

    /*isDeleted*/
    private Integer isDeleted;

    public RetrieveMvpContentRequest(Long staffId, Long mvpId, List<String> hostSerialNoList) {
        this.staffId = staffId;
        this.mvpId = mvpId;
        this.hostSerialNoList = hostSerialNoList;
    }

    public RetrieveMvpContentRequest(Long staffId, Long mvpId) {
        this.staffId = staffId;
        this.mvpId = mvpId;
    }

    public RetrieveMvpContentRequest(Long staffId, List<String> hostSerialNoList) {
        this.staffId = staffId;
        this.hostSerialNoList = hostSerialNoList;
    }
}

