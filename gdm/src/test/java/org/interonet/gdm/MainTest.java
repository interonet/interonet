package org.interonet.gdm;

import junit.framework.TestCase;
import org.interonet.gdm.Core.GDMCore;
import org.interonet.gdm.Core.IGDMAgent;
import org.interonet.gdm.WebService.RPCServer;

public class MainTest extends TestCase {
    public void testStart() throws Exception {
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
    }
}