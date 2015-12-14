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
import java.util.logging.Logger;

public class CreateVirtualMachine implements ICreateVirtualMachine {
    private String user = "root";
    private String password = "xjtu420";
    private String ip = "192.168.2.3";
    private Logger logger = Logger.getLogger(CreateVirtualMachine.class.getCanonicalName());

    @Override
    public String cloneVM(int ID) {
        String command = "virt-clone -o vmserver -n vmm" + ID + "  -f /home/400/vmuser/vm" + ID + ".img";
        Channel channel = new Channel(user, password, ip, 22);
        String result = channel.setChannel(command, true);
        return result;
    }

    @Override
    public String startVM(Connect connect, int ID) throws LibvirtException, DocumentException {

        String vmStartResult = "failure";
        SAXReader reader = new SAXReader();
        Document document = null;
        try {

            String INTERONET_HOME = System.getenv().get("INTERONET_HOME");
            document = (Document) reader.read(new File(INTERONET_HOME + "/conf/vmm.xml"));

            Element name = document.getRootElement().element("name");
            name.setText("vm" + ID);
            Element disksource = document.getRootElement().element("devices").element("disk").element("source");
            Attribute file = disksource.attribute("file");
            file.setText("/home/400/vmuser/vm" + ID + ".img");
            Element graphics = document.getRootElement().element("devices").element("graphics");
            Attribute vncPort = graphics.attribute("port");
            vncPort.setText("690" + ID);
            Element interfaces = document.getRootElement().element("devices").element("interface").element("source");
            Attribute bridge = interfaces.attribute("bridge");
            bridge.setText("br" + ID);
            String xmlDesc = document.asXML();
            Domain domain = null;
            domain = connect.domainCreateXML(xmlDesc, 0);
            domain.resume();
            logger.info("Virtual Machine ID = [" + ID + "] have been start successfully");
            return "success";

        } catch (Exception e) {
            logger.severe(e.getMessage());
            throw e;
        }
    }
}
