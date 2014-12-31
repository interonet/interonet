package org.interonet.ldm;

import com.jcraft.jsch.JSchException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.dom4j.DocumentException;
import org.interonet.ldm.Core.LDMAgent;
import org.interonet.ldm.Core.LDMCore;
import org.interonet.ldm.VMM.CreateVirtualMachine;
import org.interonet.ldm.VMM.BridgeAndVlan;
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
    public void testApp()  {
        LDMCore ldmcore = new LDMCore();
        ldmcore.start();
        LDMAgent ldmagent = new LDMAgent(ldmcore);
        String s="test";
        s =ldmagent.powerOnVM(2);




        assertTrue(s==null);
    }
}
