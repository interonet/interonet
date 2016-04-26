package org.interonet.gdm.ConfigurationCenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.interonet.gdm.Core.Switch;
import org.interonet.gdm.Core.VirtualMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurationCenter {
    private Map<String, String> globalConfiguration = new HashMap<>();

    private Map<Integer, VirtualMachine> vmDB = new HashMap<>();
    private Map<Integer, Switch> switchDB = new HashMap<>();

    private ObjectMapper objectMapper = new ObjectMapper();
    private Logger logger = LoggerFactory.getLogger(ConfigurationCenter.class);

    public ConfigurationCenter() {
        if (System.getenv().get("INTERONET_HOME") == null) {
            System.out.println("INTERONET_HOME Environment Variable Check Error.");
            System.exit(1);
        }
        initGlobalConf();
        initSwitchDB();
        initVMDB();
    }

    private void initGlobalConf() {
        logger.info("reading conf file from " + System.getenv().get("INTERONET_HOME") + "/gdm/conf/conf.json");
        File confFile = new File(System.getenv().get("INTERONET_HOME") + "/gdm/conf/conf.json");
        try {
            Map<String, String> map = objectMapper.readValue(confFile, Map.class);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                globalConfiguration.put(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            logger.error("initSwitchDB Error", e);
        }
    }

    private void initSwitchDB() {
        logger.info("reading swDB file from " + globalConfiguration.get("switchDB"));
        File switchDBFile = new File(globalConfiguration.get("switchDB"));
        try {
            Map<String, Map<String, String>> switches = objectMapper.readValue(switchDBFile, Map.class);
            for (Map.Entry<String, Map<String, String>> entry : switches.entrySet()) {
                Integer id = Integer.parseInt(entry.getKey());
                Map<String, String> s = entry.getValue();
                String name = s.get("name");
                String url = s.get("url");
                switchDB.put(id, new Switch(id, name, url));
            }
        } catch (Exception e) {
            logger.error("initSwitchDB Error", e);
        }
    }

    private void initVMDB() {
        logger.info("reading vmDB file from " + globalConfiguration.get("vmDB"));
        File vmDBFile = new File(globalConfiguration.get("vmDB"));

        try {
            Map<String, Map<String, String>> vms = objectMapper.readValue(vmDBFile, Map.class);
            for (Map.Entry<String, Map<String, String>> entry : vms.entrySet()) {
                Integer id = Integer.parseInt(entry.getKey());
                Map<String, String> s = entry.getValue();
                String name = s.get("name");
                String url = s.get("url");
                vmDB.put(id, new VirtualMachine(id, name, url));
            }
        } catch (Exception e) {
            logger.error("initSwitchDB Error", e);
        }
    }

    public String getConf(String key) {
        return globalConfiguration.get(key);
    }

    public List<Switch> getSwitchList(List<Integer> idList) {
        List<Switch> list = new ArrayList<>(idList.size());
        for (Integer id : idList) {
            list.add(switchDB.get(id));
        }
        return list;
    }

    public List<VirtualMachine> getVMList(List<Integer> idList) {
        List<VirtualMachine> list = new ArrayList<>(idList.size());
        for (Integer id : idList) {
            list.add(vmDB.get(id));
        }
        return list;
    }


    public Switch getSwitchById(Integer domSwitchId) throws Exception {
        if (domSwitchId == null) return null;
        return switchDB.get(domSwitchId);
    }

    public VirtualMachine getVMById(Integer domVMId) throws Exception {
        if (domVMId == null) return null;
        return vmDB.get(domVMId);
    }
}
