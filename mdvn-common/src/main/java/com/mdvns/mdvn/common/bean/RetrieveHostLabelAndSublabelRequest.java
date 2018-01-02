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
public class RetrieveHostLabelAndSublabelRequest implements Serializable {

    @NotNull(message = "staffId不能为空")
    private Long staffId;

    @NotNull(message = "hostLabelId不能为空")
    private Long hostLabelId;

    @NotBlank(message = "labelHostSerialNo不能为空")
    private String labelHostSerialNo;

    public RetrieveHostLabelAndSublabelRequest(Long staffId, Long hostLabelId, String labelHostSerialNo) {
        this.staffId = staffId;
        this.hostLabelId = hostLabelId;
        this.labelHostSerialNo = labelHostSerialNo;
    }

    private Integer isDeleted;

}
