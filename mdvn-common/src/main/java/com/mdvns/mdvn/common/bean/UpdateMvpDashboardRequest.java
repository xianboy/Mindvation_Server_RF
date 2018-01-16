package com.mdvns.mdvn.common.bean;

import com.mdvns.mdvn.common.bean.model.MvpContent;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class UpdateMvpDashboardRequest implements Serializable {

    /*当前用户id*/
    @NotNull(message = "staffId不能为空")
    private Long staffId;

    /*层级标识字段*/
    @NotNull(message = "layerType 不能为空")
    private Integer layerType;

    /*mvp修改的内容*/
    @NotNull(message = "mvpList 不能为空")
    @NotEmpty(message = "mvpList 必须有元素")
    private List<MvpContent> mvpList;
}