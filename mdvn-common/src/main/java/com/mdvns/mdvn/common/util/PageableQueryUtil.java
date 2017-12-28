package com.mdvns.mdvn.common.util;

import com.mdvns.mdvn.common.bean.model.PageableCriteria;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

public class PageableQueryUtil {

    private static final Logger LOG = LoggerFactory.getLogger(PageableQueryUtil.class);

    /**
     * 根据pageableCriteria 构建分页参数对象 pageRequest
     *
     * @param pageableCriteria
     * @return
     */
    public static PageRequest pageRequestBuilder(PageableCriteria pageableCriteria) {
        //当前页
        Integer page = pageableCriteria.getPage();
        //每页条数
        Integer size = pageableCriteria.getSize();

        if (null == page || null == size || page < MdvnConstant.ONE || size < MdvnConstant.ONE) {
            LOG.error("分页参数page:{}, size:{}, 不能为空且不能小于1", page, size);
            throw new IllegalArgumentException("分页参数：page和size 不能为空且不能小于1!");
        }
        //排序字段
        String[] defaultSortBy = {MdvnConstant.ID};
        List<String> sortBy = pageableCriteria.getSortBy();
        if (null == sortBy || sortBy.isEmpty()) {
            sortBy = Arrays.asList(defaultSortBy);
        }
        //排序方向
        Integer direction = pageableCriteria.getDirection();
        PageRequest pageRequest = null;
        if (direction == null || direction == MdvnConstant.ZERO) {
            pageRequest = new PageRequest(page - MdvnConstant.ONE, size, new Sort(Sort.DEFAULT_DIRECTION, sortBy));
        } else {
            pageRequest = new PageRequest(page - MdvnConstant.ONE, size, new Sort(Sort.Direction.DESC, sortBy));
        }
        return pageRequest;
    }

    /**
     * 构建默认分页查询参数对象
     * @return
     */
    public static PageRequest defaultPageReqestBuilder() {
        return new PageRequest(MdvnConstant.ZERO, MdvnConstant.TEN, new Sort(Sort.Direction.ASC, MdvnConstant.ID));
    }

}
