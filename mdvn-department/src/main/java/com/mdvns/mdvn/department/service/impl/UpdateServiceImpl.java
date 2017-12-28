package com.mdvns.mdvn.department.service.impl;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.RestResponseUtil;
import com.mdvns.mdvn.department.domain.UpdateDepartmentRequest;
import com.mdvns.mdvn.department.domain.entity.Department;
import com.mdvns.mdvn.department.repository.DeptRepository;
import com.mdvns.mdvn.department.repository.PositionRepository;
import com.mdvns.mdvn.department.service.UpdateService;
import com.mdvns.mdvn.department.uitil.DepartmentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UpdateServiceImpl implements UpdateService {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateServiceImpl.class);

    @Resource
    private DeptRepository deptRepository;

    @Resource
    private PositionRepository positionRepository;


    /**
     * 更新
     *
     * @param updateRequest request
     * @return RestResponse
     */
    @Override
    public RestResponse<?> update(UpdateDepartmentRequest updateRequest) throws BusinessException {
        //构建department并保存
        Department dept = this.deptRepository.saveAndFlush(buildDeptByRequest(updateRequest));
        //构建response对象
        return RestResponseUtil.success(DepartmentUtil.buildDetailByDepartment(dept, this.positionRepository));
    }

    /**
     * 根据updateRequest 构建Department
     * @param updateRequest request
     * @return Department
     */
    private Department buildDeptByRequest(UpdateDepartmentRequest updateRequest) throws BusinessException {
        //根据id查询Department
        Department dept = this.deptRepository.findOne(updateRequest.getId());
        if (dept == null) {
            LOG.error("ID为:{} 的Department不存在.", updateRequest.getId());
            throw new BusinessException(ErrorEnum.NOT_EXISTS, "ID为[ "+updateRequest.getId()+" ] 的部门不存在");
        }
        dept.setPositions(updateRequest.getPositions().toString());
        LOG.info("根据request构建的dept：{}", dept);
        return dept;
    }

}
