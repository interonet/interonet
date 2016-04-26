package org.interonet.gdm;

import org.interonet.gdm.Core.GDMCore;
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
        RPCServer gdmRPCServer = new RPCServer(gdmCore);
        gdmRPCServer.start();
    }
}
