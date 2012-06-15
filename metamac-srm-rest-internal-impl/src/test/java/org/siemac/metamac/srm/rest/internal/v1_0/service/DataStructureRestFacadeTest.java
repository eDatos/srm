package org.siemac.metamac.srm.rest.internal.v1_0.service;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;
import static org.mockito.Mockito.mock;
import static org.siemac.metamac.srm.core.structure.domain.DataStructureDefinitionProperties.idLogic;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.mock.OracleJNDIMock;
import org.siemac.metamac.data.mock.Mocks;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm-core-facade-rest/applicationContext-test.xml"})
public class DataStructureRestFacadeTest {

    @Autowired
    private DatastructureRestFacade datastructureRestServiceFacade;
    
    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;
    
    private final ServiceContext serviceContext = new ServiceContext("system", "123456", "junit");

    static Logger                logger         = Logger.getLogger(DataStructureRestFacadeTest.class.getName());

    @BeforeClass
    public static void setUp() {
        // JNDI SDMXDS
        SimpleNamingContextBuilder simpleNamingContextBuilder = OracleJNDIMock.setUp("SDMXDS", "srm_test", "srm_test", "jdbc:oracle:thin:@localhost:1521:XE", null);

    }

    @Test
    public void testMaintainableStructure() throws Exception {
        
        List<ConditionalCriteria> conditions = criteriaFor(DataStructureDefinition.class).withProperty(idLogic()).eq("ECB_EXR_NG").build();

        PagedResult<DataStructureDefinitionDto> dataStructureDefinitionDtoPagedList = srmCoreServiceFacade.findDsdByCondition(serviceContext, conditions, PagingParameter.pageAccess(10));

        File file = null;
        if (dataStructureDefinitionDtoPagedList.getTotalRows() < 1) {
            file = new File("src/test/resources/sdmx/2_1/dsd/ecb_exr_ng_full.xml");
            srmCoreServiceFacade.importSDMXStructureMsg(serviceContext, Mocks.createContentInput(file));
        }
        
        // TODO comprobar que existe el dsd si no insertarlo primero.
        datastructureRestServiceFacade.maintainableArtefactQuery("ECB", "ECB_EXR_NG", "1.0", "", "");
    }
    
    @Test
    public void testKAKA() throws Exception {
        // Mock
        DataStructureDefinition dsdMock = mock(DataStructureDefinition.class);
//        Page
        
//        when(dataStructureDefinitionServiceMock.findDsdByCondition((ServiceContext) Matchers.anyObject(),  (List<ConditionalCriteria>) Matchers.anyObject(), (PagingParameter) Matchers.anyObject())).thenReturn(value)
        
    }
    
}
