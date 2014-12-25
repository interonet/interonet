package org.interonet.ldm.VMM;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.libvirt.Connect;
import org.libvirt.Domain;
import org.libvirt.LibvirtException;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class createVirtualMachine implements createVM {
	@Override
	public String vmclone(int ID) throws JSchException {
		String command = "virt-clone -o vmsource -n vmm"+ID+"  -f /home/400/vmuser/vm"+ID+".img";
		// TODO Auto-generated method stub
		String result="";
		Session session =null;
		ChannelExec openChannel =null;
		try {
			JSch jsch=new JSch();
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
            	result+= new String(buf.getBytes("gbk"),"UTF-8")+"    \r\n";  
            }  
		} catch (IOException e) {
			result+=e.getMessage();
		}finally{
			if(openChannel!=null&&!openChannel.isClosed()){
				openChannel.disconnect();
			}
			if(session!=null&&session.isConnected()){
				session.disconnect();
			}
		}
		return result;
	}

	@Override
	public void vmstart(Connect connect,  int ID) throws DocumentException, LibvirtException
	{
		 SAXReader reader = new SAXReader(); 
			Document docu = (Document) reader.read(new File("/home/houlifei/vmm.xml"));		
			Element name=docu.getRootElement().element("name");  
			name.setText("vm"+ID);  
			Element disksource =docu.getRootElement().element("devices").element("disk").element("source");
			Attribute file = disksource.attribute("file");
			file.setText("/home/400/vmuser/vm"+ID+".img");
			Element graphics=docu.getRootElement().element("devices").element("graphics");  
			Attribute vncPort=graphics.attribute("port");  
			vncPort.setText("590"+ID);
			Element  interfaces = docu.getRootElement().element("devices").element("interface").element("source");
			Attribute bridge = interfaces.attribute("bridge");
			bridge.setText("br"+ID);
			String xmlDesc = docu.asXML();
			Domain domain = connect.domainCreateXML(xmlDesc, 0);
			domain.resume();
	}


}
