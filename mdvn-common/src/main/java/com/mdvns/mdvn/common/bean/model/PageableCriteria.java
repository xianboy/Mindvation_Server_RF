package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class PageableCriteria implements Serializable{

    /*当前页码*/
    @Min(value = 1, message = "查询页码参数不能小于1")
    private Integer page;
    /*每页数据条数*/
    @Min(value = 1,message = "每页数据条数不能小于1")
    private Integer size;
    /*排序方向:0-->ASC 1-->DESC*/
    private Integer direction;
    /*排序参数*/
    private List<String> sortBy;

    public PageableCriteria(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

}
