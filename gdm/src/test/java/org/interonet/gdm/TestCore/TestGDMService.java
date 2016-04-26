package org.interonet.gdm.TestCore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.googlecode.jsonrpc4j.ProxyUtil;
import org.apache.commons.io.IOUtils;
import org.interonet.gdm.Core.GDMCore;
import org.interonet.gdm.WebService.GDMService;
import org.interonet.gdm.WebService.RPCServer;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestGDMService {
    public static GDMService gdmService;
    Logger logger = LoggerFactory.getLogger(TestGDMService.class);

    @BeforeClass
    public static void startServer() throws Exception {
        JsonRpcHttpClient jsonRpcHttpClient = new JsonRpcHttpClient(new URL("http://localhost:8080/"));
        gdmService = ProxyUtil.createClientProxy(ClassLoader.getSystemClassLoader(), GDMService.class, jsonRpcHttpClient);
        GDMCore gdmCore = new GDMCore();
        gdmCore.start();

        RPCServer gdmRPCServer = new RPCServer(gdmCore);
        gdmRPCServer.start();
        Thread.sleep(3000);
    }

    @Test
    public void testSubmit2ExampleSlice() throws Throwable {
        String sliceStr = null;
        try {
            sliceStr = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("testSliceTemplate.json"));
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> order = mapper.readValue(sliceStr, Map.class);
            logger.info("order" + order);
            Map<String, String> time = (Map<String, String>) order.get("time");
            time.put("begin", ZonedDateTime.now().plusSeconds(3).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
            time.put("end", ZonedDateTime.now().plusSeconds(6).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

            String newOrder = mapper.writeValueAsString(order);
            logger.info("newOrder" + newOrder);

            String token = gdmService.getToken("root", "root");
            logger.debug("token: " + token);
            assertFalse(token.equals("Failed"));
            if (!token.equals("Failed")) {
                String sliceId1 = gdmService.submitSlice(token, newOrder);
                logger.debug("sliceId1: " + sliceId1);
                assertTrue(!sliceId1.equals("Failed"));
                assertTrue(!sliceId1.equals("Exception"));
                logger.info(gdmService.getSwitchTimeTable(token));
                logger.info(gdmService.getVMTimeTable(token));
                String sliceId2 = gdmService.submitSlice(token, newOrder);
                logger.debug("sliceId2: " + sliceId2);
                assertTrue(!sliceId2.equals("Failed"));
                assertTrue(!sliceId2.equals("Exception"));
                logger.info("SwitchTimeTable: " + gdmService.getSwitchTimeTable(token));
                logger.info("VMTimeTable: " + gdmService.getVMTimeTable(token));

                String slices = gdmService.getSlicePool(token);
                logger.info(slices);
            }
        } catch (Throwable throwable) {
            logger.info("", throwable);
            assertTrue(false);
        }
        printSlicePool(7, 200);
    }

    @Test
    public void testSubmitAndStopSlice() throws Throwable {
        try {
            String sliceStr = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("testSliceTemplate.json"));
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> order = mapper.readValue(sliceStr, Map.class);
            logger.info("order" + order);
            Map<String, String> time = (Map<String, String>) order.get("time");
            time.put("begin", ZonedDateTime.now().plusSeconds(3).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
            time.put("end", ZonedDateTime.now().plusMinutes(20).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

            String newOrder = mapper.writeValueAsString(order);
            logger.info("newOrder" + newOrder);


            String token = gdmService.getToken("root", "root");

            logger.debug("token: " + token);
            //assertFalse(token.equals("Failed"));
            String sliceId = null;
            if (!token.equals("Failed")) {
                sliceId = gdmService.submitSlice(token, newOrder);
                logger.info(gdmService.getSwitchTimeTable(token));
                logger.info(gdmService.getVMTimeTable(token));
                logger.debug("sliceId: " + sliceId);
                assertTrue(!sliceId.equals("Failed"));
                assertTrue(!sliceId.equals("Exception"));
            }

            //Wait for 10 seconds.
            printSlicePool(10, 200);

            //Kill the slice
            String isOk = gdmService.tryToTerminateSlice(token, sliceId);
            logger.info("tryToTerminateSlice=" + isOk);
            printSlicePool(35, 200);
        } catch (Throwable throwable) {
            logger.info("", throwable);
            assertTrue(false);
        }


    }

    @Test
    public void testGetSlice() throws Throwable {
        String sliceStr = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("testSliceTemplate.json"));
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> order = mapper.readValue(sliceStr, Map.class);
        logger.info("order" + order);
        Map<String, String> time = (Map<String, String>) order.get("time");
        time.put("begin", ZonedDateTime.now().plusSeconds(3).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        time.put("end", ZonedDateTime.now().plusSeconds(10).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

        String newOrder = mapper.writeValueAsString(order);
        logger.info("newOrder" + newOrder);


        String token = gdmService.getToken("root", "root");
        logger.debug("token: " + token);
        //assertFalse(token.equals("Failed"));
        String sliceId = null;
        if (!token.equals("Failed")) {
            sliceId = gdmService.submitSlice(token, newOrder);
            logger.debug("sliceId: " + sliceId);
            assertTrue(!sliceId.equals("Failed"));
            assertTrue(!sliceId.equals("Exception"));
            String slice = gdmService.getSlice(token, sliceId);
            logger.info(slice);
            assertTrue(!slice.equals("Failed"));
            assertTrue(!slice.equals("Exception"));
        }
    }

    private void printSlicePool(int seconds, int freq) throws Throwable {
        for (int counter = 0; counter < seconds * 1000 / freq; counter++) {
            Thread.sleep(freq);
            String token = gdmService.getToken("root", "root");
            logger.debug("token: " + token);
            assertFalse(token.equals("Failed"));
            if (!token.equals("Failed")) {
                String slices = gdmService.getSlicePool(token);
                logger.info(slices);
            }
        }

    }
}
