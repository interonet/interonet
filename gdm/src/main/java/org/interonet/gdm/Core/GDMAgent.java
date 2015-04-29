package org.interonet.gdm.Core;

import org.interonet.gdm.AuthenticationCenter.AuthToken;
import org.interonet.gdm.AuthenticationCenter.IAuthTokenManager;

import java.io.IOException;

public class GDMAgent implements IGDMAgent {
    GDMCore gdmCore;

    public GDMAgent(GDMCore gdmCore) {
        this.gdmCore = gdmCore;
    }

    @Override
    public String authenticateUser(String username, String password) {
        return gdmCore.authenticateUser(username, password);
    }

    @Override
    public String getSwitchesUsageStatus(AuthToken authToken) throws IOException {
        return gdmCore.getSwitchesUsageStatus(authToken);
    }

    @Override
    public String getVmsUsageStatus(AuthToken authToken) throws IOException {
        return gdmCore.getVmsUsageStatus(authToken);
    }


    @Override
    public Boolean orderSlice(AuthToken authToken, String order) throws Exception {
        return gdmCore.orderSlice(authToken, order);
    }

    @Override
    public String getOrdersList(AuthToken authToken) throws IOException {
        return gdmCore.getOrdersIDList(authToken);
    }

    @Override
    public String getOrderInfoByID(AuthToken authToken, String orderID) throws IOException {
        return gdmCore.getOrderInfoByID(authToken, orderID);
    }

    @Override
    public Boolean deleteOrderByID(AuthToken authToken, String orderID) {
        return gdmCore.deleteOrderByID(authToken, orderID);
    }

    @Override
    public String getRunningSlice(AuthToken authToken) throws IOException {
        return gdmCore.getRunningSliceIDsList(authToken);
    }

    @Override
    public IAuthTokenManager getAuthTokenManager() {
        return gdmCore.getAuthTokenManager();
    }

    @Override
    public String stopRunningSliceByID(AuthToken authToken, String orderID) {
        return gdmCore.stopRunningSliceByID(authToken, orderID);
    }

    @Override
    public String getRunningSliceInfoById(AuthToken authToken, String sliceID) throws IOException {
        return gdmCore.getRunningSliceInfoByID(authToken, sliceID);
    }
}
