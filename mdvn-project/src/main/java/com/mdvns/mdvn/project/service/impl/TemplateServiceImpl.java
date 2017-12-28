package com.mdvns.mdvn.project.service.impl;

import com.mdvns.mdvn.common.bean.model.AddOrRemoveById;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.MdvnCommonUtil;
import com.mdvns.mdvn.project.domain.entity.ProjectTemplate;
import com.mdvns.mdvn.project.repository.TemplateRepository;
import com.mdvns.mdvn.project.service.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class TemplateServiceImpl implements TemplateService {
    private static final Logger LOG = LoggerFactory.getLogger(TagServiceImpl.class);

    @Autowired
    private TemplateRepository projectTemplateRepository;


    /**
     * 新建项目后，添加项目模板映射
     *
     * @param projId
     * @param templates
     * @param creatorId
     * @return
     */
    @Override
    public List<ProjectTemplate> createProjectTemplate(Long creatorId, Long projId, List<Long> templates) {
        LOG.info("id为：[{}]的staff，准备创建id为：[{}] 的项目的模板id为：[{}]的映射信息.", creatorId, projId, templates.toString());
        List<ProjectTemplate> projectTemplates = new ArrayList<>();
        //遍历leaders构建ProjectStaff
        for (Long templateId : templates) {
            ProjectTemplate projectTemplate = new ProjectTemplate();
            projectTemplate.setCreatorId(creatorId);
            projectTemplate.setProjId(projId);
            projectTemplate.setTemplateId(templateId);
            projectTemplate.setCreateTime(new Timestamp(System.currentTimeMillis()));
            projectTemplate.setIsDeleted(MdvnConstant.ZERO);
            projectTemplate = this.projectTemplateRepository.saveAndFlush(projectTemplate);
            projectTemplates.add(projectTemplate);
        }
        LOG.info("保存映射信息完成");
        return projectTemplates;
    }

    /**
     * 更新项目标签映射
     *
     * @param staffId   当前用户id
     * @param projId    当前项目id
     * @param templates 需要更新的模板
     */
    @Override
    public void updateProjectTemplate(Long staffId, Long projId, AddOrRemoveById templates) throws BusinessException {
        //删除模板映射
        if (null != templates.getRemoveList()) {
            MdvnCommonUtil.emptyList(templates.getRemoveList(), ErrorEnum.ILLEGAL_ARG, "删除模板不能为空");
            updateIsDeleted(staffId, projId, templates.getRemoveList(), MdvnConstant.ONE);
        }
        //添加新增模板映射
        if (null != templates.getAddList()) {
            MdvnCommonUtil.emptyList(templates.getAddList(), ErrorEnum.ILLEGAL_ARG, "新增模板不能为空");
            List<Long> addList = new ArrayList<>();
            List<Long> updateList = new ArrayList<>();
            for (Long id : templates.getAddList()) {
                //如果模板映射不存在添加id到addTemplates,已存在则添加id到updateTemplates,
                ProjectTemplate pt = this.projectTemplateRepository.findByProjIdAndTemplateId(projId, id);
                if (null == pt) {
                    addList.add(id);
                } else {
                    updateList.add(id);
                }
            }
            //更新已存在映射的isDeleted为0
            if (updateList.size()>0) {
                updateIsDeleted(staffId, projId, updateList, MdvnConstant.ZERO);
            }
            //添加新映射
            if (addList.size()>0) {
                createProjectTemplate(staffId, projId, addList);
            }
        }
    }

    /**
     * 移除模板映射
     * @param staffId 当前用户id
     * @param projId  当前项目id
     * @param templates 需要移除的模板id
     * @return number
     */
    @Modifying
    public Integer updateIsDeleted(Long staffId, Long projId, List<Long> templates, Integer isDeleted) {
        LOG.info("id为：[{}]的staff，准备去掉id为：[{}] 的项目的id为：[{}]的模板映射信息.", staffId, projId, templates.toString());
        Integer number = this.projectTemplateRepository.updateIsDeleted(isDeleted, projId, templates);
        return number;
    }
}