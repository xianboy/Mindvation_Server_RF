package com.mdvns.mdvn.common.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class RetrieveReqmtByLabelRequest implements Serializable {

    @NotNull(message = "staffId不能为空")
    /*当前用户ID*/
    private Long staffId;

    /*项目编号*/
    @NotBlank(message = "hostSerialNo 不能为空")
    private String hostSerialNo;

    /*过程方法ID*/
    @NotNull(message = "labelID 不能为空")
    private List<Long> labels;

    private Integer isDeleted;

    public RetrieveReqmtByLabelRequest(Long staffId, String hostSerialNo, List<Long> labels) {
        this.staffId = staffId;
        this.hostSerialNo = hostSerialNo;
        this.labels = labels;
    }
}
