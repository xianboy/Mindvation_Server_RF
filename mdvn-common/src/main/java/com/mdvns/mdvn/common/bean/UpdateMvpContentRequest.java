package com.mdvns.mdvn.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMvpContentRequest implements Serializable {

    /*当前用户ID*/
    @NotNull(message = "staffId 不能为空")
    private Long staffId;

    /*requirement编号*/
    @NotNull(message = "serialNo不能为空")
    @NotEmpty(message = "serialNo必须有元素")
    private List<String> serialNo;

    /*mvpId*/
    @NotNull(message = "mvpId不能为空")
    private Long mvpId;

}
