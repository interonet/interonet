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
        assertTrue(true);
    }
}
