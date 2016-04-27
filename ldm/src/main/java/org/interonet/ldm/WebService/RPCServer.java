package org.interonet.ldm.WebService;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.interonet.ldm.Core.LDMCore;

public class RPCServer {
    private LDMCore ldmCore;
    private Server rpcServer;

    public RPCServer(LDMCore ldmCore) {
        this.ldmCore = ldmCore;
    }

    public void start() {
        try {
            rpcServer = new Server(8081);
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            context.setMaxFormContentSize(100000000); //100MB
            rpcServer.setHandler(context);
            context.addServlet(new ServletHolder(new RPCServlet(ldmCore)), "/*");

            rpcServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        try {
            rpcServer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
