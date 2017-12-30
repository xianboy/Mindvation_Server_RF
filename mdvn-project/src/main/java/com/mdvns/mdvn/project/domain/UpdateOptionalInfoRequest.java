package com.mdvns.mdvn.project.domain;

import com.mdvns.mdvn.common.bean.model.AddOrRemoveById;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class UpdateOptionalInfoRequest {

    /*当前用户id*/
    @NotNull(message = "staffId不能为空")
    @Min(value = 1, message = "staffId不能小于1")
    private Long staffId;
    /*当前对象id*/
    @NotNull(message = "对象id不能为空")
    @Min(value = 1, message = "hostId不能小于1")
    private Long hostId;
    /*更新附件*/
    private AddOrRemoveById attaches;
}
