package com.mdvns.mdvn.websocket.service.impl;


import org.apache.log4j.Logger;

import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class WebSocketUtils {

    private static Logger log = Logger.getLogger(WebSocketUtils.class);

    public static Map<String, Session> clients = new ConcurrentHashMap<String, Session>();

    /*
    Add Session
     */
    public static void add(String userId, Session session) {
        clients.put(userId,session);
        log.info("当前连接数 = " + clients.size());

    }

    /*
    Receive Message
     */
    public static void receive(String userId, String message) {
        log.info("收到消息 : UserId = " + userId + " , Message = " + message);
        log.info("当前连接数 = " + clients.size());
    }

    /*
    Remove Session
     */
    public static void remove(String userId) {
        clients.remove(userId);
        log.info("当前连接数 = " + clients.size());

    }

    /*
    Get Session
     */
    public static boolean sendMessage(String userId , String message) {
        log.info("当前连接数 = " + clients.size());
        if(clients.get(userId) == null){
            return false;
        }else{
            clients.get(userId).getAsyncRemote().sendText(message);
            return true;
        }

    }
}