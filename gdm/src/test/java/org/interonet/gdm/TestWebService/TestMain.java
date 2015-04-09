package org.interonet.gdm.TestWebService;

import junit.framework.TestCase;
import org.interonet.gdm.Core.GDMCore;
import org.interonet.gdm.Core.IGDMAgent;
import org.interonet.gdm.WebService.RPCServer;
import org.junit.Assert;

public class TestMain extends TestCase {
    public void testStart() throws Exception {
        assertTrue(System.getenv().get("INTERONET_HOME") != null);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                GDMCore gdmCore = new GDMCore();
                gdmCore.start();
                IGDMAgent gdmAgent = gdmCore.getAgent();
                RPCServer gdmRPCServer = new RPCServer(gdmAgent);
                gdmRPCServer.start();
                System.out.println("**************************DONE****************************");
            }
        });
        thread.start();
        Thread.sleep(5000);
    }
}