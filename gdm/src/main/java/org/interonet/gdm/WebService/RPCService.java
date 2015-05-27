package org.interonet.gdm.WebService;

import org.interonet.gdm.AuthenticationCenter.AuthToken;
import org.interonet.gdm.AuthenticationCenter.IAuthTokenManager;
import org.interonet.gdm.Core.IGDMAgent;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

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
        logger.info("");
        String token = gdmAgent.authenticateUser(username, password);
        return token != null ? token : "Failed";
    }

    @Override
    public String getSwitchesUsageStatus(String authToken) {
        logger.info("");
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
        logger.info("");
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
        try {
            logger.info("");
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
        logger.info("");
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
        logger.info("");
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
        logger.info("");
        Boolean status = gdmAgent.deleteOrderByID(authTokenManager.toAuthToken(authToken), orderID);
        return status ? "Success" : "Failed";
    }

    @Override
    public String getRunningSlices(String authToken) {
        logger.info("");
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
        logger.info("");
        try {
            return gdmAgent.getRunningSliceInfoById(authTokenManager.toAuthToken(authToken), sliceID);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return "Exception Occur";
        }
    }
}
