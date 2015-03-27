package org.interonet.ldm.SwitchManager;

import java.io.IOException;

public interface ISwitchManager {
    void changeConnectionPropertyFromNFS(Integer switchID, String controllerIP, int controllerPort) throws IOException;
}
