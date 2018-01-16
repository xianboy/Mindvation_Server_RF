package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class MvpContent implements Serializable {

    /*更新的mvp的Id*/
    @NotNull(message = "mvpId 不能为空")
    private Long mvpId;

    /*mvp的内容对象ID*/
    private List<Long> contents;

}
