package org.interonet.ldm;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.interonet.ldm.Core.LDMAgent;
import org.interonet.ldm.Core.LDMCore;

public class MainTest
    extends TestCase
{
    public MainTest(String testName)
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( MainTest.class );
    }

    public void testApp() {
        LDMCore ldmCore = new LDMCore();
        ldmCore.start();
        LDMAgent ldmAgent = new LDMAgent(ldmCore);
        String s ="test";
        s=ldmAgent.powerOffVM(2);
        assertTrue(s==null);
    }
}
