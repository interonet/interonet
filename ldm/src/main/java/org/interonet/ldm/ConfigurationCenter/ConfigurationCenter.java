package org.interonet.ldm.ConfigurationCenter;

import org.interonet.ldm.Core.LDMCore;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationCenter implements IConfigurationCenter {
    private Map<String, String> swId2NFSRootDirMapping;

    public ConfigurationCenter(LDMCore ldmCore) {
        swId2NFSRootDirMapping = new HashMap<>();
        swId2NFSRootDirMapping.put("0", "/export/0");
        swId2NFSRootDirMapping.put("1", "/export/1");
        swId2NFSRootDirMapping.put("2", "/export/2");
        swId2NFSRootDirMapping.put("3", "/export/3");
    }

    @Override
    public Map<String, String> getSwitchId2NFSRootDirectoryMapping() {
        return swId2NFSRootDirMapping;
    }
}
