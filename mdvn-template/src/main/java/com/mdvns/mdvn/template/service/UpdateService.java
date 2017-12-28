package com.mdvns.mdvn.template.service;

import com.mdvns.mdvn.common.bean.RestResponse;

public interface UpdateService {
    //删除指定id的FunctionLabel
    RestResponse<?> deleteLabelById(Long id);
}
