package com.mdvns.mdvn.department.service.impl;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.RestResponseUtil;
import com.mdvns.mdvn.department.domain.UpdateDepartmentRequest;
import com.mdvns.mdvn.department.domain.entity.Department;
import com.mdvns.mdvn.department.domain.entity.Position;
import com.mdvns.mdvn.department.repository.DeptRepository;
import com.mdvns.mdvn.department.repository.PositionRepository;
import com.mdvns.mdvn.department.service.UpdateService;
import com.mdvns.mdvn.department.uitil.DepartmentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class UpdateServiceImpl implements UpdateService {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateServiceImpl.class);

    @Resource
    private DeptRepository deptRepository;

    @Resource
    private PositionRepository positionRepository;


    /**
     * 更新dept
     * @param updateRequest
     * @return
     * @throws BusinessException
     */
    @Override
    public RestResponse<?> updateDept(UpdateDepartmentRequest updateRequest) throws BusinessException {
        //构建department并保存
        Department dept = this.deptRepository.saveAndFlush(buildDeptAndPositionByRequest(updateRequest));
        //构建response对象
        return RestResponseUtil.success(DepartmentUtil.buildDetailByDepartment(dept, this.positionRepository));
    }

    /**
     * 删除一个部门信息
     * @param retrieveDetailRequest
     * @return
     */
    @Override
    public RestResponse<?> deleteDept(SingleCriterionRequest retrieveDetailRequest) throws BusinessException {
        if (retrieveDetailRequest == null || retrieveDetailRequest.getCriterion() == null) {
            return null;
        }
        Department department = deptRepository.findOne(Long.valueOf(retrieveDetailRequest.getCriterion()));
        if (department != null) {
            department.setIsDeleted(1);
            deptRepository.save(department);
        } else {
            LOG.error("ID为:{} 的Department不存在.", retrieveDetailRequest.getCriterion());
            throw new BusinessException(ErrorEnum.NOT_EXISTS, "ID为[ "+retrieveDetailRequest.getCriterion()+" ] 的部门不存在");
        }
        return RestResponseUtil.success(true);
    }

    /**
     * 根据updateRequest 构建Department  And  Position
     * @param updateRequest request
     * @return Department
     */
    private Department buildDeptAndPositionByRequest(UpdateDepartmentRequest updateRequest) throws BusinessException {
        //根据id查询Department
        Department dept = this.deptRepository.findOne(updateRequest.getId());
        if (dept == null) {
            LOG.error("ID为:{} 的Department不存在.", updateRequest.getId());
            throw new BusinessException(ErrorEnum.NOT_EXISTS, "ID为[ "+updateRequest.getId()+" ] 的部门不存在");
        }

        /*修改部门名称*/
        if (!dept.getName().equals(updateRequest.getName())) {
            dept.setName(updateRequest.getName());
            dept.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            deptRepository.save(dept);
        }

        /*修改职位信息*/
        List<Position> positionList = updateRequest.getPositionList();
        if (positionList == null || positionList.isEmpty()) {
            // delete all position
            positionList = positionRepository.findAllByHostIdAndIsDeleted(updateRequest.getId(), 0);
            positionRepository.delete(positionList);
        } else {
            // 逐个对比
            List<Position> oldItems = positionRepository.findAllByHostIdAndIsDeleted(updateRequest.getId(), 0);
            for (Position position : oldItems) {
                position.setIsDeleted(1);
            }

            List<Position> positionsForAdd = new ArrayList<>();
            for (Position position : positionList) {
                if (StringUtils.isEmpty(position.getId())) {
                    position.setCreatorId(updateRequest.getStaffId());
                    position.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    position.setIsDeleted(0);
                    position.setHostId(updateRequest.getId());
                    positionsForAdd.add(position);
                    continue;
                }

                boolean find = false;
                for (int i = 0; i < oldItems.size(); i++) {
                    if (oldItems.get(i).getId() == position.getId()) {
                        if (!oldItems.get(i).getName().equals(position.getName())) {
                            oldItems.get(i).setUpdateTime(new Timestamp(System.currentTimeMillis()));
                            oldItems.get(i).setName(position.getName());
                        }
                        oldItems.get(i).setIsDeleted(0);
                        find = true;
                    }
                }
            }
            positionRepository.save(oldItems);
            if (!positionsForAdd.isEmpty()) {
                positionRepository.save(positionsForAdd);
            }
        }
        LOG.info("根据request构建的dept：{}", dept);
        return dept;
    }

}
