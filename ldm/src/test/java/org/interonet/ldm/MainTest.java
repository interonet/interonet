package org.interonet.ldm;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

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
//        LDMCore ldmCore = new LDMCore();
//        ldmCore.start();
//        LDMAgent ldmAgent = new LDMAgent(ldmCore);
//        String s1 = "test";
//        //String s2 = "test";
//        s1 = ldmAgent.powerOnVM(1);
//       // s2 = ldmAgent.powerOffVM(2);
//        System.out.println(s1);
//       // System.out.println(s2);
//        //assertTrue(s2.equals("success"));
//        assert (true);



    }
}
