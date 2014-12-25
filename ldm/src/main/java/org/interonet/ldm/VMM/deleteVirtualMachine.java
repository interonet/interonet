package org.interonet.ldm.VMM;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.libvirt.Connect;
import org.libvirt.Domain;
import org.libvirt.LibvirtException;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class deleteVirtualMachine implements deleteVM {

	@Override
	public void vmdestroy(Connect connect , int ID) throws LibvirtException {
		// TODO Auto-generated method stub
		Domain domain = connect.domainLookupByName("vm"+ID);
		domain.destroy();
	}



	@Override
	public String vmdelete(int ID) throws JSchException {
		// TODO Auto-generated method stub
		String command = "virsh undefine vmm"+ID+";rm -f /home/400/vmuser/vm"+ID+".img";
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

}
