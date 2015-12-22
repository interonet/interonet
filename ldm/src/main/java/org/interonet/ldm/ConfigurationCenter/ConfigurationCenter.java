package org.interonet.ldm.ConfigurationCenter;

import org.interonet.ldm.Core.LDMCore;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationCenter implements IConfigurationCenter {
    private Map<String, String> swId2NFSRootDirMapping;
    private Map<String, String> swId2IpAddressMapping;
    private String libvirtConnectURL = "qemu+tcp://400@192.168.2.3/system";

    public ConfigurationCenter(LDMCore ldmCore) {
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

    @Override
    public String getLibvirtConnectURL(){
        return libvirtConnectURL;
    }

    @Override
    public Map<String, String> getSwitchId2NFSRootDirectoryMapping() {
        return swId2NFSRootDirMapping;
    }

    @Override
    public Map<String, String> getSwitchId2AddressMapping() {
        return swId2IpAddressMapping;
    }
}
