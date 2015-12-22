package org.interonet.gdm.ConfigurationCenter;

public interface IConfigurationCenter {
    int getTopologyTransformerPortFromPeerPort(int switchID, int switchIDPortNum) throws Exception;

    String getConf(String key);

    String getSwitchUrlById(Integer domSwitchId) throws Exception;

    String getVMUrlById(Integer domVMId) throws Exception;
}
