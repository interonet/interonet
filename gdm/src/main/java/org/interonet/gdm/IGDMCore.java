package org.interonet.gdm;


import java.util.Map;

public interface IGDMCore {
    public void start();

    public GDMAgent getAgent();

    public AuthToken authenticateUser(String username, String password);

    public String getSwitchesUsageStatus(AuthToken authToken);

    public String getVmsUsageStatus(AuthToken authToken);

    public Boolean orderSlice(AuthToken authToken, int swichesNum, int vmsNum, String beginTime, String endTime, Map<String, String> topology, Map<String, String> switchConf, String controllerIP, int controllerPort);

    public String getOrdersIDList(AuthToken authToken);

    public String getOrderInfoByID(AuthToken authToken, String orderID);

    public String deleteOrderByID(AuthToken authToken, String orderID);

    public String getRunningSliceIDsList(AuthToken authToken);

    public String getRuningSliceInfoByID(AuthToken authToken, String SliceID);

    public AuthTokenManager getAuthTokenManager();
    /*
    public String stopRunningSliceByID(AuthToken authToken, String orderID);
    */
}
