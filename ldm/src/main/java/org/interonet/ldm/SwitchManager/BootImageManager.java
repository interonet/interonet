package org.interonet.ldm.SwitchManager;

import com.jcraft.jsch.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

public class BootImageManager {
    Logger logger = Logger.getLogger(BootImageManager.class.getCanonicalName());

    public BootImageManager() {

    }

    public void changeUImage(Integer switchId, String uImageUrl) throws IOException {
        try {
            FileUtils.copyURLToFile(new URL(uImageUrl), new File("/tftpboot/" + switchId + "/uImage"));
            logger.info("copy " + uImageUrl + " to " + "/tftpboot/" + switchId + "/uImage successfully");
        } catch (IOException e) {
            logger.severe(e.getMessage());
            throw e;
        }
    }

    public void changeDeviceTree(Integer switchId, String deviceTreeUrl) throws IOException {
        try {
            FileUtils.copyURLToFile(new URL(deviceTreeUrl), new File("/tftpboot/" + switchId + "/devicetree.dtb"));
            logger.info("copy " + deviceTreeUrl + " to " + "/tftpboot/\" + switchId + \"/devicetree.dtb successfully");
        } catch (IOException e) {
            logger.severe(e.getMessage());
            throw e;
        }
    }

    public void changeBootBin(String bootBinUrl, String hostIp, String username, String password, String knownHostFile, String fileDestination) throws JSchException, SftpException, IOException {
        Session session = null;
        ChannelSftp c = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(username, hostIp, 22);
            session.setPassword(password);
            jsch.setKnownHosts(knownHostFile);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            c = (ChannelSftp) channel;

            c.put(new URL(bootBinUrl).openStream(), fileDestination);
            logger.info("scp " + bootBinUrl + " to " + hostIp + ":" + fileDestination + " successfully");

        } catch (Exception e) {
            logger.severe(e.getMessage());
            throw e;
        } finally {
            if (c != null) c.disconnect();
            if (session != null) session.disconnect();
        }
    }
}
