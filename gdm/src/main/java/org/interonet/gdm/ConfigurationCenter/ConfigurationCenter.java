package org.interonet.gdm.ConfigurationCenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.interonet.gdm.Core.GDMCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationCenter implements IConfigurationCenter {
    private GDMCore core;
    public Map<String, String> globalConfiguration = new HashMap<>();

    public Map<String, Integer> TTPortMap = new HashMap<>();
    Map<String, Map<String, String>> vmDB = new HashMap<>();
    Map<String, Map<String, String>> switchDB = new HashMap<>();

    private ObjectMapper objectMapper = new ObjectMapper();
    private Logger logger = LoggerFactory.getLogger(ConfigurationCenter.class);

    public ConfigurationCenter() {
        if (System.getenv().get("INTERONET_HOME") == null) {
            System.out.println("INTERONET_HOME Environment Variable Check Error.");
            System.exit(1);
        }
        initGlobalConf();
        initTopologyTransformerPortMap();
        initSwitchDB();
        initVMDB();
    }

    private void initVMDB() {
        logger.info("reading vmDB file from " + globalConfiguration.get("vmDB"));
        File vmDBFile = new File(globalConfiguration.get("vmDB"));

        try {
            vmDB = objectMapper.readValue(vmDBFile, Map.class);
        } catch (Exception e) {
            logger.error("initSwitchDB Error", e);
        }
    }

    private void initSwitchDB() {
        logger.info("reading vmDB file from " + globalConfiguration.get("switchDB"));
        File switchDBFile = new File(globalConfiguration.get("switchDB"));

        try {
            switchDB = objectMapper.readValue(switchDBFile, Map.class);
        } catch (Exception e) {
            logger.error("initSwitchDB Error", e);
        }
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

    private void initTopologyTransformerPortMap() {
        logger.info("reading TTProtMap file from " + globalConfiguration.get("TTPortMapDBTTPortMapDB"));
        File confFile = new File(globalConfiguration.get("TTPortMapDBTTPortMapDB"));

        try {
            Map<String, Integer> map = objectMapper.readValue(confFile, Map.class);
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                TTPortMap.put(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            logger.error("initSwitchDB Error", e);
        }
    }

    @Override
    public int getTopologyTransformerPortFromPeerPort(int switchID, int switchIDPortNum) throws Exception {
        String key;
        int value = 0;
        for (Map.Entry<String, Integer> entry : TTPortMap.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            if (Integer.parseInt(key.split(":")[0].substring(1, 2)) == switchID && Integer.parseInt(key.split(":")[1]) == switchIDPortNum) {
                return value;
            }
        }

        throw new Exception("Can not find this port<SwitchID:" + switchID + " SwitchIDPortNum:" + switchIDPortNum + "> on TT");
    }

    @Override
    public String getConf(String key) {
        return globalConfiguration.get(key);
    }

    @Override
    public String getSwitchUrlById(Integer domSwitchId) throws Exception {
        Map<String, String> switchInfo = switchDB.get(domSwitchId.toString());
        if (switchInfo == null) throw new Exception("Can not find the switch's URL." + " domSwitchId = " + domSwitchId);
        String switchUrl = switchInfo.get("url");
        return switchUrl == null ? "N/A" : switchUrl;
    }

    @Override
    public String getVMUrlById(Integer domVMId) throws Exception {
        Map<String, String> VMInfo = vmDB.get(domVMId.toString());
        if (VMInfo == null) throw new Exception("Can not find the vm's URL." + " domVMId = " + domVMId);
        String vmUrl = VMInfo.get("url");
        return vmUrl == null ? "N/A" : vmUrl;
    }
}
