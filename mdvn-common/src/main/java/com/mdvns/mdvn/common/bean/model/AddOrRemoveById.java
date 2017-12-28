package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class AddOrRemoveById implements Serializable {
    /*新增对象*/
    @Size(min = 1, message = "如果有新增对象，addList必须有元素")
    private List<Long> addList;
    /*删除对象*/
    @Size(min = 1, message = "如果有删除对象，removeList必须有元素")
    private List<Long> removeList;
}
