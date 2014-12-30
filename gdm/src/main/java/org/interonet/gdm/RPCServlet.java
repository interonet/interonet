package org.interonet.gdm;

import com.googlecode.jsonrpc4j.JsonRpcServer;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RPCServlet extends HttpServlet {
    private RPCServiceImpl rpcServiceImpl;
    private JsonRpcServer jsonRpcServer;
    private GDMAgent gdmAgent;

    public RPCServlet(GDMAgent gdmAgent) {
        this.gdmAgent = gdmAgent;
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        jsonRpcServer.handle(req, resp);
    }

    @Override
    public void init(ServletConfig config) {
        RPCService rpcService = new RPCServiceImpl(gdmAgent);
        jsonRpcServer = new JsonRpcServer(rpcService, RPCService.class);
    }
}
