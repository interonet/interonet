package org.interonet.gdm.ConfigurationCenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.interonet.gdm.Core.GDMCore;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationCenter implements IConfigurationCenter {
    public Map<String, Integer> TTPortMap = new HashMap<>();
    public Map<String, String> globalConfiguration = new HashMap<>();
    private GDMCore core;
    private Logger logger = LoggerFactory.getLogger(ConfigurationCenter.class);

    public ConfigurationCenter() {
        if (System.getenv().get("INTERONET_HOME") == null){
            System.out.println("INTERONET_HOME Environment Variable Check Error.");
            System.exit(1);
        }
        initGlobalConf();
        initTopologyTransformerPortMap();
    }

    private void initGlobalConf(){
        logger.info("reading conf file from" + System.getenv().get("INTERONET_HOME") + "/gdm/conf/conf.json");
        File confFile = new File(System.getenv().get("INTERONET_HOME") + "/gdm/conf/conf.json");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, String> map = objectMapper.readValue(confFile, Map.class);
            for(Map.Entry<String,String> entry : map.entrySet()){
                globalConfiguration.put(entry.getKey(), entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTopologyTransformerPortMap() {
        logger.info("reading TTProtMap file from" + globalConfiguration.get("TTPortMapDBTTPortMapDB"));
        File confFile = new File(globalConfiguration.get("TTPortMapDBTTPortMapDB"));

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Integer> map = objectMapper.readValue(confFile, Map.class);
            for(Map.Entry<String,Integer> entry : map.entrySet()){
                TTPortMap.put(entry.getKey(), entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
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
}
