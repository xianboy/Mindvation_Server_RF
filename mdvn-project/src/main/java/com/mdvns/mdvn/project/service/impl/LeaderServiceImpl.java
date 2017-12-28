package com.mdvns.mdvn.project.service.impl;

import com.mdvns.mdvn.common.bean.model.AddOrRemoveById;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.MdvnCommonUtil;
import com.mdvns.mdvn.project.domain.entity.ProjectStaff;
import com.mdvns.mdvn.project.repository.LeaderRepository;
import com.mdvns.mdvn.project.service.LeaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class LeaderServiceImpl implements LeaderService {
    private static final Logger LOG = LoggerFactory.getLogger(TagServiceImpl.class);

    @Resource
    private LeaderRepository projectStaffRepository;

    /**
     * 新建ProjectStaff
     *
     * @param projId  projId
     * @param leaders leaders
     * @param creatorId creatorId
     * @return List
     */
    @Override
    public List<ProjectStaff> createProjectStaff(Long creatorId, Long projId, List<Long> leaders) {
        LOG.info("id为：[{}]的staff，准备创建id为：[{}] 的项目的Staff id为：[{}]的映射信息.", creatorId, projId, leaders.toString());
        List<ProjectStaff> projectStaffList = new ArrayList<>();
        //遍历leaders构建ProjectStaff
        for (Long staffId : leaders) {
            ProjectStaff projectStaff = new ProjectStaff();
            projectStaff.setCreatorId(creatorId);
            projectStaff.setStaffId(staffId);
            projectStaff.setProjId(projId);
            projectStaff.setCreateTime(new Timestamp(System.currentTimeMillis()));
            projectStaff.setIsDeleted(MdvnConstant.ZERO);
            projectStaff = this.projectStaffRepository.save(projectStaff);
            projectStaffList.add(projectStaff);
        }
        LOG.info("保存映射信息完成");
        return projectStaffList;
    }


    /**
     * 更新项目负责人映射
     * @param staffId 当前用户id
     * @param projId 当前项目id
     * @param leaders 需要更新的标签
     */
    @Override
    public void updateProjectLeader(Long staffId, Long projId, AddOrRemoveById leaders) throws BusinessException {
        //删除负责人映射
        if (null != leaders.getRemoveList()) {
            MdvnCommonUtil.emptyList(leaders.getRemoveList(), ErrorEnum.ILLEGAL_ARG, "删除负责人不能为空");
            updateIsDeleted(staffId, projId, leaders.getRemoveList(), MdvnConstant.ONE);
        }
        //添加新增负责人映射
        if (null != leaders.getAddList()) {
            MdvnCommonUtil.emptyList(leaders.getAddList(), ErrorEnum.ILLEGAL_ARG, "新增负责人不能为空");
            List<Long> addLeaders = new ArrayList<>();
            List<Long> updateLeaders = new ArrayList<>();
            for (Long id : leaders.getAddList()) {
                //如果负责人映射不存在添加id到addLeaders,已存在则添加id到updateLeaders,
                ProjectStaff ps = this.projectStaffRepository.findByProjIdAndStaffId(projId, id);
                if (null == ps) {
                    addLeaders.add(id);
                } else {
                    updateLeaders.add(id);
                }
            }
            //更新已存在映射的isDeleted为0
            if (updateLeaders.size() > 0) {
                updateIsDeleted(staffId, projId, updateLeaders, MdvnConstant.ZERO);
            }
            //添加新映射
            if (addLeaders.size() > 0) {
                createProjectStaff(staffId, projId, addLeaders);
            }
        }
    }

    /**
     * 移除标签映射
     * @param staffId 当前用户id
     * @param projId 当前项目id
     * @param leaders 需要移除的负责人id
     * @return number
     */
    @Modifying
    private Integer updateIsDeleted(Long staffId, Long projId, List<Long> leaders, Integer isDeleted) throws BusinessException {
        MdvnCommonUtil.emptyList(leaders, ErrorEnum.ILLEGAL_ARG, "新增负责人不能为空");
        LOG.info("id为：[{}]的staff，准备去掉id为：[{}] 的项目的id为：[{}]的负责人映射信息.", staffId, projId, leaders.toString());
        return this.projectStaffRepository.updateIsDeleted(isDeleted, projId, leaders);
    }
}
