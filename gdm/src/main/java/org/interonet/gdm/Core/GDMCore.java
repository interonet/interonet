package org.interonet.gdm.Core;

import org.interonet.gdm.AuthenticationCenter.AuthTokenManager;
import org.interonet.gdm.ConfigurationCenter.ConfigurationCenter;
import org.interonet.gdm.Core.DateTime.SliceDuration;
import org.interonet.gdm.Core.FuturePool.LDMTaskRunningFuturePool;
import org.interonet.gdm.Core.FuturePool.LDMTaskTerminatedFuturePool;
import org.interonet.gdm.Core.Runner.*;
import org.interonet.gdm.Core.SlicePool.*;
import org.interonet.gdm.LDMConnector.LDMConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GDMCore {
    private ConfigurationCenter configurationCenter = new ConfigurationCenter();

    private LDMConnector ldmConnector;
    private AuthTokenManager authTokenManager;

    /*
    * timetable of resources like sdn switch and virtual machine.
    * */
    private TimeTable timeTable;

    /*
    * store different state's slice.
    * */
    private SlicePool slicePool;
    private TimeWaitingSlicePool timeWaitingSlicePool;
    private RunnableSlicePool runnableSlicePool;
    private RunningWaitingSlicePool runningWaitingSlicePool;
    private RunningSlicePool runningSlicePool;
    private TerminatableSlicePool terminatableSlicePool;
    private TerminatedWaitingPool terminatedWaitingPool;
    private TerminatedSlicePool terminatedSlicePool;

    /*
    * store the future task of LDMStartCall and LDMStopCall
    * */
    private LDMTaskRunningFuturePool ldmTaskRunningFuturePool = new LDMTaskRunningFuturePool();
    private LDMTaskTerminatedFuturePool ldmTaskTerminatedFuturePool = new LDMTaskTerminatedFuturePool();

    /*
    * thread to check the pool and do the process for the slice.
    * */
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private SliceBeginTimeChecker sliceBeginTimeChecker;
    private SliceRunner sliceRunner;
    private SliceRunningChecker sliceRunningChecker;
    private SliceEndTimeChecker sliceEndTimeChecker;
    private SliceTerminator sliceTerminator;
    private SliceTerminatedChecker sliceTerminatedChecker;

    private Logger logger = LoggerFactory.getLogger(GDMCore.class);


    public GDMCore() {
        try {
            int totalSwitchesNumber = Integer.parseInt(configurationCenter.getConf("SwitchesNumber"));
            int totalVmsNumber = Integer.parseInt(configurationCenter.getConf("VMsNumber"));
            URL url = new URL(configurationCenter.getConf("LDMConnectionURL"));
            boolean debug = configurationCenter.getConf("gdmDebug").equals("true");

            logger.debug("totalSwitchesNumber: " + totalSwitchesNumber);
            logger.debug("totalVmsNumber: " + totalVmsNumber);
            logger.debug("LDMConnectionURL: " + url);
            logger.debug("gdmDebug: " + debug);

            int timeout = Integer.parseInt(configurationCenter.getConf("LDMConnectionReadTimeoutMillis"));
            timeTable = new TimeTable(totalSwitchesNumber, totalVmsNumber);
            ldmConnector = new LDMConnector(url, timeout, debug);

            String userDB = configurationCenter.getConf("userDB");
            logger.debug("userDB: " + userDB);
            String tokenUpdatePeriodStr = configurationCenter.getConf("tokenUpdatePeriod");
            logger.debug("tokenUpdatePeriod: " + tokenUpdatePeriodStr + " Min");
            if (tokenUpdatePeriodStr != null) {
                Integer tokenUpdatePeriod = Integer.parseInt(tokenUpdatePeriodStr);
                authTokenManager = new AuthTokenManager(tokenUpdatePeriod, userDB);
            } else {
                authTokenManager = new AuthTokenManager(userDB);
            }

        } catch (MalformedURLException e) {
            logger.error("LDMConnectionURL ERROR", e);
        } catch (IOException e) {
            logger.error("IO ERROR for reading from userDB");
        }

        sliceBeginTimeChecker = new SliceBeginTimeChecker();
        sliceRunner = new SliceRunner(ldmConnector);
        sliceRunningChecker = new SliceRunningChecker();
        sliceEndTimeChecker = new SliceEndTimeChecker();
        sliceTerminator = new SliceTerminator(ldmConnector);
        sliceTerminatedChecker = new SliceTerminatedChecker();


        slicePool = new SlicePool();
        SlicePoolFactory slicePoolFactory = new SlicePoolFactory();
        timeWaitingSlicePool = (TimeWaitingSlicePool) slicePoolFactory.getSlicePool("TimeWaitingSlicePool");
        runnableSlicePool = (RunnableSlicePool) slicePoolFactory.getSlicePool("RunnableSlicePool");
        runningWaitingSlicePool = (RunningWaitingSlicePool) slicePoolFactory.getSlicePool("RunningWaitingSlicePool");
        runningSlicePool = (RunningSlicePool) slicePoolFactory.getSlicePool("RunningSlicePool");
        terminatableSlicePool = (TerminatableSlicePool) slicePoolFactory.getSlicePool("TerminatableSlicePool");
        terminatedWaitingPool = (TerminatedWaitingPool) slicePoolFactory.getSlicePool("TerminatedWaitingPool");
        terminatedSlicePool = (TerminatedSlicePool) slicePoolFactory.getSlicePool("TerminatedSlicePool");

        sliceBeginTimeChecker.setTimeWaitingSlicePool(timeWaitingSlicePool);
        sliceBeginTimeChecker.setRunnableSlicePool(runnableSlicePool);

        sliceRunner.setRunnableSlicePool(runnableSlicePool);
        sliceRunner.setRunningWaitingSlicePool(runningWaitingSlicePool);
        sliceRunner.setLdmTaskRunningFuturePool(ldmTaskRunningFuturePool);

        sliceRunningChecker.setLDMTaskRunningFuturePool(ldmTaskRunningFuturePool);
        sliceRunningChecker.setRunningWaitingSlicePool(runningWaitingSlicePool);
        sliceRunningChecker.setRunningSlicePool(runningSlicePool);

        sliceEndTimeChecker.setRunningSlicePool(runningSlicePool);
        sliceEndTimeChecker.setTerminatableSlicePool(terminatableSlicePool);

        sliceTerminator.setTerminatableSlicePool(terminatableSlicePool);
        sliceTerminator.setTerminatedWaitingSlicePool(terminatedWaitingPool);
        sliceTerminator.setLdmTaskTerminatedFuturePool(ldmTaskTerminatedFuturePool);

        sliceTerminatedChecker.setLDMTaskTerminatedFuturePool(ldmTaskTerminatedFuturePool);
        sliceTerminatedChecker.setTerminatedWaitingSlicePool(terminatedWaitingPool);
        sliceTerminatedChecker.setTerminatedSlicePool(terminatedSlicePool);
    }

    public void start() {
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(6);
        scheduledThreadPoolExecutor.setThreadFactory(new RunnerFactory());

        scheduledThreadPoolExecutor.scheduleAtFixedRate(sliceBeginTimeChecker, 0, 1, TimeUnit.SECONDS);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(sliceRunner, 0, 1, TimeUnit.SECONDS);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(sliceRunningChecker, 0, 1, TimeUnit.SECONDS);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(sliceEndTimeChecker, 0, 1, TimeUnit.SECONDS);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(sliceTerminator, 0, 1, TimeUnit.SECONDS);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(sliceTerminatedChecker, 0, 1, TimeUnit.SECONDS);
    }

    public boolean authToken(String token) {
        return authTokenManager.isTokenExisted(token);
    }

    public String getToken(String username, String password) {
        if (username == null || password == null) return null;
        return authTokenManager.getTokenByUserPassword(username, password);
    }

    /**
     * GDMService Function:
     * <p>
     * Try to submit a slice string to {@link TimeWaitingSlicePool}.
     * </p>
     *
     * @param authToken token for user.
     * @param sliceStr  json string which contains the order.
     * @return sliceId digit number. maybe null.
     * @throws Exception
     */
    public String submitSlice(String authToken, String sliceStr) throws Exception {
        if (authToken == null) return null;
        if (sliceStr == null) return null;
        try {
            if (!authTokenManager.isTokenExisted(authToken)) {
                logger.info("authentication failed");
                throw new Exception("authToken = [" + authToken + "], sliceStr = [" + sliceStr + "]");
            }
            Slice slice = Slice.SliceParser.parse(sliceStr);
            slice.setUsername(authTokenManager.getUserByToken(authToken).getUsername());
            slice.setStatus(Slice.SliceStatus.NEW);
            return submitSlice(slice);

        } catch (Exception e) {
            logger.error("Submit Slice Error", e);
            throw e;
        }
    }

    /**
     * GDMService Function:
     * <p>
     * Get User slice pool.
     * </p>
     *
     * @param authToken token for user.
     * @return slice list for the user, null if auth failed.
     */
    public List<Slice> getUserSlicePool(String authToken) {
        if (authToken == null) return null;
        if (!authTokenManager.isTokenExisted(authToken)) return null;
        // TODO: 4/25/16 it should be some bug if the token refresh when happen here.
        AuthTokenManager.User user = authTokenManager.getUserByToken(authToken);
        if (user == null) return null;
        return getUserSlicePool(user);
    }

    /**
     * GDMService Function:
     * <p>
     * Try to terminate the slice if it is in {@link TimeWaitingSlicePool},
     * {@link RunnableSlicePool} or {@link RunningSlicePool}.
     * </p>
     *
     * @param authToken the token for user.
     * @param sliceId   the slice id, not null
     * @return success if terminate the slice else failed.
     */
    public boolean tryToTerminateSlice(String authToken, String sliceId) {
        if (authToken == null || sliceId == null) return false;
        return authTokenManager.isTokenExisted(authToken) && tryToTerminateSlice(sliceId);
    }

    /**
     * GDMService Function:
     * <p>
     * Get the switch time table of whole system.
     * </p>
     *
     * @param authToken string of the token of user, not null.
     * @return switch time table, null if authToken is null
     * @throws IOException
     */
    public Map<Integer, TreeSet<SliceDuration>> getSwitchTimeTable(String authToken) throws IOException {
        if (authToken == null) return null;
        if (!authTokenManager.isTokenExisted(authToken)) return null;
        return timeTable.getSwitchTimeTableSnapShot();
    }

    /**
     * GDMService Function:
     * <p>
     * Get the vm time table of whole system.
     * </p>
     *
     * @param authToken string of the token of user, not null.
     * @return vm time table, null if authToken is null
     * @throws IOException
     */
    public Map<Integer, TreeSet<SliceDuration>> getVMTimeTable(String authToken) throws IOException {
        if (authToken == null) return null;
        if (!authTokenManager.isTokenExisted(authToken)) return null;
        return timeTable.getVmTimeTableSnapShot();
    }


    /**
     * Try to submit a slice to {@link TimeWaitingSlicePool}.
     *
     * @param slice slice bean to submit.
     * @return sliceId, null if resource is not enough.
     * @throws Exception
     */
    public String submitSlice(Slice slice) throws Exception {
        try {
            logger.debug(slice.toString());

            int switchesNum = slice.getSwitchesNum();
            int vmsNum = slice.getVmsNum();
            Duration TimePeriod = slice.getTimePeriod();
            ZonedDateTime beginTime = slice.getBeginTime();
            ZonedDateTime endTime = slice.getEndTime();
            String sliceId = UUID.randomUUID().toString();
            slice.setId(sliceId);

            Map<String, Integer> resourceRequest = new HashMap<>(2);
            resourceRequest.put("switch", switchesNum);
            resourceRequest.put("vm", vmsNum);
            Map<String, List<Integer>> reservedResources = timeTable.tryToReserve(sliceId, resourceRequest, beginTime, endTime,TimePeriod);

            List<Integer> reservedSwitch = reservedResources.get("switch");
            List<Integer> reservedVM = reservedResources.get("vm");

            if (reservedSwitch.size() != switchesNum || reservedVM.size() != vmsNum) return null;

            slice.setSwitchIdList(reservedSwitch);
            slice.setSwitchList(configurationCenter.getSwitchList(reservedSwitch));
            slice.setVmIdList(reservedVM);
            slice.setVmList(configurationCenter.getVMList(reservedVM));
            slice.setStatus(Slice.SliceStatus.TIME_WAITING);

            slicePool.submit(slice);
            timeWaitingSlicePool.submit(slice);

            return sliceId;

        } catch (Exception e) {
            logger.error("Submit Slice Error", e);
            throw e;
        }
    }

    /**
     * Try to terminate the slice if it is in {@link TimeWaitingSlicePool},
     * {@link RunnableSlicePool} or {@link RunningSlicePool}.
     *
     * @param sliceId the slice id, not null
     * @return success if terminate the slice else failed.
     */
    public boolean tryToTerminateSlice(String sliceId) {
        if (sliceId == null) return false;
        Slice slice = slicePool.consumeBySliceId(sliceId);
        if (slice != null) {
            Slice.SliceStatus status = slice.getStatus();
            /*
            * move the slice to terminatedPool if it's in the TIME_WAITING & RUNNABLE pool
            * */
            if (status == Slice.SliceStatus.TIME_WAITING || status == Slice.SliceStatus.RUNNABLE) {
                ZonedDateTime now = ZonedDateTime.now(slice.getBeginTime().getZone());
                slice.setEndTime(now);
                slice.setStatus(Slice.SliceStatus.TERMINATED);
                slice.setException(Slice.SliceException.USER_OPERATION);
                terminatedSlicePool.submit(slice);
                return true;
            }
            /*
            * Setting to endTime early to let the sliceEndTimerChecker to kill it.
            * set the delay for 30 seconds to end time.
            * */
            if (status == Slice.SliceStatus.RUNNING) {
                ZonedDateTime now = ZonedDateTime.now(slice.getBeginTime().getZone());
                slice.setEndTime(now.plusSeconds(30));
                return true;
            }
            return false;
        }
        return false;
    }

    public Map<Integer, TreeSet<SliceDuration>> getSwitchTimeTable() throws IOException {
        return timeTable.getSwitchTimeTableSnapShot();
    }

    public Map<Integer, TreeSet<SliceDuration>> getVMTimeTable() throws IOException {
        return timeTable.getVmTimeTableSnapShot();
    }

    public List<Slice> getUserSlicePool(AuthTokenManager.User user) {
        String username = user.getUsername();
        List<Slice> userSliceList = new ArrayList<>();
        for (Slice slice : getGDMSlicePool("SlicePool")) {
            if (slice.getUsername().equals(username)) {
                userSliceList.add(slice);
            }
        }
        return userSliceList;
    }

    public List<Slice> getGDMSlicePool(String slicePoolType) {
        if (slicePoolType == null) {
            return new ArrayList<>();
        }
        if (slicePoolType.equalsIgnoreCase("SlicePool")) {
            return slicePool.getSlicePoolSnapShot();
        } else if (slicePoolType.equalsIgnoreCase("TimeWaitingSlicePool")) {
            return timeWaitingSlicePool.getSlicePoolSnapShot();
        } else if (slicePoolType.equalsIgnoreCase("RunnableSlicePool")) {
            return runnableSlicePool.getSlicePoolSnapShot();
        } else if (slicePoolType.equalsIgnoreCase("RunningWaitingSlicePool")) {
            return runningWaitingSlicePool.getSlicePoolSnapShot();
        } else if (slicePoolType.equalsIgnoreCase("RunningSlicePool")) {
            return runningSlicePool.getSlicePoolSnapShot();
        } else if (slicePoolType.equalsIgnoreCase("TerminatableSlicePool")) {
            return terminatableSlicePool.getSlicePoolSnapShot();
        } else if (slicePoolType.equalsIgnoreCase("TerminatedWaitingPool")) {
            return terminatedWaitingPool.getSlicePoolSnapShot();
        } else if (slicePoolType.equalsIgnoreCase("TerminatedSlicePool")) {
            return terminatedSlicePool.getSlicePoolSnapShot();
        }
        return slicePool.getSlicePoolSnapShot();
    }

    public Slice getUserSlice(String auToken, String sliceId) {
        if (auToken == null || sliceId == null) return null;
        if (authTokenManager.isTokenExisted(auToken)) {
            AuthTokenManager.User user = authTokenManager.getUserByToken(auToken);
            return getUserSlice(user, sliceId);
        }
        return null;
    }

    public Slice getUserSlice(AuthTokenManager.User user, String sliceId) {
        String username = user.getUsername();
        for (Slice slice : getGDMSlicePool("SlicePool")) {
            if (slice.getUsername().equals(username) && slice.getId().equals(sliceId)) {
                return slice;
            }
        }
        return null;
    }
}
