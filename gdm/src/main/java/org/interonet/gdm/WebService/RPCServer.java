package org.interonet.gdm.WebService;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.interonet.gdm.Core.GDMAgent;

public class RPCServer implements IRPCServer {
    private GDMAgent gdmAgent;
    private Server rpcServer;

    public RPCServer(GDMAgent gdmAgent) {
        this.gdmAgent = gdmAgent;
    }

    @Override
    public void start() {
        try {
            rpcServer = new Server(8080);
            //rpcServer = new Server(new InetSocketAddress("202.117.15.79",8080));

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            rpcServer.setHandler(context);

            context.addServlet(new ServletHolder(new RPCServlet(gdmAgent)), "/*");
            rpcServer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {
        try {
            rpcServer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
