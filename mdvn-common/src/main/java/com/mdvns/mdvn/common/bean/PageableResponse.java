package com.mdvns.mdvn.common.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class PageableResponse<T> implements Serializable {

    /*对象列表*/
    private List<T> content;
    /*总页数*/
    private Integer totalPages;
    /*数据总数*/
    private Long totalElements;
    /*是否最后一页*/
    private Boolean last;
    /*当前页码*/
    private Integer number;
    /*每页额定数据条数*/
    private Integer size;
    /*当前页数据数*/
    private Integer numberOfElements;
    /*是否第一页*/
    private Boolean first;

}
