package org.interonet.gdm.Core;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class OrderParser {
    //TODO check the parameter.
    Map<String, Map<String, String>> parser;

    public OrderParser(String order) throws Exception {
        parser = new ObjectMapper().readValue(order, Map.class);
        validate();
    }

    private void validate() throws Exception {
        String beginT = getBeginTime();
        String endT = getEndTime();

        if (! beginT.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]"))
            throw new Exception("order format wrong: beginTime");
        if (! endT.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]"))
            throw new Exception("order format wrong: endTime");

        try {
            int switchesNum = getSwitchesNum();
            int vmsNum = getvmsNum();
        } catch (Exception e){
            throw e;
        }

        //TODO validate the topology and switch configuration.
        Map<String, String> topology = getTopology();
        Map<String, String> swConf = getSwitchConfig();

        String ctrlIP = getControllerIP();
        String match = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        if (!ctrlIP.matches(match)) {
            throw new Exception("order format wrong: controller ip");
        }
        try {
            int ctrlPort = getControllerPort();
            if (ctrlPort > 65535){
                throw new Exception("order format wrong: controller port");
            }
        } catch (Exception e){
            throw e;
        }
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