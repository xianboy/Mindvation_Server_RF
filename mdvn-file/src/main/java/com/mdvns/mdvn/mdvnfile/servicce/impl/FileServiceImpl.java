package com.mdvns.mdvn.mdvnfile.servicce.impl;


import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.RestResponseUtil;
import com.mdvns.mdvn.mdvnfile.domain.UpdateAttchRequest;
import com.mdvns.mdvn.mdvnfile.domain.entity.AttchInfo;
import com.mdvns.mdvn.mdvnfile.repository.AttchInfoRepository;
import com.mdvns.mdvn.mdvnfile.servicce.FileService;
import com.mdvns.mdvn.mdvnfile.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    /*初始化日志常量*/
    private static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);

    /*注入repository*/
    @Autowired
    private AttchInfoRepository attchRepository;

    /*注入AttchInfo*/
    @Autowired
    private AttchInfo attchInfo;

    /*附件保存目录*/
    @Value("${web.upload-path}")
    private String uploadDir;

    private final RestTemplate restTemplate;
    public FileServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * 保存上传成功的附件的信息
     *
     * @param attch
     * @return
     */
    public AttchInfo create(AttchInfo attch) throws BusinessException {
        LOG.info("上传附件,开始保存上传成功的附件的信息...");
        //附件原名，上传人Id， 附件url 不能为空
        if (StringUtils.isEmpty(attch.getOriginName()) || StringUtils.isEmpty(String.valueOf(attch.getCreatorId())) || StringUtils.isEmpty(attch.getUrl())) {
            throw new BusinessException(ErrorEnum.ILLEGAL_ARG, "附件没有上传成功");
        }
        try {
            //实例化附件信息添加时间*
            Timestamp createTime = new Timestamp(System.currentTimeMillis());
            //附件信息添加时间赋值为系统当前时间*
            attch.setCreateTime(createTime);
            //附件信息是否已删除赋值为0，未删除
            attch.setIsDeleted(0);
            //保存附件信息
            attchInfo = this.attchRepository.save(attch);
        } catch (Exception ex) {
            LOG.error("保存信息失败:{}", ex.getLocalizedMessage());
        }
        LOG.info("========保存成功========{}", attchInfo);
        return attchInfo;
    }

    @Override
    public ResponseEntity<?> update(UpdateAttchRequest updateAttchRequest) {
        LOG.info("更新附件,开始更新上传成功的附件的信息...");
        try {
            attchInfo = this.attchRepository.findOne(updateAttchRequest.getAttchId());
            if (!(updateAttchRequest.getSubjectid().equals(attchInfo.getSubjectId())) || attchInfo == null) {
                LOG.error("更新的附件不存在!");
            }
            attchInfo.setIsDeleted(1);
            attchInfo = this.attchRepository.saveAndFlush(attchInfo);
        } catch (Exception ex) {
            LOG.error("更新附件：{}, 失败:{}", updateAttchRequest.getAttchId(), ex.getLocalizedMessage());
        }
        LOG.info("========更新成功========{}", attchInfo);
        return RestResponseUtil.successResponseEntity(attchInfo);
    }

    /**
     * 删除附件
     *
     * @param attchId
     * @return
     */
    @Override
    public ResponseEntity<?> delete(Integer attchId) {
        LOG.info("删除附件开始");
        attchInfo = this.attchRepository.findOne(attchId);
        if (attchInfo == null) {
            LOG.error("删除失败，Id 为：{} 的附件不存在!", attchId);
        }
        attchInfo.setIsDeleted(1);
        attchInfo = this.attchRepository.saveAndFlush(attchInfo);
        LOG.info("删除后Attch:{} 的状态为： {}", attchInfo.getId(), attchInfo.getIsDeleted());
        return RestResponseUtil.successResponseEntity();
    }

    /**
     * 根据Id获取附件信息
     *
     * @param id
     * @return
     */
    @Override
    public ResponseEntity<?> retrieve(Integer id) {
        attchInfo = this.attchRepository.findOne(id);
        LOG.info("查询到的AttchInfo 是： {}", attchInfo.getId());
        return RestResponseUtil.successResponseEntity(attchInfo);
    }

    /**
     * 根据Id获取附件信息
     *
     * @param id
     * @return
     */
    @Override
    public AttchInfo rtrvAttachInfo(Integer id) {
        attchInfo = this.attchRepository.findOne(id);
        LOG.info("查询到的AttchInfo 是： {}", attchInfo.getId());
        return attchInfo;
    }


    /**
     * 根据指定Id集合查询多个附件信息
     *
     * @param ids
     * @return
     */
    @Override
    public ResponseEntity<?> retrieve(String ids) {
        LOG.info("获取多个附件信息开始");
        List<Integer> idList = new ArrayList<Integer>();
        for (String id : ids.split(",")) {
            idList.add(Integer.valueOf(id));
        }

        List<AttchInfo> attchs = this.attchRepository.findByIdIn(idList);
        LOG.info("获取的附件列表为：{}", attchs);
        return RestResponseUtil.successResponseEntity(attchs);
    }
    //----------------------------------------------文件上传--------------------------------------------
    /**
     * 多文件上传
     * @param request
     * @param mFiles
     * @param creatorId
     * @return
     * @throws IOException
     */
    @Transactional
    @Override
    public ResponseEntity<?> uploadFiles(HttpServletRequest request, List<MultipartFile> mFiles, Long creatorId,String subjectId) throws IOException, BusinessException {

        //保存成功的附件信息Id使用List保存
        List<AttchInfo> attchs = new ArrayList<AttchInfo>();

        //文件依次上传
        try {
            for (MultipartFile mFile : mFiles) {
                attchs.add(doUpload(request, mFile, creatorId,subjectId));
            }
        } catch (Exception ex) {
            LOG.info("文件上传失败:{}", ex.getLocalizedMessage());
            throw new IOException("文件上传失败,文件大小不能超过5MB");
        }

        return RestResponseUtil.successResponseEntity(attchs);
    }

    /**
     * 单文件上传
     * @param request
     * @param mFile
     * @param creatorId
     * @return
     * @throws IOException
     */
    @Transactional
    @Override
    public ResponseEntity<?> uploadFile(HttpServletRequest request, MultipartFile mFile, Long creatorId,String subjectId) throws IOException, BusinessException {
        AttchInfo attch = doUpload(request, mFile, creatorId,subjectId);
        return RestResponseUtil.successResponseEntity(attch);
    }

    /**
     * @param request
     * @param mFile   文件对象
     * @return 文件信息保存后的Id
     * @throws IOException
     */
    private AttchInfo doUpload(HttpServletRequest request, MultipartFile mFile, Long creatorId,String subjectId) throws IOException, BusinessException {

        //如果目录不存在，自动创建文件夹
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String contentType = mFile.getContentType();
        LOG.info("文件类型：{}",contentType);
        //原文件名
        String fileOrigName = mFile.getOriginalFilename();
        //文件后缀名
        String suffix = fileOrigName.substring(fileOrigName.lastIndexOf("."));
        //重命名上传文件
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + System.nanoTime() + suffix;
        //将上传的文件写入到服务器端文件内
        mFile.transferTo(new File(uploadDir, fileName));
        //文件上传成功后，生成url
        String url = FileUtil.genUrl(request, fileName);
        //实例化AttchInfo对象
        attchInfo.setOriginName(fileOrigName);
        attchInfo.setCreatorId(creatorId);
        attchInfo.setUrl(url);
        attchInfo.setSubjectId(subjectId);
        //调用SAPI保存实例化AttchInfo对象
        return create(attchInfo);
    }
}
