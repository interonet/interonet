package org.interonet.ldm.ConfigurationCenter;

import java.util.Map;

public interface IConfigurationCenter {
    Map<String, String> getSwitchId2NFSRootDirectoryMapping();

    Map<String, String> getSwitchId2AddressMapping();
    String getLibvirtConnectURL();
}
