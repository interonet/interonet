package org.interonet.gdm;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting InterONet System");
        GDMCore gdmCore = new GDMCoreImpl();
        gdmCore.start();

        System.out.println("Starting InterONet RPC Server");
        GDMAgent gdmAgent = gdmCore.getAgent();
        RPCServer gdmRPCServer = new RPCServerImpl(gdmAgent);
        gdmRPCServer.start();
        System.out.println("End");
    }
}
