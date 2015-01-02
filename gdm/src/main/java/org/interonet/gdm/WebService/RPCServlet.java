package org.interonet.gdm.WebService;

import com.googlecode.jsonrpc4j.JsonRpcServer;
import org.interonet.gdm.TestCore.IGDMAgent;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RPCServlet extends HttpServlet {
    private RPCService rpcService;
    private JsonRpcServer jsonRpcServer;
    private IGDMAgent gdmAgent;

    public RPCServlet(IGDMAgent gdmAgent) {
        this.gdmAgent = gdmAgent;
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        jsonRpcServer.handle(req, resp);
    }

    @Override
    public void init(ServletConfig config) {
        IRPCService rpcService = new RPCService(gdmAgent);
        jsonRpcServer = new JsonRpcServer(rpcService, IRPCService.class);
    }
}
