package org.interonet.ldm.VMM;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.libvirt.Connect;
import org.libvirt.Domain;
import org.libvirt.LibvirtException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DeleteVirtualMachine implements IDeleteVirtualMachine {
    @Override
    public void vmdestroy(Connect connect, int ID)  {
        try {
        Domain domain = connect.domainLookupByName("vm" + ID);
            domain.destroy();
        } catch (LibvirtException e) {
            e.printStackTrace();
        }    }

    @Override
    public String vmdelete(int ID)  {
        String command = "virsh undefine vmm" + ID + ";rm -f /home/400/vmuser/vm" + ID + ".img";
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
        }
        catch (JSchException e) {
            e.printStackTrace();
        }finally {
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
