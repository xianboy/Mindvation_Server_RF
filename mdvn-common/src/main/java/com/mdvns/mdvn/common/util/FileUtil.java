package com.mdvns.mdvn.common.util;

import com.mdvns.mdvn.common.bean.UpdateOptionalInfoRequest;
import com.mdvns.mdvn.common.bean.model.AddOrRemoveById;
import com.mdvns.mdvn.common.bean.model.AttchInfo;
import com.mdvns.mdvn.common.bean.model.BuildAttachesById;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 构建附件
     *
     * @param attaches
     * @param serialNo
     * @return
     */
    public static List<AttchInfo> buildAttaches(List<Long> attaches, String serialNo) throws BusinessException {
        String updateAttachesUrl = "http://localhost:20011/mdvn-file/files/updateAttaches";
        //实例化restTem对象
        RestTemplate restTemplate = new RestTemplate();
        /**
         * 更改附件的状态
         */
        BuildAttachesById buildAttachesById = new BuildAttachesById();
        AddOrRemoveById addOrRemoveById = new AddOrRemoveById();
        addOrRemoveById.setAddList(attaches);
        buildAttachesById.setAddOrRemoveById(addOrRemoveById);
        buildAttachesById.setSubjectId(serialNo);
        List<AttchInfo> attchInfos = new ArrayList<>();
        try {
            attchInfos = restTemplate.postForObject(updateAttachesUrl, buildAttachesById, List.class);
        } catch (Exception ex) {
            throw new BusinessException(ErrorEnum.ATTACHES_CREATE_FAILED, "添加附件信息失败");
        }
        return attchInfos;
    }

    /**
     * 通过subjectId获取附件列表信息
     *
     * @param subjectId
     * @return
     * @throws BusinessException
     */
    public static List<AttchInfo> getAttaches(String subjectId) throws BusinessException {
        String rtrvAttsBySubjectIdUrl = "http://localhost:20011/mdvn-file/files/rtrvAttsBySubjectId";
        LOG.info("获取编号为【{}】的模块的附件列表开始, Url为【{}】.", subjectId, rtrvAttsBySubjectIdUrl);
        //实例化restTem对象
        RestTemplate restTemplate = new RestTemplate();
        List<AttchInfo> attchInfos = new ArrayList<>();
        try {
            //构建rtrvAttsBySubjectIdUrl
            attchInfos = restTemplate.postForObject(rtrvAttsBySubjectIdUrl, subjectId, List.class);
        } catch (Exception ex) {
            throw new BusinessException(ErrorEnum.ATTACHES_RTRV_FAILED, "获取附件列表信息失败");
        }
        return attchInfos;
    }

    /**
     * 更新附件信息
     *
     * @param updateRequest
     * @return
     */
    public static List<AttchInfo> updateAttaches(UpdateOptionalInfoRequest updateRequest, String serialNo) throws BusinessException {
        String updateAttachesUrl = "http://localhost:20011/mdvn-file/files/updateAttaches";
        //实例化restTem对象
        RestTemplate restTemplate = new RestTemplate();
        List<Long> addList = updateRequest.getAttaches().getAddList();
        List<Long> removeList = updateRequest.getAttaches().getRemoveList();
        BuildAttachesById buildAttachesById = new BuildAttachesById();
        AddOrRemoveById addOrRemoveById = new AddOrRemoveById();
        if (null != addList && addList.size() > 0) {
            addOrRemoveById.setAddList(addList);
        }
        if (null != removeList && removeList.size() > 0) {
            addOrRemoveById.setRemoveList(removeList);
        }
        buildAttachesById.setAddOrRemoveById(addOrRemoveById);
        buildAttachesById.setSubjectId(serialNo);
        List<AttchInfo> attchInfos = new ArrayList<>();
        try {
            attchInfos = restTemplate.postForObject(updateAttachesUrl, buildAttachesById, List.class);
        } catch (Exception ex) {
            throw new BusinessException(ErrorEnum.ATTACHES_UPDATE_FAILED, "更改附件列表信息失败");
        }
        return attchInfos;
    }
}
