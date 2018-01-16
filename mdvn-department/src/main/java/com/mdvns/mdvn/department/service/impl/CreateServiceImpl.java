package com.mdvns.mdvn.department.service.impl;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.MdvnCommonUtil;
import com.mdvns.mdvn.common.util.RestResponseUtil;
import com.mdvns.mdvn.department.domain.CreateDeptRequest;
import com.mdvns.mdvn.department.domain.entity.Department;
import com.mdvns.mdvn.department.domain.entity.Position;
import com.mdvns.mdvn.department.repository.DeptRepository;
import com.mdvns.mdvn.department.repository.PositionRepository;
import com.mdvns.mdvn.department.service.CreateService;
import com.mdvns.mdvn.department.uitil.DepartmentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Service
public class CreateServiceImpl implements CreateService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateServiceImpl.class);

    /*注入部门repository*/
    @Resource
    private DeptRepository deptRepository;

    /*注入职位repository*/
    @Resource
    private PositionRepository positionRepository;

    /**
     * 新建部门
     *
     * @param createRequest request
     * @return RestResponse
     */
    @Override
    @Transactional
    public RestResponse<?> create(CreateDeptRequest createRequest) throws BusinessException {
        LOG.info("创建部门开始...");
        String name = createRequest.getName();
        Department dept;
        //根据name查询
        dept = this.deptRepository.findByName(name);
        //如果给定name的数据已存在,抛出异常
        MdvnCommonUtil.existingError(dept, ErrorEnum.DEPT_EXISTS, "name为【"+name+"】的部门已存在.");
        //saveAndBuildDetail(createRequest);
        //根据request构建department
        dept = buildDepartmentByRequest(createRequest);
        //保存dept
        dept = this.deptRepository.saveAndFlush(dept);
        //构建部门职位
        if (!(createRequest.getPositions().isEmpty())) {
            List<Position> positions = buildDeptPosition(createRequest.getCreatorId(),dept, createRequest.getPositions());
//            dept.setPositions(positions);
        }
        LOG.info("创建部门成功...");
        //构建response
        return RestResponseUtil.success(DepartmentUtil.buildDetailByDepartment(dept, this.positionRepository));
    }

    /**
     * 构建部门职位id字符串
     * @param pNames names
     * @return StringBuilder
     */
    private List<Position> buildDeptPosition(Long creatorId,Department dept, List<String> pNames) {
        LOG.info("创建部门职位开始...");
        List<Position> positions = new ArrayList<>();
        Long deptId = dept.getId();
        for (String pName : pNames) {
            Position position = this.positionRepository.findByName(pName);
            //如果Position不存在，保存
            if (null == position) {
                position = new Position();
                //设置hostId
                position.setHostId(deptId);
                //设置creatorId
                position.setCreatorId(creatorId);
                //设置name
                position.setName(pName);
                //设置创建时间
                position.setCreateTime(new Timestamp(System.currentTimeMillis()));
                //是否已删除
                position.setIsDeleted(MdvnConstant.ZERO);
                //新建position
                position = this.positionRepository.saveAndFlush(position);
                positions.add(position);
            }
        }
        LOG.info("创建部门职位成功...");
        return positions;
    }

    /**
     * 根据createRequest构建department对象
     *
     * @param createRequest request
     * @return DepartMent
     */
    private Department buildDepartmentByRequest(CreateDeptRequest createRequest) {
        Department dept = new Department();
        //构建部门编号
        dept.setSerialNo(buildSerialNo());
        //名称
        dept.setName(createRequest.getName());
        //创建人
        dept.setCreatorId(createRequest.getCreatorId());
        //创建时间
        dept.setCreateTime(new Timestamp(System.currentTimeMillis()));
        //是否已删除
        dept.setIsDeleted(MdvnConstant.ZERO);

        return dept;
    }

    /**
     * 构建部门编号
     * @return String
     */
    private String buildSerialNo() {
        //查询表中的最大id  maxId
        Long maxId = this.deptRepository.getMaxId();
        //如果表中没有数据，则给maxId赋值为0
        if (maxId == null) {
            maxId = 0L;
        }
        maxId += MdvnConstant.ONE;
        return MdvnConstant.D + maxId;
    }

}
