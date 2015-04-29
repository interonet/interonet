package org.interonet.gdm.Core;

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
        this.wtQueue = new ArrayList<>();
        this.userOrderNum = new HashMap<>();
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

    synchronized public String getRunningSliceInfoByID(String sliceID) throws IOException {
        Map<String, Object> sliceInfo = new HashMap<>();
        for (WTOrder wtOrder : wtQueue) {
            if (wtOrder.sliceID.equals(sliceID)) {
                sliceInfo.put("SliceID", wtOrder.sliceID);
                sliceInfo.put("BeginTime", wtOrder.beginTime);
                sliceInfo.put("EndTime", wtOrder.endTime);
                sliceInfo.put("Topology", wtOrder.topology);
                sliceInfo.put("ControllerIP", wtOrder.controllerIP);
                sliceInfo.put("ControllerPort", String.valueOf(wtOrder.controllerPort));
                sliceInfo.put("userSW2domSWMapping", wtOrder.userSW2domSW);
                sliceInfo.put("userVM2domVMMapping", wtOrder.userVM2domVM);
                break;
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(sliceInfo);
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
