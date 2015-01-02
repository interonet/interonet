package org.interonet.gdm.WebService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.interonet.gdm.AuthenticationCenter.AuthToken;
import org.interonet.gdm.AuthenticationCenter.AuthTokenManager;
import org.interonet.gdm.AuthenticationCenter.IAuthTokenManager;
import org.interonet.gdm.TestCore.IGDMAgent;

import java.io.IOException;
import java.util.Map;

public class RPCService implements IRPCService {
    private IGDMAgent gdmagent;
    private IAuthTokenManager authTokenManager;

    public RPCService(IGDMAgent gdmAgent) {
        this.gdmagent = gdmAgent;
        this.authTokenManager = gdmAgent.getAuthTokenManager();

    }

    @Override
    public String authenticateUser(String username, String password) {
        AuthToken authToken = gdmagent.authenticateUser(username, password);
        if (authToken != null) {
            return AuthTokenManager.toPlainText(authToken);
        } else {
            return "wrong username or password";
        }
    }

    @Override
    public String getSwitchesUsageStatus(String authToken) {
        return gdmagent.getSwitchesUsageStatus(authTokenManager.toAuthToken(authToken));
    }

    @Override
    public String getVMsUsageStatus(String authToken) {
        return gdmagent.getVmsUsageStatus(authTokenManager.toAuthToken(authToken));
    }

    @Override
    public String orderSlice(String authToken, String order) {
        try {
            OrderParser orderParser = new OrderParser(order);
            int switchesNum = orderParser.getSwitchesNum();
            int vmsNum = orderParser.getvmsNum();
            String beginT = orderParser.getBeginTime();
            String endT = orderParser.getEndTime();
            Map<String, String> topology = orderParser.getTopology();
            Map<String, String> swConf = orderParser.getSwitchConfig();
            String ctrlIP = orderParser.getControllerIP();
            int ctrlPort = orderParser.getControllerPort();

            AuthToken authTk = authTokenManager.toAuthToken(authToken);

            Boolean status = gdmagent.orderSlice(authTk, switchesNum, vmsNum, beginT, endT, topology, swConf, ctrlIP, ctrlPort);
            return status ? "Success" : "Failed";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getOrdersList(String authToken) {
        return gdmagent.getOrdersList(authTokenManager.toAuthToken(authToken));
    }

    @Override
    public String getOrderInfoByID(String authToken, String orderID) {
        return gdmagent.getOrderInfoByID(authTokenManager.toAuthToken(authToken), orderID);
    }

    @Override
    public String deleteOrderByID(String authToken, String orderID) {
        return gdmagent.deleteOrderByID(authTokenManager.toAuthToken(authToken), orderID);
    }

    @Override
    public String getRunningSlices(String authToken) {
        return gdmagent.getRunningSlice(authTokenManager.toAuthToken(authToken));
    }

//    @Override
//    public String stopRunningSliceByID(String authToken, String orderID) {
//        return gdmagent.stopRunningSliceByID(authTokenManager.toAuthToken(authToken), orderID);
//    }

    public class OrderParser {
        Map<String, Map<String, String>> parser;

        public OrderParser(String order) throws IOException {
            parser = new ObjectMapper().readValue(order, Map.class);
        }

        public int getSwitchesNum() {
            Map<String, String> num = parser.get("num");
            String switchNum = num.get("switchesNum");
            return Integer.parseInt(switchNum);
        }

        public int getvmsNum() {
            Map<String, String> num = parser.get("num");
            String vmsNum = num.get("vmsNum");
            return Integer.parseInt(vmsNum);
        }

        public String getBeginTime() {
            Map<String, String> time = parser.get("time");
            return time.get("begin");
        }

        public String getEndTime() {
            Map<String, String> time = parser.get("time");
            return time.get("end");
        }

        public Map<String, String> getTopology() {
            Map<String, String> topology = parser.get("topology");
            /*for (Map.Entry<String, String> entry : topology.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
            }*/
            return topology;
        }

        public Map<String, String> getSwitchConfig() {
            Map<String, String> switchConf = parser.get("switchConf");
            /*for (Map.Entry<String, String> entry : switchConf.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
            }*/
            return switchConf;
        }

        public String getControllerIP() {
            Map<String, String> controllerConf = parser.get("controllerConf");
            return controllerConf.get("ip");
        }

        public int getControllerPort() {
            Map<String, String> controllerConf = parser.get("controllerConf");
            String port = controllerConf.get("port");
            return Integer.parseInt(port);
        }
    }
}
