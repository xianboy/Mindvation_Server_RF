package com.mdvns.mdvn.department.uitil;

import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.department.domain.DepartmentDetail;
import com.mdvns.mdvn.department.domain.entity.Department;
import com.mdvns.mdvn.department.domain.entity.Position;
import com.mdvns.mdvn.department.repository.PositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DepartmentUtil {


    private static final Logger LOG = LoggerFactory.getLogger(DepartmentUtil.class);

    /**
     * 构建部门详情
     *
     * @param dept
     * @param positionRepository
     * @return
     */
    public static DepartmentDetail buildDetailByDepartment(Department dept, PositionRepository positionRepository) throws BusinessException {

        DepartmentDetail departmentDetail = new DepartmentDetail();
        //id
        departmentDetail.setId(dept.getId());
        //名称
        departmentDetail.setName(dept.getName());
        //编号
        if (!StringUtils.isEmpty(dept.getSerialNo())) {
            departmentDetail.setSerialNo(dept.getSerialNo());
        }
        //创建时间
        if (!StringUtils.isEmpty(dept.getCreateTime())) {
            departmentDetail.setCreateTime(dept.getCreateTime().getTime());
        }
        //职位
        String ids = dept.getPositions();
        if (!StringUtils.isEmpty(ids)) {
            List<Long> positions = new ArrayList<>();
            for (String id : ids.split(",")) {
                positions.add(Long.valueOf(id));
            }

            List<Position> positionList = positionRepository.findByIdIn(positions);
            departmentDetail.setPositions(positionList);
        }
        return departmentDetail;
    }
}
