package org.interonet.gdm.WebService;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.interonet.gdm.Core.GDMCore;

public class RPCServer {
    private GDMCore gdmCore;
    private Server rpcServer;

    public RPCServer(GDMCore gdmCore) {
        this.gdmCore = gdmCore;
    }

    public void start() {
        try {
            rpcServer = new Server(8080);
            //rpcServer = new Server(new InetSocketAddress("127.0.0.1",8080));

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setMaxFormContentSize(100000000);
            context.setContextPath("/");
            rpcServer.setHandler(context);

            context.addServlet(new ServletHolder(new RPCServlet(gdmCore)), "/*");
            rpcServer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        try {
            rpcServer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
