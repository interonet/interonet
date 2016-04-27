package org.interonet.gdm.service;

public interface GDMService {
    /**
     * Get token string via username and password. token is a number of abs(uuid.least64bits).str+abs(uuid.most64bits).str.
     * <p>
     * NOTICE: the token will be refresh in several period of time.
     * Default is 1 Mins. System Operator can modify it via /gdm/conf.json#tokenUpdatePeriod
     * </p>
     *
     * @param username username string, not null.
     * @param password password string, not null.
     * @return digits string if success else return "Failed".
     */
    String getToken(String username, String password);

    /**
     * Get the switch timetable.
     *
     * @param authToken token for user.
     * @return json string of timetable, Failed, Exception.
     */
    String getSwitchTimeTable(String authToken);

    /**
     * Get the vm timetable.
     *
     * @param authToken token for user.
     * @return json string of vm timetable, Failed, Exception.
     */
    String getVMTimeTable(String authToken);

    /**
     * Try to submit a slice to gdm.
     *
     * @param authToken token for user.
     * @param slice     slice json string
     * @return sliceId, Failed, Exception.
     */
    String submitSlice(String authToken, String slice);

    /**
     * Try to terminate slice.
     * <p>
     * suitable for TIME_WAITING,RUNNABLE,RUNNING in {@link org.interonet.gdm.Core.Slice.SliceStatus}
     * </p>
     *
     * @param authToken token for user.
     * @param sliceId   sliceId string.
     * @return Success, Failed, Exception
     */
    String tryToTerminateSlice(String authToken, String sliceId);

    /**
     * Get slice pool of the user.
     *
     * @param authToken token.
     * @return Success, Failed, Exception.
     */
    String getSlicePool(String authToken);

    /**
     * Get slice of the sliceId.
     *
     * @param auToken token for user.
     * @param sliceId sliceId, uuid format.
     * @return slice json string else Failed, Exception.
     */
    String getSlice(String auToken, String sliceId);
}
