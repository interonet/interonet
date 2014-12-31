package org.interonet.ldm;

import org.interonet.ldm.Agent.RPCServer;

public class Main
{
    public static void main( String[] args )
    {
        System.out.println( "Starting InterONet LDM" );
        LDMCore ldmCore = new LDMCore();
        ldmCore.start();

        System.out.println("Starting InterONet LDM RPC Server");
        LDMAgent ldmAgent = ldmCore.getAgent();
        RPCServer ldmRPCServer = new RPCServer(ldmAgent);
        ldmRPCServer.start();
        System.out.println("End");
    }
}
