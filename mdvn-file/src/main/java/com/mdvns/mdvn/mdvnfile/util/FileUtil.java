package com.mdvns.mdvn.mdvnfile.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

@Configuration
public class FileUtil {
    private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);

    /*声明访问类型*/
    private static String accessType;

    /*附件存储目录*/
    private static String uploadPath;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM");

    public String genDir(long currentTimeMillis) {
        String dateTime = dateFormat.format(currentTimeMillis);

        return dateTime + File.separator;
    }

    /**
     * 生成Url
     * @param fileName
     * @return
     * @throws UnknownHostException
     */
    public static String genUrl(HttpServletRequest request, String fileName) throws UnknownHostException {
        StringBuilder url = new StringBuilder(accessType);
//        String ip = (null==LocalHostUtil.getIp())?request.getLocalAddr():LocalHostUtil.getIp();
//        String ip = "112.74.45.247";
//        String ip = "47.100.100.211";
        String ip = request.getLocalAddr();
//        Integer port = (null==LocalHostUtil.getPort())?request.getServerPort():LocalHostUtil.getPort();
        Integer port = request.getLocalPort();
        url.append(ip).append(":").append(port)
                .append(request.getContextPath()).append("/").append(fileName);
        LOG.info("该附件上传后的URL为：{}", url);
        return url.toString();
    }


    //使用@Value注解获取配置文件中的值
    @Value("${access.type}")
    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    //使用@Value注解获取配置文件中的值
    @Value("${web.upload-path}")
    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

}
