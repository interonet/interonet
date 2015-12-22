package org.interonet.gdm;

import org.interonet.gdm.Core.GDMCore;
import org.interonet.gdm.Core.IGDMAgent;
import org.interonet.gdm.WebService.RPCServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    public static void main(String[] args) {

        Logger gdmMainLogger = LoggerFactory.getLogger(Main.class);
        gdmMainLogger.info("Starting InterONet GDM System");
        GDMCore gdmCore = new GDMCore();
        gdmCore.start();

        gdmMainLogger.info("Starting InterONet GDM RPC Server");
        IGDMAgent gdmAgent = gdmCore.getAgent();
        RPCServer gdmRPCServer = new RPCServer(gdmAgent);
        gdmRPCServer.start();
    }
}
