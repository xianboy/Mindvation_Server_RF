package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class RoleMember implements Serializable {

    /*模板角色id和name*/
    private TerseInfo role;

    /*成员*/
    private List<TerseInfo> members;

}
