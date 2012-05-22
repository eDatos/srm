package org.siemac.metamac.srm.core.serialization;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import oracle.jdbc.pool.OracleDataSource;

import org.apache.commons.lang.SerializationUtils;
import org.apache.log4j.Logger;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:oracle/core/applicationContext-oracle-test.xml", "classpath:spring/data/oracle/applicationContext-oracle.xml",})
public class JavaSerialization {

    @Autowired
    protected SrmCoreServiceFacade srmCoreServiceFacade;

    private ByteBuffer                   buffer = ByteBuffer.allocateDirect(1024);

    static Logger                        logger = Logger.getLogger(JavaSerialization.class.getName());

    @BeforeClass
    public static void setUp() {
        OracleDataSource odSDMXDS = null;
        OracleDataSource odStatisticDatasetDS = null;

        try {
            odSDMXDS = new OracleDataSource();
            odStatisticDatasetDS = new OracleDataSource();
        } catch (SQLException e) {
            logger.error(e);
        }

        odSDMXDS.setURL("jdbc:oracle:thin:@localhost:1521:XE");
        odSDMXDS.setUser("metamac");
        odSDMXDS.setPassword("metamac");

        odStatisticDatasetDS.setURL("jdbc:oracle:thin:@localhost:1521:XE");
        odStatisticDatasetDS.setUser("sdmx_data");
        odStatisticDatasetDS.setPassword("sdmx_data");

        SimpleNamingContextBuilder builder = null;
        try {
            builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
            builder.bind("java:comp/env/jdbc/SDMXDS", odSDMXDS);
            builder.bind("java:comp/env/jdbc/StatisticDatasetDS", odStatisticDatasetDS);
        } catch (NamingException e) {
            logger.error(e);
        }
    }

    private final ServiceContext serviceContext = new ServiceContext("system", "123456", "junit");

    protected ServiceContext getServiceContext() {
        return serviceContext;
    }

    @Test
    // @Rollback(false)
    public void testSerializeDSD() throws Exception {

        List<DataStructureDefinitionDto> dsds = srmCoreServiceFacade.findAllDsds(getServiceContext());

        File serializationFile = File.createTempFile("METAMAC", "ser");

        // New file output stream for the file
        FileOutputStream fos = new FileOutputStream(serializationFile);

        // Serialize DSD
        SerializationUtils.serialize(dsds.get(dsds.size() - 1), fos);
        fos.close();

        // Open FileInputStream to the file
        FileInputStream fis = new FileInputStream(serializationFile);

        // Deserialize and cast into DSD
        DataStructureDefinitionDto ser = (DataStructureDefinitionDto) SerializationUtils.deserialize(fis);
        System.out.println(ser);
        fis.close();

        assertTrue(ser.getIdLogic() != null);

    }

    /************************************************************************************
     * PRIVATE
     ************************************************************************************/

}
