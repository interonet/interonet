package org.interonet.gdm.Core;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaitingTermQueue {
    public List<WTOrder> wtQueue;
    private Map<String, Integer> userOrderNum;

    public WaitingTermQueue() {
        this.wtQueue = new ArrayList<WTOrder>();
        this.userOrderNum = new HashMap<String, Integer>();
    }

    synchronized public String getOrderIDListByUsername(String username) throws IOException {
//        StringBuilder orderIDList = new StringBuilder("orderIDList: ");
//        for (WTOrder wtOrder : wtQueue) {
//            if (wtOrder.username.equals(username)) {
//                orderIDList.append(wtOrder.sliceID);
//                orderIDList.append("\n");
//            }
//        }
//        return orderIDList.toString();

        List<String> orderIDList = new ArrayList<>();
        for (WTOrder wtOrder : wtQueue) {
            if (wtOrder.username.equals(username)) {
                orderIDList.add(wtOrder.sliceID);
            }
        }
        return new ObjectMapper().writeValueAsString(orderIDList);
    }

    synchronized public String getOrderInfoByID(String sliceID) {
        StringBuilder sliceInfo = new StringBuilder();
        for (WTOrder wtOrder : wtQueue) {
            if (wtOrder.sliceID.equals(sliceID)) {
                sliceInfo.append("Slice ID: ").append(wtOrder.sliceID).append("\n");
                sliceInfo.append("Switch Number: ").append(wtOrder.switchIDs.size()).append("\n");
                sliceInfo.append("VM Number: ").append(wtOrder.vmIDs.size()).append("\n");
                sliceInfo.append("Begin Time: ").append(wtOrder.beginTime).append("\n");
                sliceInfo.append("End Time: ").append(wtOrder.endTime).append("\n");
                sliceInfo.append("Controller IP: ").append(wtOrder.controllerIP).append("\n");
                sliceInfo.append("Controller Port: ").append(wtOrder.controllerPort).append("\n");
            }
            return sliceInfo.toString();
        }
        return null;
    }

    synchronized public void newOrder(WTOrder wtOrder) {
        if (userOrderNum.get(wtOrder.username) == null)
            userOrderNum.put(wtOrder.username, 0);
        Integer userOdNum = userOrderNum.get(wtOrder.username);

        userOrderNum.put(wtOrder.username, userOdNum + 1);
        wtQueue.add(wtOrder);

    }

    public List<WTOrder> getQueue() {
        return wtQueue;
    }


    synchronized public boolean deleteOrderByID(String sliceID) {
        for (WTOrder wtOrder : wtQueue) {
            if (wtOrder.sliceID.equals(sliceID)) {
                wtQueue.remove(wtOrder);
                return true;
            }
        }
        return false;
    }
}
