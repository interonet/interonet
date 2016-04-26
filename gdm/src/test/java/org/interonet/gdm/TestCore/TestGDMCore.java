package org.interonet.gdm.TestCore;

import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.interonet.gdm.Core.GDMCore;
import org.interonet.gdm.Core.Slice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class TestGDMCore extends TestCase {
    public static final long SLICE_BEGIN_TIME_DELAY = 2;
    public static final long SLICE_END_TIME_DELAY = 20;

    public static final int SYSTEM_INIT_DELAY = 2 * 1000;
    public static final long BEGIN_TIME_SCHEDULE_DELAY = 8 * 1000;

    GDMCore gdmCore = new GDMCore();

    Logger logger = LoggerFactory.getLogger(TestGDMCore.class);

    public void testSliceLifeCycle() throws MalformedURLException, ExecutionException, InterruptedException {
        Thread thread = new Thread() {
            @Override
            public void run() {
                gdmCore.start();
            }
        };
        thread.start();

        Thread.sleep(SYSTEM_INIT_DELAY);

        /*
        * New Thread to submit a slice request.
        * */
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String sliceStr = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("testSliceTemplate.json"));
                Slice slice = Slice.SliceParser.parse(sliceStr);
                slice.setBeginTime(ZonedDateTime.now().plusSeconds(SLICE_BEGIN_TIME_DELAY));
                slice.setEndTime(ZonedDateTime.now().plusSeconds(SLICE_END_TIME_DELAY));
                slice.setUsername("UnitTestSamuel");
                return gdmCore.submitSlice(slice);
            }
        });
        new Thread(futureTask).start();


        /*
        * Wait for the slice to running.
        * */
        Thread.sleep(SLICE_BEGIN_TIME_DELAY * 1000 + BEGIN_TIME_SCHEDULE_DELAY);
        String sliceId = futureTask.get();
        assertTrue(sliceId != null);
        logger.info("sliceId=" + sliceId);
        List<Slice> runningSlicePool = gdmCore.getGDMSlicePool("RunningSlicePool");
        boolean hasFoundInRunningPool = false;
        for (Slice slice : runningSlicePool) {
            if (slice.getId().equals(sliceId)) {
                hasFoundInRunningPool = true;
                logger.debug(slice.toString());
                break;
            }
        }
        assertTrue(hasFoundInRunningPool);

        Thread.sleep(SLICE_END_TIME_DELAY * 1000);
        List<Slice> terminatedSlicePool = gdmCore.getGDMSlicePool("TerminatedSlicePool");
        boolean hasFoundInTerminatedPool = false;
        for (Slice slice : terminatedSlicePool) {
            if (slice.getId().equals(sliceId)) {
                hasFoundInTerminatedPool = true;
                logger.debug(slice.toString());
                break;
            }
        }
        assertTrue(hasFoundInTerminatedPool);
    }

    public void testThreeSliceLifeCycle() throws Exception {
        /* new thread to start gdmCore.*/
        Thread gdmCoreThread = new Thread() {
            @Override
            public void run() {
                gdmCore.start();
            }
        };
        gdmCoreThread.start();

        /*Main thread to submit a slice to gdmCore.*/
        Thread.sleep(SYSTEM_INIT_DELAY);

        /*Slice 1*/
        String sliceStr1;
        sliceStr1 = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("testSliceTemplate.json"));
        Slice slice = Slice.SliceParser.parse(sliceStr1);
        slice.setBeginTime(ZonedDateTime.now().plusSeconds(3));
        slice.setEndTime(ZonedDateTime.now().plusSeconds(30));
        slice.setUsername("UnitTestSamuel");
        String sliceId1 = gdmCore.submitSlice(slice);

        /*Slice 2*/
        String sliceStr2 = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("testSliceTemplate.json"));
        Slice slice2 = Slice.SliceParser.parse(sliceStr2);
        slice2.setBeginTime(ZonedDateTime.now().plusSeconds(20));
        slice2.setEndTime(ZonedDateTime.now().plusSeconds(40));
        slice2.setUsername("UnitTestSamuel");
        String sliceId2 = gdmCore.submitSlice(slice2);

        /*Slice 3*/
        String sliceStr3 = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("testSliceTemplate.json"));
        Slice slice3 = Slice.SliceParser.parse(sliceStr3);
        slice3.setBeginTime(ZonedDateTime.now().plusSeconds(20));
        slice3.setEndTime(ZonedDateTime.now().plusSeconds(40));
        slice3.setUsername("UnitTestSamuel");
        String sliceId3 = gdmCore.submitSlice(slice3);

        logger.info("AFTER 3 SLICE SUBMITED: " + gdmCore.getGDMSlicePool("SlicePool").toString());
        assertTrue(sliceId1 != null);
        assertTrue(sliceId2 != null);
        assertTrue(sliceId3 != null);

        logger.info("SwitchTimeTable: " + gdmCore.getSwitchTimeTable().toString());
        logger.info("VMTimeTable: " + gdmCore.getVMTimeTable().toString());

        Thread.sleep(60000);
        logger.info("AFTER 60 SEC SLEEP: " + gdmCore.getGDMSlicePool("SlicePool").toString());
        boolean isEverySliceTerminatedNormal = true;

        for (Slice s : gdmCore.getGDMSlicePool("SlicePool")) {
            if (s.getStatus() != Slice.SliceStatus.TERMINATED) isEverySliceTerminatedNormal = false;
            if (s.getException() != Slice.SliceException.NONE) isEverySliceTerminatedNormal = false;
        }
        assertTrue(isEverySliceTerminatedNormal);
    }

    public void testAuthUser() throws Exception {
        String token1 = gdmCore.getToken("root", "root");
        logger.info("token1: " + token1);
        assertTrue(gdmCore.authToken(token1));

        String token2 = gdmCore.getToken("admin", "admin");
        logger.info("token2: " + token2);
        assertTrue(gdmCore.authToken(token2));

        /*
        * Wait for 1 min to wait for token refresh.
        * */
        Thread.sleep(1 * 60 * 1000);
        String token3 = gdmCore.getToken("root", "root");
        logger.info("token3: " + token3);
        assertTrue(gdmCore.authToken(token3));
        assertFalse(token1.equals(token3));

        String token4 = gdmCore.getToken("admin", "admin");
        logger.info("token4: " + token4);
        assertTrue(gdmCore.authToken(token4));
        assertFalse(token2.equals(token4));
    }
}
