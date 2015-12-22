package org.interonet.ldm.SwitchManager;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.util.logging.Logger;

public class NFSManager {
    Logger logger = Logger.getLogger(NFSManager.class.getCanonicalName());

    public NFSManager() {
    }


    private void changeConnectionPropertyFromNFS(Integer switchID, String nfsRootPath, String controllerIP, int controllerPort) throws IOException {
        // nfsRootPath like /export/0
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(nfsRootPath + "/root/start_switch.sh", true)));
        pw.println("#!/bin/ash");
        pw.println("echo \"Networking Initial, Please wait ...\"");
        pw.println("ifconfig eth1 up");
        pw.println("ifconfig eth2 up");
        pw.println("ifconfig eth3 up");
        pw.println("ifconfig eth4 up");
        pw.println("sleep 3");

        pw.println("ifconfig eth1 down");
        pw.println("ifconfig eth2 down");
        pw.println("ifconfig eth3 down");
        pw.println("ifconfig eth4 down");
        pw.println("sleep 1");
        pw.println("");

        //FIXME duplicate MAC cause problem?
        pw.println("ifconfig eth1 hw ether 00:0a:35:01:26:01");
        pw.println("ifconfig eth2 hw ether 00:0a:35:01:26:02");
        pw.println("ifconfig eth3 hw ether 00:0a:35:01:26:03");
        pw.println("ifconfig eth4 hw ether 00:0a:35:01:26:04");
        pw.println("");

        pw.println("sleep 3");
        pw.println("ifconfig eth1 up");
        pw.println("ifconfig eth2 up");
        pw.println("ifconfig eth3 up");
        pw.println("ifconfig eth4 up");
        pw.println("ifconfig lo up");
        pw.println("");
        pw.println("sleep 3");
        pw.println("echo \"@++< Initial Done\"");
        pw.println("");

        pw.println("route add default gw " + "10.0.0.254");
        pw.println("cd /root/ofs-hw");
        pw.println("./udatapath/ofdatapath --datapath-id=" + to12HexDigits(switchID) + " --interfaces=eth1,eth2,eth3,eth4 ptcp:6632 --no-slicing &");
        pw.println("sleep 5");
        pw.println("./secchan/ofprotocol tcp:127.0.0.1:6632 tcp:" + controllerIP + ":" + controllerPort + "--inactivity-probe=90 &");
        pw.println("");

        pw.close();
    }

    private String to12HexDigits(Integer switchId) {
        Integer switchIdMagic = switchId + 1;
        String swIdhex = Integer.toHexString(switchIdMagic);
        int swIdhexLen = swIdhex.length();

        StringBuffer Hex12Digits = new StringBuffer();
        for (int i = 0; i < 12 - swIdhexLen; i++) {
            Hex12Digits.append("0");
        }
        Hex12Digits.append(swIdhex);
        return Hex12Digits.toString();
    }


    public void copyDefaultRootFsFileToDir(String type, Integer switchID, String nfsRootPath, String controllerIP, Integer controllerPort) throws IOException, InterruptedException {
        Process process2Copy;
        Process process2Chmod;
        // nfsRootPath equals like /export/0
        try {
            process2Copy = Runtime.getRuntime().exec("cp -r /export/of13_fs /export/" + switchID.toString());
            process2Copy.waitFor();
            logger.info("cp -r /export/of13_fs /export/" + switchID.toString());
            changeConnectionPropertyFromNFS(switchID, nfsRootPath, controllerIP, controllerPort);
            process2Chmod = Runtime.getRuntime().exec("chmod -R 777 /export/" + switchID.toString() + "/");
            logger.info("chmod -R 777 " + "/export/" + switchID.toString() + "/");
            process2Chmod.waitFor();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            throw e;
        }
    }

    public void copyCustomRootFsFileToDir(String rootFsUrl, Integer switchID) throws IOException {
        try {
            FileUtils.copyURLToFile(new URL(rootFsUrl), new File("/export/" + switchID.toString() + ".tar.xz"));
        } catch (IOException e) {
            logger.severe(e.getMessage());
            throw e;
        }
    }

    public void createSwitchDirectory(Integer switchId) throws IOException, InterruptedException {
        try {
            Process process2Mkdir = Runtime.getRuntime().exec("mkdir /export/" + switchId.toString());
            logger.info("mkdir /export/" + switchId.toString());
            process2Mkdir.waitFor();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            throw e;
        }
    }

    public void unCompressXZFile(Integer switchId) throws IOException, InterruptedException {
        try {
            Process process2xz = Runtime.getRuntime().exec("tar xfJ /export/" + switchId.toString() + ".tar.xz -C " + "/export/" + switchId + "/");
            process2xz.waitFor();
            logger.info("Finish: tar xvfJ /export/" + switchId.toString() + ".tar.xz -C " + "/export/" + switchId + "/");
        } catch (Exception e) {
            logger.severe(e.getMessage());
            throw e;
        }

    }

    public void changeFilePermission(Integer switchId) throws IOException, InterruptedException {
        try {
            Process process2Chmod = Runtime.getRuntime().exec("chmod -R 777 /export/" + switchId.toString() + "/");
            logger.info("chmod -R 777 " + "/export/" + switchId.toString() + "/");
            process2Chmod.waitFor();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            throw e;
        }
    }
}
