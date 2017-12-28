package com.mdvns.mdvn.common.util;

import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MdvnCommonUtil {

    private static final Logger LOG = LoggerFactory.getLogger(MdvnCommonUtil.class);

    /**
     * 校验根据单个参数查询的数据不存在
     *
     * @param object     object
     * @param paramName  paramName
     * @param paramValue paramValue
     * @throws BusinessException not existing exception
     */
    public static void notExistingError(Object object, String paramName, String paramValue) throws BusinessException {
        if (null == object) {
            LOG.error(paramName + " 为:[" + paramValue + "]的数据不存在.");
            throw new BusinessException(ErrorEnum.NOT_EXISTS, paramName + " 为:[" + paramValue + "]的数据不存在.");
        }
    }

    public static void notExistingError(Object object, ErrorEnum errorCode, String msg) throws BusinessException {
        if (null == object) {
            LOG.error(msg);
            throw new BusinessException(errorCode, msg);
        }
    }

    /**
     * 校验属性为某个值的数据已存在
     *
     * @param object     object
     * @param paramName  paramName
     * @param paramValue paramValue
     * @throws BusinessException existing exception
     */
    public static void existingError(Object object, String paramName, String paramValue) throws BusinessException {
        if (null != object) {
            LOG.error(paramName + " 为:[" + paramValue + "]的数据已存在.");
            throw new BusinessException(ErrorEnum.EXISTED, paramName + " 为:[" + paramValue + "]的数据已存在.");
        }
    }


    public static void existingError(Object object, ErrorEnum errorCode, String msg) throws BusinessException {
        if (null != object) {
            LOG.error(msg);
            throw new BusinessException(errorCode, msg);
        }
    }

    /**
     * 校验集合是否为空
     * @param list
     * @param code
     * @param msg
     * @throws BusinessException
     */
    public static void emptyList(List<?> list, ErrorEnum code, String msg) throws BusinessException {
        if (null == list || list.isEmpty()) {
            LOG.error(msg);
            throw new BusinessException(code, msg);
        }
    }

    /**
     * 对字符串进行去除所有空格操作
     *
     * @param target str
     * @return String
     */
    public static String trimAllSpace(String target) {
        if (null == target || target.isEmpty()) {
            return target;
        }
        return target.replaceAll("\\s*", "");
    }
}
