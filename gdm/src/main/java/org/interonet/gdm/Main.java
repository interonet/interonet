package org.interonet.gdm;

import org.interonet.gdm.TestCore.GDMCore;
import org.interonet.gdm.TestCore.IGDMAgent;
import org.interonet.gdm.TestCore.IGDMCore;
import org.interonet.gdm.WebService.IRPCServer;
import org.interonet.gdm.WebService.RPCServer;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting InterONet GDM System");
        IGDMCore gdmCore = new GDMCore();
        gdmCore.start();

        System.out.println("Starting InterONet GDM RPC Server");
        IGDMAgent gdmAgent = gdmCore.getAgent();
        IRPCServer gdmRPCServer = new RPCServer(gdmAgent);
        gdmRPCServer.start();
    }
}
