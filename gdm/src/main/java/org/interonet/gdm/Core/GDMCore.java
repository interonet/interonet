package org.interonet.gdm.Core;

import org.interonet.gdm.AuthenticationCenter.*;
import org.interonet.gdm.OperationCenter.IOperationCenter;
import org.interonet.gdm.OperationCenter.OperationCenter;

import java.util.List;
import java.util.Map;

public class GDMCore {

    private WaitingStartQueue wsQueue;
    private WaitingTermQueue wtQueue;
    private WSQueueManager wsQueueOperator;
    private WTQueueManager wtQueueOperator;
    private IOperationCenter operationCenter;
    private IGDMAgent gdmAgent;
    private IAuthTokenManager authTokenManager;
    private SwitchTimeTable switchTimeTable;
    private VMTimeTable vmTimeTable;

    public GDMCore() {
    }

    public void start() {
        wsQueue = new WaitingStartQueue();
        wtQueue = new WaitingTermQueue();
        operationCenter = new OperationCenter();
        gdmAgent = new GDMAgent(this);
        wsQueueOperator = new WSQueueManager(wsQueue, wtQueue, operationCenter);
        wtQueueOperator = new WTQueueManager(wsQueue, wtQueue, operationCenter);
        Thread wsQueueOperatorThread = new Thread(wsQueueOperator);
        wsQueueOperatorThread.start();
        Thread wtQueueOperatorThread = new Thread(wtQueueOperator);
        wtQueueOperatorThread.start();
        authTokenManager = new AuthTokenManager();
        switchTimeTable = new SwitchTimeTable();
        vmTimeTable = new VMTimeTable();
    }


    public IGDMAgent getAgent() {
        return gdmAgent;
    }

    public Boolean orderSlice(AuthToken authToken,
                              int switchesNum,
                              int vmsNum,
                              String beginTime,
                              String endTime,
                              Map<String, String> topology,
                              Map<String, String> switchConf,
                              String controllerIP,
                              int controllerPort) {
        if (!authTokenManager.auth(authToken))
            return false;

        String username = authTokenManager.getUsernameByToken(authToken);
        List<Integer> switchIDs = switchTimeTable.checkSWAvailability(switchesNum, beginTime, endTime);
        List<Integer> vmIDs = vmTimeTable.checkVMAvailability(vmsNum, beginTime, endTime);

        if (username == null || switchIDs == null || vmIDs == null)
            return false;

        boolean swStatus = switchTimeTable.setOccupied(switchIDs, beginTime, endTime);
        boolean vmStatus = vmTimeTable.setOccupied(vmIDs, beginTime, endTime);
        return !(!swStatus || !vmStatus) && wsQueue.newOrder(username, switchIDs, vmIDs, beginTime, endTime, topology, switchConf, controllerIP, controllerPort);

    }

    public AuthToken authenticateUser(String username, String password) {
        IUserManager userManager = new UserManager();
        if (!(userManager.authUser(username, password))) return null;
        return authTokenManager.generate(username, password);
    }

    public String getSwitchesUsageStatus(AuthToken authToken) {
        if (!authTokenManager.auth(authToken))
            return "Authentication failed.";
        return switchTimeTable.getTimeTable();
    }

    public String getVmsUsageStatus(AuthToken authToken) {
        if (!authTokenManager.auth(authToken))
            return "Authentication failed.";
        return vmTimeTable.getTimeTable();
    }

    public String getOrdersIDList(AuthToken authToken) {
        if (!authTokenManager.auth(authToken))
            return "Authentication failed.";
        String username = authTokenManager.getUsernameByToken(authToken);
        return wsQueue.getOrderIDListByUsername(username);
    }

    public String getOrderInfoByID(AuthToken authToken, String orderID) {
        if (!authTokenManager.auth(authToken))
            return "Authentication failed.";

        String orderInfo = wsQueue.getOrderInfoByID(orderID);

        return orderInfo != null ? orderInfo : "Failed";

    }

    public String deleteOrderByID(AuthToken authToken, String orderID) {
        if (!authTokenManager.auth(authToken))
            return "Authentication failed.";

        //TODO delete the info in VMTable && SwitchTable.
        boolean wsQueueStatus = wsQueue.deleteOrderByID(orderID);

        if (!wsQueueStatus)
            return "Failed.";
        else
            return "Successful";
    }

    public String getRunningSliceIDsList(AuthToken authToken) {
        if (!authTokenManager.auth(authToken))
            return "Authentication failed.";
        String username = authTokenManager.getUsernameByToken(authToken);
        return wtQueue.getOrderIDListByUsername(username);

    }

    public String getRuningSliceInfoByID(AuthToken authToken, String SliceID) {
        if (!authTokenManager.auth(authToken))
            return "Authentication failed.";

        return wtQueue.getOrderInfoByID(SliceID);
    }

    public IAuthTokenManager getAuthTokenManager() {
        return authTokenManager;
    }

/*
    public String stopRunningSliceByID(AuthToken authToken, String orderID) {
        if (authTokenManager.auth(authToken) == false)
            return "Authentication failed.";

        String status = wtQueue.deleteOrderByID(orderID);
        return status;
    }
*/

}
