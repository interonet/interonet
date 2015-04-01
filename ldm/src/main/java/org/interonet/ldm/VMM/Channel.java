package org.interonet.ldm.VMM;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Channel {
    private String user;
    private String password;
    private String IP;
    private int port;
    public Channel(String user,String password,String IP,int port)
    {
        this.user = user;
        this.password = password;
        this.IP = IP;
        this.port = port;
    }
    public String setChannel(String command,boolean judge)
    {
        String result = "";
        Session session = null;
        ChannelExec openChannel = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, IP, port);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword(password);
            session.connect();
            openChannel = (ChannelExec) session.openChannel("exec");
            openChannel.setCommand(command);
            openChannel.connect();
            InputStream in = (InputStream) openChannel.getInputStream();
            openChannel.setErrStream(System.err);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String buf = null;
            if(judge==true) {
                while ((buf = reader.readLine()) != null) {
                    result = new String(buf.getBytes("gbk"), "UTF-8");
                }
            }
            else{
                while ((buf = reader.readLine()) != null) {
                    result += new String(buf.getBytes("gbk"), "UTF-8");
                }
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



}
