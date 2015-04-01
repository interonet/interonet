package org.interonet.gdm;

import org.interonet.gdm.Core.GDMCore;
import org.interonet.gdm.Core.IGDMAgent;
import org.interonet.gdm.WebService.RPCServer;

import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        if (System.getenv().get("INTERONET_HOME") == null){
            System.out.println("INTERONET_HOME Environment Variable Check Error.");
            System.exit(1);
        }
        Logger gdmMainLogger = Logger.getLogger("GDMMainLogger");
        gdmMainLogger.info("Starting InterONet GDM System");
        GDMCore gdmCore = new GDMCore();
        gdmCore.start();

        gdmMainLogger.info("Starting InterONet GDM RPC Server");
        IGDMAgent gdmAgent = gdmCore.getAgent();
        RPCServer gdmRPCServer = new RPCServer(gdmAgent);
        gdmRPCServer.start();
    }
}
