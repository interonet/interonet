package org.interonet.gdm.TestCore;

import org.interonet.gdm.AuthenticationCenter.AuthToken;
import org.interonet.gdm.AuthenticationCenter.IAuthTokenManager;

import java.util.Map;

public interface IGDMAgent {
    AuthToken authenticateUser(String username, String password);

    String getSwitchesUsageStatus(AuthToken authToken);

    String getVmsUsageStatus(AuthToken authToken);

    Boolean orderSlice(AuthToken authToken, int swichesNum, int vmsNum, String beginTime, String endTime,
                       Map<String, String> topology,
                       Map<String, String> switchConf,
                       String controllerIP, int controllerPort);

    String getOrdersList(AuthToken authToken);

    String getOrderInfoByID(AuthToken authToken, String orderID);

    String deleteOrderByID(AuthToken authToken, String orderID);

    String getRunningSlice(AuthToken authToken);

    IAuthTokenManager getAuthTokenManager();
}
