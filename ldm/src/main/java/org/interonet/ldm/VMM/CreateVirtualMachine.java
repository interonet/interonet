package org.interonet.ldm.VMM;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.libvirt.Connect;
import org.libvirt.Domain;
import org.libvirt.LibvirtException;

import java.io.File;

public class CreateVirtualMachine implements ICreateVirtualMachine {
    private String user = "root";
    private String password = "xjtu420";
    private String ip = "192.168.2.3";
    @Override
    public String vmclone(int ID) {
        String command = "virt-clone -o vmsource -n vmm" + ID + "  -f /home/400/vmuser/vm" + ID + ".img";
        Channel channel = new Channel(user,password,ip, 22);
        String result = channel.setChannel(command,true);
        return result;
    }

    @Override
    public String  vmstart(Connect connect, int ID) {
        String vmStartResult = "failure";
        SAXReader reader = new SAXReader();
        Document docu = null;
        try {
            String INTERONET_HOME = System.getenv().get("INTERONET_HOME");
            System.out.println(INTERONET_HOME);
            System.out.println(INTERONET_HOME+"/conf/vmm.xml");
            docu = (Document) reader.read(new File(INTERONET_HOME+"/conf/vmm.xml"));

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
            System.out.println(xmlDesc);
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
