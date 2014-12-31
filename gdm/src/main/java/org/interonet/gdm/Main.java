package org.interonet.gdm;

import org.interonet.gdm.Core.GDMAgent;
import org.interonet.gdm.Core.GDMCore;
import org.interonet.gdm.Core.IGDMCore;
import org.interonet.gdm.WebService.IRPCServer;
import org.interonet.gdm.WebService.RPCServer;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting InterONet GDM System");
        IGDMCore gdmCore = new GDMCore();
        gdmCore.start();

        System.out.println("Starting InterONet GDM RPC Server");
        GDMAgent gdmAgent = gdmCore.getAgent();
        IRPCServer gdmRPCServer = new RPCServer(gdmAgent);
        gdmRPCServer.start();
    }
}
