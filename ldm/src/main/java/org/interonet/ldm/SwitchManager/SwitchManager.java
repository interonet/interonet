package org.interonet.ldm.SwitchManager;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.commons.io.FileUtils;
import org.interonet.ldm.ConfigurationCenter.IConfigurationCenter;
import org.interonet.ldm.Core.LDMCore;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Logger;

public class SwitchManager implements ISwitchManager {
    private NFSManager nfsManager;
    private BootImageManager bootImageManager;
    private FPGAManager fpgaManager;
    private IConfigurationCenter configurationCenter;
    private Logger logger;

    public SwitchManager(LDMCore ldmCore) {
        logger = Logger.getLogger(SwitchManager.class.getName());
        configurationCenter = ldmCore.getConfigurationCenter();
        nfsManager = new NFSManager();
        bootImageManager = new BootImageManager();
        fpgaManager = new FPGAManager();
        initNFSDirectory();
    }

    private void initNFSDirectory() {
        Collection<Process> processes = new HashSet<>();
        Map<String, String> nfsMapping = configurationCenter.getSwitchId2NFSRootDirectoryMapping();
        for (Map.Entry<String, String> entry : nfsMapping.entrySet()) {
            try {
                Process p = Runtime.getRuntime().exec("rm -rf " + entry.getValue());
                logger.info("rm -rf " + entry.getValue());
                processes.add(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (Process process : processes) {
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info("Initiate the NFS Directory Successfully.");
    }

    /*
    *   customSwitchConf should be like this.
    *
    *  {
    *     "root-fs": "http://202.117.15.79/ons_bak/1449315538610-backup.tar.xz",
    *     "boot-bin": "http://202.117.15.79/ons_bak/1449315538611-system.bit",
    *     "uImage": "http://202.117.15.79/ons_bak/1449315538612-uImage",
    *     "device-tree": "http://202.117.15.79/ons_bak/1449315538613-devicetree.dtb"
    *  }
    *
    * */
    @Override
    public void changeConnectionPropertyFromNFS(Map<String, String> customSwitchConfGDM, Integer switchID, String controllerIP, int controllerPort) throws Exception {
        //Parameter Parsing.
        String rootFsUrl = customSwitchConfGDM.get("root-fs");
        String bootBinUrl = customSwitchConfGDM.get("boot-bin");
        String uImageUrl = customSwitchConfGDM.get("uImage");
        String deviceTreeUrl = customSwitchConfGDM.get("device-tree");

        //Url Validation.
        String urlRegex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        if (!rootFsUrl.matches(urlRegex) ||
                !bootBinUrl.matches(urlRegex) ||
                !uImageUrl.matches(uImageUrl) ||
                !deviceTreeUrl.matches(urlRegex)) {
            logger.severe("rootFsUrl = " + rootFsUrl);
            logger.severe("bootBinUrl = " + bootBinUrl);
            logger.severe("uImageUrl = " + uImageUrl);
            logger.severe("deviceTreeUrl = " + deviceTreeUrl);
            throw new Exception("Wrong URL");
        }

        //root-fs Processing
        // 1. Download the root-fs.tar.xz from URL
        // 2. Create a directory for every switch.
        // 3. Uncompress the tar.xz file into this directory.
        // 4. change the mode bit for every file.
        FileUtils.copyURLToFile(new URL(rootFsUrl), new File("/export/" + switchID.toString() + ".tar.xz"));

        Process process2Mkdir = Runtime.getRuntime().exec("mkdir /export/" + switchID.toString());
        logger.info("mkdir /export/" + switchID.toString());
        process2Mkdir.waitFor();

        Process process2xz = Runtime.getRuntime().exec("tar xfJ /export/" + switchID.toString() + ".tar.xz -C " + "/export/" + switchID + "/");
        process2xz.waitFor();
        logger.info("Finish: tar xvfJ /export/" + switchID.toString() + ".tar.xz -C " + "/export/" + switchID + "/");

        Process process2Chmod = Runtime.getRuntime().exec("chmod -R 777 /export/" + switchID.toString() + "/");
        logger.info("chmod -R 777 " + "/export/" + switchID.toString() + "/");
        process2Chmod.waitFor();


        //uImage and devicetree.dtb Processing
        // 1. Copy the url file to the /tftpboot/0/ directory.
        try {
            FileUtils.copyURLToFile(new URL(uImageUrl), new File("/tftpboot/" + switchID + "/uImage"));
            logger.info("copy " + uImageUrl + " to " + "/tftpboot/" + switchID + "/uImage successfully");
            FileUtils.copyURLToFile(new URL(deviceTreeUrl), new File("/tftpboot/" + switchID + "/devicetree.dtb"));
            logger.info("copy " + deviceTreeUrl + " to " + "/tftpboot/\" + switchID + \"/devicetree.dtb successfully");
        } catch (Exception e) {
            logger.severe(e.getMessage());
            throw e;
        }

        //boot.bin Processing.
        // 1. scp the boot.bin into every switch's /mnt/ directory.
        String username = "root";

        String host = configurationCenter.getSwitchId2AddressMapping().get(switchID.toString());
        String password = "root";
        String knownHostFile = "/home/samuel/.ssh/known_hosts";
        String fileDestination = "/mnt/boot.bin";

        Session session = null;
        ChannelSftp c = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(username, host, 22);
            session.setPassword(password);
            jsch.setKnownHosts(knownHostFile);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            c = (ChannelSftp) channel;

            c.put(new URL(bootBinUrl).openStream(), fileDestination);
            logger.info("scp " + bootBinUrl + " to " + host + ":" + fileDestination + " successfully");

        } catch (Exception e) {
            logger.severe(e.getMessage());
            throw e;
        } finally {
            if (c != null) c.disconnect();
            if (session != null) session.disconnect();
        }

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
                process2Copy = Runtime.getRuntime().exec("cp -r /export/of13_fs /export/" + switchID.toString());
                process2Copy.waitFor();
                logger.info("cp -r /export/of13_fs /export/" + switchID.toString());
                nfsManager.changeConnecitonPropertyFromNFS(switchID, nfsRootPath, controllerIP, controllerPort);
                process2Chmod = Runtime.getRuntime().exec("chmod -R 777 /export/" + switchID.toString() + "/");
                logger.info("chmod -R 777 " + "/export/" + switchID.toString() + "/");
                process2Chmod.waitFor();
                break;
            case "OF1.0":
                //TODO
                logger.severe("Someone want a OF1.0 switch, but it is not available now");
                break;
            default:
                logger.severe("error: type = [" + type + "], not supported.");
                break;
        }
    }

    @Override
    public void resetSwitchConf(Integer switchID) throws InterruptedException, IOException {
        //TODO move to nfsManager
        Process process2Remove = Runtime.getRuntime().exec("rm -rf /export/" + switchID.toString());
        process2Remove.waitFor();
        logger.info("rm -rf /export/" + switchID.toString());

        Process process2RemoveTarBall = Runtime.getRuntime().exec("rm -rf /export/" + switchID.toString() + ".tar.xz");
        process2RemoveTarBall.waitFor();
        logger.info("rm -rf /export/" + switchID.toString() + ".tar.xz");
    }
}
