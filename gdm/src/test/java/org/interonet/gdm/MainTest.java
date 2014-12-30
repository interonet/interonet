package org.interonet.gdm;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Unit test for simple App.
 */
public class MainTest
    extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MainTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(MainTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() throws IOException {

        assertTrue(true);
    }
}