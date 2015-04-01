package org.interonet.ldm.WebService;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.interonet.ldm.Core.LDMAgent;

public class RPCServer {
    private LDMAgent ldmAgent;
    private Server rpcServer;

    public RPCServer(LDMAgent ldmAgent) {
        this.ldmAgent = ldmAgent;
    }

    public void start() {
        try {
            rpcServer = new Server(8081);
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            rpcServer.setHandler(context);
            context.addServlet(new ServletHolder(new RPCServlet(ldmAgent)), "/*");

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
