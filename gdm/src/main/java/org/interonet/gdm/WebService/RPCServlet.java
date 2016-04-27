package org.interonet.gdm.WebService;

import com.googlecode.jsonrpc4j.JsonRpcServer;
import org.interonet.gdm.Core.GDMCore;
import org.interonet.gdm.service.GDMService;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RPCServlet extends HttpServlet {
    private GDMService gdmService;
    private JsonRpcServer jsonRpcServer;
    private GDMCore gdmCore;

    public RPCServlet(GDMCore gdmCore) {
        this.gdmCore = gdmCore;
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        jsonRpcServer.handle(req, resp);
    }

    @Override
    public void init(ServletConfig config) {
        gdmService = new GDMServiceImpl(gdmCore);
        jsonRpcServer = new JsonRpcServer(gdmService);
    }
}
