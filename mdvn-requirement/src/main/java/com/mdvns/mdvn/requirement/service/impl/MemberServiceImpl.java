package com.mdvns.mdvn.requirement.service.impl;

import com.mdvns.mdvn.common.bean.MemberRequest;
import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.bean.model.RoleMember;
import com.mdvns.mdvn.common.bean.model.TerseInfo;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.MdvnCommonUtil;
import com.mdvns.mdvn.common.util.RestTemplateUtil;
import com.mdvns.mdvn.requirement.config.WebConfig;
import com.mdvns.mdvn.requirement.domain.entity.RequirementMember;
import com.mdvns.mdvn.requirement.repository.MemberRepository;
import com.mdvns.mdvn.requirement.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    private static final Logger LOG = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Resource
    private MemberRepository memberRepository;

    @Resource
    private WebConfig webConfig;

    /**
     * 保存需求成员映射
     *
     * @param creatorId creatorId
     * @param requirementId requirementId
     * @param members members
     */
    @Override
    public Integer handleMembers(Long creatorId, Long requirementId, List<MemberRequest> members) {
        //遍历members构建并保存RequirementMember
        Integer size = 0;
        for (MemberRequest memberRequest : members) {
            //遍历保存每个模板角色下的member
            size += memberRequest.getAddList().size();
            saveRoleMembers(creatorId, requirementId, memberRequest.getRoleId(), memberRequest.getAddList());
        }
        LOG.info("保存成员映射信息完成");
        return size;
    }

    /**
     * 保存角色成员映射
     *
     * @param creatorId     creatorId
     * @param requirementId requirementId
     * @param roleId        roleId
     * @param memberSet     memberSet
     */
    private void saveRoleMembers(Long creatorId, Long requirementId, Long roleId, List<Long> memberSet) {
        for (Long memberId : memberSet) {
            RequirementMember member = new RequirementMember();
            member.setCreatorId(creatorId);
            member.setMemberId(memberId);
            member.setRequirementId(requirementId);
            member.setRoleId(roleId);
            this.memberRepository.saveAndFlush(member);
        }
    }

    /**
     * 获取指定requirement的成员信息
     *
     * @param staffId       staffId
     * @param requirementId requirementId
     * @param templateId  templateId
     * @return list
     */
    @Override
    public List<RoleMember> getRoleMembers(Long staffId, Long requirementId, Long templateId, Integer isDeleted) throws BusinessException {
        LOG.info("获取指定requirement的成员信息开始...");
        //查询requirement对应模板的所有角色
        List<TerseInfo> roles = getTemplateRoles(staffId, templateId);
        //如果没有角色, 则返回null
        if (roles.isEmpty()) {
            LOG.info("ID为【{}】的模板暂无角色", templateId);
            return null;
        }
        //角色成员集合
        List<RoleMember> roleMembers = new ArrayList<>();
        //查询每个角色对应的成员
        for (TerseInfo tmplRole : roles) {
            RoleMember roleMember = new RoleMember();
            //根据roleId和requirementId查询角色对应的成员
            //如果成员信息为空, 抛出异常
            List<TerseInfo> members = getMembers(staffId, requirementId, tmplRole.getId(), isDeleted);
            //设置templateRole
            roleMember.setRole(tmplRole);
            //设置members
            roleMember.setMembers(members);
            //添加roleMember 到集合中
            roleMembers.add(roleMember);
        }
        LOG.info("获取指定requirement的成员信息成功...");
        return roleMembers;
    }

    /**
     * 查询指定模板的所有角色
     * @param staffId staffId
     * @param templateId templateId
     * @return List
     */
    private List<TerseInfo> getTemplateRoles(Long staffId, Long templateId) throws BusinessException {
        String retrieveRolesUrl = webConfig.getRetrieveRolesUrl();
        return RestTemplateUtil.getTemplateRoles(staffId, templateId, retrieveRolesUrl);
    }

    /**
     * 修改角色成员
     *
     * @param staffId       staffId
     * @param requirementId requirementId
     * @param roleMembers   roleMembers
     */
    @Override
    @Modifying
    @Transactional
    public void updateRoleMembers(Long staffId, Long requirementId, List<MemberRequest> roleMembers) {
        LOG.info("修改角色成员开始...");
        //遍历 roleMembers, 处理每一个角色对应的成员修改
        for (MemberRequest memberRequest : roleMembers) {
            //删除成员
            List<Long> removeList = memberRequest.getRemoveList();
            if (!(null == removeList || removeList.isEmpty())) {
                this.memberRepository.updateIsDeleted(requirementId, memberRequest.getRoleId(), memberRequest.getRemoveList(), MdvnConstant.ONE);
                LOG.info("删除ID为【{}】的角色下ID为【{}】的成员成功...", memberRequest.getRoleId(), removeList);
            }
            //新增成员
            List<Long> addList = memberRequest.getAddList();
            if (!(null == addList || addList.isEmpty())) {
                updateMembers(staffId, requirementId, memberRequest.getRoleId(), memberRequest.getAddList());
            }
        }
        LOG.info("修改角色成员成功...");
    }

    /**
     * 添加指定角色的成员映射
     *
     * @param staffId       staffId
     * @param requirementId requirementId
     * @param roleId        roleId
     * @param addList       addList
     */
    private void updateMembers(Long staffId, Long requirementId, Long roleId, List<Long> addList) {
        LOG.info("添加角色成员开始...");
        List<Long> addMembers = new ArrayList<>();
        List<Long> updateMembers = new ArrayList<>();
        for (Long memberId : addList) {
            //如果映射不存在添加,已存在则修改isDeleted
            RequirementMember rm = this.memberRepository.findByRequirementIdAndRoleIdAndMemberId(requirementId, roleId, memberId);

            if (null == rm) {
                addMembers.add(memberId);
            } else {
                updateMembers.add(memberId);
            }
        }
        //更新已存在映射的isDeleted为0
        if (updateMembers.size()>0) {
            updateIsDeleted(requirementId, roleId, updateMembers, MdvnConstant.ZERO);
        }
        //添加新映射
        if (addMembers.size()>0) {
            saveRoleMembers(staffId, requirementId, roleId, addMembers);
        }
        LOG.info("添加ID为【{}】的角色下ID为【{}】的成员成功...", roleId, addList);
    }

    /**
     * 修改角色成员映射
     *
     * @param requirementId requirementId
     * @param roleId        roleId
     * @param updateMembers updateMembers
     * @param isDeleted     zero
     */
    private void updateIsDeleted(Long requirementId, Long roleId, List<Long> updateMembers, Integer isDeleted) {
        this.memberRepository.updateIsDeleted(requirementId, roleId, updateMembers, isDeleted);
    }

    /**
     * 查询指定requirementId 和 roleId的成员信息
     *
     * @param staffId       staffId
     * @param requirementId requirementId
     * @param roleId        roleId
     * @return list
     * @throws BusinessException exception
     */
    private List<TerseInfo> getMembers(Long staffId, Long requirementId, Long roleId, Integer isDeleted) throws BusinessException {
        LOG.info("获取requirement的成员信息开始...");
        //获取成员id和name
        List<Long> ids = this.memberRepository.findMemberIdByRoleIdAndRequirementIdAndIsDeleted(requirementId, roleId, isDeleted);
        if (ids.isEmpty()) {
            LOG.info("roleId为【{}】的角色暂无成员.", roleId);
            return null;
        }
        //获取member的url
        String retrieveMembersUrl = webConfig.getRetrieveMembersUrl();
        //调用staff模块获取成员信息
        List<TerseInfo> members = RestTemplateUtil.retrieveTerseInfo(staffId, ids, retrieveMembersUrl);
        MdvnCommonUtil.emptyList(members, ErrorEnum.STAFF_NOT_EXISTS, "Id为【" + ids.toString() + "】的用户不存在.");
        LOG.info("获取requirement的成员信息成功...");
        return members;
    }

}
