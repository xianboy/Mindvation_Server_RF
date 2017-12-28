package com.mdvns.mdvn.common.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class MemberRequest implements Serializable {

    /*模板角色id*/
    @NotNull(message = "tmplRoleId不能为空")
    private Long roleId;

    /*添加的member*/
    @Size(min = 1, message = "addList必须有元素")
    private List<Long> addList;

    /*删除的member*/
    @Size(min = 1, message = "addList必须有元素")
    private List<Long> removeList;
}
