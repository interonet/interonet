package org.interonet.ldm.SwitchManager;

import org.apache.commons.io.FileUtils;
import org.interonet.ldm.ConfigurationCenter.IConfigurationCenter;
import org.interonet.ldm.Core.LDMCore;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
        Process process2Chmod = Runtime.getRuntime().exec("chmod -R 777 /export/" + switchID.toString() + "/");
        Logger.getAnonymousLogger().info("chmod -R 777 " + "/export/" + switchID.toString() + "/");
        process2Chmod.waitFor();
        nfsManager.changeConnecitonPropertyFromNFS(switchID, nfsRootPath, controllerIP, controllerPort);
    }

    @Override
    public void changeConnectionPropertyFromNFS(String type, Integer switchID, String controllerIP, int controllerPort) throws InterruptedException, IOException {
        Process process2Copy;
        Process process2Chmod;

        switch (type) {
            case "OF1.3":
                Map<String, String> nfsMapping = configurationCenter.getSwitchId2NFSRootDirectoryMapping();
                String nfsRootPath = nfsMapping.get(switchID.toString());
                // nfsRootPath equals like /export/0
                //TODO move to nfsManager
                process2Copy = Runtime.getRuntime().exec("cp -r /export/backup /export/" + switchID.toString());
                process2Copy.waitFor();
                Logger.getAnonymousLogger().info("cp -r /export/backup /export/" + switchID.toString());

                process2Chmod = Runtime.getRuntime().exec("chmod -R 777 /export/" + switchID.toString() + "/");
                Logger.getAnonymousLogger().info("chmod -R 777 " + "/export/" + switchID.toString() + "/");
                process2Chmod.waitFor();
                nfsManager.changeConnecitonPropertyFromNFS(switchID, nfsRootPath, controllerIP, controllerPort);
                break;
            case "OF1.0":
                //TODO
                Logger.getAnonymousLogger().severe("Someone want a OF1.0 switch, but it is not available now");
                break;
            default:
                String urlRegex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
                if (!type.matches(urlRegex)) {
                    Logger.getAnonymousLogger().severe("Wrong URL");
                    throw new RuntimeException("Wrong URL");
                }
                FileUtils.copyURLToFile(new URL(type), new File("/export/" + switchID.toString() + ".tar.xz"));

                Process process2Mkdir = Runtime.getRuntime().exec("mkdir /export/" + switchID.toString());
                Logger.getAnonymousLogger().info("mkdir /export/" + switchID.toString());
                process2Mkdir.waitFor();

                Process process2xz = Runtime.getRuntime().exec("tar xfJ /export/" + switchID.toString() +".tar.xz -C " + "/export/" + switchID + "/");
                process2xz.waitFor();
                Logger.getAnonymousLogger().info("Finish: tar xvfJ /export/" + switchID.toString() + ".tar.xz -C " + "/export/" + switchID + "/");

                process2Chmod = Runtime.getRuntime().exec("chmod -R 777 /export/" + switchID.toString() + "/");
                Logger.getAnonymousLogger().info("chmod -R 777 " + "/export/" + switchID.toString() + "/");
                process2Chmod.waitFor();
        }
    }

    @Override
    public void resetSwitchConf(Integer switchID) throws InterruptedException, IOException {
        //TODO move to nfsManager
        Logger.getAnonymousLogger().info("111111111111111111111");
        Process process2Remove = Runtime.getRuntime().exec("rm -rf /export/" + switchID.toString());
        process2Remove.waitFor();
        Logger.getAnonymousLogger().info("rm -rf /export/" + switchID.toString());

        Process process2RemoveTarBall = Runtime.getRuntime().exec("rm -rf /export/" + switchID.toString() + ".tar.xz");
        process2RemoveTarBall.waitFor();
        Logger.getAnonymousLogger().info("rm -rf /export/" + switchID.toString() + ".tar.xz");
    }
}
