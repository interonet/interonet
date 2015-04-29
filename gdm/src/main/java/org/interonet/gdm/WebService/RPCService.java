package org.interonet.gdm.WebService;

import org.interonet.gdm.AuthenticationCenter.AuthToken;
import org.interonet.gdm.AuthenticationCenter.IAuthTokenManager;
import org.interonet.gdm.Core.IGDMAgent;

import java.io.IOException;
import java.util.logging.Logger;

public class RPCService implements IRPCService {
    private IGDMAgent gdmAgent;
    private IAuthTokenManager authTokenManager;

    public RPCService(IGDMAgent gdmAgent) {
        this.gdmAgent = gdmAgent;
        this.authTokenManager = gdmAgent.getAuthTokenManager();

    }

    @Override
    public String authenticateUser(String username, String password) {
        Logger.getAnonymousLogger().info("");
        String token = gdmAgent.authenticateUser(username, password);
        return token != null ? token : "Failed";
    }

    @Override
    public String getSwitchesUsageStatus(String authToken) {
        Logger.getAnonymousLogger().info("");
        try {
            String status = gdmAgent.getSwitchesUsageStatus(authTokenManager.toAuthToken(authToken));
            return status != null ? status : "Failed";
        } catch (IOException e) {
            e.printStackTrace();
            Logger.getAnonymousLogger().severe(e.getMessage());
            return "Exception Occur.";
        }
    }

    @Override
    public String getVMsUsageStatus(String authToken) {
        Logger.getAnonymousLogger().info("");
        try {
            String status = gdmAgent.getVmsUsageStatus(authTokenManager.toAuthToken(authToken));
            return status != null ? status : "Failed";
        } catch (IOException e) {
            e.printStackTrace();
            Logger.getAnonymousLogger().severe(e.getMessage());
            return "Exception Occur.";
        }
    }

    @Override
    public String orderSlice(String authToken, String order) {
        try {
            Logger.getAnonymousLogger().info("");
            AuthToken authTk = authTokenManager.toAuthToken(authToken);
            Boolean status;
            status = gdmAgent.orderSlice(authTk, order);
            return status ? "Success" : "Failed";
        } catch (Exception e) {
            Logger.getAnonymousLogger().severe(e.getMessage());
            e.printStackTrace();
            return "Exception Occur.";
        }
    }

    @Override
    public String getOrdersList(String authToken) {
        Logger.getAnonymousLogger().info("");
        try {
            String status = gdmAgent.getOrdersList(authTokenManager.toAuthToken(authToken));
            return status != null ? status : "Failed";
        } catch (IOException e) {
            e.printStackTrace();
            Logger.getAnonymousLogger().severe(e.getMessage());
            return "Exception Occur.";
        }
    }

    @Override
    public String getOrderInfoByID(String authToken, String orderID) {
        Logger.getAnonymousLogger().info("");
        try {
            String status = gdmAgent.getOrderInfoByID(authTokenManager.toAuthToken(authToken), orderID);
            return status != null ? status : "Failed";
        } catch (IOException e) {
            e.printStackTrace();
            Logger.getAnonymousLogger().severe(e.getMessage());
            return "Exception Occur.";
        }
    }

    @Override
    public String deleteOrderByID(String authToken, String orderID) {
        Logger.getAnonymousLogger().info("");
        Boolean status = gdmAgent.deleteOrderByID(authTokenManager.toAuthToken(authToken), orderID);
        return status ? "Success" : "Failed";
    }

    @Override
    public String getRunningSlices(String authToken) {
        Logger.getAnonymousLogger().info("");
        try {
            return gdmAgent.getRunningSlice(authTokenManager.toAuthToken(authToken));
        } catch (IOException e) {
            e.printStackTrace();
            Logger.getAnonymousLogger().severe(e.getMessage());
            return "Exception Occur.";
        }
    }

//    @Override
//    public String stopRunningSliceByID(String authToken, String orderID) {
//        return gdmAgent.stopRunningSliceByID(authTokenManager.toAuthToken(authToken), orderID);
//    }

    @Override
    public String getRunningSliceInfoById(String authToken, String sliceID) {
        Logger.getAnonymousLogger().info("");
        try {
            return gdmAgent.getRunningSliceInfoById(authTokenManager.toAuthToken(authToken), sliceID);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getAnonymousLogger().severe(e.getMessage());
            return "Exception Occur";
        }
    }
}
