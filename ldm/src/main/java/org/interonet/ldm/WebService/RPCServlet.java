package org.interonet.ldm.WebService;

import com.googlecode.jsonrpc4j.JsonRpcServer;
import org.interonet.ldm.Core.LDMCore;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RPCServlet extends HttpServlet {
    private LDMCore ldmCore;
    private JsonRpcServer jsonRpcServer;

    public RPCServlet(LDMCore ldmCore) {
        this.ldmCore = ldmCore;
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        jsonRpcServer.handle(req, resp);
    }

    @Override
    public void init(ServletConfig config) {
        LDMServiceImpl LDMServiceImpl = new LDMServiceImpl(ldmCore);
        jsonRpcServer = new JsonRpcServer(LDMServiceImpl, LDMServiceImpl.class);
    }

}
