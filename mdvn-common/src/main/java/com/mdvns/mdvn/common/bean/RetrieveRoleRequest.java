package com.mdvns.mdvn.common.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class RetrieveRoleRequest implements Serializable {

    /*当前用户id*/
    @NotNull(message = "用户id不能为空.")
    private Long staffId;

    /*模板id*/
    @NotNull(message = "模板id不能为空.")
    private Long templateId;

    /*角色关联主体id*/
    private Long hostId;

    /*模板关联主体编号*/
    @NotBlank(message = "模板关联主体编号不能为空")
    private String hostSerialNo;

    /*是否已删除*/
    private Integer isDeleted;

}
