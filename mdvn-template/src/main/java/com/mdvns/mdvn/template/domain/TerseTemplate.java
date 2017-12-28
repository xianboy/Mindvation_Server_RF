package com.mdvns.mdvn.template.domain;

import com.mdvns.mdvn.template.domain.entity.Industry;
import com.mdvns.mdvn.template.domain.entity.Template;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class TerseTemplate implements Serializable {

    /*行业信息*/
    private Industry industry;
    /*模板*/
    private List<Template> templates;

}
