package org.interonet.gdm.Core;

import org.interonet.gdm.AuthenticationCenter.*;
import org.interonet.gdm.ConfigurationCenter.ConfigurationCenter;
import org.interonet.gdm.ConfigurationCenter.IConfigurationCenter;
import org.interonet.gdm.Core.Utils.DayTime;
import org.interonet.gdm.Core.Utils.Duration;
import org.interonet.gdm.OperationCenter.IOperationCenter;
import org.interonet.gdm.OperationCenter.OperationCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private IConfigurationCenter configurationCenter;
    private IUserManager userManager;
    private Logger logger = LoggerFactory.getLogger(GDMCore.class);

    public GDMCore() {
    }

    public void start() {
        configurationCenter = new ConfigurationCenter();
        userManager = new UserManager(this);
        wsQueue = new WaitingStartQueue();
        wtQueue = new WaitingTermQueue();
        operationCenter = new OperationCenter(this);
        gdmAgent = new GDMAgent(this);
        wsQueueOperator = new WSQueueManager(this, wsQueue, wtQueue, operationCenter);
        wtQueueOperator = new WTQueueManager(this, wsQueue, wtQueue, operationCenter);
        Thread wsQueueOperatorThread = new Thread(wsQueueOperator);
        wsQueueOperatorThread.start();
        Thread wtQueueOperatorThread = new Thread(wtQueueOperator);
        wtQueueOperatorThread.start();
        authTokenManager = new AuthTokenManager();
        switchTimeTable = new SwitchTimeTable(this);
        vmTimeTable = new VMTimeTable(this);
    }


    public IGDMAgent getAgent() {
        return gdmAgent;
    }

    public IConfigurationCenter getConfigurationCenter() {
        return configurationCenter;
    }

    public Boolean orderSlice(AuthToken authToken, String order) throws Exception {
        try {
            if (!authTokenManager.auth(authToken)) {
                logger.info("authentication error");
                return false;
            }

            OrderParser orderParser = new OrderParser(order);
            int switchesNum = orderParser.getSwitchesNum();
            int vmsNum = orderParser.getvmsNum();
            String beginT = orderParser.getBeginTime();
            String endT = orderParser.getEndTime();
            Map<String, String> topology = orderParser.getTopology();
            Map<String, String> swConf = orderParser.getSwitchConfig();
            String ctrlIP = orderParser.getControllerIP();
            int ctrlPort = orderParser.getControllerPort();
            Map<String, Map> customSwitchConf = orderParser.getCustomSwitchConf();

            logger.debug("[switchesNum]= " + switchesNum);
            logger.debug("[vmsNum]= " + vmsNum);
            logger.debug("[beginT]= " + beginT);
            logger.debug("[endT]= " + endT);
            logger.debug("[topology]= " + topology);
            logger.debug("[swConf]= " + swConf);
            logger.debug("[ctrlIP]= " + ctrlIP);
            logger.debug("[ctrlPort]= " + ctrlPort);
            logger.debug("[customSwitchConf]= " + customSwitchConf);

            Date date = new Date();
            DayTime beginTime = new DayTime(beginT);
            DayTime nowTime = new DayTime(new SimpleDateFormat("HH:mm").format(date));

            if (beginTime.earlyThan(nowTime)) {
                logger.error("begin time is early than now time, failed");
                logger.error("beginTime = " + beginTime);
                logger.error("nowTime = " + nowTime);
                return false;
            }

            String username = authTokenManager.getUsernameByToken(authToken);

            /*
            * FIXME transaction.
            * Bug Tracking Link: https://github.com/samueldeng/interonet/issues/15
            * */
            List<Integer> switchIDs = switchTimeTable.checkSWAvailability(switchesNum, beginT, endT);
            List<Integer> vmIDs = vmTimeTable.checkVMAvailability(vmsNum, beginT, endT);

            if (switchIDs == null) {
                logger.error("switches is not enough now");
                return false;
            }

            if (vmIDs == null) {
                logger.error("vms is not enough now");
                return false;
            }

            boolean swStatus = switchTimeTable.setOccupied(switchIDs, beginT, endT);
            boolean vmStatus = vmTimeTable.setOccupied(vmIDs, beginT, endT);
            if (!swStatus || !vmStatus) return false;
            return wsQueue.newOrder(username, switchIDs, vmIDs, beginT, endT, topology, swConf, ctrlIP, ctrlPort, customSwitchConf);

        } catch (Exception e) {
            logger.error(e.toString());
            throw e;
        }
    }

    public String authenticateUser(String username, String password) {
        if (!(userManager.authUser(username, password))) return null;
        AuthToken authToken = authTokenManager.generate(username, password);
        return AuthTokenManager.toPlainText(authToken);
    }

    public String getSwitchesUsageStatus(AuthToken authToken) throws IOException {
        if (!authTokenManager.auth(authToken)) return null;
        return switchTimeTable.getTimeTable();
    }

    public String getVmsUsageStatus(AuthToken authToken) throws IOException {
        if (!authTokenManager.auth(authToken)) return null;
        return vmTimeTable.getTimeTable();
    }

    public String getOrdersIDList(AuthToken authToken) throws IOException {
        if (!authTokenManager.auth(authToken)) return null;
        String username = authTokenManager.getUsernameByToken(authToken);
        return wsQueue.getOrderIDListByUsername(username);
    }

    public String getOrderInfoByID(AuthToken authToken, String orderID) throws IOException {
        if (!authTokenManager.auth(authToken)) return null;

        String orderInfo = wsQueue.getOrderInfoByID(orderID);

        return orderInfo != null ? orderInfo : "Failed";

    }

    public Boolean deleteOrderByID(AuthToken authToken, String orderID) {
        if (!authTokenManager.auth(authToken)) return null;

        List<WSOrder> queue = wsQueue.wsQueue;
        String beginT = null;
        String endT = null;
        for (WSOrder wsOrder : queue) {
            if (wsOrder.orderID.equals(orderID)) {
                beginT = wsOrder.beginTime;
                endT = wsOrder.endTime;
            }
        }
        for (Map.Entry<Integer, List<Duration>> entry : vmTimeTable.vmTimeTable.entrySet()) {
            List<Duration> found = new ArrayList<>();
            for (Duration d : entry.getValue()) {
                if (d.start.equals(beginT) && d.end.equals(endT)) {
                    found.add(d);
                }
            }
            entry.getValue().removeAll(found);
        }
        for (Map.Entry<Integer, List<Duration>> entry : switchTimeTable.switchTimeTable.entrySet()) {
            List<Duration> found = new ArrayList<>();
            for (Duration d : entry.getValue()) {
                if (d.start.equals(beginT) && d.end.equals(endT)) {
                    found.add(d);
                }
            }
            entry.getValue().removeAll(found);
        }
        return wsQueue.deleteOrderByID(orderID);
    }

    public String getRunningSliceIDsList(AuthToken authToken) throws IOException {
        if (!authTokenManager.auth(authToken)) return null;
        String username = authTokenManager.getUsernameByToken(authToken);
        return wtQueue.getOrderIDListByUsername(username);

    }

    public String getRunningSliceInfoByID(AuthToken authToken, String sliceID) throws IOException {
        if (!authTokenManager.auth(authToken)) return null;

        return wtQueue.getRunningSliceInfoByID(sliceID);
    }

    public IAuthTokenManager getAuthTokenManager() {
        return authTokenManager;
    }

    public String stopRunningSliceByID(AuthToken authToken, String orderID) {
        if (!authTokenManager.auth(authToken)) return null;

        boolean status = wtQueue.deleteOrderByID(orderID);
        return status ? "Success" : "Failed";
    }

}
