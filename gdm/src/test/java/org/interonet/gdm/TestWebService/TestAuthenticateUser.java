package org.interonet.gdm.TestWebService;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import junit.framework.TestCase;

import java.net.URL;

public class TestAuthenticateUser extends TestCase {
    JsonRpcHttpClient client;

    public TestAuthenticateUser() throws Exception {
        client = new JsonRpcHttpClient(new URL("http://127.0.0.1:8080/"));
    }

    public void testAuthenticateUser() throws Throwable {
        String token;
        token = client.invoke("authenticateUser", new Object[]{"root", "root"}, String.class);
        assertTrue(!token.equals("Failed"));

        token = client.invoke("authenticateUser", new Object[]{"admin", "admin"}, String.class);
        assertTrue(!token.equals("Failed"));

        token = client.invoke("authenticateUser", new Object[]{"test1", "test1"}, String.class);
        assertTrue(!token.equals("Failed"));

        token = client.invoke("authenticateUser", new Object[]{"test2", "test2"}, String.class);
        assertTrue(!token.equals("Failed"));

        token = client.invoke("authenticateUser", new Object[]{"test2", "test1"}, String.class);
        assertTrue(token.equals("Failed"));
    }
}
