package org.interonet.ldm.SwitchManager;

import java.io.IOException;
import java.util.Map;

public interface ISwitchManager {
    void resetSwitchConf(Integer switchID) throws InterruptedException, IOException;

    void changeSwitchConf(String type, Integer switchID, String controllerIP, int controllerPort) throws InterruptedException, IOException;

    void changeSwitchConf(Map<String, String> customSwitchConfGDM, Integer switchID, String controllerIP, int controllerPort) throws Exception;
}
