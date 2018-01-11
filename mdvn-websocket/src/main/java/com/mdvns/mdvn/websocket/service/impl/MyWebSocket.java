package com.mdvns.mdvn.websocket.service.impl;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.bean.model.SendMessageRequest;
import com.mdvns.mdvn.common.bean.model.Staff;
import com.mdvns.mdvn.common.util.RestResponseUtil;
import com.mdvns.mdvn.websocket.config.WebConfig;
import com.mdvns.mdvn.websocket.domain.RtrvServerPushListRequest;
import com.mdvns.mdvn.websocket.domain.RtrvServerPushResponse;
import com.mdvns.mdvn.websocket.domain.ServerPushInfo;
import com.mdvns.mdvn.websocket.domain.entity.ServerPush;
import com.mdvns.mdvn.websocket.repository.ServerPushRepository;
import com.mdvns.mdvn.websocket.service.WebSocketService;
import net.sf.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;

/**
 * 使用@ServerEndpoint创立websocket endpoint
 */
//该注解用来指定一个URI，客户端可以通过这个URI来连接到WebSocket。类似Servlet的注解mapping。无需在web.xml中配置。
@ServerEndpoint(value = "/websocket/{id}")
@Service
@Component
public class MyWebSocket implements WebSocketService {

    @Autowired
    private ServerPushRepository serverPushRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebConfig webConfig;

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //-------------------外加-------------------
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");    // 日期格式化

    private Logger logger = Logger.getLogger(this.getClass().getName());

    /* 日志常亮 */
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(MyWebSocket.class);

    private final String CLASS = this.getClass().getName();

    static Map<Long, Session> sessionMap = new Hashtable<Long, Session>();

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("id") String id)
            throws IOException, InterruptedException {

        Set<Map.Entry<Long, Session>> set = sessionMap.entrySet();

        for (Map.Entry<Long, Session> i : set) {
            try {
                Long key = i.getKey();//用户的staffId
                Session value = i.getValue();
                i.getValue().getAsyncRemote().sendText(message);//发信息给所有人
                if (key.equals("123")) {//发信息给部分人
                    logger.info("发信人id: " + id + ";[WebSocketServer] Received Message : userId = " + key + " , message = " + message);
                    WebSocketUtils.receive(key, message);
                    i.getValue().getBasicRemote().sendText(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("id") Long id) throws IOException {
        this.session = session;
        WebSocketUtils.add(id, session);
        logger.info("[WebSocketServer] Connected : userId = " + id);
        session.setMaxIdleTimeout(30 * 60 * 1000);//毫秒算的（设置session过期时间）
        sessionMap.put(id, session);
//        /*推送定时给自己的消息开始*/
//        this.checkMessageToOneself(session,id);
//        /*推送定时给自己的消息结束*/
        webSocketSet.add(this);    //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        System.out.println("Client connected");
        System.out.println("参数" + id);
    }


    @OnClose
    public void onClose(@PathParam("id") Long id) {
        sessionMap.remove(id);
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
        System.out.println("Connection closed");

        logger.info("[WebSocketServer] Close Connection : userId = " + id);
        WebSocketUtils.remove(id);
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error, @PathParam("id") Long id) {
        System.out.println("发生错误");
        error.printStackTrace();

        logger.info("[WebSocketServer] Connection Exception : userId = " + id + " , throwable = " + error.getMessage());
        WebSocketUtils.remove(id);
    }

    /**
     * 推送消息
     *
     * @param request
     * @throws IOException
     */
    public Boolean sendMessage(SendMessageRequest request) throws IOException {
//        this.session.getBasicRemote().sendText(message);//阻塞式的,同步
//        this.session.getAsyncRemote().sendText(message);//非阻塞式的
        com.mdvns.mdvn.common.bean.model.ServerPush serverPushResponse = request.getServerPushResponse();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        long curTime = currentTime.getTime();
        serverPushResponse.setCreateTime(curTime);
        //1、使用JSONObject
        JSONObject json = JSONObject.fromObject(serverPushResponse);
        String message = json.toString();
        Long initiatorId = request.getInitiatorId();//发起人Id
        List<Long> staffIds = request.getStaffIds();
        Set<Map.Entry<Long, Session>> set = sessionMap.entrySet();

        List<Long> keyStaffIds = new ArrayList<>();
        for (Map.Entry<Long, Session> i : set) {
            try {
                Long key = i.getKey();//用户的staffId
                Session value = i.getValue();
//                i.getValue().getAsyncRemote().sendText(message);//发信息给所有人
                for (int j = 0; j < staffIds.size(); j++) {
                    /**
                     * 如果接收人在线，直接推送
                     */
                    if (!key.equals(initiatorId) && key.equals(staffIds.get(j))) {//发信息给部分人
                        logger.info("发起人id: " + initiatorId + ";[WebSocketServer] Received Message : userId = " + key + " , message = " + message);
                        WebSocketUtils.receive(key, message);
                        i.getValue().getBasicRemote().sendText(message);
                    }
                    if (key.equals(staffIds.get(j))) {//发信息给部分人
                        if (!keyStaffIds.isEmpty() && keyStaffIds.contains(key)) {
                            continue;
                        }
                        keyStaffIds.add(key);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /**
         * 如果接收人不在线，入库
         */
        staffIds.remove(initiatorId);
        staffIds.removeAll(keyStaffIds);
        for (int i = 0; i < staffIds.size(); i++) {
            ServerPush serverPush = new ServerPush();
            serverPush.setCreateTime(currentTime);
            serverPush.setInitiatorId(initiatorId);
            serverPush.setRecipientId(staffIds.get(i));
            serverPush.setSubjectType(serverPushResponse.getSubjectType());
            serverPush.setSubjectId(serverPushResponse.getSubjectId());
            serverPush.setType(serverPushResponse.getType());
            if (serverPushResponse.getType().equals("update progress")) {
                serverPush.setOldProgress(serverPushResponse.getOldProgress());
                serverPush.setNewProgress(serverPushResponse.getNewProgress());
            }
            if (serverPushResponse.getSubjectType().equals("task")) {
                serverPush.setTaskByStoryId(serverPushResponse.getTaskByStoryId());
            }
            serverPush = this.serverPushRepository.saveAndFlush(serverPush);
        }
        return true;
    }

    /**
     * 定时给自己推送消息
     *
     * @param request
     * @throws IOException
     */
    public Boolean sendMessageToOneself(SendMessageRequest request) throws IOException {
        com.mdvns.mdvn.common.bean.model.ServerPush serverPushResponse = request.getServerPushResponse();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        long curTime = currentTime.getTime();
        serverPushResponse.setCreateTime(curTime);
        //1、使用JSONObject
        JSONObject json = JSONObject.fromObject(serverPushResponse);
        String message = json.toString();
        Long recipientId = request.getInitiatorId();//接收人Id
        Set<Map.Entry<Long, Session>> set = sessionMap.entrySet();
        Long keyStaffId = null;
        for (Map.Entry<Long, Session> i : set) {
            try {
                Long key = i.getKey();//用户的staffId
                /**
                 * 如果接收人在线，直接推送
                 */
                if (key.equals(recipientId)) {//发信息给部分人
                    logger.info("发起人id: " + recipientId + ";[WebSocketServer] Received Message : userId = " + key + " , message = " + message);
                    WebSocketUtils.receive(key, message);
                    i.getValue().getBasicRemote().sendText(message);
                    keyStaffId = recipientId;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /**
         * 如果接收人不在线，入库
         */
        if (keyStaffId == null) {
            ServerPush serverPush = new ServerPush();
            serverPush.setCreateTime(currentTime);
            serverPush.setInitiatorId(recipientId);
            serverPush.setRecipientId(recipientId);
            serverPush.setSubjectType(serverPushResponse.getSubjectType());
            serverPush.setSubjectId(serverPushResponse.getSubjectId());
            serverPush.setType(serverPushResponse.getType());
            serverPush = this.serverPushRepository.saveAndFlush(serverPush);
        }
        return true;
    }

    /**
     * 检测是不是有定时给自己推送的消息
     *
     * @param session,id
     * @return
     * @throws IOException
     */
    public Boolean checkMessageToOneself(Session session, Long id) throws IOException {
        /*查询所有的要定时给自己推送的消息*/
//        List<ServerPush> serverPushes = this.serverPushRepository.findByIsTimedPush(1);
        List<ServerPush> serverPushes = new ArrayList<>();
        for (int i = 0; i < serverPushes.size(); i++) {
            Long recipientId = serverPushes.get(i).getRecipientId();//接收人Id
            try {
                if (id.equals(recipientId)) {//发信息给部分人
                    logger.info("发起人id: " + recipientId + ";[WebSocketServer] Received Message : userId = " + id + " , message = " + serverPushes.get(i));
                    /**
                     * 包装要推送的消息
                     */
                    ServerPush serverPush = serverPushes.get(i);
                    //赋值·
                    ServerPushInfo serverPushInfo = new ServerPushInfo();
                    BeanUtils.copyProperties(serverPush, serverPushInfo);
                    serverPushInfo.setCreateTime(serverPush.getCreateTime().getTime());
                    Long initiatorId = serverPush.getInitiatorId();//发起人
                    /**
                     * 查询发起者详细信息
                     */
                    SingleCriterionRequest singleCriterionRequest = new SingleCriterionRequest();
                    singleCriterionRequest.setCriterion(String.valueOf(initiatorId));
                    singleCriterionRequest.setStaffId(initiatorId);
                    ParameterizedTypeReference<RestResponse<Staff>> typeRef = new ParameterizedTypeReference<RestResponse<Staff>>() {
                    };
                    ResponseEntity<RestResponse<Staff>> responseEntity = restTemplate.exchange(webConfig.getRetrieveByIdUrl(), HttpMethod.POST, new HttpEntity<Object>(singleCriterionRequest), typeRef, RestResponse.class);
                    RestResponse<Staff> restResponse = responseEntity.getBody();
                    serverPushInfo.setInitiator(restResponse.getData());
                    //1、使用JSONObject
                    JSONObject json = JSONObject.fromObject(serverPushInfo);
                    String message = json.toString();
                    //推送这条信息
                    session.getBasicRemote().sendText(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    /**
     * 获取推送信息列表
     *
     * @param request
     * @return
     */
    @Override
    public RestResponse<?> rtrvServerPushInfoList(RtrvServerPushListRequest request) {
        LOG.info("开始执行{} rtrvServerPushInfoList()方法.", this.CLASS);
        RtrvServerPushResponse rtrvServerPushResponse = new RtrvServerPushResponse();
        Long recipientId = request.getRecipientId();
        Integer m = request.getStartNum();
        Integer n = request.getSize();
        List<ServerPush> serverPushes = this.serverPushRepository.findByRecipientId(recipientId, m, n);
        List<ServerPush> serverPushList = this.serverPushRepository.findByRecipientId(recipientId);
        if (serverPushes.size() > 0) {
            List<ServerPushInfo> serverPushInfos = new ArrayList<>();
            for (int i = 0; i < serverPushes.size(); i++) {
                ServerPush serverPush = serverPushes.get(i);
                //赋值·
                ServerPushInfo serverPushInfo = new ServerPushInfo();
                BeanUtils.copyProperties(serverPush, serverPushInfo);
                serverPushInfo.setCreateTime(serverPush.getCreateTime().getTime());
                Long initiatorId = serverPush.getInitiatorId();//发起人
                /**
                 * 查询发起者详细信息
                 */
                SingleCriterionRequest singleCriterionRequest = new SingleCriterionRequest();
                singleCriterionRequest.setCriterion(String.valueOf(initiatorId));
                singleCriterionRequest.setStaffId(initiatorId);
                ParameterizedTypeReference<RestResponse<Staff>> typeRef = new ParameterizedTypeReference<RestResponse<Staff>>() {
                };
                ResponseEntity<RestResponse<Staff>> responseEntity = restTemplate.exchange(webConfig.getRetrieveByIdUrl(), HttpMethod.POST, new HttpEntity<Object>(singleCriterionRequest), typeRef, RestResponse.class);
                RestResponse<Staff> restResponse = responseEntity.getBody();
                serverPushInfo.setInitiator(restResponse.getData());
                serverPushInfos.add(serverPushInfo);
            }
            rtrvServerPushResponse.setServerPushes(serverPushInfos);
        }
        if (serverPushList.size() > 0) {
            rtrvServerPushResponse.setTotalNumbers(serverPushList.size());
        } else {
            rtrvServerPushResponse.setTotalNumbers(0);
        }
        logger.info("返回的serverpush信息wei" + serverPushes.toString());
        LOG.info("结束执行{} rtrvServerPushInfoList()方法.", this.CLASS);
        return RestResponseUtil.success(rtrvServerPushResponse);
    }

    /**
     * 删除推送信息
     *
     * @param uuId
     * @return
     */
    @Override
    public RestResponse<?> deleteServerPushInfo(Integer uuId) {
        LOG.info("开始执行{} deleteServerPushInfo()方法.", this.CLASS);
        try {
            this.serverPushRepository.delete(uuId);
            LOG.info("结束执行{} deleteServerPushInfo()方法.", this.CLASS);
            return RestResponseUtil.success(true);
        } catch (Exception e) {
            logger.info("消息推送(创建comment)出现异常，异常信息：");
            return RestResponseUtil.error("2301", "Fail to delete serberPush");
        }
    }

    //-----------------外加----------------
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        MyWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        MyWebSocket.onlineCount--;
    }


    //-----------------外加----------------

    /**
     * 广播给所有人
     *
     * @param message
     */
    public static void broadcastAll(String message) {
        Set<Map.Entry<Long, Session>> set = sessionMap.entrySet();
        for (Map.Entry<Long, Session> i : set) {
            try {
                i.getValue().getBasicRemote().sendText("'text:'" + message + "'}");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

