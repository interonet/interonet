package org.interonet.gdm;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting InterONet GDM System");
        IGDMCore gdmCore = new GDMCore();
        gdmCore.start();

        System.out.println("Starting InterONet GDM RPC Server");
        GDMAgent gdmAgent = gdmCore.getAgent();
        IRPCServer gdmRPCServer = new RPCServer(gdmAgent);
        gdmRPCServer.start();
        System.out.println("End");
    }
}
