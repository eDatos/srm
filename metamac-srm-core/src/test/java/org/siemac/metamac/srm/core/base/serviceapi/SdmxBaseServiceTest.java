package org.siemac.metamac.srm.core.base.serviceapi;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.junit.Test;
import org.siemac.metamac.srm.core.base.domain.Component;
import org.siemac.metamac.srm.core.base.domain.ComponentList;
import org.siemac.metamac.srm.core.mock.Mocks;
import org.siemac.metamac.srm.core.structure.domain.Dimension;
import org.siemac.metamac.srm.core.structure.domain.DimensionDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class SdmxBaseServiceTest extends AbstractTransactionalJUnit4SpringContextTests implements SdmxBaseServiceTestBase {

    @Autowired
    protected SdmxBaseService    sdmxBaseService;

    private final ServiceContext serviceContext               = new ServiceContext("system", "123456", "junit");

    private final String         organizationByDefaultLogicId = "METAMAC_ORGANISATION";

    protected ServiceContext getServiceContext() {
        return serviceContext;
    }

    public void initializeData() {
        // Check for existence Data
        // if (sdmxBaseService.findAllConcepts(getServiceContext()).isEmpty()) {
        // executeSqlScript("classpath:oracle/core/script_insert.sql", false);
        // }
    }

    /**************************************************************************
     * Component Tests
     **************************************************************************/

    @Test
    public void testSaveComponent() throws Exception {

        initializeData();

        Component component = sdmxBaseService.saveComponent(getServiceContext(), createDimension().get(0));
        assertNotNull(component);

    }

    @Test
    public void testDeleteComponent() throws Exception {

        initializeData();

        testSaveComponent();

        List<Component> components = sdmxBaseService.findAllComponent(getServiceContext());
        Component component = components.get(components.size() - 1);

        sdmxBaseService.deleteComponent(getServiceContext(), component);

        assertTrue(sdmxBaseService.findAllComponent(getServiceContext()).size() < components.size());

    }

    @Override
    @Test
    public void testFindAllComponent() throws Exception {
        initializeData();

        testSaveComponent();

        List<Component> components = sdmxBaseService.findAllComponent(getServiceContext());
        assertTrue(!components.isEmpty());
    }

    /**************************************************************************
     * ComponentList Tests
     **************************************************************************/
    @Test
    public void testSaveComponentList() throws Exception {

        initializeData();

        ComponentList componentList = sdmxBaseService.saveComponentList(getServiceContext(), createDimensionDescriptor());

        assertNotNull(componentList);

    }

    @Override
    @Test
    public void testDeleteComponentList() throws Exception {
        initializeData();

        testSaveComponentList();

        List<ComponentList> componentLists = sdmxBaseService.findAllComponentList(getServiceContext());
        ComponentList componentList = componentLists.get(componentLists.size() - 1);

        sdmxBaseService.deleteComponentList(getServiceContext(), componentList);

        assertTrue(sdmxBaseService.findAllComponentList(getServiceContext()).size() < componentLists.size());

    }

    @Override
    @Test
    public void testFindAllComponentList() throws Exception {
        initializeData();

        testSaveComponentList();

        List<ComponentList> componentLists = sdmxBaseService.findAllComponentList(getServiceContext());
        assertTrue(!componentLists.isEmpty());
    }

    private List<Dimension> createDimension() {

        // List<Concept> concepts = sdmxBaseService.findAllConcepts(getServiceContext());

        Dimension dimension1 = new Dimension();
        dimension1.setIdLogic(RandomStringUtils.random(50, true, true));

        // Required
        dimension1.setUri(RandomStringUtils.random(50, true, true));
        dimension1.setCptIdRef(Mocks.createConceptExternalItemBt());
        dimension1.setOrderLogic(1);

        // Some Auditory
        dimension1.setCreatedBy("Junit");
        dimension1.setCreatedDate(new DateTime());

        Dimension dimension2 = new Dimension();
        dimension2.setIdLogic(RandomStringUtils.random(50, true, true));

        // Required
        dimension2.setUri(RandomStringUtils.random(50, true, true));
        dimension2.setCptIdRef(Mocks.createConceptExternalItemBt());
        dimension2.setOrderLogic(1);

        // Some Auditory
        dimension2.setCreatedBy("Junit");
        dimension2.setCreatedDate(new DateTime());

        List<Dimension> dimensions = new ArrayList<Dimension>();
        dimensions.add(dimension2);
        dimensions.add(dimension2);

        return dimensions;
    }

    private ComponentList createDimensionDescriptor() {
        DimensionDescriptor dimensionDescriptor = new DimensionDescriptor();
        dimensionDescriptor.setIdLogic(RandomStringUtils.random(50, true, true));
        // dimensionDescriptor.getComponents().addAll(createDimension()); No CASCADE !!!!
        // Required
        dimensionDescriptor.setUri(RandomStringUtils.random(50, true, true));

        return dimensionDescriptor;
    }

    @Override
    public void testFindComponentListById() throws Exception {

    }

    @Override
    public void testPopulateAssociationsComponentList() throws Exception {

    }
}
