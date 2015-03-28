package org.interonet.gdm.Core;

import org.interonet.gdm.AuthenticationCenter.AuthToken;
import org.interonet.gdm.AuthenticationCenter.IAuthTokenManager;

import java.io.IOException;
import java.util.Map;

public interface IGDMAgent {
    String authenticateUser(String username, String password);

    String getSwitchesUsageStatus(AuthToken authToken) throws IOException;

    String getVmsUsageStatus(AuthToken authToken) throws IOException;

    Boolean orderSlice(AuthToken authToken, int swichesNum, int vmsNum, String beginTime, String endTime,
                       Map<String, String> topology,
                       Map<String, String> switchConf,
                       String controllerIP, int controllerPort);

    String getOrdersList(AuthToken authToken);

    String getOrderInfoByID(AuthToken authToken, String orderID);

    String deleteOrderByID(AuthToken authToken, String orderID);

    String getRunningSlice(AuthToken authToken);

    IAuthTokenManager getAuthTokenManager();

    String stopRunningSliceByID(AuthToken authToken, String orderID);
}
