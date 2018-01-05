package com.mdvns.mdvn.template.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateMvpTemplateRequest implements Serializable {

    /*MVP序列号*/
    @NotNull(message = "index不能为空")
    private Integer mvpIndex;

    /*内容Id*/
    @NotNull(message = "contents不能为空")
    private List<String> contents;

}
