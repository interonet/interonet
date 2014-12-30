package org.interonet.gdm;

import java.util.Map;

public class GDMAgent {
    GDMCore gdmCore;

    public GDMAgent(GDMCore gdmCore) {
        this.gdmCore = gdmCore;
    }

    public AuthToken authenticateUser(String username, String password) {
        return gdmCore.authenticateUser(username, password);
    }

    public String getSwitchesUsageStatus(AuthToken authToken) {
        return gdmCore.getSwitchesUsageStatus(authToken);
    }

    public String getVmsUsageStatus(AuthToken authToken) {
        return gdmCore.getVmsUsageStatus(authToken);
    }


    public Boolean orderSlice(AuthToken authToken, int swichesNum, int vmsNum, String beginTime, String endTime,
                              Map<String, String> topology,
                              Map<String, String> switchConf,
                              String controllerIP, int controllerPort) {
        return gdmCore.orderSlice(authToken, swichesNum, vmsNum, beginTime, endTime, topology, switchConf, controllerIP, controllerPort);
    }

    public String getOrdersList(AuthToken authToken) {
        return gdmCore.getOrdersIDList(authToken);
    }

    public String getOrderInfoByID(AuthToken authToken, String orderID) {
        return gdmCore.getOrderInfoByID(authToken, orderID);
    }

    public String deleteOrderByID(AuthToken authToken, String orderID) {
        return gdmCore.deleteOrderByID(authToken, orderID);
    }

    public String getRunningSlice(AuthToken authToken) {
        return gdmCore.getRunningSliceIDsList(authToken);
    }

    public AuthTokenManager getAuthTokenManager() {
        return gdmCore.getAuthTokenManager();
    }

//    public String stopRunningSliceByID(AuthToken authToken, String orderID) {
//        return gdmCore.stopRunningSliceByID(authToken, orderID);
//    }
}
