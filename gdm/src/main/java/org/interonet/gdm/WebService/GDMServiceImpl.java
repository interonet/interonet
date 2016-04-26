package org.interonet.gdm.WebService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.interonet.gdm.Core.DateTime.SliceDuration;
import org.interonet.gdm.Core.GDMCore;
import org.interonet.gdm.Core.Slice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class GDMServiceImpl implements GDMService {
    private GDMCore gdmCore;
    private Logger logger = LoggerFactory.getLogger(GDMServiceImpl.class);

    public GDMServiceImpl(GDMCore gdmCore) {
        this.gdmCore = gdmCore;
    }

    /**
     * Get token string via username and password.
     *
     * @param username username string, not null.
     * @param password password string, not null.
     * @return digits string if success else return "Failed".
     */
    @Override
    public String getToken(String username, String password) {
        logger.debug("username = [" + username + "], password = [" + password + "]");
        String token = gdmCore.getToken(username, password);
        return token != null ? token : "Failed";
    }


    /**
     * Get the switch timetable.
     *
     * @param authToken token for user.
     * @return json string of timetable, Failed, Exception.
     */
    @Override
    public String getSwitchTimeTable(String authToken) {
        logger.debug("authToken = [" + authToken + "]");
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            Map<Integer, TreeSet<SliceDuration>> switchTimeTable = gdmCore.getSwitchTimeTable(authToken);
            String status = mapper.writeValueAsString(switchTimeTable);
            return status != null ? status : "Failed";
        } catch (IOException e) {
            logger.error("getSwitchTimeTable", e);
            return "Exception";
        }
    }


    /**
     * Get the vm timetable.
     *
     * @param authToken token for user.
     * @return json string of vm timetable, Failed, Exception.
     */
    @Override
    public String getVMTimeTable(String authToken) {
        logger.debug("authToken = [" + authToken + "]");
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            Map<Integer, TreeSet<SliceDuration>> vmTimeTable = gdmCore.getVMTimeTable(authToken);
            String status = mapper.writeValueAsString(vmTimeTable);
            return status != null ? status : "Failed";
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("getVMTimeTable", e);
            return "Exception";
        }
    }


    /**
     * Try to submit a slice to gdm.
     *
     * @param authToken token for user.
     * @param slice     slice json string
     * @return sliceId, Failed, Exception.
     */
    @Override
    public String submitSlice(String authToken, String slice) {
        logger.debug("authToken = [" + authToken + "], slice = [" + slice + "]");
        try {
            String sliceId = gdmCore.submitSlice(authToken, slice);
            if (sliceId != null)
                return sliceId;
            else
                return "Failed";
        } catch (Exception e) {
            logger.error("submitSlice", e);
            return "Exception";
        }
    }

    /**
     * try to terminate slice.
     *
     * @param authToken token for user.
     * @param sliceId   sliceId string.
     * @return Success, Failed, Exception
     */
    @Override
    public String tryToTerminateSlice(String authToken, String sliceId) {
        logger.debug("authToken = [" + authToken + "], sliceId = [" + sliceId + "]");
        try {
            if (gdmCore.tryToTerminateSlice(authToken, sliceId))
                return "Success";
            else
                return "Failed";
        } catch (Exception e) {
            logger.error("submitSlice", e);
            return "Exception";
        }

    }

    /**
     * Get slice pool of the user.
     *
     * @param authToken token.
     * @return Success, Failed, Exception.
     */
    @Override
    public String getSlicePool(String authToken) {
        logger.debug("authToken = [" + authToken + "]");
        try {
            List<Slice> slicePool = gdmCore.getUserSlicePool(authToken);
            if (slicePool == null) return "Failed";
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.writeValueAsString(slicePool);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return "Exception";
        }
    }

    /**
     * Get slice of the sliceId.
     *
     * @param auToken token for user.
     * @param sliceId sliceId, uuid format.
     * @return slice json string else Failed, Exception.
     */
    @Override
    public String getSlice(String auToken, String sliceId) {
        logger.debug("auToken = [" + auToken + "], sliceId = [" + sliceId + "]");
        try {
            Slice slice = gdmCore.getUserSlice(auToken, sliceId);
            if (slice == null) return "Failed";
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.writeValueAsString(slice);
        } catch (Exception e) {
            logger.error("getSlice", e);
            return "Exception";
        }
    }
}
