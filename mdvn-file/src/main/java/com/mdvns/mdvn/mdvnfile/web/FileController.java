package com.mdvns.mdvn.mdvnfile.web;


import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.mdvnfile.domain.UpdateAttchRequest;
import com.mdvns.mdvn.mdvnfile.domain.entity.AttchInfo;
import com.mdvns.mdvn.mdvnfile.servicce.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


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
    public ResponseEntity<?> delete(@PathVariable("attchId") Integer id) {
        return this.fileService.delete(id);
    }


    /**
     * 查询指定Id的附件详情
     * @param id
     * @return
     */
    @GetMapping(value = "/rtrvAttch/{attchId}")
    public ResponseEntity<?> retrieve(@PathVariable("attchId") Integer id) {
        return this.fileService.retrieve(id);
    }

    /**
     * 查询指定Id的附件详情
     * @param id
     * @return
     */
    @PostMapping(value = "/rtrvAttachInfo")
    public AttchInfo rtrvAttachInfo(@RequestBody Integer id) {
        return this.fileService.rtrvAttachInfo(id);
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
    public ResponseEntity<?> uploadFile(HttpServletRequest request, @RequestParam MultipartFile mFile, @RequestParam Long creatorId,@RequestParam String subjectId) throws IOException, BusinessException {
        LOG.info("Contrller 开始执行:{}");
        return this.fileService.uploadFile(request, mFile, creatorId,subjectId);
    }
}
