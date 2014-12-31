package org.interonet.gdm.Core;

import org.interonet.gdm.AuthenticationCenter.AuthToken;
import org.interonet.gdm.AuthenticationCenter.AuthTokenManager;
import org.interonet.gdm.AuthenticationCenter.UserManager;
import org.interonet.gdm.OperationCenter.OperationCenter;

import java.util.List;
import java.util.Map;

public class GDMCore implements IGDMCore {

    private WaitingStartQueue wsQueue;
    private WaitingTermQueue wtQueue;
    private WSQueueManager wsQueueOperator;
    private WTQueueManager wtQueueOperator;
    private OperationCenter operationCenter;
    private GDMAgent gdmAgent;
    private AuthTokenManager authTokenManager;
    private SwitchTimeTable switchTimeTable;
    private VMTimeTable vmTimeTable;

    public GDMCore() {
    }


    @Override

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


    @Override
    public GDMAgent getAgent() {
        return gdmAgent;
    }

    @Override
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

    @Override
    public AuthToken authenticateUser(String username, String password) {
        UserManager userManager = new UserManager();
        if (!(userManager.authUser(username, password))) return null;
        return authTokenManager.generate(username, password);
    }

    @Override
    public String getSwitchesUsageStatus(AuthToken authToken) {
        if (!authTokenManager.auth(authToken))
            return "Authentication failed.";
        return switchTimeTable.getTimeTable();
    }

    @Override
    public String getVmsUsageStatus(AuthToken authToken) {
        if (!authTokenManager.auth(authToken))
            return "Authentication failed.";
        return vmTimeTable.getTimeTable();
    }

    @Override
    public String getOrdersIDList(AuthToken authToken) {
        if (!authTokenManager.auth(authToken))
            return "Authentication failed.";
        String username = authTokenManager.getUsernameByToken(authToken);
        return wsQueue.getOrderIDListByUsername(username);
    }

    @Override
    public String getOrderInfoByID(AuthToken authToken, String orderID) {
        if (!authTokenManager.auth(authToken))
            return "Authentication failed.";

        String orderInfo = wsQueue.getOrderInfoByID(orderID);

        return orderInfo != null ? orderInfo : "Failed";

    }

    @Override
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

    @Override
    public String getRunningSliceIDsList(AuthToken authToken) {
        if (!authTokenManager.auth(authToken))
            return "Authentication failed.";
        String username = authTokenManager.getUsernameByToken(authToken);
        return wtQueue.getOrderIDListByUsername(username);

    }

    @Override
    public String getRuningSliceInfoByID(AuthToken authToken, String SliceID) {
        if (!authTokenManager.auth(authToken))
            return "Authentication failed.";

        return wtQueue.getOrderInfoByID(SliceID);
    }

    @Override
    public AuthTokenManager getAuthTokenManager() {
        return authTokenManager;
    }

/*
    @Override
    public String stopRunningSliceByID(AuthToken authToken, String orderID) {
        if (authTokenManager.auth(authToken) == false)
            return "Authentication failed.";

        String status = wtQueue.deleteOrderByID(orderID);
        return status;
    }
*/

}
