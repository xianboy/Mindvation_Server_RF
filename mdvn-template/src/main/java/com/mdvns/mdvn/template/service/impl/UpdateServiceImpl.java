package com.mdvns.mdvn.template.service.impl;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.util.RestResponseUtil;
import com.mdvns.mdvn.template.repository.LabelRepository;
import com.mdvns.mdvn.template.service.UpdateService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UpdateServiceImpl implements UpdateService {

    @Resource
    private LabelRepository labelRepository;

    /**
     * 删除指定id的FunctionLabel
     * @param id id
     * @return restResponse
     */
    @Override
    @Modifying
    public RestResponse<?> deleteLabelById(Long id) {
        //此处删除的FunctionLabel为脏数据, 故直接删除
        this.labelRepository.delete(id);
        //删除成功, 返回"SUCCESS"
        return RestResponseUtil.success(MdvnConstant.SUCCESS_VALUE);
    }
}
