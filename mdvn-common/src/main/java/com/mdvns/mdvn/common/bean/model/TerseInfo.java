package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class TerseInfo implements Serializable {
    /*id*/
    private Long id;

    /*serialNo*/
    private String serialNo;

    /*name*/
    private String name;
}
