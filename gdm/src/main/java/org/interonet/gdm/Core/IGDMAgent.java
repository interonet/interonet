package org.interonet.gdm.Core;

import org.interonet.gdm.AuthenticationCenter.AuthToken;
import org.interonet.gdm.AuthenticationCenter.IAuthTokenManager;

import java.io.IOException;

public interface IGDMAgent {
    String authenticateUser(String username, String password);

    String getSwitchesUsageStatus(AuthToken authToken) throws IOException;

    String getVmsUsageStatus(AuthToken authToken) throws IOException;

    Boolean orderSlice(AuthToken authToken, String order) throws Exception;

    String getOrdersList(AuthToken authToken) throws IOException;

    String getOrderInfoByID(AuthToken authToken, String orderID) throws IOException;

    Boolean deleteOrderByID(AuthToken authToken, String orderID);

    String getRunningSlice(AuthToken authToken) throws IOException;

    IAuthTokenManager getAuthTokenManager();

    String stopRunningSliceByID(AuthToken authToken, String orderID);

    String getRunningSliceInfoById(AuthToken authToken, String sliceID) throws Exception;
}
