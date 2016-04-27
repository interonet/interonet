package org.interonet.ldm.ConfigurationCenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.interonet.ldm.Core.LDMCore;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ConfigurationCenter {
    private Map<String, String> globalConfiguration = new HashMap<>();


    private Map<String, Integer> TTPortMap = new HashMap<>();
    private Map<String, Map<String, String>> vmDB = new HashMap<>();
    private Map<String, Map<String, String>> switchDB = new HashMap<>();
    private Map<String, Integer> vmVlanIdMap = new HashMap<>();


    private Map<String, String> swId2NFSRootDirMapping;
    private Map<String, String> swId2IpAddressMapping;
    private String libvirtConnectURL = "qemu+tcp://400@192.168.2.3/system";

    private ObjectMapper objectMapper = new ObjectMapper();
    private Logger logger = Logger.getLogger(ConfigurationCenter.class.getCanonicalName());

    public ConfigurationCenter(LDMCore ldmCore) {
        if (System.getenv().get("INTERONET_HOME") == null) {
            System.out.println("INTERONET_HOME Environment Variable Check Error.");
            System.exit(1);
        }

        initGlobalConf();
        initTopologyTransformerPortMap();
        initVmVlanId();
        initSwitchDB();
        initVMDB();

        swId2NFSRootDirMapping = new HashMap<>();
        swId2NFSRootDirMapping.put("0", "/export/0");
        swId2NFSRootDirMapping.put("1", "/export/1");
        swId2NFSRootDirMapping.put("2", "/export/2");
        swId2NFSRootDirMapping.put("3", "/export/3");

        swId2IpAddressMapping = new HashMap<>();
        swId2IpAddressMapping.put("0", "10.0.0.3");
        swId2IpAddressMapping.put("1", "10.0.0.4");
        swId2IpAddressMapping.put("2", "10.0.0.5");
        swId2IpAddressMapping.put("3", "10.0.0.6");
    }

    private void initVmVlanId() {
        logger.info("reading vmVlanIdMap file from " + globalConfiguration.get("vmVlanIdDB"));
        File confFile = new File(globalConfiguration.get("vmVlanIdDB"));
        try {
            Map<String, Integer> map = objectMapper.readValue(confFile, Map.class);
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                vmVlanIdMap.put(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            logger.severe("init VmVlanId Error");
        }
    }

    private void initGlobalConf() {
        logger.info("reading conf file from " + System.getenv().get("INTERONET_HOME") + "/ldm/conf/conf.json");
        File confFile = new File(System.getenv().get("INTERONET_HOME") + "/ldm/conf/conf.json");
        try {
            Map<String, String> map = objectMapper.readValue(confFile, Map.class);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                globalConfiguration.put(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            logger.severe("initSwitchDB Error");
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
            logger.severe("init TTProtMap Error");
        }
    }

    private void initVMDB() {
        logger.info("reading vmDB file from " + globalConfiguration.get("vmDB"));
        File vmDBFile = new File(globalConfiguration.get("vmDB"));

        try {
            vmDB = objectMapper.readValue(vmDBFile, Map.class);
        } catch (Exception e) {
            logger.severe("initSwitchDB Error");
        }
    }

    private void initSwitchDB() {
        logger.info("reading vmDB file from " + globalConfiguration.get("switchDB"));
        File switchDBFile = new File(globalConfiguration.get("switchDB"));

        try {
            switchDB = objectMapper.readValue(switchDBFile, Map.class);
        } catch (Exception e) {
            logger.severe("initSwitchDB Error");
        }
    }


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

    public String getLibvirtConnectURL(){
        return libvirtConnectURL;
    }

    public Map<String, String> getSwitchId2NFSRootDirectoryMapping() {
        return swId2NFSRootDirMapping;
    }

    public Map<String, String> getSwitchId2AddressMapping() {
        return swId2IpAddressMapping;
    }

    public int getVlanIdByVM(int vmId, int vmPort) throws Exception {
        String key;
        int value = 0;
        for (Map.Entry<String, Integer> entry : vmVlanIdMap.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            if (Integer.parseInt(key.split(":")[0].substring(1, 2)) == vmId && Integer.parseInt(key.split(":")[1]) == vmPort) {
                return value;
            }
        }
        throw new Exception("Can not find this port<vmId:" + vmId + " vmPort:" + vmPort + "> on DB");
    }
}
