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

    public void testApp()  {
//        LDMCore ldmCore = new LDMCore();
//        ldmCore.start();
//        LDMAgent ldmAgent = new LDMAgent(ldmCore);
//        Collection<Thread> threadsStartVM = new HashSet<>();
 //      String s1 = "test";
//          String s2 = "test";
//      s1 = ldmAgent.powerOnVM(2);
//        //s2 = ldmAgent.powerOffVM(1);
//        System.out.println(s1);
//       // System.out.println(s2);
//        //assertTrue(s2.equals("success"));
//        Thread t1 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//               String s1 =ldmAgent.powerOnVM(3);
//                System.out.println(s1);
//
//            }
//        });
//        t1.start();
//        threadsStartVM.add(t1);
//        //t1.join();
//        Thread t2 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String s2 = ldmAgent.powerOnVM(4);
//                System.out.println(s2);
//
//            }
//        });
//        t2.start();
//        threadsStartVM.add(t2);
//        //t2.join();
//        for(Thread t : threadsStartVM){
//            t.join();
//        }
        assert (true);



    }
}
