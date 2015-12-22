package org.interonet.gdm.Core;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.interonet.gdm.Core.Utils.DayTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaitingStartQueue {

    public List<WSOrder> wsQueue;
    private Map<String, Integer> userOrderNum;
    private Logger logger;

    public WaitingStartQueue() {
        this.wsQueue = new ArrayList<>();
        this.userOrderNum = new HashMap<>();
        logger = LoggerFactory.getLogger(WaitingStartQueue.class);
    }

    public List<WSOrder> getQueue() {
        return wsQueue;
    }

    synchronized public boolean newOrder(String username,
                                         List<Integer> switchIDs,
                                         List<Integer> vmIDs,
                                         String beginTime,
                                         String endTime,
                                         Map<String, String> topology,
                                         Map<String, String> switchConf,
                                         String controllerIP,
                                         int controllerPort,
                                         Map<String, Map> customSwitchConf
    ) {

        if (userOrderNum.get(username) == null)
            userOrderNum.put(username, 0);
        userOrderNum.put(username, userOrderNum.get(username) + 1);

        String orderID = username + userOrderNum.get(username);

        WSOrder wsOrder = new WSOrder(orderID, username, switchIDs, vmIDs, beginTime, endTime, topology, switchConf, controllerIP, controllerPort, customSwitchConf);
        return wsQueue.add(wsOrder);
    }

    synchronized public String getOrderIDListByUsername(String username) throws IOException {
        List<String> orderIDList = new ArrayList<>();
        for (WSOrder wsOrder : wsQueue) {
            if (wsOrder.username.equals(username)) {
                orderIDList.add(wsOrder.orderID);
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(orderIDList);
    }

    synchronized public boolean deleteOrderByID(String orderID) {
        for (WSOrder wsOrder : wsQueue) {
            if (wsOrder.orderID.equals(orderID)) {
                wsQueue.remove(wsOrder);
                return true;
            }
        }
        return false;
    }

    synchronized public String getOrderInfoByID(String orderID) throws IOException {
        Map<String, String> orderInfo = new HashMap<>();
        for (WSOrder wsOrder : wsQueue) {
            if (wsOrder.orderID.equals(orderID)) {
                orderInfo.put("OrderID", wsOrder.orderID);
                orderInfo.put("SwitchNumber", String.valueOf(wsOrder.switchIDs.size()));
                orderInfo.put("VMNumber", String.valueOf(wsOrder.vmIDs.size()));
                orderInfo.put("BeginTime", wsOrder.beginTime);
                orderInfo.put("EndTime", wsOrder.endTime);
                orderInfo.put("ControllerIP", wsOrder.controllerIP);
                orderInfo.put("ControllerPort", String.valueOf(wsOrder.controllerPort));
                break;
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(orderInfo);
    }

    synchronized public List<WSOrder> getTimeReadyWSOrders(DayTime currentTime) {
        List<WSOrder> list = new ArrayList<>();
        for (WSOrder wsOrder : wsQueue) {
            if (new DayTime(wsOrder.beginTime).earlyThan(currentTime))
                list.add(wsOrder);
        }
        return list;
    }
}
