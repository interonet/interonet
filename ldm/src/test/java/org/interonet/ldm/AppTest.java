package org.interonet.ldm;

import com.jcraft.jsch.JSchException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.dom4j.DocumentException;
import org.interonet.ldm.VMM.createVirtualMachine;
import org.interonet.ldm.VMM.deleteVirtualMachine;
import org.interonet.ldm.VMM.initBridgeVLAN;
import org.libvirt.Connect;
import org.libvirt.LibvirtException;

/**
 * Unit test for simple Main.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() throws JSchException, LibvirtException, DocumentException {
        initBridgeVLAN bridge = new initBridgeVLAN();
        bridge.createBridge();
        bridge.createVLAN();
        bridge.addBridgeToVlan();
       Connect conn = new Connect("qemu+tcp://400@202.117.15.94/system",false);
        createVirtualMachine vmm = new createVirtualMachine();
        int ID=2;
        vmm.vmclone(ID);
        vmm.vmstart(conn, ID);
//        deleteVirtualMachine vm = new deleteVirtualMachine();
//        vm.vmdestroy(conn,ID);
//        vm.vmdelete(ID);



        assertTrue(true);
    }
}
