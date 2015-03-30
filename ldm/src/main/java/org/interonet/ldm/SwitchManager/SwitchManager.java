package org.interonet.ldm.SwitchManager;

import org.interonet.ldm.ConfigurationCenter.IConfigurationCenter;
import org.interonet.ldm.Core.LDMCore;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

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
    public void changeConnectionPropertyFromNFS(Integer switchID, String controllerIP, int controllerPort) throws IOException, InterruptedException {
        Map<String, String> nfsMapping = configurationCenter.getSwitchId2NFSRootDirectoryMapping();
        String nfsRootPath = nfsMapping.get(switchID.toString());
        // nfsRootPath equals like /export/0
        //TODO move to nfsManager
        Process process2Copy = Runtime.getRuntime().exec("cp -r /export/backup /export/" + switchID.toString());
        process2Copy.waitFor();
        Logger.getAnonymousLogger().info("cp -r /export/backup /export/" + switchID.toString());
        nfsManager.changeConnecitonPropertyFromNFS(switchID, nfsRootPath, controllerIP, controllerPort);
        Process process2Chmod = Runtime.getRuntime().exec("chmod -R 777 " + switchID.toString() + "/");
        process2Chmod.waitFor();
        Logger.getAnonymousLogger().info("chmod -R 777 " + "/export/" + switchID.toString() + "/");
    }

    @Override
    public void resetSwitchConf(Integer switchID) throws InterruptedException, IOException {
        //TODO move to nfsManager
        Process process2Remove = Runtime.getRuntime().exec("rm -rf /export/" + switchID.toString());
        process2Remove.waitFor();
        Logger.getAnonymousLogger().info("rm -rf /export/" + switchID.toString());
    }
}
