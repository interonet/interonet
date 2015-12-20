package org.interonet.gdm.WebService;

import org.interonet.gdm.AuthenticationCenter.AuthToken;
import org.interonet.gdm.AuthenticationCenter.IAuthTokenManager;
import org.interonet.gdm.Core.IGDMAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RPCService implements IRPCService {
    private IGDMAgent gdmAgent;
    private IAuthTokenManager authTokenManager;
    private Logger logger = LoggerFactory.getLogger(RPCService.class);

    public RPCService(IGDMAgent gdmAgent) {
        this.gdmAgent = gdmAgent;
        this.authTokenManager = gdmAgent.getAuthTokenManager();

    }

    @Override
    public String authenticateUser(String username, String password) {
        logger.debug("username = [" + username + "], password = [" + password + "]");
        String token = gdmAgent.authenticateUser(username, password);
        return token != null ? token : "Failed";
    }

    @Override
    public String getSwitchesUsageStatus(String authToken) {
        logger.debug("authToken = [" + authToken + "]");
        try {
            String status = gdmAgent.getSwitchesUsageStatus(authTokenManager.toAuthToken(authToken));
            return status != null ? status : "Failed";
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return "Exception Occur.";
        }
    }

    @Override
    public String getVMsUsageStatus(String authToken) {
        logger.debug("authToken = [" + authToken + "]");
        try {
            String status = gdmAgent.getVmsUsageStatus(authTokenManager.toAuthToken(authToken));
            return status != null ? status : "Failed";
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return "Exception Occur.";
        }
    }

    @Override
    public String orderSlice(String authToken, String order) {
        logger.debug("authToken = [" + authToken + "], order = [" + order + "]");
        try {
            AuthToken authTk = authTokenManager.toAuthToken(authToken);
            Boolean status;
            status = gdmAgent.orderSlice(authTk, order);
            return status ? "Success" : "Failed";
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return "Exception Occur.";
        }
    }

    @Override
    public String getOrdersList(String authToken) {
        logger.debug("authToken = [" + authToken + "]");
        try {
            String status = gdmAgent.getOrdersList(authTokenManager.toAuthToken(authToken));
            return status != null ? status : "Failed";
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return "Exception Occur.";
        }
    }

    @Override
    public String getOrderInfoByID(String authToken, String orderID) {
        logger.debug("authToken = [" + authToken + "], orderID = [" + orderID + "]");
        try {
            String status = gdmAgent.getOrderInfoByID(authTokenManager.toAuthToken(authToken), orderID);
            return status != null ? status : "Failed";
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return "Exception Occur.";
        }
    }

    @Override
    public String deleteOrderByID(String authToken, String orderID) {
        logger.debug("authToken = [" + authToken + "], orderID = [" + orderID + "]");
        Boolean status = gdmAgent.deleteOrderByID(authTokenManager.toAuthToken(authToken), orderID);
        return status ? "Success" : "Failed";
    }

    @Override
    public String getRunningSlices(String authToken) {
        logger.debug("authToken = [" + authToken + "]");
        try {
            return gdmAgent.getRunningSlice(authTokenManager.toAuthToken(authToken));
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return "Exception Occur.";
        }
    }

//    @Override
//    public String stopRunningSliceByID(String authToken, String orderID) {
//        return gdmAgent.stopRunningSliceByID(authTokenManager.toAuthToken(authToken), orderID);
//    }

    @Override
    public String getRunningSliceInfoById(String authToken, String sliceID) {
        logger.debug("authToken = [" + authToken + "], sliceID = [" + sliceID + "]");
        try {
            return gdmAgent.getRunningSliceInfoById(authTokenManager.toAuthToken(authToken), sliceID);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return "Exception Occur";
        }
    }
}
