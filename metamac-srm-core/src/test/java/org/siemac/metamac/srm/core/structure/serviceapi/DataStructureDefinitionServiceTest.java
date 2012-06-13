package org.siemac.metamac.srm.core.structure.serviceapi;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.siemac.metamac.srm.core.structure.domain.DataStructureDefinitionProperties.id;
import static org.siemac.metamac.srm.core.structure.domain.DataStructureDefinitionProperties.serviceURL;

import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.siemac.metamac.common.test.mock.OracleJNDIMock;
import org.siemac.metamac.core.common.bt.domain.ExternalItemBt;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.base.serviceapi.SdmxBaseService;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(locations = {"classpath:spring/srm-core/oracle/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class DataStructureDefinitionServiceTest extends AbstractTransactionalJUnit4SpringContextTests implements DataStructureDefinitionServiceTestBase {

    @Autowired
    protected DataStructureDefinitionService dataStructureDefinitionService;

    @Autowired
    protected SdmxBaseService                sdmxBaseService;

    private final ServiceContext             serviceContext               = new ServiceContext("system", "123456", "junit");

    private final String                     organizationByDefaultLogicId = "METAMAC_ORGANISATION";

    static Logger                            logger                       = Logger.getLogger(DataStructureDefinitionServiceTest.class.getName());

    @BeforeClass
    public static void setUp() {
        // JNDI SDMXDS
        SimpleNamingContextBuilder simpleNamingContextBuilder = OracleJNDIMock.setUp("SDMXDS", "srm_test", "srm_test", "jdbc:oracle:thin:@localhost:1521:XE", null);

        // JNDI tatisticDatasetDS
//        simpleNamingContextBuilder = OracleJNDIMock.setUp("StatisticDatasetDS", "sdmx_data", "sdmx_data", "jdbc:oracle:thin:@localhost:1521:XE", simpleNamingContextBuilder);
    }

    protected ServiceContext getServiceContext() {
        return serviceContext;
    }


    /**************************************************************************
     * DSD Tests
     **************************************************************************/
    @Test
    public void testSaveDsd() throws Exception {
        
        DataStructureDefinition dataStructureDefinition = dataStructureDefinitionService.saveDsd(getServiceContext(), createDsd());
        assertNotNull(dataStructureDefinition);
    }

    @Test
    public void testDeleteDsd() throws Exception {

        testSaveDsd();

        List<DataStructureDefinition> dataStructureDefinitions = dataStructureDefinitionService.findAllDsds(getServiceContext());
        DataStructureDefinition dataStructureDefinition = dataStructureDefinitions.get(dataStructureDefinitions.size() - 1);

        dataStructureDefinitionService.deleteDsd(getServiceContext(), dataStructureDefinition);

        List<ConditionalCriteria> conditions = criteriaFor(DataStructureDefinition.class).withProperty(id()).eq(dataStructureDefinition.getId()).build();

        PagedResult<DataStructureDefinition> dataStructureDefinitionPagedList = dataStructureDefinitionService.findDsdByCondition(getServiceContext(), conditions, PagingParameter.pageAccess(10));

        assertTrue(dataStructureDefinitionPagedList.getValues().isEmpty());
    }

    @Test
    public void testFindDsdByCondition() throws Exception {

        testSaveDsd();

        List<ConditionalCriteria> conditions = criteriaFor(DataStructureDefinition.class).withProperty(serviceURL()).eq("www.prueba.url").build();

        PagedResult<DataStructureDefinition> dataStructureDefinitionPagedList = dataStructureDefinitionService.findDsdByCondition(getServiceContext(), conditions, PagingParameter.pageAccess(10));

        assertTrue(!dataStructureDefinitionPagedList.getValues().isEmpty());
    }

    @Test
    public void testFindAllDsds() throws Exception {

        testSaveDsd();
        List<DataStructureDefinition> dsds = dataStructureDefinitionService.findAllDsds(getServiceContext());
        assertTrue(!dsds.isEmpty());
    }

    @Test
    public void testFindDsdById() throws Exception {

        testSaveDsd();

        List<DataStructureDefinition> dsds = dataStructureDefinitionService.findAllDsds(getServiceContext());

        DataStructureDefinition dataStructureDefinition = dataStructureDefinitionService.findDsdById(getServiceContext(), dsds.get(0).getId());

        assertNotNull(dataStructureDefinition);
    }

    /*********************************************************************
     * MOCKS
     * 
     * @throws MaintenanceAgencyNotFoundException
     * @throws NumberFormatException
     * @throws OrganisationNotFoundException
     *********************************************************************/

    private DataStructureDefinition createDsd() throws NumberFormatException {
        // Load Agency for associated it to DSD
        // Organisation organisation = sdmxBaseService.findOrganization(getServiceContext() ,organizationByDefaultLogicId);

        // Required: Name
        InternationalString name = new InternationalString();

        LocalisedString name_es = new LocalisedString();
        name_es.setLabel("NAME ES DSD");
        name_es.setLocale("es");
        LocalisedString name_en = new LocalisedString();
        name_en.setLabel("NAME EN DSD");
        name_en.setLocale("en");
        // LocalisedString name_es = LocalisedString.localisedString("Ejemplo de nombre DSD", "es");
        // LocalisedString name_en = LocalisedString.localisedString("Name example DSD", "en");

        name.addText(name_en);
        name.addText(name_es);

        // DataStructureDefinition dataStructureDefinition = new DataStructureDefinition(name, (Agency)organisation);
        // DataStructureDefinition dataStructureDefinition = new DataStructureDefinition(name, new ExternalItemBt("METAMAC_ORGANISTION", "METAMAC_ORGANISTION", TypeExternalArtefactsEnum.AGENCY));
        DataStructureDefinition dataStructureDefinition = new DataStructureDefinition();
        dataStructureDefinition.setName(name);
        dataStructureDefinition.setMaintainer(new ExternalItemBt("METAMAC_ORGANISTION", "METAMAC_ORGANISTION", TypeExternalArtefactsEnum.AGENCY));

        dataStructureDefinition.setIdLogic(RandomStringUtils.random(50, true, true));

        // Required
        // Boolean sdmxFinal,
        // Boolean isExternalReference, String sdmxVersion, String sdmxId,
        // String uri, MaintenanceAgency maintainer, InternationalString name,
        // Set<ComponentList> grouping)
        dataStructureDefinition.setVersionLogic(VersionUtil.VERSION_LOGIC);
        dataStructureDefinition.setFinalLogic(false);
        dataStructureDefinition.setIsExternalReference(false);
        dataStructureDefinition.setUri(RandomStringUtils.random(50, true, true));

        // Optionals
        dataStructureDefinition.setCreatedBy("Junit");
        dataStructureDefinition.setCreatedDate(new DateTime());
        dataStructureDefinition.setServiceURL("www.prueba.url");

        return dataStructureDefinition;
    }

    @Override
    public void testSaveDescriptorForDsd() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void testDeleteDescriptorForDsd() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void testSaveComponentForDsd() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void testDeleteComponentForDsd() throws Exception {
        // TODO Auto-generated method stub
        
    }

}
