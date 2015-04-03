package org.interonet.gdm.TestWebService;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import junit.framework.TestCase;
import org.interonet.gdm.MainTest;

import java.net.URL;

public class TestOrderSlice extends TestCase{
    JsonRpcHttpClient client;

    public TestOrderSlice() throws Exception {
        client = new JsonRpcHttpClient(new URL("http://202.117.15.79:8080/"));
        MainTest main = new MainTest();
        main.testStart();
        Thread.sleep(2000);
    }

    public void testOrderSlice0() throws Exception, Throwable {
        String token = client.invoke("authenticateUser", new Object[]{"root", "root"}, String.class);
        String order1 = "{"
                + "  \"num\": {"
                + "      \"switchesNum\": \"4\","
                + "      \"vmsNum\": \"4\""
                + "  },"
                + "  \"time\": {"
                + "      \"begin\": \"12:00\","
                + "      \"end\" : \"21:00\""
                + "  },"
                + "  \"topology\": {"
                + "      \"s0:0\": \"s2:2\","
                + "      \"s0:1\": \"s3:2\","
                + "      \"s1:0\": \"s2:3\","
                + "      \"s1:1\": \"s3:3\","
                + "      \"h0:0\": \"s2:0\","
                + "      \"h1:0\": \"s2:1\","
                + "      \"h2:0\": \"s3:0\","
                + "      \"h3:0\": \"s3:1\""
                + "  },"
                + "  \"switchConf\": {"
                + "      \"s0\": \"OF1.0\","
                + "      \"s1\": \"OF1.0\","
                + "      \"s2\": \"OF1.0\","
                + "      \"s3\" : \"custom\""
                + "  },"
                + "  \"controllerConf\": {"
                + "      \"ip\": \"10.0.0.253\","
                + "      \"port\" : \"6633\""
                + "  }"
                + "}";
        String orderingStatus = client.invoke("orderSlice", new Object[]{token, order1}, String.class);
        assertTrue(orderingStatus.equals("Success"));
    }

    public void testOrderSlice1() throws Exception, Throwable {
        String token = client.invoke("authenticateUser", new Object[]{"root", "root"}, String.class);
        String order1 = "{"
                + "  \"num\": {"
                + "      \"switchesNum\": \"4\","
                + "      \"vmsNum\": \"4\""
                + "  },"
                + "  \"time\": {"
                + "      \"begin\": \"12:00\","
                + "      \"end\" : \"21:00\""
                + "  },"
                + "  \"topology\": {"
                + "      \"s0:1\": \"s1:0\","
                + "      \"s1:2\": \"s2:2\","
                + "      \"s2:2\": \"s3:0\","
                + "      \"h0:0\": \"s0:1\","
                + "      \"h1:0\": \"s1:1\","
                + "      \"h2:0\": \"s2:1\","
                + "      \"h3:0\": \"s3:1\""
                + "  },"
                + "  \"switchConf\": {"
                + "      \"s0\": \"OF1.0\","
                + "      \"s1\": \"OF1.0\","
                + "      \"s2\": \"OF1.0\","
                + "      \"s3\" : \"custom\""
                + "  },"
                + "  \"controllerConf\": {"
                + "      \"ip\": \"10.0.0.253\","
                + "      \"port\" : \"6633\""
                + "  }"
                + "}";
        String orderingStatus = client.invoke("orderSlice", new Object[]{token, order1}, String.class);
        assertTrue(orderingStatus.equals("Success"));
    }
}
