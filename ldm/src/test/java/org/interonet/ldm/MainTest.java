package org.interonet.ldm;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.File;

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
        SAXReader reader = new SAXReader();
        Document docu = null;
        String INTERONET_HOME = System.getenv().get("INTERONET_HOME");
        String PATH = INTERONET_HOME+"/vmm.xml";
        try
       {
        docu = (Document) reader.read(new File(PATH));
        String xmlDesc = docu.asXML();
        System.out.println(xmlDesc);
    } catch (DocumentException e)
       {
    e.printStackTrace();
        }
        assertTrue(true);


    }
}
