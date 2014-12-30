package org.interonet.gdm;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaitingStartQueue {

    public List<WSOrder> wsQueue;
    private Map<String, Integer> userOrderNum;

    public WaitingStartQueue() {
        this.wsQueue = new ArrayList<WSOrder>();
        this.userOrderNum = new HashMap<String, Integer>();
    }

    public List<WSOrder> getQueue() {
        return wsQueue;
    }

    public boolean newOrder(String username,
                            List<Integer> switchIDs,
                            List<Integer> vmIDs,
                            String beginTime,
                            String endTime,
                            Map<String, String> topology,
                            Map<String, String> switchConf,
                            String controllerIP,
                            int controllerPort) {

        if (userOrderNum.get(username) == null)
            userOrderNum.put(username, 0);
        userOrderNum.put(username, userOrderNum.get(username) + 1);

        String orderID = username + userOrderNum.get(username);

        WSOrder wsOrder = new WSOrder(orderID, username, switchIDs, vmIDs, beginTime, endTime, topology, switchConf, controllerIP, controllerPort);
        return wsQueue.add(wsOrder);
    }

    public String getOrderIDListByUsername(String username) {
        StringBuilder orderIDList = new StringBuilder();
        for (WSOrder wsOrder : wsQueue) {
            if (wsOrder.username.equals(username)) {
                orderIDList.append(wsOrder.orderID);
                orderIDList.append("\n");
            }
        }
        return orderIDList.toString();
    }

    public boolean deleteOrderByID(String orderID) {
        for (WSOrder wsOrder : wsQueue) {
            if (wsOrder.orderID.equals(orderID)) {
                wsQueue.remove(wsOrder);
                return true;
            }
        }
        return false;
    }

    public String getOrderInfoByID(String orderID) {
        StringBuilder orderInfo = new StringBuilder();
        for (WSOrder wsOrder : wsQueue) {
            if (wsOrder.orderID.equals(orderID)) {
                orderInfo.append("OrderID: ").append(wsOrder.orderID).append("\n");
                orderInfo.append("Switch Number: ").append(wsOrder.switchIDs.size()).append("\n");
                orderInfo.append("VM Number: ").append(wsOrder.vmIDs.size()).append("\n");
                orderInfo.append("Begin Time: ").append(wsOrder.beginTime).append("\n");
                orderInfo.append("End Time: ").append(wsOrder.endTime).append("\n");
                orderInfo.append("Controller IP: ").append(wsOrder.controllerIP).append("\n");
                orderInfo.append("Controller Port: ").append(wsOrder.controllerPort).append("\n");
                return orderInfo.toString();
            }
        }
        return null;
    }
}
