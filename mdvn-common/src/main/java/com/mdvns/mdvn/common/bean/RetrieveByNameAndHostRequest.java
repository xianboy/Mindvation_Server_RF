package com.mdvns.mdvn.common.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class RetrieveByNameAndHostRequest implements Serializable {

    @NotBlank(message = "name不能为空.")
    private String name;

    @NotBlank(message = "hostSerialNo不能为空.")
    private String hostSerialNo;
}
