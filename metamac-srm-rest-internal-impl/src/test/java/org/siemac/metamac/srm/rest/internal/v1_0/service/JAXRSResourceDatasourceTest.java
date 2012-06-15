package org.siemac.metamac.srm.rest.internal.v1_0.service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.testutil.common.AbstractBusClientServerTestBase;
import org.junit.BeforeClass;
import org.junit.Test;
import org.siemac.metamac.common.test.mock.OracleJNDIMock;
import org.siemac.metamac.common.test.rest.ServerResource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

public class JAXRSResourceDatasourceTest extends AbstractBusClientServerTestBase {
    public static final String PORT = ServerResource.PORT;

    @BeforeClass
    public static void startServers() throws Exception {
        // JNDI SDMXDS
        SimpleNamingContextBuilder simpleNamingContextBuilder = OracleJNDIMock.setUp("SDMXDS", "srm_test", "srm_test", "jdbc:oracle:thin:@localhost:1521:XE", null);

        assertTrue("server did not launch correctly", launchServer(ServerResource.class, true));
    }
    
    @Test
    public void testDataStructureMaintainableArtefactQuery() throws Exception {
        // Path("datastructure/{agencyID}/{resourceID}/{version}")
        
        String endpointAddress = "http://localhost:" + PORT + "/webapp/rest/datastructure/ECB/ECB_EXR_NG/1.0";
        
        URL url = new URL(endpointAddress);
        URLConnection connect = url.openConnection();
        connect.addRequestProperty("Accept", "application/vnd.sdmx.structure+xml;version=2.1");
        InputStream in = connect.getInputStream();
        assertNotNull(in);        
        System.out.println(getStringFromInputStream(in));
//        InputStream expected = getClass().getResourceAsStream("resources/expected_get_book123.txt");
//        InputStream expected = new FileInputStream("./responses/expected_get_datastructure_NOTFOUND.txt");
//        assertEquals(getStringFromInputStream(in), getStringFromInputStream(expected));
    }
/*    
    @Test
    public void testGetBookSimple() throws Exception {
        String address = "http://localhost:" + PORT + "/webapp/rest/simplebooks/444";
        assertEquals(444L, WebClient.create(address).get(Book.class).getId());
    }
    
*/ 
    private String getStringFromInputStream(InputStream in) throws Exception {        
        CachedOutputStream bos = new CachedOutputStream();
        IOUtils.copy(in, bos);
        in.close();
        bos.close();
        //System.out.println(bos.getOut().toString());        
        return bos.getOut().toString();        
    }

}
