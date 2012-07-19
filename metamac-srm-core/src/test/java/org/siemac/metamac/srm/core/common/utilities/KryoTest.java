package org.siemac.metamac.srm.core.common.utilities;

import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.naming.NamingException;

import oracle.jdbc.pool.OracleDataSource;

import org.apache.log4j.Logger;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.domain.srm.dto.AnnotationDto;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serialize.BeanSerializer;
import com.esotericsoftware.kryo.serialize.CollectionSerializer;
import com.esotericsoftware.kryo.serialize.DateSerializer;
import com.esotericsoftware.kryo.serialize.EnumSerializer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
public class KryoTest {

    @Autowired
    protected SrmCoreServiceFacade srmCoreServiceFacade;

    private ByteBuffer                   buffer = ByteBuffer.allocateDirect(1024);

    static Logger                        logger = Logger.getLogger(KryoTest.class.getName());

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

    @Ignore // TODO está fallando
    @Test
    // @Rollback(false)
    public void testSerializeDSD() throws Exception {

        List<DataStructureDefinitionDto> dsds = srmCoreServiceFacade.findAllDsds(getServiceContext());

        Kryo kryo = new Kryo();

        kryo.register(Date.class, new DateSerializer());
        kryo.register(TypeExternalArtefactsEnum.class, new EnumSerializer(TypeExternalArtefactsEnum.class));

        kryo.register(ExternalItemDto.class, new BeanSerializer(kryo, ExternalItemDto.class));
        kryo.register(InternationalStringDto.class, new BeanSerializer(kryo, InternationalStringDto.class));
        kryo.register(LocalisedStringDto.class, new BeanSerializer(kryo, LocalisedStringDto.class));

        CollectionSerializer col = new CollectionSerializer(kryo);
        col.setElementClass(LocalisedStringDto.class);

        kryo.register(HashSet.class, col);

        kryo.register(AnnotationDto.class, new BeanSerializer(kryo, AnnotationDto.class));

        kryo.register(DataStructureDefinitionDto.class, new BeanSerializer(kryo, DataStructureDefinitionDto.class));

        buffer.clear();
        kryo.writeObject(buffer, dsds.get(dsds.size() - 1));

        buffer.flip();
        DataStructureDefinitionDto backup = kryo.readObject(buffer, DataStructureDefinitionDto.class);

        assertTrue(backup.getIdLogic() != null);

    }

    @Test
    public void testSerializeEnum() throws Exception {
        Kryo kryo = new Kryo();
        buffer.clear();

        EnumSerializer enumSerializer = new EnumSerializer(TypeExternalArtefactsEnum.class);

        enumSerializer.writeObject(buffer, TypeExternalArtefactsEnum.AGENCY);

        buffer.flip();
        TypeExternalArtefactsEnum text = enumSerializer.readObject(buffer, TypeExternalArtefactsEnum.class);

        assertTrue(text != null);

    }

    @Test
    public void testSerializeEnum2() throws Exception {
        Kryo kryo = new Kryo();

        buffer.clear();

        kryo.register(TypeExternalArtefactsEnum.class, new EnumSerializer(TypeExternalArtefactsEnum.class));

        kryo.writeObject(buffer, TypeExternalArtefactsEnum.AGENCY);
        buffer.flip();
        TypeExternalArtefactsEnum text = kryo.readObject(buffer, TypeExternalArtefactsEnum.class);

        assertTrue(text != null);

    }

    /************************************************************************************
     * PRIVATE
     ************************************************************************************/

}
