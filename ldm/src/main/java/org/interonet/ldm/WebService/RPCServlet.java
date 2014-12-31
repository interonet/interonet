package org.interonet.ldm.WebService;

import com.googlecode.jsonrpc4j.JsonRpcServer;
import org.interonet.ldm.LDMAgent;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RPCServlet extends HttpServlet {
    private LDMAgent ldmAgent;
    private JsonRpcServer jsonRpcServer;

    public RPCServlet(LDMAgent ldmAgent) {
        this.ldmAgent = ldmAgent;
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        jsonRpcServer.handle(req, resp);
    }

    @Override
    public void init(ServletConfig config) {
        RPCService rpcService = new RPCService(ldmAgent);
        jsonRpcServer = new JsonRpcServer(rpcService, RPCService.class);
    }

}
