package com.mdvns.mdvn.mdvnfile.servicce;


import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.mdvnfile.domain.UpdateAttchRequest;
import com.mdvns.mdvn.mdvnfile.domain.entity.AttchInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface FileService {

    AttchInfo create(AttchInfo attchInfo) throws BusinessException;

    ResponseEntity<?> update(UpdateAttchRequest updateAttchRequest);

    ResponseEntity<?> delete(Integer attchId);

    ResponseEntity<?> retrieve(Integer id);

    ResponseEntity<?> retrieve(String ids);

    AttchInfo rtrvAttachInfo(Integer id);

    ResponseEntity<?> uploadFiles(HttpServletRequest request, List<MultipartFile> mFiles, Long creatorId, String subjectId) throws IOException, BusinessException;

    ResponseEntity<?> uploadFile(HttpServletRequest request, MultipartFile mFile, Long creatorId, String subjectId) throws IOException, BusinessException;
}
