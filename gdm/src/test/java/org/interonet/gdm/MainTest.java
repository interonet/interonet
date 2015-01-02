package org.interonet.gdm;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.IOException;

public class MainTest extends TestCase {
    public MainTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(MainTest.class);
    }

    public void testApp() throws IOException {
        assertTrue(true);
    }
}