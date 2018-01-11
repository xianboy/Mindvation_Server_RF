package com.mdvns.mdvn.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveReqmtSerialNoRequest implements Serializable {

    /*当前用户ID*/
    @NotNull(message = "staffId 不能为空")
    private Long staffId;

    /*hostSerialNo*/
    @NotBlank(message = "hostSerialNo 不能为空")
    private String hostSerialNo;

    /*模板ID*/
    @NotNull(message = "templateId 不能为空")
    private Long templateId;

    /*是否已删除*/
    private Integer isDeleted;

    public RetrieveReqmtSerialNoRequest(Long staffId, String hostSerialNo, Long templateId) {
        this.staffId = staffId;
        this.hostSerialNo = hostSerialNo;
        this.templateId = templateId;
    }
}
