package org.interonet.gdm.ConfigurationCenter;

public interface IConfigurationCenter {
    int getTopologyTransformerPortFromPeerPort(int switchID, int switchIDPortNum) throws Exception;
    public String getConf(String key);
}
