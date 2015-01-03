package org.interonet.ldm.VMM;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BridgeAndVlan implements IBridgeAndVlan {
    @Override
    public String createBridge() {
        String command = "";
        for (int i = 1; i < 9; i++) {
            command = command + "brctl addbr br" + i + ";ifconfig br" + i + " up;";
        }
        String result = "";
        Session session = null;
        ChannelExec openChannel = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession("root", "202.117.15.94", 22);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword("xjtu420");
            session.connect();
            openChannel = (ChannelExec) session.openChannel("exec");
            openChannel.setCommand(command);
            openChannel.connect();
            InputStream in = (InputStream) openChannel.getInputStream();
            openChannel.setErrStream(System.err);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String buf = null;
            while ((buf = reader.readLine()) != null) {
                result += new String(buf.getBytes("gbk"), "UTF-8") + "    \r\n";
            }
        } catch (IOException e) {
            result += e.getMessage();
        } catch (JSchException e) {
            e.printStackTrace();
        } finally {
            if (openChannel != null && !openChannel.isClosed()) {
                openChannel.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
        return result;

    }

    @Override
    public String createVLAN() {
        String command = "modprobe 8021q;";
        for (int i = 1; i < 9; i++) {
            command = command + "vconfig add eth1 1" + i + ";ifconfig eth1.1" + i + " up;";
        }
        String result = "";
        Session session = null;
        ChannelExec openChannel = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession("root", "202.117.15.94", 22);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword("xjtu420");
            session.connect();
            openChannel = (ChannelExec) session.openChannel("exec");
            openChannel.setCommand(command);
            openChannel.connect();
            InputStream in = (InputStream) openChannel.getInputStream();
            openChannel.setErrStream(System.err);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String buf = null;
            while ((buf = reader.readLine()) != null) {
                result += new String(buf.getBytes("gbk"), "UTF-8") + "    \r\n";
            }
        } catch (IOException e) {
            result += e.getMessage();
        } catch (JSchException e) {
            e.printStackTrace();
        } finally {
            if (openChannel != null && !openChannel.isClosed()) {
                openChannel.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
        return result;

    }

    @Override
    public String addBridgeToVlan() {
        String command = "";
        for (int i = 1; i < 9; i++) {
            command = command + "brctl addif br" + i + " eth1.1" + i + ";";
        }
        String result = "";
        Session session = null;
        ChannelExec openChannel = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession("root", "202.117.15.94", 22);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword("xjtu420");
            session.connect();
            openChannel = (ChannelExec) session.openChannel("exec");
            openChannel.setCommand(command);
            openChannel.connect();
            InputStream in = (InputStream) openChannel.getInputStream();
            openChannel.setErrStream(System.err);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String buf = null;
            while ((buf = reader.readLine()) != null) {
                result += new String(buf.getBytes("gbk"), "UTF-8") + "    \r\n";
            }
        } catch (IOException e) {
            result += e.getMessage();
        } catch (JSchException e) {
            e.printStackTrace();
        } finally {
            if (openChannel != null && !openChannel.isClosed()) {
                openChannel.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
        return result;
    }

    @Override
    public void bridgeAndvlan() {
        this.createBridge();
        this.createVLAN();
        this.addBridgeToVlan();


    }
}
