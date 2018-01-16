package com.mdvns.mdvn.dashboard.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateDashboardRequest implements Serializable {

    /*当前用户id*/
    @NotNull(message = "staffId不能为空")
    private Long staffId;

    /*dashboard 依赖的主体编号*/
    @NotBlank(message = "hostSerialNo不能为空")
    private String hostSerialNo;



}
