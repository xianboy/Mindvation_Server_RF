package com.mdvns.mdvn.mdvnfile.web;


import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.bean.model.AddOrRemoveById;
import com.mdvns.mdvn.common.bean.model.BuildAttachesById;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.util.BindingResultUtil;
import com.mdvns.mdvn.mdvnfile.domain.UpdateAttchRequest;
import com.mdvns.mdvn.mdvnfile.domain.entity.AttchInfo;
import com.mdvns.mdvn.mdvnfile.servicce.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping(value = {"/files", "/v1.0/files"})
public class FileController {

    private static final Logger LOG = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    /**
     * 添加附件
     * @param attchInfo
     * @return
     */
    @PostMapping
    public AttchInfo create(@RequestBody AttchInfo attchInfo) throws BusinessException {

        return this.fileService.create(attchInfo);
    }


    public ResponseEntity<?> update(@RequestBody UpdateAttchRequest updateAttchRequest) {
        return this.fileService.update(updateAttchRequest);
    }

    /**
     * 删除附件
     * @param id
     * @return
     */
    @PutMapping(value = "/delete/{attchId}")
    public ResponseEntity<?> delete(@PathVariable("attchId") Long id) {
        return this.fileService.delete(id);
    }

    /**
     * 更改附件的状态
     * @param request
     * @return
     */
    @PostMapping(value = "/updateAttaches")
    public List<AttchInfo> updateAttaches(@RequestBody BuildAttachesById  request) throws BusinessException {
        return this.fileService.updateAttaches(request);
    }

    /**
     * 获取附件列表信息（通过subjectId）
     * @param subjectId
     * @return
     */
    @PostMapping(value = "/rtrvAttsBySubjectId")
    public List<AttchInfo> rtrvAttsBySubjectId(@RequestBody String subjectId) {
        return this.fileService.rtrvAttsBySubjectId(subjectId);
    }


    /**
     * 查询指定Id的附件详情
     * @param id
     * @return
     */
    @GetMapping(value = "/rtrvAttch/{attchId}")
    public ResponseEntity<?> retrieve(@PathVariable("attchId") Long id) {
        return this.fileService.retrieve(id);
    }

    /**
     * 查询指定Id的附件详情
     * @param id
     * @return
     */
    @PostMapping(value = "/rtrvAttachInfo")
    public AttchInfo rtrvAttachInfo(@RequestBody SingleCriterionRequest retrieveRequest, BindingResult bindingResult) {
        BindingResultUtil.brResolve(bindingResult);
        return this.fileService.rtrvAttachInfo(retrieveRequest);
    }


    @GetMapping(value = "/rtrvAttchList/{attchIds}")
    public ResponseEntity<?> retrieve(@PathVariable("attchIds") String ids) {
        return this.fileService.retrieve(ids);
    }

    /**
     * 多文件上传处理
     *
     * @param request
     * @param mFile
     * @return
     */
    @PostMapping(value = "/uploadFile")
    public ResponseEntity<?> uploadFile(HttpServletRequest request, @RequestParam MultipartFile mFile, @RequestParam Long creatorId) throws IOException, BusinessException {
        LOG.info("Contrller 开始执行:{}");
        return this.fileService.uploadFile(request, mFile, creatorId);
    }
}
