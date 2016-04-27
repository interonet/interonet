package org.interonet.ldm;

import org.interonet.ldm.Core.LDMCore;
import org.interonet.ldm.WebService.RPCServer;

import java.util.logging.Logger;

public class Main
{
    public static void main( String[] args )  {
        if(!System.getProperty("user.name").equals("root")) {
            System.out.println("Root Permission Check Failed.");
            System.exit(1);
        }
        if (System.getenv().get("INTERONET_HOME") == null){
            System.out.println("INTERONET_HOME Environment Variable Check Error.");
            System.exit(1);
        }
        Logger ldmMainLogger = Logger.getLogger("ldmMainLogger");
        ldmMainLogger.info( "Starting InterONet LDM" );
        LDMCore ldmCore = new LDMCore();
        ldmCore.start();

        ldmMainLogger.info("Starting InterONet LDM RPC Server");
        RPCServer ldmRPCServer = new RPCServer(ldmCore);
        ldmRPCServer.start();
    }
}
