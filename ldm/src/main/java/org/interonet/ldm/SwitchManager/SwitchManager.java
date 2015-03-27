package org.interonet.ldm.SwitchManager;

import org.interonet.ldm.ConfigurationCenter.IConfigurationCenter;
import org.interonet.ldm.Core.LDMCore;

import java.io.IOException;
import java.util.Map;

public class SwitchManager implements ISwitchManager {
    private NFSManager nfsManager;
    private BootImageManager bootImageManager;
    private FPGAManager fpgaManager;
    private IConfigurationCenter configurationCenter;

    public SwitchManager(LDMCore ldmCore) {
        configurationCenter = ldmCore.getConfigurationCenter();
        nfsManager = new NFSManager();
        bootImageManager = new BootImageManager();
        fpgaManager = new FPGAManager();
    }

    @Override
    public void changeConnectionPropertyFromNFS(Integer switchID, String controllerIP, int controllerPort) throws IOException {
        Map<String, String> nfsMapping = configurationCenter.getSwitchId2NFSRootDirectoryMapping();
        String nfsRootPath = nfsMapping.get(switchID.toString());
        // nfsRootPath equals like /export/0
        nfsManager.changeConnecitonPropertyFromNFS(switchID, nfsRootPath, controllerIP, controllerPort);
    }
}
