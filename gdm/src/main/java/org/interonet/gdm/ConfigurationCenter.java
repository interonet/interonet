package org.interonet.gdm;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationCenter {
    // configuration center. saving the mapping info from topology transformer port to the peer port, eg, switch port, or, vm port.
    public Map<String, Integer> topologyTransformer;

    public ConfigurationCenter() {
        topologyTransformer = new HashMap<String, Integer>();
        topologyTransformer.put("s0:0", 1);
        topologyTransformer.put("s0:1", 2);
        topologyTransformer.put("s0:2", 3);
        topologyTransformer.put("s0:3", 4);
        topologyTransformer.put("s1:0", 5);
        topologyTransformer.put("s1:1", 6);
        topologyTransformer.put("s1:2", 7);
        topologyTransformer.put("s1:3", 8);
        topologyTransformer.put("s2:0", 9);
        topologyTransformer.put("s2:1", 10);
        topologyTransformer.put("s2:2", 11);
        topologyTransformer.put("s2:3", 12);
        topologyTransformer.put("s3:0", 13);
        topologyTransformer.put("s3:1", 14);
        topologyTransformer.put("s3:2", 15);
        topologyTransformer.put("s3:3", 16);
    }

    public int getTopologyTransformerPortFromPeerPort(int switchID, int switchIDPortNum) {
        String key;
        int value = 0;
        for (Map.Entry<String, Integer> entry : topologyTransformer.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            if (Integer.parseInt(key.split(":")[0].substring(1, 2)) == switchID && Integer.parseInt(key.split(":")[1]) == switchIDPortNum)
                break;
        }
        return value;
    }
}
