package org.interonet.ldm.VMM;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.libvirt.Connect;
import org.libvirt.Domain;
import org.libvirt.LibvirtException;

import java.io.*;

public class CreateVirtualMachine implements ICreateVirtualMachine {
    @Override
    public String vmclone(int ID) {
        String command = "virt-clone -o vmsource -n vmm" + ID + "  -f /home/400/vmuser/vm" + ID + ".img";
        String vmCloneResult = "";
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
                vmCloneResult = new String(buf.getBytes("gbk"), "UTF-8") ;
            }
        } catch (IOException e) {
            vmCloneResult += e.getMessage();
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
        return vmCloneResult;
    }

    @Override
    public String  vmstart(Connect connect, int ID) {
        String vmStartResult = "failure";
        SAXReader reader = new SAXReader();
        Document docu = null;
        try {
            String INTERONET_HOME = System.getenv().get("INTERONET_HOME");
            docu = (Document) reader.read(new File(INTERONET_HOME+"/vmm.xml"));

            Element name = docu.getRootElement().element("name");
            name.setText("vm" + ID);
            Element disksource = docu.getRootElement().element("devices").element("disk").element("source");
            Attribute file = disksource.attribute("file");
            file.setText("/home/400/vmuser/vm" + ID + ".img");
            Element graphics = docu.getRootElement().element("devices").element("graphics");
            Attribute vncPort = graphics.attribute("port");
            vncPort.setText("590" + ID);
            Element interfaces = docu.getRootElement().element("devices").element("interface").element("source");
            Attribute bridge = interfaces.attribute("bridge");
            bridge.setText("br" + ID);
            String xmlDesc = docu.asXML();
            Domain domain = null;
            domain = connect.domainCreateXML(xmlDesc, 0);
            domain.resume();
            vmStartResult = "success";
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (LibvirtException e) {
            e.printStackTrace();
        }
        return  vmStartResult;
    }


}
